package ru.vtb.messengers.telegram.elements;

import config.elements.common.interfaces.IClickable;
import config.elements.mobile.AbstractMobileElement;
import org.openqa.selenium.By;
import utils.CustomLogger;

public class Button extends AbstractMobileElement implements IClickable {

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
