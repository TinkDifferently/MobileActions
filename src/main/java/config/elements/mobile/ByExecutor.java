package config.elements.mobile;

import config.session.DriverUtils;
import io.appium.java_client.MobileElement;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.By;
import org.openqa.selenium.DeviceRotation;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import utils.CustomLogger;

/**
 * примитивный декоратор WebElement для переопределений поведения
 */
public final class ByExecutor {
    private By locator;
    private WebElement found;
    private boolean isLocated;

    @Contract(pure = true)
    ByExecutor(By by) {
        this.locator = by;
        isLocated = false;
    }

    @Contract(pure = true)
    ByExecutor(WebElement element) {
        isLocated = true;
        this.found = element;
    }

    private WebElement getElement() {
        if (!isLocated) {
            if (locator == null)
                CustomLogger.fail("Пустой локатор");
            found = DriverUtils.findNullableElement(locator);
            isLocated = true;
        }
        return found;
    }

    @Contract(pure = true)
    ByExecutor() {
        locator = null;
        isLocated = false;
        found = null;
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
