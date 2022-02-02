package io.dimension.elements.api;

import io.dimension.elements.base.interfaces.HasText;
import io.dimension.elements.base.interfaces.Editable;
import io.dimension.elements.mobile.AbstractMobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Input extends AbstractMobileElement implements HasText, Editable {
    public Input(WebElement initialElement) {
        super(initialElement);
    }

    public Input(By locator) {
        super(locator);
    }

    @Override
    public String getText() {
        return $().getText();
    }

    @Override
    public void setText(String text) {
        $().sendKeys(text);
    }

    @Override
    public void clear() {
        $().getInitialElement().clear();
    }
}
