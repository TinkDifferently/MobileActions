package ru.vtb.app.chat.actions;

import io.dimension.actions.PageAction;
import io.dimension.actions.annotations.ActionProvider;
import io.dimension.actions.annotations.MobileAction;
import io.dimension.actions.annotations.Parameter;
import io.dimension.config.session.DriverController;
import io.dimension.elements.base.interfaces.Clickable;
import io.dimension.elements.base.interfaces.Editable;
import io.dimension.elements.base.interfaces.Select;
import io.dimension.elements.base.interfaces.Waitable;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

import java.util.concurrent.TimeUnit;

@ActionProvider("Оценки")
public class CsiAction extends PageAction {
    @MobileAction(value = "Оценить работу оператора")
    @Step("Оценена работа оператора {0}")
    void rate(@Parameter("Оценка") String rate) throws InterruptedException {
        element("Оценить работу оператора").as(Waitable.class)
                .waitFor(70);
        TimeUnit.SECONDS.sleep(10);
        element("Оценить работу оператора").as(Select.class)
                .select(rate);
        DriverController.getInstance().getDriver().findElement(By.id("ru.vtb.mobilebanking.android.rc:id/interactiveButton"))
                .click();
//        element("Быстрые ответы").as(Select.class)
//                .select("Отправить оценку");
        element("Быстрые ответы").as(Select.class)
                .select("Да");
    }

    @MobileAction(value = "Отправить комментарий")
    @Step("Отправлен комментарий {0}")
    void sendMessage(@Parameter("Комментарий") @NotNull String message) {
        element("Комментарий").as(Editable.class)
                .setText(message);
        element("Отправить оценку").as(Clickable.class)
                .click();
    }
}
