package io.dimension.utils;

import io.dimension.elements.mobile.AbstractMobileElement;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlatformElementsBuilder<T extends AbstractMobileElement> {
    private Function<WebElement, T> mapper;
    private final PlatformByBuilder platformByBuilder;

    private PlatformElementsBuilder() {
        platformByBuilder = PlatformByBuilder.create();
    }

    public static PlatformElementsBuilder create() {
        return new PlatformElementsBuilder();
    }

    public PlatformElementsBuilder mapper(Function<WebElement, T> mapper) {
        this.mapper = mapper;
        return this;
    }

    public PlatformElementsBuilder iOS(By by) {
        platformByBuilder.iOS(by);
        return this;
    }

    public PlatformElementsBuilder android(By by) {
        platformByBuilder.android(by);
        return this;
    }

    public List<T> build(@NotNull WebElement context) {
        var by = platformByBuilder.build();
        return context.findElements(by).stream().map(mapper)
                .collect(Collectors.toUnmodifiableList());
    }


}
