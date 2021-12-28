package ru.vtb.app.chat.tests;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class BackTest extends VtbTest {

    public void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var sendSymbolMessage = actions("Приложение")
                .data("Сообщение", String.format("текст с эмодзи %s продолжение текста","Aro ктп # кл-$#"))
                .action("Отправить сообщение")
                .build();

        var back= actions("Приложение")
                .action("Назад")
                .build();

        var history= actions("Приложение")
                .action("Открыть историю")
                .build();



        join(login);
        join(openChat);
        join(sendSymbolMessage);
        join(back);
        join(history);
        join(openChat);
        join(back);
    }
}
