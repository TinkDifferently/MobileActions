package utils;

import org.jetbrains.annotations.Contract;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * Класс для работы с рефлексией.
 */
public class CustomReflection {

    /**
     * Получение списка полей класса (с суперклассами).
     *
     * @param clazz класс
     * @return сет полей класса
     */
    public static Set<Field> getFields(Class clazz) {
        Set<Field> result = new HashSet<>();
        if (clazz.getSuperclass() != Object.class) {
            result.addAll(getFields(clazz.getSuperclass()));
        }
        result.addAll(Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList()));
        result.forEach(o -> o.setAccessible(true));
        return result;
    }

    /**
     * Получение полного списка методов класса (с суперклассами).
     *
     * @param clazz класс
     * @return сет методов класса
     */
    public static Set<Method> getMethods(Class clazz) {
        Set<Method> result = new HashSet<>();
        if (clazz.getSuperclass() != Object.class && clazz.getSuperclass() != null) {
            result.addAll(getMethods(clazz.getSuperclass()));
        }
        result.addAll(Arrays.stream(clazz.getDeclaredMethods()).collect(Collectors.toList()));
        result.forEach(o -> o.setAccessible(true));
        return result;
    }

    /**
     * Получение полного списка методов класса (с суперклассами).
     *
     * @param clazz класс
     * @return сет методов класса
     */
    public static Set<Constructor> getConstructors(Class clazz) {
        Set<Constructor> result = Arrays.stream(clazz.getConstructors()).collect(Collectors.toSet());
        result.addAll(Arrays.stream(clazz.getDeclaredConstructors()).collect(Collectors.toSet()));
        result.forEach(o -> o.setAccessible(true));
        return result;
    }

    private static Optional<Constructor> getOptionalConstructor(Class clazz, Class... argTypes) {
        BiPredicate<Class[], Class[]> argMatcher = (a1, a2) -> {
            if (a1 == a2) {
                return true;
            }
            if ((a1 == null || a2 == null) || (a1.length != a2.length)) {
                return false;
            }
            for (int i = 0; i < a1.length; i++) {
                if (!a1[i].equals(a2[i]) && !a2[i].isAssignableFrom(a1[i])) {
                    return false;
                }
            }
            return true;
        };
        return getConstructors(clazz).stream().filter(o -> argMatcher.test(argTypes, o.getParameterTypes())).findFirst();
    }

    @SuppressWarnings( {"unchecked", "OptionalGetWithoutIsPresent"})
    public static <T> T createNewInstanceOr(Class<T> clazz, T defaultValue, Object... args) {
        try {
            if (args.length == 0) {
                return (T) getOptionalConstructor(clazz).get().newInstance();
            }
            Class<?>[] types = new Class[args.length];
            Arrays.stream(args).map(Object::getClass).collect(Collectors.toList()).toArray(types);
            return (T) getOptionalConstructor(clazz, types).get().newInstance(args);
        } catch (Exception e) {
            if (defaultValue != null) {
                return defaultValue;
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Получение метода класса с обертыванием NoSuchMethodException.
     *
     * @param clazz класс
     * @param methodName имя метода
     * @param argTypes типы аргументов
     * @return метод
     */
    private static Optional<Method> getOptionalMethod(Class clazz, String methodName, Class... argTypes) {
        BiPredicate<Class[], Class[]> argMatcher = (a1, a2) -> {
            if (a1 == a2) {
                return true;
            }
            if ((a1 == null || a2 == null) || (a1.length != a2.length)) {
                return false;
            }
            for (int i = 0; i < a1.length; i++) {
                if (!a1[i].equals(a2[i])) {
                    return false;
                }
            }
            return true;
        };
        return getMethods(clazz).stream().filter(o -> o.getName().equals(methodName) && argMatcher.test(argTypes, o.getParameterTypes()))
            .findFirst();
    }

    /**
     * Получение поля класса с обертыванием NoSuchFieldException.
     *
     * @param clazz класс
     * @param fieldName имя поля
     * @return поле
     */
    private static Optional<Field> getOptionalField(Class clazz, String fieldName) {
        BiPredicate<Class[], Class[]> argMatcher = (a1, a2) -> {
            if (a1 == a2) {
                return true;
            }
            if ((a1 == null || a2 == null) || (a1.length != a2.length)) {
                return false;
            }
            for (int i = 0; i < a1.length; i++) {
                if (!a1[i].equals(a2[i])) {
                    return false;
                }
            }
            return true;
        };
        return getFields(clazz).stream().filter(o -> o.getName().equals(fieldName)).findFirst();
    }

    /**
     * Проверка наличия метода у класса.
     *
     * @param clazz класс
     * @param methodName имя метода
     * @param argTypes типы аргументов метода
     * @return наличие метода
     */
    public static boolean hasMethod(Class clazz, String methodName, Class... argTypes) {
        return getOptionalMethod(clazz, methodName, argTypes).isPresent();
    }

    /**
     * Проверка наличия поля у класса.
     *
     * @param clazz класс
     * @param fieldName имя поля
     * @return наличие поля
     */
    public static boolean hasField(Class clazz, String fieldName) {
        return getOptionalField(clazz, fieldName).isPresent();
    }

    /**
     * Получение поля класса. Отсутствие поля не оборачивается, уровень доступа не расширяется.
     *
     * @param clazz класс
     * @param fieldName имя поля
     * @return поле
     * @throws NoSuchFieldException поле не обнаружено
     */
    public static Field getField(Class clazz, String fieldName) throws NoSuchFieldException {
        return getOptionalField(clazz, fieldName).orElseThrow(NoSuchFieldException::new);
    }

    /**
     * Установка доступа к полю.
     *
     * @param field объект-поле
     * @param accessible тип доступа
     * @return переданный объект с новым типом доступа
     */
    public static Field setAccessible(Field field, boolean accessible) {
        field.setAccessible(accessible);
        return field;
    }

    /**
     * Установка доступа к методу.
     *
     * @param method объект-метод
     * @param accessible тип доступа
     * @return переданный объект с новым типом доступа
     */
    public static Method setAccessible(Method method, boolean accessible) {
        method.setAccessible(accessible);
        return method;
    }

    /**
     * Получение метода класса. Отсутствие метода не оборачивается, уровень доступа не расширяется.
     *
     * @param clazz класс
     * @param methodName имя метода
     * @return метод
     * @throws NoSuchMethodException метод не обнаружен
     */
    public static Method getMethod(Class clazz, String methodName, Class... argTypes) throws NoSuchMethodException {
        return getOptionalMethod(clazz, methodName, argTypes).orElseThrow(NoSuchMethodException::new);
    }

    @Contract("null, _, _, _ -> param3")
    @SuppressWarnings("unchecked")
    public static <T> T invokeOr(Object object, String methodName, T defaultResult, Object... args) {
        try {
            Class[] argTypes = new Class[args.length];
            Arrays.parallelSetAll(argTypes, o -> args[o].getClass());
            return args.length == 0 ? (T) getMethod(object.getClass(), methodName).invoke(object) :
                (T) getMethod(object.getClass(), methodName, argTypes).invoke(object, args);
        } catch (Exception e) {
            return defaultResult;
        }
    }

    @Contract("null, _, _, _ -> param3")
    @SuppressWarnings("unchecked")
    public static <T> T invokeOr(Class clazz, String methodName, T defaultResult, Object... args) {
        try {
            Class[] argTypes = new Class[args.length];
            Arrays.parallelSetAll(argTypes, o -> args[o].getClass());
            return args.length == 0 ? (T) getMethod(clazz, methodName).invoke(null) :
                    (T) getMethod(clazz, methodName, argTypes).invoke(null, args);
        } catch (Exception e) {
            return defaultResult;
        }
    }

    /**
     * Получение значения поля либо дефолтного значения.
     *
     * @param object объект значение поля которого будет считано
     * @param fieldName имя поля
     * @param defaultResult дефолтное возвращаемое значение
     * @param <T> тип возврата
     * @return значение поля, либо дефолтное значение
     */
    @Contract("null, _, _, -> param3")
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValueOr(Object object, String fieldName, T defaultResult) {
        try {
            return (T) getField(object.getClass(), fieldName).get(object);
        } catch (Exception e) {
            return defaultResult;
        }
    }

    /**
     * Замена значения поля либо дефолтного значения.
     *
     * @param clazz класс объекта, либо класс, содержащий статический метод
     * @param object объект значение поля которого будет заменено (null -> статическое поле)
     * @param fieldName имя поля
     * @param value новое значение
     * @param <T> тип возврата
     * @return успешность замены
     */
    private static <T> boolean replaceValue(Class clazz, Object object, String fieldName, T value) {
        try {
            setAccessible(getField(clazz, fieldName), true).set(object, value);
            return true;
        } catch (IllegalAccessException | NoSuchFieldException e) {
            return false;
        }
    }

    /**
     * Замена значения поля либо дефолтного значения.
     *
     * @param object объект значение поля которого будет заменено
     * @param fieldName имя поля
     * @param value новое значение
     * @param <T> тип возврата
     * @return успешность замены
     */
    public static <T> boolean replaceValue(Object object, String fieldName, T value) {
        return replaceValue(object.getClass(), object, fieldName, value);
    }

    /**
     * Замена значения поля либо дефолтного значения.
     *
     * @param clazz класс, содержащий статический метод
     * @param fieldName имя поля
     * @param value новое значение
     * @param <T> тип возврата
     * @return успешность замены
     */
    public static <T> boolean replaceValue(Class clazz, String fieldName, T value) {
        return replaceValue(clazz, null, fieldName, value);
    }

    /**
     * Получение значения статического поля.
     *
     * @param clazz класс значение статического поля которого будет считано
     * @param fieldName имя поля
     * @param defaultResult дефолтное возвращаемое значение
     * @param <T> тип возврата
     * @return значение поля, либо дефолтное значение
     */
    @Contract("null, _, _ -> param3")
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValueOr(Class clazz, String fieldName, T defaultResult) {
        try {
            return (T) setAccessible(getField(clazz, fieldName), true).get(null);
        } catch (Exception e) {
            return defaultResult;
        }
    }

    /**
     * Получение полей, помеченных аннотацией.
     *
     * @param clazz класс
     * @param annotation тип аннотации
     * @return сет полей
     */
    public static Set<Field> getAnnotatedFields(Class clazz, Class<? extends Annotation> annotation) {
        return getFields(clazz).stream().filter(o -> o.isAnnotationPresent(annotation)).collect(Collectors.toSet());
    }

    /**
     * Получение методов, помеченных аннотацией.
     *
     * @param clazz класс
     * @param annotation тип аннотации
     * @return сет методов
     */
    public static Set<Method> getAnnotatedMethods(Class clazz, Class<? extends Annotation> annotation) {
        return getMethods(clazz).stream().filter(o -> o.isAnnotationPresent(annotation)).collect(Collectors.toSet());
    }
}
