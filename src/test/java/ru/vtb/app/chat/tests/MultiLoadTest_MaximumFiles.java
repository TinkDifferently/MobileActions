package ru.vtb.app.chat.tests;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class MultiLoadTest_MaximumFiles extends VtbTest {
    public void mount() {

        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var addImage = actions("Приложение")
                .data("Количество изображений",10)
                .action("Добавить изображения из галереи")
                .build();

        var deleteImage = actions("Приложение")
                .action("Удалить изображение")
                .build();




        join(login);
        join(openChat);
        join(addImage);
        join(deleteImage);
    }
}
