package ru.vtb.app.chat.actions;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.connection.ConnectionState;
import io.appium.java_client.android.connection.ConnectionStateBuilder;
import io.dimension.actions.PageAction;
import io.dimension.actions.annotations.ActionProvider;
import io.dimension.actions.annotations.MobileAction;
import io.dimension.config.session.DriverController;
import io.dimension.elements.base.interfaces.Clickable;
import io.dimension.elements.base.interfaces.Select;
import io.qameta.allure.Step;

import java.time.Duration;

@ActionProvider("Устройство")
public class DeviceAction extends PageAction {

    @MobileAction(value = "Отключить Wi-Fi")
    @Step("Отключение Wi-Fi")
    void toggleWifiOff() {
        AndroidDriver<?> driver= (AndroidDriver<?>) DriverController.getInstance().getDriver();
        ConnectionState state = driver.setConnection(new ConnectionStateBuilder().withWiFiDisabled().build());
//        Assert.assertFalse(state.isWiFiEnabled(), "Wifi is not switched off");
    }

    @MobileAction(value = "Включить Wi-Fi")
    @Step("Включение Wi-Fi")
    void toggleWiFiOn() {
        AndroidDriver<?> driver= (AndroidDriver<?>) DriverController.getInstance().getDriver();
        ConnectionState state = driver.setConnection(new ConnectionStateBuilder().withWiFiEnabled().build());
//        Assert.assertFalse(state.isWiFiEnabled(), "Wifi is not switched off");
    }

    @MobileAction(value = "Запустить приложение в фоновом режиме")
    @Step("Приложение свернуто")
    void runInBackground() {
        AndroidDriver<?> driver= (AndroidDriver<?>) DriverController.getInstance().getDriver();
        driver.runAppInBackground(Duration.ofSeconds(10));
    }

    @MobileAction(value = "Сделать фотографию")
    @Step("Фотографирование")
    void makePhoto() {
        switchTo("Изображения");
        element("Сфотографировать").as(Clickable.class)
                .click();
        element("Подтвердить").as(Select.class)
                .select("Ок");
        switchBack();
    }



}
