package ru.vtb.app.chat.tests.pack;

import io.dimension.UseActions;
import io.dimension.actions.IAction;

public interface Login extends UseActions {

    default IAction pinAuthorise(){
        return actions("Авторизация")
//                .data("Логин#k3","18013277")
//                .data("Логин#k4","20057842")
                .data("Пин код", "1570")
                .data("Код подтверждения","000000")
//                .action("Авторизоваться с помощью логина")
                .action("Авторизоваться с помощью PIN")
                .build();
    }

    default IAction loginAuthorise(){
        return actions("Авторизация")
//                .data("Логин#k3","18013277")
                .data("Логин#k4","20057842")
                .data("Пин код", "1570")
                .data("Код подтверждения","000000")
                .action("Авторизоваться с помощью логина")
//                .action("Авторизоваться с помощью PIN")
                .build();
    }
}
