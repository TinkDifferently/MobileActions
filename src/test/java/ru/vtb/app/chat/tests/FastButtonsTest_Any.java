package ru.vtb.app.chat.tests;

import io.dimension.BaseTest;
import org.testng.annotations.BeforeTest;
import ru.vtb.app.chat.tests.pack.VtbTest;

public class FastButtonsTest_Any extends VtbTest {
    public void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var fastButton = actions("Приложение")
                .action("Нажать на произвольную быструю кнопку")
                .build();

        join(login);
        join(openChat);
        join(fastButton);
    }

    @Override
    public String getK3Login() {
        return "20060643";
    }

    @Override
    public String getK4Login() {
        return null;
    }
}
