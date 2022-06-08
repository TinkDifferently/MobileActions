package ru.vtb.app.chat.tests.light;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class LightClientText_Gallery extends VtbTest {
    public void mount() {

        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var addPhoto=actions("Приложение")
                .data("Количество изображений",1)
                .action("Добавить изображения из галереи")
                .build();

        join(login);
        join(openChat);
        join(addPhoto);
    }
}
