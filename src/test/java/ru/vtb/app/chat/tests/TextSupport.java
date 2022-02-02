package ru.vtb.app.chat.tests;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class TextSupport extends VtbTest {
    public void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var sendSymbolMessage = actions("Приложение")
                .data("Сообщение", String.format("текст с эмодзи %s продолжение текста", " \uD83D\uDE00 Aro ктп # кл-$#"))
                .action("Отправить сообщение")
                .build();

        var sendLongMessage = actions("Приложение")
                .data("Сообщение", "a".repeat(2047))
                .action("Отправить сообщение")
                .build();

        var sendCapsMessage = actions("Приложение")
                .data("Сообщение", "A".repeat(20).concat("\nABCD").repeat(12))
                .action("Отправить сообщение")
                .build();

        var sendMessageWithCaps = actions("Приложение")
                .data("Сообщение", "аaaa ASDAD")
                .action("Отправить сообщение")
                .build();

        var sendScriptMessage = actions("Приложение")
                .data("Сообщение", "Какао для Светы")
                .action("Отправить сообщение")
                .build();


        join(login);
        join(openChat);
        join(sendSymbolMessage);
        join(sendLongMessage);
        join(sendCapsMessage);
        join(sendMessageWithCaps);
        join(sendScriptMessage);
    }
}
