package ru.vtb.app.chat.tests.light;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class Buttons_Cursive extends VtbTest {
    @Override
    protected void mount() {

        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var sendSymbolMessage = actions("Приложение")
                .data("Сообщение","Тестовый сценарий с кнопками")
                .action("Отправить сообщение")
                .build();

        var init=actions("Приложение")
                .data("Быстрая кнопка", "Вариант 1")
                .action("Нажать на быструю кнопку")
                .build();

        var emojiButton=actions("Приложение")
                .data("Быстрая кнопка", "Кнопка с курсивом и полужирным")
                .action("Нажать на быструю кнопку")
                .build();

        join(login);
        join(openChat);
        join(sendSymbolMessage);
        join(init);
        join(emojiButton);
    }
}
