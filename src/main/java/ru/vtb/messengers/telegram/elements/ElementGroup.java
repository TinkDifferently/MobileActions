package ru.vtb.messengers.telegram.elements;

import config.elements.mobile.AbstractMobileElement;
import config.elements.mobile.ByExecutor;
import exceptions.LoggedException;
import exceptions.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class ElementGroup<I extends AbstractMobileElement> extends AbstractMobileElement {
    protected List<I> collection;

    protected abstract List<I> getCollection();

    public ElementGroup(By locator) {
        super(locator);
    }

    @Override
    public String getTagName() {
        throw new NotImplementedException("Невозможно взаимодействовать с коллекцией");
    }

    @Override
    public ByExecutor getInitialElement() {
        throw new NotImplementedException("Невозможно взаимодействовать с коллекцией");
    }

    @Override
    public boolean isEnabled() {
        throw new NotImplementedException("Невозможно взаимодействовать с коллекцией");
    }

    @Override
    public String getAttribute(String attribute) {
        throw new NotImplementedException("Невозможно взаимодействовать с коллекцией");
    }

    @Override
    public boolean isDisplayed() {
        throw new NotImplementedException("Невозможно взаимодействовать с коллекцией");
    }

    @Override
    protected String getObjectName() {
        throw new NotImplementedException("Невозможно взаимодействовать с коллекцией");
    }

    public I getElement(int index) {
        return getCollection().get(index);
    }

    public void withElement(Predicate<I> filter, @NotNull Consumer<I> action) {
        action.accept(getElement(filter));
    }

    public I getElement(Predicate<I> filter) {
        return getCollection().stream().filter(filter).findFirst().orElseThrow(LoggedException::new);
    }

    public <T> T withElement(Predicate<I> filter, @NotNull Function<I, T> function) {
        return function.apply(getElement(filter));
    }

    public void forEach(Consumer<I> consumer) {
        getCollection().forEach(consumer);
    }
}
