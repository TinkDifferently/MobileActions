package ru.vtb.app.chat.tests.light;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class LightClientText_Images extends VtbTest {
    public void mount() {

        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var addImage = actions("Приложение")
                .action("Добавить изображение")
                .build();

        var openPreview = actions("Приложение")
                .action("Открыть последнее изображение")
                .build();

        var back = actions("Приложение")
                .action("Назад")
                .build();

        join(login);
        join(openChat);
        join(addImage);
        join(openPreview);
        join(back);
    }
}
