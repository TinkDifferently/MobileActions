package ru.vtb.app.chat.tests;

import io.dimension.BaseTest;
import org.testng.annotations.BeforeTest;
import ru.vtb.app.chat.tests.pack.VtbTest;

public class ImagesTest extends VtbTest {
    public void mount() {

        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var fastButton = actions("Приложение")
                .action("Добавить изображение")
                .build();

        join(login);
        join(openChat);
        join(fastButton);
    }
}
