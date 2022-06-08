package ru.vtb.app.chat.tests.light;

import io.dimension.config.session.DriverController;
import org.openqa.selenium.Platform;
import ru.vtb.app.chat.tests.pack.VtbTest;

public class LightClientCsi extends VtbTest {
    @Override
    protected void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var requestOperator = actions("Приложение")
                .data("Сообщение", "Тест михайлов")
                .action("Отправить сообщение")
                .build();

        var sendHello = actions("Приложение")
                .data("Сообщение", "Привет, я автотест мобильного приложения")
                .action("Отправить сообщение")
                .build();

        String ev= DriverController.getInstance().getCurrentPlatform().is(Platform.ANDROID)
                ? "Оценка: 5"
                : "5 звезд ";

        var rate = actions("Оценки")
                .data("Оценка", ev)
                .action("Оценить работу оператора")
                .build();

        var leaveAComment = actions("Оценки")
                .data("Комментарий", "Оценка # автотест мобильного приложения")
                .action("Отправить комментарий")
                .build();

        join(login);
        join(openChat);
        join(requestOperator);
        join(sendHello);
        join(rate);
        join(leaveAComment);
    }
}
