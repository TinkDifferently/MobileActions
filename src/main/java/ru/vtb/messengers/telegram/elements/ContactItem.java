package ru.vtb.messengers.telegram.elements;

import io.dimension.elements.mobile.ByExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ContactItem extends Item{
    public ContactItem(WebElement initialElement) {
        super(initialElement);
    }

    public ContactItem(By locator) {
        super(locator);
    }

    protected ContactItem(ByExecutor element) {
        super(element);
    }

    @Override
    public String getTitle() {
        return super.getTitle();
    }
}
