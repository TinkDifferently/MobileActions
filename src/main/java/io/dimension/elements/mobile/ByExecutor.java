package io.dimension.elements.mobile;

import io.dimension.config.session.DriverUtils;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import io.dimension.utils.CustomLogger;

/**
 * примитивный декоратор WebElement для переопределений поведения
 */
public final class ByExecutor {
    private By locator;
    private WebElement found;
    private final boolean isLocated;
    private final String title;

    @Contract(pure = true)
    ByExecutor(By by, String title) {
        this.locator = by;
        this.title = title;
        isLocated = true;
    }

    @Contract(pure = true)
    ByExecutor(WebElement element, String title) {
        this.title = title;
        isLocated = false;
        this.found = element;
    }

    private WebElement getElement(int timeout) {
        if (isLocated) {
            if (locator == null) {
                CustomLogger.fail("Пустой локатор");
            }
            found = timeout==-1? DriverUtils.findNullableElement(locator) :DriverUtils.waitFor(timeout,driver->driver.findElement(locator));
        }
        if (found==null){
            throw new AssertionError(String.format("Не удалось найти элемент '%s'", title));
        }
        return found;
    }

    private WebElement getElement(){
        return getElement(-1);
    }

    boolean isEmpty() {
        return getElement() == null;
    }

    public String getTagName() {
        return getElement().getTagName();
    }

    public boolean isEnabled() {
        return getElement().isEnabled();
    }

    public String getAttribute(String attribute) {
        return getElement().getAttribute(attribute);
    }

    public boolean isDisplayed() {
        return getElement().isDisplayed();
    }

    public String getText() {
        return getElement().getText();
    }

    public void click() {
        getElement().click();
    }

    public WebElement getInitialElement() {
        return getElement();
    }

    public WebElement getInitialElement(int timeout) {
        return getElement(timeout);
    }

    public Point getLocation() {
        return getElement().getLocation();
    }

    @Contract(pure = true)
    public By getLocator() {
        return locator;
    }

    public void sendKeys(String keys) {
        getElement().sendKeys(keys);
    }

    public void sendKeysAsMobile(String text) {
        ((io.appium.java_client.android.AndroidElement) getElement()).replaceValue(text);
    }
}
