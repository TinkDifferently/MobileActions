package io.dimension.actions;

import io.dimension.actions.annotations.*;
import io.dimension.config.session.DriverController;
import io.dimension.exceptions.NoSuchActionException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.atteo.classindex.ClassIndex;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.openqa.selenium.Platform;
import io.dimension.utils.CustomLogger;
import io.dimension.utils.CustomReflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;

public final class ActionBuilder {

    @NotNull
    @Unmodifiable
    private final static Map<String, Class<? extends IAction>> providers;

    static {
        Map<String, Class<? extends IAction>> map = new HashMap<>();
        var types = ClassIndex.getAnnotated(ActionProvider.class);
        for (var type : types) {
            if (IAction.class.isAssignableFrom(type)) {
                map.put(type.getAnnotation(ActionProvider.class).value(), (Class<? extends IAction>) type);
            }
        }
        providers = Collections.unmodifiableMap(map);
    }

    @Contract(pure = true)
    private ActionBuilder() {
        data = new ArrayList<>();
        actions = new ArrayList<>();
    }

    private final Collection<Pair<String, Object>> data;

    private Class<? extends IAction> actionClass;

    private final Collection<Consumer<IAction>> actions;

    @NotNull
    private final static ThreadLocal<ActionBuilder> pInstance = ThreadLocal.withInitial(ActionBuilder::new);

    @NotNull
    @Contract(pure = true)
    public static ActionBuilder type(@NotNull String actionGroupName) {
        var result = pInstance.get();
        result.actionClass = providers.get(actionGroupName);
        return result;
    }

    @Contract("_, _ -> this")
    public ActionBuilder data(@NotNull String key, @NotNull Object value) {
        int index = key.lastIndexOf('#');
        data.add(new ImmutablePair<>(index == -1 ? key : key.substring(0, index), value));
        return this;
    }

    private @Nullable Method findAction(IAction instance, String actionName) {
        try {
            Optional<Method> invokable;
            if (DriverController.getInstance().getCurrentPlatform().is(Platform.IOS)) {
                invokable = CustomReflection.getAnnotatedMethods(instance.getClass(), IOSAction.class)
                        .stream()
                        .filter(o -> o.getAnnotation(IOSAction.class).value().equals(actionName))
                        .findFirst();
                if (invokable.isPresent() && invokable.get().getAnnotation(IOSAction.class).ignore()) {
                    return null;
                }
            } else {
                invokable = CustomReflection.getAnnotatedMethods(instance.getClass(), AndroidAction.class)
                        .stream()
                        .filter(o -> o.getAnnotation(AndroidAction.class).value().equals(actionName))
                        .findFirst();
                if (invokable.isPresent() && invokable.get().getAnnotation(AndroidAction.class).ignore()) {
                    return null;
                }
            }
            return invokable.or(() -> CustomReflection.getAnnotatedMethods(instance.getClass(), MobileAction.class)
                    .stream()
                    .filter(o -> o.getAnnotation(MobileAction.class).value().equals(actionName))
                    .findFirst())
                    .orElseThrow(NoSuchActionException::new);
        } catch (Exception e) {
            CustomLogger.fail(String.format("Не удалось найти действие '%s'", actionName), e);
            throw new RuntimeException();
        }
    }

    private <T> T reduce(String key) {
        var pair = data.stream().filter(o -> o.getKey().equals(key)).findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("Data for key '%s' was not supplied", key)));
        data.remove(pair);
        return (T) pair.getValue();
    }

    private void executeAction(IAction instance, String actionName) {
        var method = findAction(instance, actionName);
        if (method == null) {
            throw new RuntimeException("never happens");
        }
        var methodParameters = method.getParameters();
        var parameters = new Object[methodParameters.length];
        for (var index = 0; index < methodParameters.length; index++) {
            var parameter = methodParameters[index];
            if (!parameter.isAnnotationPresent(Parameter.class)) {
                throw new RuntimeException(String.format("All parameters in action methods must be annotated with '%s'", Parameter.class.getCanonicalName()));
            }
            var key = parameter.getAnnotation(Parameter.class).value();
            parameters[index] = reduce(key);
        }
        try {
            method.invoke(instance, parameters);
        } catch (Exception any) {
            throw new RuntimeException(String.format("Could not execute action '%s'", actionName), any.getCause());
        }
    }

    public ActionBuilder action(String actionName) {
        actions.add(instance -> executeAction(instance, actionName));
        return this;
    }

    @NotNull
    public IAction build() {
        pInstance.set(new ActionBuilder());
        if (actionClass.isInterface() || Modifier.isAbstract(actionClass.getModifiers())) {
            throw new RuntimeException(String.format("Could not intanciate class '%s'", actionClass.getCanonicalName()));
        }
        try {
            var constructor = actionClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            var instance = (IAction) constructor.newInstance();
            IAction result = () -> {
            };
            for (var actionMethod : actions) {
                result = result.andThen(() -> actionMethod.accept(instance));
            }
            return result;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(String.format("No default constructor for '%s'", actionClass.getCanonicalName()));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("No public default constructor for '%s'", actionClass.getCanonicalName()));
        } catch (InstantiationException e) {
            //never happens
            throw new RuntimeException("java stream was corrupted");
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Could not instantiate object", e);
        }
    }
}
