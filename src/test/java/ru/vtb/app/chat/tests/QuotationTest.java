package ru.vtb.app.chat.tests;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class QuotationTest extends VtbTest {
    @Override
    protected void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var sendSymbolMessage = actions("Приложение")
                .data("Сообщение", String.format("текст с эмодзи %s продолжение текста", " \uD83D\uDE00 Aro ктп # кл-$#"))
                .action("Отправить сообщение")
                .build();

        var quote = actions("Приложение")
                .data("Сообщение", "сообщение от автотеста")
                .action("Цитировать последнее сообщение")
                .build();

        var addImage = actions("Приложение")
                .action("Добавить изображение")
                .build();


        var quoteFile = actions("Приложение")
                .data("Сообщение", "какой-то текст")
                .action("Цитировать последнее сообщение")
                .build();



        join(login);
        join(openChat);
        join(sendSymbolMessage);
        join(quote);
        join(addImage);
        join(quoteFile);
    }
}
