package ru.vtb.messengers.telegram.actions;

import actions.PageAction;
import actions.annotations.*;
import io.qameta.allure.Step;
import ru.vtb.messengers.telegram.elements.Item;
import ru.vtb.messengers.telegram.elements.ItemGroup;

/**
 * класс экшнов для входа в систему
 */
@ActionProvider("Чаты и каналы")
public class ChatAction extends PageAction {

    @MobileAction(value = "Выбрать чат")
    void selectChat(@Parameter("Название чата") String targetName) {
        switchTo("Главная");
        getElement("Контакты").as(ItemGroup.class)
                .getElement(element -> element.getTitle().equals(targetName))
                .click();
    }

    public ChatAction() {
        super();
    }

    @Override
    @Step("Первый вход в приложение")
    public void run() {
        super.run();
    }
}
