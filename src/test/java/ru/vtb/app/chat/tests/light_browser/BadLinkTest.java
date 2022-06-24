package ru.vtb.app.chat.tests.light_browser;

import io.dimension.BaseTest;
import io.dimension.DisplayName;
import io.dimension.actions.IAction;

@DisplayName("Запрос по устаревшей ссылке")
public class BadLinkTest extends BaseTest {
    @Override
    protected void mount() {
        IAction openLink = actions("Браузер")
                .data("Ссылка", "https://k4-online.vtb24.ru/messengers/auth?key=jEQ8j6bxVxDN4Bcl7Zgrc3LZe7czh=hAOxYYmZ50GiY3Aq1RExQmHRUZKh01PCFrR0tuX4MG0BZz4kYljWErcKePbSvG")
                .action("Открыть ссылку")
                .build();
        IAction checkLinkExpired = actions("Браузер")
                .action("Ссылка устарела")
                .build();

        join(openLink);
        join(checkLinkExpired);
    }
}
