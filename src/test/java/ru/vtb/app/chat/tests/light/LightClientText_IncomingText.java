package ru.vtb.app.chat.tests.light;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class LightClientText_IncomingText extends VtbTest {
    @Override
    protected void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var sendScriptMessage = actions("Приложение")
                .data("Сообщение", "Какао для Светы")
                .action("Отправить сообщение")
                .build();

        join(login);
        join(openChat);
        join(sendScriptMessage);
    }
}
