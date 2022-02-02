package ru.vtb.app.chat.tests;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class PinCodeTest extends VtbTest {
    @Override
    protected void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();


        var orderCard = actions("Приложение")
                .data("Сообщение", "Хочу сменить пин код")
                .action("Отправить сообщение")
                .build();

        var debetCard = actions("Приложение")
                .data("Быстрая кнопка", "Выбрать")
                .action("Нажать на быструю кнопку")
                .build();

        var selectAnyCard = actions("Приложение")
                .action("Выбрать произвольную карту")
                .build();

        var setNewPin = actions("Приложение")
                .action("Установить пин-код для карты")
                .build();

        var cancel = actions("Приложение")
                .action("Отменить поиск")
                .build();




        join(login);
        join(openChat);
        join(orderCard);
        join(debetCard);
        join(selectAnyCard);
        join(setNewPin);
        join(cancel);
    }

    @Override
    public String getK3Login() {
        return "20002750";
    }
}
