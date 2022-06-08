package ru.vtb.app.chat.elements;

import io.dimension.elements.api.Input;
import io.dimension.elements.mobile.ByExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ContainedInput extends Input {
    public ContainedInput(WebElement initialElement) {
        super(initialElement);
    }

    public ContainedInput(By locator) {
        super(locator);
    }

    @Override
    public ByExecutor $() {
        var element = super.$().getInitialElement().findElement(By.xpath(".//android.widget.EditText"));
        return new ByExecutor(element, getFieldName());
    }
}
