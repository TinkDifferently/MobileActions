package ru.vtb.app.chat.tests.light;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class LightClientText_Photo extends VtbTest {
    public void mount() {

        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var addPhoto = actions("Приложение")
                .data("Повтор", true)
                .action("Добавить фотографию")
                .build();

        var openPreview = actions("Приложение")
                .action("Открыть последнее изображение")
                .build();

        var back= actions("Приложение")
                .action("Назад")
                .build();

        join(login);
        join(openChat);
        join(addPhoto);
        join(openPreview);
        join(back);
    }
}
