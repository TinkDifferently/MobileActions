package ru.vtb.messengers.telegram.elements;

import config.elements.common.interfaces.HasText;
import config.elements.mobile.AbstractMobileElement;
import config.elements.mobile.ByExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Label extends AbstractMobileElement implements HasText {
    public Label(WebElement initialElement) {
        super(initialElement);
    }

    public Label(By locator) {
        super(locator);
    }

    protected Label(ByExecutor element) {
        super(element);
    }

    @Override
    public String getText() {
        return byExecutor.getText();
    }
}
