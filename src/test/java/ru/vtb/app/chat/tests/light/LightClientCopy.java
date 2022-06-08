package ru.vtb.app.chat.tests.light;

import org.testng.annotations.Test;
import ru.vtb.app.chat.tests.pack.VtbTest;

@Test()
public class LightClientCopy extends VtbTest {
    @Override
    protected void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var sendSymbolMessage = actions("Приложение")
                .data("Сообщение", String.format("текст с эмодзи %s продолжение текста", " \uD83D\uDE00 Aro ктп # кл-$#"))
                .action("Отправить сообщение")
                .build();

        var copy = actions("Приложение")
                .action("Копировать последнее сообщение")
                .build();

        join(login);
        join(openChat);
        join(sendSymbolMessage);
        join(copy);


    }
}
