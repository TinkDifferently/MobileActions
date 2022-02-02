package ru.vtb.app.chat.elements;

import io.dimension.config.session.DriverController;
import io.dimension.elements.api.Select;
import io.dimension.elements.api.Selectable;
import io.dimension.elements.base.interfaces.SelectableItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.stream.Collectors;

public class Images extends Select {
    public Images(WebElement initialElement) {
        super(initialElement);
    }

    public Images(By locator) {
        super(locator);
    }

    @Override
    public Collection<SelectableItem> getOptions() {
        var platform = DriverController.getInstance().getCurrentPlatform();
        switch (platform){
            case IOS:
                return $().getInitialElement().findElements(By.xpath(".//XCUIElementTypeImage")).stream()
                        .map(Selectable::new).collect(Collectors.toUnmodifiableSet());
            case ANDROID:
                return super.getOptions();
            default:
                throw new RuntimeException();
        }
    }
}
