package ru.vtb.messengers.telegram.tests;

import org.testng.annotations.BeforeTest;
import io.dimension.BaseTest;
import org.testng.annotations.Ignore;

@Ignore
public class TelegramSendMessageTest extends BaseTest {
    public void mount() {
        var selectChat = actions("Чаты и каналы")
                .data("Название чата", "vtb_k3")
                .action("Выбрать чат")
                .build();

        join(selectChat);

        var sendMessage = actions("Чаты и каналы")
                .data("Текст сообщения", "Привет")
                .action("Написать сообщение")
                .build();

        join(sendMessage);
    }
}
