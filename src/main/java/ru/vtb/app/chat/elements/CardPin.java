package ru.vtb.app.chat.elements;

import io.dimension.config.session.DriverController;
import io.dimension.elements.api.Button;
import io.dimension.elements.base.interfaces.Select;
import io.dimension.elements.base.interfaces.SelectableItem;
import io.dimension.elements.mobile.AbstractMobileElement;
import io.dimension.elements.mobile.ByExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.stream.Collectors;

public class CardPin extends AbstractMobileElement implements Select<CardPin.PinCodeItem> {

    public CardPin(WebElement initialElement) {
        super(initialElement);
    }

    public CardPin(By locator) {
        super(locator);
    }

    protected CardPin(ByExecutor element) {
        super(element);
    }

    static class PinCodeItem extends Button implements SelectableItem {
        public PinCodeItem(WebElement initialElement) {
            super(initialElement);
        }
    }


    @Override
    public Collection<CardPin.PinCodeItem> getOptions() {
        By by= DriverController.getInstance().getCurrentPlatform()== Platform.IOS
                ? By.xpath(".//XCUIElementTypeKey")
                : By.xpath(".//android.widget.Button");
        return $().getInitialElement().findElements(by).stream().map(CardPin.PinCodeItem::new)
                .collect(Collectors.toUnmodifiableSet());
    }
}
