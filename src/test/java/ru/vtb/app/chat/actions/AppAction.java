package ru.vtb.app.chat.actions;

import io.dimension.actions.PageAction;
import io.dimension.actions.annotations.ActionProvider;
import io.dimension.actions.annotations.MobileAction;
import io.dimension.actions.annotations.Parameter;
import io.dimension.config.session.DriverUtils;
import io.dimension.elements.base.interfaces.Clickable;
import io.dimension.elements.base.interfaces.Editable;
import io.dimension.elements.base.interfaces.Select;
import io.dimension.elements.base.interfaces.SelectableItem;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

import java.util.Collection;

/**
 * класс экшнов для входа в систему
 */
@ActionProvider("Приложение")
public class AppAction extends PageAction {
    @MobileAction(value = "Открыть чат")
    @Step("Открыть чат")
    void openChat() {
        element("Навигация").as(Select.class)
                .select("Чат");
    }

    @MobileAction(value = "Открыть историю")
    @Step("Открыть историю")
    void openHistory() {
        element("Навигация").as(Select.class)
                .select("История");
    }

    @MobileAction(value = "Проверить сообщение о недоступности сети")
    @Step("Проверка сообщение о недоступности сети")
    void checkOfflineMsg() {
        var offlineMsg = DriverUtils.findNullableElement(By.xpath("//*[@text='Интернет соединение потеряно']"));
        if (offlineMsg == null) {
            throw new RuntimeException("Не отобразилось сообщение о недоступности сети");
        }
    }

    @MobileAction(value = "Отправить сообщение")
    @Step("Отправлено сообщение {0}")
    void sendMessage(@Parameter("Сообщение") @NotNull String message) {
        element("Сообщение для отправки").as(Editable.class)
                .setText(message);
        element("Отправить сообщение").as(Clickable.class)
                .click();
    }

    @MobileAction(value = "Назад")
    @Step("Назад")
    void back() {
        element("Назад").as(Clickable.class)
                .click();
    }

    @MobileAction(value = "Нажать на быструю кнопку")
    @Step("Выбрана быстрая кнопка {0}")
    void fastButton(@Parameter("Быстрая кнопка") @NotNull String fastButton) {
        element("Быстрые кнопки").as(Select.class)
                .select(fastButton);
    }

    @MobileAction(value = "Нажать на произвольную быструю кнопку")
    @Step("Выбор произвольной быстрой кнопки")
    void randomFastButton() {
        var options = (Collection<SelectableItem>) element("Быстрые кнопки").as(Select.class).getOptions();
        var option = options.stream().map(SelectableItem::getTitle).filter(item -> !"Привет".equals(item)).findAny()
                .orElse("Привет");
        fastButton(option);
    }

    @MobileAction(value = "Добавить фотографию")
    @Step("Добавление фотографии")
    void addPhoto() {
        element("Добавить изображение").as(Clickable.class)
                .click();
        element("Изображения").as(Select.class)
                .select(0);
        dispatch("Устройство")
                .action("Сделать фотографию")
                .build()
                .run();
    }

    @MobileAction(value = "Добавить изображение")
    @Step("Добавление изображения")
    void addImage() {
        element("Добавить изображение").as(Clickable.class)
                .click();
        element("Изображения").as(Select.class)
                .select(1);
    }
}
