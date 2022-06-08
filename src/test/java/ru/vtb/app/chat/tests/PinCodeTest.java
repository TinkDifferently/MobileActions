package ru.vtb.app.chat.tests;

import io.dimension.actions.IAction;
import io.dimension.config.session.DriverController;
import org.openqa.selenium.Platform;
import ru.vtb.app.chat.tests.pack.VtbTest;

public class PinCodeTest extends VtbTest {
    @Override
    protected void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();


        var requestPin = actions("Приложение")
                .data("Сообщение", "Хочу сменить пин код")
                .action("Отправить сообщение")
                .build();


        IAction accept = DriverController.getInstance().getCurrentPlatform().is(Platform.IOS)
                ? actions("Приложение")
                .data("Быстрая кнопка", "Выбрать")
                .action("Нажать на быструю кнопку")
                .build()
                : (() -> {
        });

        var selectAnyCard = actions("Приложение")
                .action("Выбрать произвольную карту")
                .build();

        var setNewPin = actions("Приложение")
                .action("Установить пин-код для карты")
                .build();

        var cancel = actions("Приложение")
                .action("Отменить поиск")
                .build();


        join(login);
        join(openChat);
        join(requestPin);
        join(accept);
        join(selectAnyCard);
        join(setNewPin);
        join(cancel);
    }
}
