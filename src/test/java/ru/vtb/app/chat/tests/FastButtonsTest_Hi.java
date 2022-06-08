package ru.vtb.app.chat.tests;

import io.dimension.DisplayName;
import ru.vtb.app.chat.tests.pack.VtbTest;
@DisplayName("Проверка быстрой кнопки 'Привет'")
public class FastButtonsTest_Hi extends VtbTest {
    public void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var fastButton = actions("Приложение")
                .data("Быстрая кнопка", "Привет")
                .action("Нажать на быструю кнопку")
                .build();


        join(login);
        join(openChat);
        join(fastButton);
    }

}
