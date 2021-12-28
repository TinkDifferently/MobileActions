package io.dimension.elements.mobile;

import io.dimension.elements.base.CommonPage;
import io.dimension.elements.base.annotations.Element;
import io.dimension.config.session.DriverController;
import org.openqa.selenium.WebElement;
import io.dimension.utils.CustomLogger;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Базовый класс страниц.
 */
public abstract class AbstractMobilePage extends CommonPage<AbstractMobileElement> {

    public AbstractMobilePage() {
        super(DriverController.getInstance().getDriver());
    }

    public AbstractMobilePage(WebElement context) {
        super(context);
    }

    /**
     * Возвращает элемент по имени, указанному в аннотации {@link Element},
     * не останавливая работу проограммы. (Логирование на уровне предупреждения).
     *
     * @param name имя элемента
     * @param <T>  тип элемента.
     */
    //todo make fix
    @SuppressWarnings("unchecked")
    public <T> T getElementNotStrict(String name) {
        try {
            AccessibleObject member = getMemberByName(name);
            if ((member.getClass().isAssignableFrom(Field.class))) {
                Object field = ((Field) member).get(this);
                ((AbstractMobileElement) field).setFieldName(name);
                return (T) field;
            } else {
                return (T) ((Method) member).invoke(this);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            CustomLogger.warn("Элемент отсутствует");
            throw new RuntimeException(e);
        }
    }

    /**
     * Возвращает список элементов по его (списка) имени,
     * указанному в аннотации {@link Element }.
     *
     * @param name имя элемента
     * @param <T>  тип элемента
     */
    @SuppressWarnings("unchecked")
    public <T extends AbstractMobileElement> List<T> getElements(String name) {
        return super.getElements(name);
    }
}
