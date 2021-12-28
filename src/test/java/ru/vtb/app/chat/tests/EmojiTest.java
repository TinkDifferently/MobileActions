package ru.vtb.app.chat.tests;

import ru.vtb.app.chat.tests.pack.VtbTest;


public class EmojiTest extends VtbTest {

    public void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();
        String emoji="\uD83D\uDE01";
        var sendEmojiMessage = actions("Приложение")
                .data("Сообщение", String.format("текст с эмодзи %s продолжение текста",emoji))
                .action("Отправить сообщение")
                .build();
        var sendMaxEmojiMessage = actions("Приложение")
                .data("Сообщение", emoji.repeat(2048))
                .action("Отправить сообщение")
                .build();
        var sendEmoji = actions("Приложение")
                .data("Сообщение", emoji)
                .action("Отправить сообщение")
                .build();
        join(login);
        join(openChat);
        join(sendEmoji);
        join(sendMaxEmojiMessage);
        join(sendEmojiMessage);
    }
}
