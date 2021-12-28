package io.dimension.elements.mobile;

import io.dimension.elements.base.BaseElement;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import io.dimension.utils.CustomLogger;

import java.util.Objects;

/**
 * Абстрактный класс для кастомных элементов.
 */
public abstract class AbstractMobileElement extends BaseElement {
    @Override
    public String getTagName() {
        return byExecutor.isEmpty() ? "" : byExecutor.getTagName();
    }

    protected ByExecutor byExecutor;

    /**
     * Конструктор.
     *
     * @param initialElement элемент, который следует обернуть
     */
    public AbstractMobileElement(WebElement initialElement) {
        this.byExecutor = new ByExecutor(initialElement, fieldName);
    }

    /**
     * Конструктор.
     *
     * @param locator локатор для поиска обертываемого элемента
     */
    public AbstractMobileElement(By locator) {
        this.byExecutor = new ByExecutor(locator, fieldName);
    }

    protected AbstractMobileElement(ByExecutor element) {
        this.byExecutor = element;
    }

    /**
     * Возвращает веб-элемент, с которым был инициализирован текущий кастомный элемент.
     *
     * @return веб-элемент.
     */
    public ByExecutor $() {
        return byExecutor;
    }

    /**
     * Находимтся ли элемент во "включенном состоянии".
     *
     * @return true, если состояние велюченное.
     */
    public boolean isEnabled() {
        return this.byExecutor.isEnabled();
    }

    /**
     * Получает атрибут элемента для унификации.
     *
     * @param attribute имя аттрибута
     * @return знаечние аттрибута
     */
    public String getAttribute(String attribute) {
        return byExecutor.getAttribute(attribute);
    }

    /**
     * Видимость элемента на экране.
     *
     * @return true, если видимый
     */
    public boolean isDisplayed() {
        try {
            if (byExecutor.isEmpty()) {
                return false;
            }
            return byExecutor.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Возвращает имя объекта в системе.
     *
     * @return Строка с именем объекта (если существует) или пустая строка
     */
    protected String getObjectName() {
        try {
            if (fieldName != null) {
                return fieldName;
            }
            return byExecutor.getText();
        } catch (Exception ignored) {
            return "";
        }
    }

    /**
     * Выполнение клика по элементу.
     */
    protected void click() {
        if (byExecutor.isEmpty()) {
            String message = "Не удалось выполнить клик по элементу";
            if (!StringUtils.isEmpty(fieldName))
                message += String.format(" '%s'", fieldName);
            CustomLogger.fail(message);
        }
        try {
            byExecutor.click();
        } catch (NoSuchElementException | ElementNotVisibleException e) {
            CustomLogger.fail(String.format("Элемент '%s' недоступен", fieldName));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o == this)
            return true;
        if (!(o instanceof AbstractMobileElement))
            return false;
        AbstractMobileElement e = (AbstractMobileElement) o;
        return Objects.equals(e.$(), $());
    }
}
