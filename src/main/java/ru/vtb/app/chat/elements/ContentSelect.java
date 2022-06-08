package ru.vtb.app.chat.elements;

import io.dimension.config.session.DriverController;
import io.dimension.elements.api.Button;
import io.dimension.elements.base.interfaces.Select;
import io.dimension.elements.base.interfaces.SelectableItem;
import io.dimension.elements.mobile.AbstractMobileElement;
import io.dimension.utils.PlatformByBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.stream.Collectors;

public class ContentSelect extends AbstractMobileElement implements Select<ContentSelect.Item> {
    public ContentSelect(WebElement initialElement) {
        super(initialElement);
    }

    public ContentSelect(By locator) {
        super(locator);
    }

    static class Item extends Button implements SelectableItem {
        public Item(WebElement initialElement) {
            super(initialElement);
        }

        @Override
        public String getTitle() {
            return DriverController.getInstance().getCurrentPlatform().is(Platform.IOS)
                    ? super.getTitle()
                    : getAttribute("content-desc");
        }
    }



    @Override
    public Collection<ContentSelect.Item> getOptions() {
        return $().getInitialElement().findElements(By.xpath(".//*")).stream()
                .map(Item::new)
                .collect(Collectors.toList());
    }
}
