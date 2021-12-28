package io.dimension.elements.api;

import io.dimension.elements.base.interfaces.Clickable;
import io.dimension.elements.base.interfaces.HasTitle;
import io.dimension.elements.mobile.AbstractMobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Button extends AbstractMobileElement implements Clickable, HasTitle {
    public Button(WebElement initialElement) {
        super(initialElement);
    }

    @Override
    public void click() {
        $().click();
    }

    public Button(By locator) {
        super(locator);
    }

    @Override
    public String getTitle() {
        return $().getText();
    }
}
