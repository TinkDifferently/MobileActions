package io.dimension.elements.api;

import io.dimension.elements.base.interfaces.Checkable;
import io.dimension.elements.mobile.AbstractMobileElement;
import io.dimension.elements.mobile.ByExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class CheckBox extends AbstractMobileElement implements Checkable {
    public CheckBox(WebElement initialElement) {
        super(initialElement);
    }

    public CheckBox(By locator) {
        super(locator);
    }

    protected CheckBox(ByExecutor element) {
        super(element);
    }

    @Override
    public boolean getState() {
        return $().getAttribute("checked").equals("true");
    }

    @Override
    public void switchState() {
        $().click();
    }
}
