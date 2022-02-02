package ru.vtb.app.chat.tests;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class CardOrderTest extends VtbTest {
    @Override
    protected void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();


        var orderCard = actions("Приложение")
                .data("Сообщение", "Хочу оформить карту")
                .action("Отправить сообщение")
                .build();

        var debetCard = actions("Приложение")
                .data("Быстрая кнопка", "Дебетовую")
                .action("Нажать на быструю кнопку")
                .build();

        var digitalCard = actions("Приложение")
                .data("Быстрая кнопка", "Цифровая карта")
                .action("Нажать на быструю кнопку")
                .build();

        var cancel = actions("Приложение")
                .action("Отменить поиск")
                .build();

        join(login);
        join(openChat);
        join(orderCard);
        join(debetCard);
        join(digitalCard);
        join(cancel);
    }

//    @Override
//    public String getK3Login() {
//        return "20002750";
//    }
}
