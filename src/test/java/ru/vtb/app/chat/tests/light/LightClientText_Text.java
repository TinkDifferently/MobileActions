package ru.vtb.app.chat.tests.light;

import io.dimension.utils.Generator;
import ru.vtb.app.chat.tests.pack.VtbTest;

public class LightClientText_Text extends VtbTest {
    @Override
    protected void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();
        var sendSymbolMessage = actions("Приложение")
                .data("Сообщение", String.format("текст с эмодзи %s продолжение текста", " \uD83D\uDE00 Aro ктп # кл-$#"))
                .action("Отправить сообщение")
                .build();
        var prepareLongMessage = actions("Приложение")
                .data("Сообщение", Generator.generateString("any",2038))
                .action("Подготовить сообщение")
                .build();
        join(login);
        join(openChat);
        join(sendSymbolMessage);
        join(prepareLongMessage);
    }
}
