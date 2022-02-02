package ru.vtb.app.chat.tests;

import io.dimension.BaseTest;
import org.testng.annotations.BeforeTest;
import ru.vtb.app.chat.tests.pack.VtbTest;

public class FastButtonsTest_Text extends VtbTest {
    public void mount() {

        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var sendSymbolMessage = actions("Приложение")
                .data("Сообщение", String.format("текст с эмодзи %s продолжение текста","Aro ктп # кл-$#"))
                .action("Отправить сообщение")
                .build();


        join(login);
        join(openChat);
        join(sendSymbolMessage);
    }

    @Override
    public String getK3Login() {
        return "20011354";
    }

}
