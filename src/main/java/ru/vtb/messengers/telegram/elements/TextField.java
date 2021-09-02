package ru.vtb.messengers.telegram.elements;

import config.elements.common.interfaces.IClickable;
import config.elements.mobile.AbstractMobileElement;
import config.elements.mobile.ByExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.CustomLogger;

public class TextField extends AbstractMobileElement implements IClickable {
    public TextField(WebElement initialElement) {
        super(initialElement);
    }

    public TextField(By locator) {
        super(locator);
    }

    protected TextField(ByExecutor element) {
        super(element);
    }

    public void setText(String text) {
        CustomLogger.info(String.format("Ввод текста в поле '%s'", this.getFieldName()));
        try {
            getInitialElement().sendKeys(text);
            ;
        } catch (Exception e) {
            CustomLogger.fail("Не удалось ввести текст", e);
        }
    }

    @Override
    public void click() {
        CustomLogger.info(String.format("Клик по элементу '%s'", this.getFieldName()));
        try {
            super.click();
        } catch (Exception e) {
            CustomLogger.fail("Не удалось кликнуть по элементу", e);
        }
    }
}
