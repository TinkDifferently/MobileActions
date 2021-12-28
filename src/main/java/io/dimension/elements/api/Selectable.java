package io.dimension.elements.api;

import io.dimension.elements.base.interfaces.SelectableItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Selectable extends Button implements SelectableItem {
    public Selectable(WebElement initialElement) {
        super(initialElement);
    }

    public Selectable(By locator) {
        super(locator);
    }
}
