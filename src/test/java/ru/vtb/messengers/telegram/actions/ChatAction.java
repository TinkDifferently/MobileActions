package ru.vtb.messengers.telegram.actions;

import io.dimension.actions.PageAction;
import io.dimension.actions.annotations.*;
import io.dimension.config.session.DriverController;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import ru.vtb.messengers.telegram.elements.Button;
import ru.vtb.messengers.telegram.elements.TextField;

/**
 * класс экшнов для входа в систему
 */
@ActionProvider("Чаты и каналы")
public class ChatAction extends PageAction {

    @MobileAction(value = "Выбрать чат")
    @Step("Переход к контакту {0}")
    void selectChat(@Parameter("Название чата") @NotNull String targetName) {
        switchTo("Главная Telegram");
        if ("vtb_k3".equals(targetName)) {
            DriverController.getInstance().getDriver().findElements(By.xpath("//androidx.recyclerview.widget.RecyclerView/*"))
                    .get(0).click();
//            getElement("Контакты").as(ContactsSelector.class)
//                    .getElement(1)
//                    .click();
            return;
        }
        throw new RuntimeException("Unknown contact");
    }

    @MobileAction(value = "Написать сообщение")
    @Step("Отправка сообщения {0}")
    void sendMessage(@Parameter("Текст сообщения") @NotNull String messageText) {
        switchTo("Главная Telegram");
        element("Сообщение для отправки").as(TextField.class)
                .setText(messageText);
        element("Отправить").as(Button.class)
                .click();
    }

    public ChatAction() {
        super();
    }

    @Override
    @Step("Первый вход в приложение")
    public void run() {
        AllureLifecycle lifecycle = Allure.getLifecycle();
//change test name to "CHANGED_NAME"
        lifecycle.updateTestCase(testResult -> testResult.setName("CHANGED_NAME"));
        super.run();
    }
}
