package ru.vtb.app.chat.tests;

import org.testng.annotations.Ignore;
import ru.vtb.app.chat.tests.pack.VtbTest;

@Ignore
public class BackgroundTest extends VtbTest {

    public void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var sendSymbolMessage = actions("Приложение")
                .data("Сообщение", String.format("текст с эмодзи %s продолжение текста", "Aro ктп # кл-$#"))
                .action("Отправить сообщение")
                .build();

        var runInBackground = actions("Устройство")
                .action("Запустить приложение в фоновом режиме")
                .build();

        join(login);
        join(openChat);
        join(sendSymbolMessage);
        join(runInBackground);
    }
}
