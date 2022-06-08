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
import io.dimension.utils.PlatformByBuilder;
import io.dimension.utils.PlatformElementsBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class Messages extends AbstractMobileElement implements Select<Messages.Item> {

    public static class Item extends Button implements SelectableItem {
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
            WebElement element;
            var driver = DriverController.getInstance().getDriver();
            if (DriverController.getInstance().getCurrentPlatform().is(Platform.IOS)) {
                var elements = $().getInitialElement().findElements(By.xpath(".//*[@name]"));
                element = elements.size() == 1
                        ? elements.get(0)
                        : $().getInitialElement().findElement(MobileBy.xpath(".//XCUIElementTypeTextView/XCUIElementTypeTextView"));
            } else {
                var elements = $().getInitialElement().findElements(By.xpath(".//android.widget.ImageView"));
                element = elements.size() > 0
                        ? elements.get(0)
                        : $().getInitialElement().findElement(By.xpath(".//android.widget.TextView"));
            }
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
        return PlatformElementsBuilder.create()
                .mapper(o -> new Messages.Item((WebElement) o))
                .android(By.id("ru.vtb.mobilebanking.android.rc:id/message"))
                .iOS(By.xpath("./XCUIElementTypeCell"))
                .build($().getInitialElement());
    }

    public Collection<Messages.Item> getImages() {
        return PlatformElementsBuilder.create()
                .mapper(o -> new Messages.Item((WebElement) o))
                .android(By.id("ru.vtb.mobilebanking.android.rc:id/imagePreview"))
                .iOS(By.xpath("./XCUIElementTypeCell"))
                .build($().getInitialElement());
    }
}

