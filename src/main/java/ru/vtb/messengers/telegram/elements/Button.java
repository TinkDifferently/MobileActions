package ru.vtb.messengers.telegram.elements;

import io.dimension.elements.base.interfaces.Clickable;
import io.dimension.elements.mobile.AbstractMobileElement;
import org.openqa.selenium.By;
import io.dimension.utils.CustomLogger;

public class Button extends AbstractMobileElement implements Clickable {

    public Button(By locator) {
        super(locator);
    }

    @Override
    public void click() {
        CustomLogger.info(String.format("Клик по элементу '%s'",this.getFieldName()));
        try {
            super.click();
        } catch (Exception e){
            CustomLogger.fail("Не удалось кликнуть по элементу",e);
        }
    }
}
