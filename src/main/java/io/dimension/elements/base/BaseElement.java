package io.dimension.elements.base;

import io.dimension.elements.base.interfaces.IEnabled;
import io.dimension.utils.CustomLogger;

public abstract class BaseElement implements IEnabled {

    protected String fieldName;

    @SuppressWarnings("unchecked")
    public <T> T as(Class<T> type) {
        if (!type.isInstance(this)) {
            CustomLogger.fail(String.format("Ошибка преобразования классов. Объявление '%s' несовместимо с '%s'",
                    this.getClass(), type.getSimpleName()));
        }
        return (T) this;
    }

    /**
     * Устанавливает имя для поля в allure-отчетах.
     *
     * @param name имя поля
     */
    public void setFieldName(String name) {
        fieldName = name;
    }

    /**
     * получает аннотированное имя элемента.
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Видимость элемента на экране.
     *
     * @return true, если видимый
     */
    public abstract boolean isDisplayed();

    /**
     * Находимтся ли элемент во "включенном состоянии".
     *
     * @return true, если состояние велюченное.
     */
    public abstract boolean isEnabled();

    /**
     * получает атрибут элемента для унификации добавлено несколько псевдоаттрибутов (текст,индекс).
     *
     * @param attribute имя аттрибута
     * @return знаечние аттрибута
     */
    public abstract String getAttribute(String attribute);

    protected String getAnnotatedName() {
        return this.fieldName;
    }

    public abstract String getTagName();
}
