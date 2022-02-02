package ru.vtb.app.chat.elements;

import io.dimension.config.session.DriverController;
import io.dimension.elements.api.Button;
import io.dimension.elements.base.interfaces.Select;
import io.dimension.elements.base.interfaces.SelectableItem;
import io.dimension.elements.mobile.AbstractMobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.stream.Collectors;

public class Cards extends AbstractMobileElement implements Select<Cards.Item> {
    public Cards(WebElement initialElement) {
        super(initialElement);
    }

    public Cards(By locator) {
        super(locator);
    }

    @Override
    public Collection<Item> getOptions() {
        return $().getInitialElement().findElements(By.xpath("./XCUIElementTypeCell")).stream()
                .map(Item::new).collect(Collectors.toList());
    }

    static class Item extends Button implements SelectableItem {
        public Item(WebElement initialElement) {
            super(initialElement);
        }

        @Override
        public String getTitle() {
            return DriverController.getInstance().getCurrentPlatform() == Platform.IOS
                    ? $().getInitialElement().findElement(By.xpath(".//XCUIElementTypeStaticText[@index=1]")).getText()
                    : super.getTitle();
        }
    }
}
