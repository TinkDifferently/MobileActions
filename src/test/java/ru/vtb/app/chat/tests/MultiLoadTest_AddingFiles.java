package ru.vtb.app.chat.tests;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class MultiLoadTest_AddingFiles extends VtbTest {
    public void mount() {

        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var textA = actions("Приложение")
                .data("Сообщение","foo")
                .action("Подготовить сообщение")
                .build();

        var addImage$9 = actions("Приложение")
                .data("Количество изображений",8)
                .action("Добавить изображения из галереи")
                .build();

        var deleteImage = actions("Приложение")
                .action("Удалить изображение")
                .build();

        var addImage$2 = actions("Приложение")
                .data("Количество изображений",2)
                .action("Добавить изображения из галереи")
                .build();

        var sendCapsMessage = actions("Приложение")
                .data("Сообщение", "A".repeat(20).concat("\nABCD").repeat(12))
                .action("Отправить сообщение")
                .build();




        join(login);
        join(openChat);
        join(addImage$9);
        join(textA);
        join(deleteImage);
        join(addImage$2);
        join(sendCapsMessage);
    }
}
