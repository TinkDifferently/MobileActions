package ru.vtb.app.chat.elements;

import io.appium.java_client.MobileBy;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.offset.PointOption;
import io.dimension.config.session.DriverController;
import io.dimension.elements.api.Button;
import io.dimension.elements.base.interfaces.Select;
import io.dimension.elements.base.interfaces.SelectableItem;
import io.dimension.elements.mobile.AbstractMobileElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.stream.Collectors;

public class Messages extends AbstractMobileElement implements Select<Messages.Item> {

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

        @Override
        public void click() {
            var driver = DriverController.getInstance().getDriver();
            var elements = $().getInitialElement().findElements(By.xpath(".//*[@name]"));
            var element = elements.size() == 1
                    ? elements.get(0)
                    : $().getInitialElement().findElement(MobileBy.xpath(".//XCUIElementTypeTextView/XCUIElementTypeTextView"));
            var rect = element.getRect();
            var point = new Point(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
            var pointOption = PointOption.point(point);
            var action = new TouchAction<>((PerformsTouchActions) driver);
            action.longPress(LongPressOptions.longPressOptions().withPosition(pointOption)).release().perform();
        }
    }

    public Messages(By locator) {
        super(locator);
    }

    public Messages(WebElement initialElement) {
        super(initialElement);
    }

    @Override
    public Collection<Messages.Item> getOptions() {
        return $().getInitialElement().findElements(By.xpath("./XCUIElementTypeCell")).stream().map(Messages.Item::new)
                .collect(Collectors.toUnmodifiableList());
    }
}

