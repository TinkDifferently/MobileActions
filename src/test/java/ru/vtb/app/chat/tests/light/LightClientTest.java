package ru.vtb.app.chat.tests.light;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class LightClientTest extends VtbTest {
    @Override
    protected void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();
        join(login);
        join(openChat);
    }
}
