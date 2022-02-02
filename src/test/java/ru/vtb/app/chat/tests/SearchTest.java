package ru.vtb.app.chat.tests;

import ru.vtb.app.chat.tests.pack.VtbTest;

public class SearchTest extends VtbTest {
    @Override
    protected void mount() {
        var openChat = actions("Приложение")
                .action("Открыть чат")
                .build();

        var seachLetters = actions("Приложение")
                .data("Фрагмент", "Change_pin")
                .action("Найти сообщение")
                .build();

        var searchNumbers = actions("Приложение")
                .data("Фрагмент", "1234")
                .action("Найти сообщение")
                .build();

        var searchSymbols = actions("Приложение")
                .data("Фрагмент", "*4217")
                .action("Найти сообщение")
                .build();

        var requestOperator = actions("Приложение")
                .data("Сообщение", "Тест михайлов")
                .action("Отправить сообщение")
                .build();

        var prev = actions("Приложение")
                .action("Предыдущий результат")
                .build();

        var next = actions("Приложение")
                .action("Следующий результат")
                .build();

        var sendHello = actions("Приложение")
                .data("Сообщение", "Reply with 3 messages")
                .action("Отправить сообщение")
                .build();

        var down = actions("Приложение")
                .action("В конец истории")
                .build();

        var cancel = actions("Приложение")
                .action("Отменить поиск")
                .build();

        join(login);
        join(openChat);
        join(requestOperator);
        join(searchNumbers);
        join(searchSymbols);
        join(cancel);
        join(sendHello);
        join(seachLetters);
        join(prev);
        join(prev);
        join(next);
        join(down);

    }
}
