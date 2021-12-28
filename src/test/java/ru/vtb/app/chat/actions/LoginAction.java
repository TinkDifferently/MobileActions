package ru.vtb.app.chat.actions;

import io.dimension.actions.PageAction;
import io.dimension.actions.annotations.ActionProvider;
import io.dimension.actions.annotations.MobileAction;
import io.dimension.actions.annotations.Parameter;
import io.dimension.config.session.DriverUtils;
import io.dimension.elements.base.interfaces.Clickable;
import io.dimension.elements.base.interfaces.Editable;
import io.dimension.elements.base.interfaces.Select;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

import java.util.stream.Stream;

/**
 * класс экшнов для входа в систему
 */
@ActionProvider("Авторизация")
public class LoginAction extends PageAction {

    void setPinCode(String pinCode) {
        switchTo("ВТБ Авторизация");
        var select = element("Пин-код").as(Select.class);
        Stream.of(pinCode.split("")).forEach(select::select);
    }

    void setOtp(String otp) {
        switchTo("ВТБ Авторизация");
        element("Отп").as(Editable.class)
                .setText(otp);

    }

    @MobileAction(value = "Авторизоваться с помощью PIN")
    @Step("Авторизация с пин-кодом {0}")
    void authorisePin(@Parameter("Пин код") @NotNull String pinCode, @Parameter("Код подтверждения") @NotNull String otpCode) {
        var element = DriverUtils.findNullableElement(By.id("ru.vtb.mobilebanking.android.rc:id/noneModeButton"));
        if (element != null) {
            element.click();
        }
        setPinCode(pinCode);
        setOtp(otpCode);
        switchTo("Главная ВТБ");
    }

    void initIdAuth() {
        var element = DriverUtils.findNullableElement(By.id("ru.vtb.mobilebanking.android.rc:id/noneModeButton"));
        if (element != null) {
            element.click();
        } else {
            element("Статус клиента").as(Select.class)
                    .select("Я клиент");
        }
    }

    @MobileAction(value = "Авторизоваться с помощью логина")
    @Step("Авторизация с логином {0}")
    void authoriseLogin(@Parameter("Логин") @NotNull String login, @Parameter("Пин код") @NotNull String pinCode, @Parameter("Код подтверждения") @NotNull String otpCode) {
        switchTo("ВТБ Авторизация");
        initIdAuth();
        dismissOptionalAlert();
        element("Тип авторизации").as(Select.class)
                .select("По логину");
        element("Идентификатор клиента").as(Editable.class)
                .setText(login);
        element("Продолжить").as(Clickable.class)
                .click();
        setOtp(otpCode);
        setPinCode(pinCode);
        element("Подтвердить").as(Clickable.class)
                .click();
        setPinCode(pinCode);
        switchTo("Главная ВТБ");
        try {
        var dialog = element("Выбор в диалоге").as(Select.class);
            if (!dialog.getOptions().isEmpty()) {
                dialog.select("Не сейчас");
            }
        } catch (Throwable ignored) {

        }
    }

    public LoginAction() {
        super();
    }

    @Override
    @Step("Первый вход в приложение")
    public void run() {
        super.run();
    }
}
