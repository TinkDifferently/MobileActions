package io.dimension.elements.base;

public interface IPage {
    /**
     * Метод ожидания загрузки страницы.
     * Должен быть переопределен в наследниках, если необходимо
     */
    void waitForLoadFinished();

    <T> T getElement(String name);

    void afterClose(Object... args);
}
