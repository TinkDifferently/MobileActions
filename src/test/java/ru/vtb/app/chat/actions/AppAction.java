package ru.vtb.app.chat.actions;

import io.dimension.actions.PageAction;
import io.dimension.actions.annotations.*;
import io.dimension.config.session.DriverUtils;
import io.dimension.elements.base.interfaces.*;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Select select;
        try{
            var element=element("Быстрые кнопки");
            element.as(Waitable.class)
                    .waitFor(25);
            select=element.as(Select.class);
        } catch (Exception e){
            select=element("Быстрые ответы").as(Select.class);
        }
        select.select(fastButton);
    }

    @MobileAction(value = "Нажать на произвольную быструю кнопку")
    @Step("Выбор произвольной быстрой кнопки")
    void randomFastButton() {
        var options = (Collection<SelectableItem>) element("Быстрые кнопки").as(Select.class).getOptions();
        var option = options.stream().map(SelectableItem::getTitle).filter(item -> !"Привет" .equals(item)).findAny()
                .orElse("Привет");
        fastButton(option);
    }

    @MobileAction(value = "Выбрать произвольную карту")
    @Step("Выбор произвольную карту")
    void randomCard() {
        switchTo("Карты");
        element("Список карт")
                .as(Waitable.class)
                .waitFor(60);
        var options = (Collection<SelectableItem>) element("Список карт")
                .as(Select.class)
                .getOptions();
        options.stream().collect(Collectors.toSet()).stream().findAny().ifPresent(SelectableItem::click);
        element("Готово").as(Clickable.class)
                .click();
    }

    @MobileAction(value = "Установить пин-код для карты")
    @Step("Установлен пин-код")
    void pinCode() {
        element("Пин-код")
                .as(Waitable.class)
                .waitFor(35);
        element("Пин-код")
                .as(Clickable.class)
                .click();
        var select = element("Новый пин-код")
                .as(Select.class);
        Stream.of("6572".split("")).forEach(select::select);
        element("Установить")
                .as(Clickable.class)
                .click();
        var submit = element("Подтвердить пин-код")
                .as(Select.class);
        Stream.of("1570".split("")).forEach(submit::select);
        switchTo("Главная ВТБ");
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

    @AndroidAction(value = "Добавить изображение")
    @Step("Добавление изображения")
    void addImageAndroid() {
        element("Добавить изображение").as(Clickable.class)
                .click();
        element("Изображения").as(Select.class)
                .select(1);
        element("Отправить сообщение").as(Clickable.class)
                .click();
    }

    @IOSAction(value = "Добавить изображение")
    @Step("Добавление изображения")
    void addImageIOS() throws InterruptedException {
        element("Добавить изображение").as(Clickable.class)
                .click();
        element("Источник изображения").as(Select.class)
                .select("Выбрать из галереи");
        TimeUnit.SECONDS.sleep(7);
        element("Изображения").as(Select.class)
                .select(1);
    }

    @MobileAction(value = "Цитировать последнее сообщение")
    @Step("Процитировано последнее сообщение с текстом ответа {0}")
    void quoteMessage(@Parameter("Сообщение") @NotNull String message) {
        element("Сообщения").as(Select.class)
                .select(0);
        element("Меню сообщения").as(Select.class)
                .select("Ответить");
        sendMessage(message);
    }

    @MobileAction(value = "Найти сообщение")
    @Step("Поиск сообщения с текстом {0}")
    void findMessage(@Parameter("Фрагмент") @NotNull String fragment) {
        try {
            element("Строка поиска").as(Waitable.class)
                    .waitFor(5);
            element("Строка поиска").as(Editable.class)
                    .clear();
        } catch (Exception any) {
            element("Поиск").as(Clickable.class)
                    .click();
        }
        element("Строка поиска").as(Editable.class)
                .setText(fragment+"\n");
    }

    @MobileAction(value = "Следующий результат")
    @Step("Переход к следующему результату")
    void nextResult() {
        element("Следующий результат").as(Clickable.class)
                .click();
    }

    @MobileAction(value = "Предыдущий результат")
    @Step("Переход к предыдущему результату")
    void prevResult() {
        element("Предыдущий результат").as(Clickable.class)
                .click();
    }

    @MobileAction(value = "Отменить поиск")
    @Step("Отмена поиска")
    void cancelSearch() {
        element("Отменить поиск").as(Clickable.class)
                .click();
    }

    @MobileAction(value = "В конец истории")
    @Step("Возвращение в конец истории")
    void backToHistory() {
        element("В конец истории").as(Clickable.class)
                .click();
    }


    @AndroidAction(value = "Пропустить приветствие")
    @Step("Пропущено приветствие")
    void skipOptionsDialog() {
        switchTo("Главная ВТБ");
        try {
            var dialog = element("Выбор в диалоге").as(Select.class);
            if (!dialog.getOptions().isEmpty()) {
                dialog.select("Не сейчас");
            }
        } catch (Throwable ignored) {

        }
    }

    @IOSAction(value = "Пропустить приветствие")
    @Step("Пропущено приветствие")
    void skipOptionsDialogIos() {
        switchTo("Главная ВТБ");
        try {
            var dialog = element("Выбор в диалоге").as(Select.class);
            if (!dialog.getOptions().isEmpty()) {
                dialog.select("Не напоминать");
            }
        } catch (Throwable ignored) {

        }
    }

}
