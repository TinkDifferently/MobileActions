package ru.vtb.app.chat.tests;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class Csi extends VtbTest {
    @Override
    protected void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var requestOperator = actions("Приложение")
                .data("Сообщение", "Тест михайлов")
                .action("Отправить сообщение")
                .build();

        var sendHello = actions("Приложение")
                .data("Сообщение", "Привет, я автотест мобильного приложения")
                .action("Отправить сообщение")
                .build();

        var rate = actions("Оценки")
                .data("Оценка", "5 звезд ")
                .action("Оценить работу оператора")
                .build();

        var leaveAComment = actions("Оценки")
                .data("Комментарий", "Оценка # автотест мобильного приложения")
                .action("Отправить комментарий")
                .build();

        join(login);
        join(openChat);
        join(requestOperator);
        join(sendHello);
        join(rate);
        join(leaveAComment);
    }
}
