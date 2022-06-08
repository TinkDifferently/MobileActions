package ru.vtb.app.chat.tests;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class MultiLoadTest_TooMuchFiles extends VtbTest {
    public void mount() {

        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var addImage = actions("Приложение")
                .data("Количество изображений",11)
                .action("Добавить изображения из галереи")
                .build();

        join(login);
        join(openChat);
        join(addImage);
    }
}
