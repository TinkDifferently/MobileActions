package ru.vtb.app.chat.actions;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.android.AndroidDriver;
import io.dimension.actions.PageAction;
import io.dimension.actions.annotations.*;
import io.dimension.config.session.DriverController;
import io.dimension.config.session.DriverUtils;
import io.dimension.elements.base.interfaces.*;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.testng.Assert;
import ru.vtb.app.chat.elements.Messages;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        //element("Выбор в диалоге").as(Select.class)
        //      .select("Закрыть");
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

    @MobileAction(value = "Подготовить сообщение")
    @Step("Подготовлено сообщение {0}")
    void prepareMessage(@Parameter("Сообщение") @NotNull String message) {
        element("Сообщение для отправки").as(Clickable.class)
                .click();
        element("Сообщение для отправки").as(Editable.class)
                .setText(message);
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
        try {
            var element = element("Быстрые кнопки");
            element.as(Waitable.class)
                    .waitFor(5);
            select = element.as(Select.class);
        } catch (Exception e) {
            select = element("Быстрые ответы").as(Select.class);
        }
        select.select(fastButton);
    }

    @MobileAction(value = "Нажать на произвольную быструю кнопку")
    @Step("Выбор произвольной быстрой кнопки")
    void randomFastButton() {
        var options = (Collection<SelectableItem>) element("Быстрые кнопки").as(Select.class).getOptions();
        var option = options.stream().map(SelectableItem::getTitle).filter(item -> !"Привет".equals(item)).findAny()
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
    void addPhoto(@Parameter("Повтор") boolean retry) {
        element("Добавить вложение").as(Clickable.class)
                .click();
        element("Сделать фото").as(Clickable.class)
                .click();
        dispatch("Устройство")
                .data("Повтор", retry)
                .action("Сделать фотографию")
                .build()
                .run();
    }

    @MobileAction(value = "Открыть последнее изображение")
    @Step("Открыто последнее изображение")
    void selectLastImage() {
        var image = element("Сообщения").as(Messages.class)
                .getImages().stream().findFirst()
                .orElseThrow(() -> new AssertionError("Empty images list"));
        image.click();
    }

    @SneakyThrows
    @AndroidAction(value = "Добавить изображение")
    @Step("Добавление изображения")
    void addImageAndroid() {
        TimeUnit.SECONDS.sleep(5);
        element("Добавить вложение").as(Clickable.class)
                .click();
        element("Источник вложения").as(Select.class)
                .select("Выбрать из галереи");
        switchTo("Изображения");
        element("Список папок").as(Select.class)
                .select("Camera");
        element("Список изображений").as(Select.class)
                .selectAny();
        TimeUnit.SECONDS.sleep(7);
        switchTo("Главная ВТБ");
        element("Отправить сообщение").as(Clickable.class)
                .click();
    }

    @SneakyThrows
    @MobileAction(value = "Удалить изображение")
    @Step("Изображение удалено")
    void deleteMessage(){
        element("Удалить изображение").as(Clickable.class)
                .click();
        element("Отправить сообщение").as(Clickable.class)
                .click();
    }

    @SneakyThrows
    @AndroidAction(value = "Добавить файлы")
    @Step("Добавление файлов ({0})")
    void addFiles(@Parameter("Имена файлов") List<String> files) {
        TimeUnit.SECONDS.sleep(3);
        element("Добавить вложение").as(Clickable.class)
                .click();
        element("Источник вложения").as(Select.class)
                .select("Выбрать файл");
        switchTo("Изображения");
        for (var file:files) {
            element("Список файлов").as(Select.class)
                    .select(file,"longPress");
        }
        element("Выбрать").as(Clickable.class)
                .click();
        switchTo("Главная ВТБ");
        element("Удалить изображение").as(Waitable.class)
                .waitFor(90);
    }



    @SneakyThrows
    @AndroidAction(value = "Добавить изображения из галереи")
    @Step("Добавление изображений ({0}) из галереи")
    void addImageAndroidFromGallery(@Parameter("Количество изображений") Integer count) {
        TimeUnit.SECONDS.sleep(3);
        element("Добавить вложение").as(Clickable.class)
                .click();
        element("Источник вложения").as(Select.class)
                .select("Выбрать из галереи");
        switchTo("Изображения");
        element("Список папок").as(Select.class)
                .select("Camera");
        var select = element("Список изображений").as(Select.class);
        var optionsLength = element("Список изображений").as(Select.class)
                .getOptions().size();
        var list = IntStream.range(0, optionsLength).boxed()
                .collect(Collectors.toList());
        var index = ThreadLocalRandom.current().nextInt(optionsLength);
        var optionIndex = list.get(index);
        list.remove(index);
        select.select(optionIndex, "longPress");
        for (int i = 0; i < count - 1; i++) {
            index = ThreadLocalRandom.current().nextInt(list.size());
            optionIndex = list.get(index);
            list.remove(index);
            select.select(optionIndex);
        }
        element("Выбрать группу").as(Clickable.class)
                .click();
        switchTo("Главная ВТБ");
        element("Удалить изображение").as(Waitable.class)
                .waitFor(90);
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
        var options = (List<? extends SelectableItem>) element("Сообщения").as(Select.class)
                .getOptions();
        if (DriverController.getInstance().getCurrentPlatform().is(Platform.IOS)) {
            options.get(0).click();
        } else {
            options.get(options.size() - 1).click();
        }
        element("Меню сообщения").as(Select.class)
                .select("Ответить");
        sendMessage(message);
    }

    @MobileAction(value = "Копировать последнее сообщение")
    @Step("Скопировано последнее сообщение")
    void copyMessage() {
        var options = (List<? extends SelectableItem>) element("Сообщения").as(Select.class)
                .getOptions();
        var option = DriverController.getInstance().getCurrentPlatform().is(Platform.IOS)
                ? options.get(0)
                : options.get(options.size() - 1);
        option.click();
        element("Меню сообщения").as(Select.class)
                .select("Копировать");
        Assert.assertEquals(DriverUtils.getClipboardText(), option.getTitle());
//        sendMessage();
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
        if (DriverController.getInstance().getCurrentPlatform().is(Platform.ANDROID)) {
            var driver = (AndroidDriver) DriverController.getInstance().getDriver();
            element("Строка поиска").as(Editable.class)
                    .setText(fragment);
            driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "Search"));
            return;
        }
        element("Строка поиска").as(Editable.class)
                .setText(fragment + "\n");
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
