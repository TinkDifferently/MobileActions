package ru.vtb.app.chat.tests.light;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class LightClientText_Keyboard extends VtbTest {
    @Override
    protected void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();
        var prepare = actions("Приложение")
                .data("Сообщение","a")
                .action("Подготовить сообщение")
                .build();
        var hideKeyboard=actions("Устройство")
                .action("Скрыть клавиатуру")
                .build();
        join(login);
        join(openChat);
        join(prepare);
        join(hideKeyboard);
    }
}
