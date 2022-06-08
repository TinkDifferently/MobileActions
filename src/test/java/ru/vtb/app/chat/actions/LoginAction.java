package ru.vtb.app.chat.actions;

import io.appium.java_client.MobileElement;
import io.dimension.actions.ActionBuilder;
import io.dimension.actions.PageAction;
import io.dimension.actions.annotations.ActionProvider;
import io.dimension.actions.annotations.MobileAction;
import io.dimension.actions.annotations.Parameter;
import io.dimension.config.session.DriverController;
import io.dimension.config.session.DriverUtils;
import io.dimension.elements.api.Button;
import io.dimension.elements.api.Input;
import io.dimension.elements.base.interfaces.Checkable;
import io.dimension.elements.base.interfaces.Clickable;
import io.dimension.elements.base.interfaces.Editable;
import io.dimension.elements.base.interfaces.Select;
import io.dimension.utils.Generator;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import ru.vtb.app.chat.data.LoginManager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

/**
 * класс экшнов для входа в систему
 */
@ActionProvider("Авторизация")
public class LoginAction extends PageAction {

    private boolean isFirst = true;

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
        var element = DriverUtils.findNullableElement(By.id("android:id/button1"));
        if (element != null) {
            element.click();
        }
        setPinCode(pinCode);
        if (!otpCode.isEmpty()) {
            setOtp(otpCode);
        }
        switchTo("Главная ВТБ");
    }

    void initIdAuth() {
        var element = DriverUtils.findNullableElement(By.id("ru.vtb.mobilebanking.android.rc:id/noneModeButton"));
        if (element != null) {
            element.click();
        } else {
            element("Статус клиента").as(Select.class)
                    .select("Войти");
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
    }

    @MobileAction(value = "Авторизоваться в Vtb Lite")
    @Step("Авторизация клиента {0} в Vtb Lite")
    void lightAuth(@Parameter("Номер телефона") String telephoneNumber, @Parameter("Код подтверждения") @NotNull String otpCode) {
        switchTo("ВТБ Авторизация");
        element("Статус клиента").as(Select.class)
                .select("Стать клиентом ВТБ");
        element("Номер телефона").as(Input.class)
                .setText(telephoneNumber);
        element("Зарегистрироваться").as(Button.class)
                .click();
        Optional.ofNullable(DriverUtils.findNullableElement(By.id("android:id/button1")))
                .ifPresent(WebElement::click);
        setOtp(otpCode);
    }

    @MobileAction(value = "Регистрация в Vtb Lite")
    @Step("Зарегистрирован клиент {0} {1} {2}")
    void register(@Parameter("Фамилия") String surname, @Parameter("Имя") String name, @Parameter("Отчество") String patronimic, @Parameter("Дата рождения") LocalDate date, @Parameter("Пин код") String pinCode) {
        switchTo("ВТБ Регистрация");
        element("Фамилия").as(Input.class)
                .setText(surname);
        element("Имя").as(Input.class)
                .setText(name);
        element("Отчество").as(Input.class)
                .setText(patronimic);
        element("Дата рождения").as(Input.class)
                .setText(DateTimeFormatter.ofPattern("dd.MM.yyyy").format(date));
        element("Согласие с правилами и тарифами").as(Checkable.class)
                .check();
        element("Согласие на обработку персональных данных").as(Checkable.class)
                .check();
        element("Продолжить").as(Clickable.class)
                .click();
        switchTo("ВТБ Авторизация");
        setPinCode(pinCode);
        element("Подтвердить").as(Clickable.class)
                .click();
        setPinCode(pinCode);
        switchTo("Главная ВТБ");
        element("Выбор в диалоге").as(Select.class)
                .select("Запретить");
    }


    @MobileAction(value = "Авторизация")
    void lightAuth(@Parameter("Тип авторизации") String authStyle,
                   @Parameter("Первый вход") boolean isFirst,
                   @Parameter("Пин код") String pinCode,
                   @Parameter("Код подтверждения") String otpCode,
                   @Parameter("Контур") String contour,
                   @Parameter("Тип клиента") String clientType) {

        boolean isLight = authStyle.equals("Легкий банк");
        if (isFirst && this.isFirst) {
            this.isFirst = false;
            if (isLight) {
                var number = ThreadLocalRandom.current().nextLong(2924292177L, 9924292177L);
                dispatch()
                        .data("Номер телефона", String.valueOf(number))
                        .data("Код подтверждения", otpCode)
                        .action("Авторизоваться в Vtb Lite")
                        .build()
                        .andThen(
                                dispatch()
                                        .data("Имя", Generator.generateName())
                                        .data("Фамилия", Generator.generateSurname())
                                        .data("Отчество", Generator.generatePatronymic())
                                        .data("Дата рождения", Generator.generateBirthDate(18, 70))
                                        .data("Пин код", pinCode)
                                        .action("Регистрация в Vtb Lite")
                                        .build()
                        ).run();
                return;
            }
            dispatch()
                    .data("Логин", LoginManager.provideLogin(contour, clientType))
                    .data("Пин код", pinCode)
                    .data("Код подтверждения", otpCode)
                    .action("Авторизоваться с помощью логина")
                    .build().andThen(dispatch("Приложение").action("Пропустить приветствие").build())
                    .run();
            return;
        }
        otpCode = isLight
                ? ""
                : otpCode;
        dispatch().data("Пин код", pinCode)
                .data("Код подтверждения", otpCode)
                .action("Авторизоваться с помощью PIN")
                .build()
                .run();
    }
}
