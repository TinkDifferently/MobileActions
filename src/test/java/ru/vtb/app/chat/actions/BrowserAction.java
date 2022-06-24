package ru.vtb.app.chat.actions;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.dimension.actions.PageAction;
import io.dimension.actions.annotations.ActionProvider;
import io.dimension.actions.annotations.AndroidAction;
import io.dimension.actions.annotations.MobileAction;
import io.dimension.actions.annotations.Parameter;
import io.dimension.config.session.DriverController;
import io.dimension.elements.base.interfaces.Clickable;
import io.dimension.elements.base.interfaces.Editable;
import io.dimension.elements.base.interfaces.HasText;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.vtb.app.chat.data.Context;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;


@ActionProvider("Браузер")
public class BrowserAction extends PageAction {
    @AndroidAction(value = "Открыть ссылку")
    @Step("Открыта ссылка {0}")
    void openLink(@Parameter("Ссылка") String link) throws InterruptedException {
        switchTo("Firefox");
        element("Url").as(Clickable.class)
                .click();
        element("Url редактируемый").as(Editable.class)
                .setText(link);
        ((AndroidDriver) DriverController.getInstance().getDriver()).pressKey(new KeyEvent(AndroidKey.ENTER));
        TimeUnit.SECONDS.sleep(5);
    }

    @MobileAction(value = "Ссылка устарела")
    @Step("Проверка сообщения об устаревании ссылки")
    void checkLinkExpiredMessage() {
        element("Ссылка устарела").waitFor(15);
        element("Ссылка устарела").as(HasText.class)
                .getText();
    }

    @MobileAction(value = "Ввести номер телефона")
    @Step("Введен номер телефона {0}")
    void checkLinkExpiredMessage(@Parameter("Номер телефона") String phoneNumber) {
        element("Номер телефона").waitFor(5);
        element("Номер телефона").as(Editable.class)
                .setText(phoneNumber);
        element("Продолжить").as(Clickable.class)
                .click();
    }

    @MobileAction(value = "Открыть диалог")
    @Step("Открыт диалог с ботом")
    void openDialog() {
        String contour = Context.context.getContour();
        if (contour.equals("k4")) {
            element("Диалог k4").as(Clickable.class)
                    .click();
        } else throw new RuntimeException();
    }

    @MobileAction(value = "Отправить сообщение")
    @Step("Отправлено сообщение {0}")
    void sendMessage(@Parameter("Сообщение") @NotNull String message) {
        element("Сообщение для отправки").as(Editable.class)
                .setText(message);
        element("Отправить сообщение").as(Clickable.class)
                .click();
    }

    @MobileAction(value = "Зарегистрироваться")
    @Step("Пользователь зарегистрирован")
    void register() {
        var number = ThreadLocalRandom.current().nextLong(2924292177L, 9924292177L);
        element("Номер телефона").as(Editable.class)
                .setText(""+number);
        element("Продолжить").as(Clickable.class)
                .click();
    }

    @AndroidAction(value = "Перейти по ссылке")
    @Step("Сохранена ссылка")
    void storeLink() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        element("Назад").as(Clickable.class)
                .click();
        openDialog();
        String result = DriverController.getInstance().getDriver()
                .findElement(By.xpath("//*[contains(text,'https://k4-online.vtb24.ru/messengers/auth')]")).getText();
        dispatch()
                .data("Ссылка",result)
                .action("Открыть ссылку")
                .build().run();
    }
}
