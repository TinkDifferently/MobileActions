package ru.vtb.app.chat.tests;

import ru.vtb.app.chat.tests.pack.VtbTest;

import java.util.List;

public class MultiLoadTest_IncorrectFiles extends VtbTest {
    public void mount() {

        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var addImage = actions("Приложение")
                .data("Имена файлов", List.of("docx.docx","xlsx.xlsx"))
                .action("Добавить файлы")
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
