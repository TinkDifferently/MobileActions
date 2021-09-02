package tests;

import org.testng.annotations.BeforeTest;
import ru.vtb.messengers.BaseTest;

public class TelegramSendMessageTest extends BaseTest {
    @BeforeTest
    public void setUp() {
        var selectChat = actions("Чаты и каналы")
                .data("Название чата", "vtbk3_bot")
                .action("Выбрать чат")
                .build();

        join(selectChat);

        var sendMessage = actions("Чаты и каналы")
                .data("Текст сообщения", "vtbk3_bot")
                .action("Написать сообщение")
                .build();

        join(sendMessage);
    }
}
