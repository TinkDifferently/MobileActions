package ru.vtb.messengers.telegram.elements;

import config.elements.mobile.AbstractMobileElement;
import config.elements.mobile.ByExecutor;
import config.session.DriverController;
import exceptions.NotImplementedException;
import exceptions.NotSupportedPlatformException;
import org.jetbrains.annotations.NotNull;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.CustomLogger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class DateScroller extends AbstractMobileElement {

    public DateScroller(WebElement initialElement) {
        super(initialElement);
    }

    public DateScroller(By locator) {
        super(locator);
    }

    protected DateScroller(ByExecutor element) {
        super(element);
    }

    private void pickDay(String day) {
        switch (DriverController.getInstance().getCurrentPlatform()) {
            case IOS:
                iosPickDay(day);
                break;
            case ANDROID:
                androidPickDay(day);
                break;
            default:
                throw new NotSupportedPlatformException();
        }
    }

    private void pickDate(String date) {
        switch (DriverController.getInstance().getCurrentPlatform()) {
            case IOS:
                iosPickDate(date);
                break;
            case ANDROID:
                androidPickDate(date);
                break;
            default:
                throw new NotSupportedPlatformException();
        }
    }

    private void iosPickDate(String date) {
        getInitialElement().click();
        var wheels = new WebDriverWait(DriverController.getInstance().getDriver(), 10).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("XCUIElementTypePickerWheel")));
        //WebElement dayWheel=wheels.stream().filter(o->o.getText().length()<3).findFirst().get();
        //WebElement monthWheel=wheels.stream().filter(o->o.getText().matches("[^0-9]+")).findFirst().get();
        //WebElement yearWheel=wheels.stream().filter(o->o.getText().matches("\\d{4}")).findFirst().get();
        var localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        wheels.get(0).sendKeys(String.valueOf(localDate.getDayOfMonth()));
//        wheels.get(1).sendKeys(localDate.getMonth().getDisplayName(TextStyle.FULL,new Locale("ru")));
        wheels.get(2).sendKeys(String.valueOf(localDate.getYear()));
    }

    private void androidPickDate(String date) {
    }

    private void iosPickDay(String day) {
        var pickerEls = new WebDriverWait(DriverController.getInstance().getDriver(), 10).until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("XCUIElementTypePickerWheel")));
        var textElement = pickerEls.get(0);
        textElement.sendKeys(day);
    }

    private void androidPickDay(String day) {
        var textElement = getInitialElement().getInitialElement().findElement(By.xpath(".//android.widget.EditText"));
        var targetDate = Integer.parseInt(day);
        var rectangle = getInitialElement().getInitialElement().getRect();
        var highY = rectangle.getY() + rectangle.getHeight() / 10;
        var lowY = rectangle.getY() + rectangle.getHeight() * 9 / 10;
        var centerY = rectangle.getY() + rectangle.getHeight() / 2;
        var x = rectangle.getX() + rectangle.getWidth() / 2;
        for (var i = 0; i < 100; i++) {
            var currentDate = Integer.parseInt(textElement.getText());
            if (currentDate == targetDate) {
                return;
            }
            TouchAction<?> action = targetDate < currentDate
                    ? new TouchAction<>((PerformsTouchActions) DriverController.getInstance().getDriver())
                    .press(PointOption.point(x, lowY))
                    .moveTo(PointOption.point(x, centerY))
                    : new TouchAction<>((PerformsTouchActions) DriverController.getInstance().getDriver())
                    .press(PointOption.point(x, highY))
                    .moveTo(PointOption.point(x, centerY));
            action.perform().release();

        }
        CustomLogger.fail("Не удалось выполнить установку даты");
    }

    public void setDate(@NotNull String date) {
        var local = date.split(":");
        var realDate = String.join(":", Arrays.copyOfRange(local, 1, local.length));
        switch (local[0].toLowerCase()) {
            case "day":
                pickDay(realDate);
                return;
            case "date":
                pickDate(realDate);
                return;
            default:
                throw new NotImplementedException("Неизвестный тип даты");
        }
    }
}
