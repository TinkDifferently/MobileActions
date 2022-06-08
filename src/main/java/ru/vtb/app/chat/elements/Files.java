package ru.vtb.app.chat.elements;

import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.ElementOption;
import io.dimension.config.session.DriverController;
import io.dimension.elements.api.Select;
import io.dimension.elements.api.Selectable;
import io.dimension.elements.base.interfaces.LongPress;
import io.dimension.elements.base.interfaces.SelectableItem;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.stream.Collectors;

public class Files extends Select {

    private static class File extends Selectable implements LongPress {

        public File(WebElement initialElement) {
            super(initialElement);
        }

        @Override
        public void longPress() {
            var element = $().getInitialElement();
            new TouchAction<>((PerformsTouchActions) DriverController.getInstance().getDriver())
                    .longPress(ElementOption.element(element))
                    .perform();
        }
    }

    public Files(WebElement initialElement) {
        super(initialElement);
    }

    public Files(By locator) {
        super(locator);
    }

    @Override
    public Collection<SelectableItem> getOptions() {
        var platform = DriverController.getInstance().getCurrentPlatform();
        switch (platform) {
            case IOS:
                return $().getInitialElement().findElements(By.xpath(".//XCUIElementTypeImage")).stream()
                        .map(Selectable::new).collect(Collectors.toUnmodifiableList());
            case ANDROID:
                return $().getInitialElement().findElements(By.id("android:id/title"))
                        .stream().map(File::new).collect(Collectors.toUnmodifiableList());
            default:
                throw new RuntimeException();
        }
    }
}
