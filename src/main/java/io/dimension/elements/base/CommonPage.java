package io.dimension.elements.base;

import io.dimension.elements.base.annotations.Element;
import io.dimension.elements.base.annotations.Page;
import io.dimension.elements.base.annotations.Pages;
import io.dimension.exceptions.LoggedException;
import io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import io.dimension.utils.CustomLogger;
import io.dimension.utils.CustomReflection;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class CommonPage<ElementType extends BaseElement> implements IPage{

    protected final WebDriver driver;

    /**
     * Конструктор без контекста.
     *
     * @param driver инстанс драйвера.
     */
    public CommonPage(WebDriver driver) {
        try {
            waitForLoadFinished();
        } catch (Exception e) {
            CustomLogger.fail(String.format("Не удалось дождаться загрузки страницы '%s'", toString()), e);
        }
        this.driver = driver;
        PageFactory.initElements(new ElementDecorator(driver), this);
        PageHandler.getInstance().putPage(this);
    }

    /**
     * Конструктор с контекстом поиска.
     *
     * @param context контекст поиска.
     */
    public CommonPage(WebElement context) {
        try {
            waitForLoadFinished();
            if (context == null) {
                throw new ElementNotVisibleException(toString());
            }
        } catch (Exception e) {
            CustomLogger.fail(String.format("Не удалось дождаться загрузки страницы '%s'", toString()));
        }
        this.driver = WebDriverUnpackUtility.unpackWebDriverFromSearchContext(context);
        PageFactory.initElements(new ElementDecorator(context), this);
        PageHandler.getInstance().putPage(this);
    }

    /**
     * Возвращает имя page указанное в аннотации {@link Page }.
     */
    @Override
    public String toString() {
        Class<? extends CommonPage> pageClass = this.getClass();
        Page page = pageClass.isAnnotationPresent(Pages.class) ? pageClass.getAnnotation(Pages.class).value()[0]
                : pageClass.getAnnotation(Page.class);
        if (page != null) {
            return page.value();
        } else {
            return "";
        }
    }

    protected AccessibleObject getMemberByName(String name) {
        Set<Field> fieldSet = Arrays.stream(this.getClass().getDeclaredFields()).collect(Collectors.toSet());
        fieldSet.addAll(Arrays.stream(this.getClass().getFields()).collect(Collectors.toSet()));
        Optional<Field> field = fieldSet.stream()
                .filter(f -> f.isAnnotationPresent(Element.class)
                        && f.getDeclaredAnnotation(Element.class).value().equals(name))
                .findFirst();
        if (field.isPresent()) {
            field.get().setAccessible(true);
            return field.get();
        }
        Method method=CustomReflection.getAnnotatedMethods(this.getClass(),Element.class).stream()
                .filter(o->o.getAnnotation(Element.class).value().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new LoggedException(
                        String.format(
                                "Элемент с именем \"%s\" не описан на странице \"%s\"", name, this.toString())));
        method.setAccessible(true);
        return method;
    }

    /**
     * Возвращает элемент по имени, указанному в аннотации {@link Element}.
     *
     * @param name имя элемента
     * @param <T>  тип элемента
     */
    @SuppressWarnings("unchecked")
    public <T> T getElement(String name) {
        try {
            AccessibleObject member = getMemberByName(name);
            T element = (T) (member.getClass().isAssignableFrom(Field.class)
                    ? ((Field) member).get(this)
                    : ((Method) member).invoke(this));
            if (element instanceof BaseElement) {
                ((BaseElement) element).setFieldName(name);
            }
            return element;
        } catch (IllegalAccessException | InvocationTargetException e) {
            CustomLogger.fail("Ошибка получения элемента", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Возвращает список элементов по его (списка) имени,
     * указанному в аннотации {@link Element }.
     *
     * @param name имя элемента
     */
    @SuppressWarnings("unchecked")
    public <T extends ElementType> List<T> getElements(String name) {
        try {
            AccessibleObject member = getMemberByName(name);
            return (List) (member.getClass().isAssignableFrom(Field.class)
                    ? ((Field) member).get(this)
                    : ((Method) member).invoke(this));
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод, выполняемый после окончания работы со страницей.
     *
     * @param args аргументы исполняемого метода
     */
    public void afterClose(Object... args) {
        //void
    }
}
