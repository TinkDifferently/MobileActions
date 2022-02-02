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

public class NavigationMenu extends AbstractMobileElement implements Select<NavigationMenu.Item> {

    static class Item extends Button implements SelectableItem {
        public Item(WebElement initialElement) {
            super(initialElement);
        }

        @Override
        public String getTitle() {
            return DriverController.getInstance().getCurrentPlatform() == Platform.IOS
                    ? $().getAttribute("name")
                    : super.getTitle();
        }
    }

    public NavigationMenu(By locator) {
        super(locator);
    }

    public NavigationMenu(WebElement initialElement) {
        super(initialElement);
    }

    @Override
    public Collection<NavigationMenu.Item> getOptions() {
        return $().getInitialElement().findElements(By.xpath(".//*")).stream().map(NavigationMenu.Item::new)
                .collect(Collectors.toUnmodifiableSet());
    }
}

