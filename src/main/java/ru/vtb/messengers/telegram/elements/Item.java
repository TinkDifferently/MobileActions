package ru.vtb.messengers.telegram.elements;

import io.dimension.elements.base.interfaces.HasTitle;
import io.dimension.elements.base.interfaces.Clickable;
import io.dimension.elements.mobile.AbstractMobileElement;
import io.dimension.elements.mobile.ByExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Item extends AbstractMobileElement implements Clickable, HasTitle {
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
        return $().getText();
    }
}
