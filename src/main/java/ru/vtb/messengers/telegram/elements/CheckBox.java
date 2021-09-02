package ru.vtb.messengers.telegram.elements;

import config.elements.mobile.AbstractMobileElement;
import config.elements.mobile.ByExecutor;
import config.session.DriverController;
import exceptions.NotSupportedPlatformException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CheckBox extends AbstractMobileElement {

    public CheckBox(WebElement initialElement) {
        super(initialElement);
    }

    public CheckBox(By locator) {
        super(locator);
    }

    protected CheckBox(ByExecutor element) {
        super(element);
    }

    public void check() {
        if (!getState())
            click();
    }

    public void uncheck() {
        if (getState())
            click();
    }

    public void check(boolean state) {
        if (getState() != state)
            click();
    }

    public boolean getState() {
        switch (DriverController.getInstance().getCurrentPlatform()) {
            case ANDROID:
                return Boolean.valueOf(getAttribute("checked"));
            case IOS:
                String value=getAttribute("value");
                return "1".equals(value);
            default:
                throw new NotSupportedPlatformException();
        }
    }

    public void switchState() {
        click();
    }
}
