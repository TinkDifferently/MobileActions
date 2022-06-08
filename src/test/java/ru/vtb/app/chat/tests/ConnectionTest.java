package ru.vtb.app.chat.tests;

import org.testng.annotations.Ignore;
import ru.vtb.app.chat.tests.pack.VtbTest;

@Ignore
public class ConnectionTest extends VtbTest {

    public void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();
        var toggleWifiOff = actions("Устройство")
                .action("Отключить Wi-Fi")
                .build();
        var checkConnectionState=actions("Приложение")
                .action("Проверить сообщение о недоступности сети")
                .build();
        var toggleWifiOn = actions("Устройство")
                .action("Включить Wi-Fi")
                .build();

        join(login);
        join(openChat);
        join(toggleWifiOff);
        join(checkConnectionState);
        join(toggleWifiOn);
    }
}
