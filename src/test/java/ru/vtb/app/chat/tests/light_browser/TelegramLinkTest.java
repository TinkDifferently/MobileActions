package ru.vtb.app.chat.tests.light_browser;

import io.dimension.BaseTest;
import io.dimension.DisplayName;
import io.dimension.actions.IAction;
import ru.vtb.app.chat.tests.pack.VtbTest;

@DisplayName("Запрос по новой ссылке")
public class TelegramLinkTest extends VtbTest {

    @Override
    protected void mount() {
        IAction openTelegram = actions("Браузер")
                .data("Ссылка", "https://web.telegram.org/z/")
                .action("Открыть ссылку")
                .build();

        IAction openDialog = actions("Браузер")
                .action("Открыть диалог")
                .build();

        IAction sendMessage = actions("Браузер")
                .data("Сообщение", "test_registration_messengerid")
                .action("Отправить сообщение")
                .build();

        IAction storeLink = actions("Браузер")
                .action("Сохранить ссылку")
                .build();


        IAction checkLinkExpired = actions("Браузер")
                .action("Перейти по ссылке")
                .build();

        IAction register = actions("Браузер")
                .action("Зарегистрироваться")
                .build();

        join(openTelegram);
        join(openDialog);
        join(sendMessage);
        join(storeLink);
        join(checkLinkExpired);
        join(register);
    }
}
