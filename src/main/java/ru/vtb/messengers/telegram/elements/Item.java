package ru.vtb.messengers.telegram.elements;

import config.elements.common.interfaces.HasTitle;
import config.elements.common.interfaces.IClickable;
import config.elements.mobile.AbstractMobileElement;
import config.elements.mobile.ByExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Item extends AbstractMobileElement implements IClickable, HasTitle {
    public Item(WebElement initialElement) {
        super(initialElement);
    }

    public Item(By locator) {
        super(locator);
    }

    protected Item(ByExecutor element) {
        super(element);
    }

    @Override
    public void click() {
        super.click();
    }

    @Override
    public String getTitle() {
        return getInitialElement().getText();
    }
}
