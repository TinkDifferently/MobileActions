package ru.vtb.app.chat.tests.pack;

import io.dimension.UseActions;
import io.dimension.actions.IAction;

public interface Login extends UseActions {

    default String getK3Login(){
        return "20057842";
    }

    default String getK4Login(){
        return "18013277";
    }

    default String getLogin(){
        return getK3Login();
    }

    default IAction pinAuthorise(){
        return actions("Авторизация")
                .data("Пин код", "1570")
                .data("Код подтверждения","000000")
                .action("Авторизоваться с помощью PIN")
                .build();
    }

    default IAction loginAuthorise(){
        return actions("Авторизация")
                .data("Логин",getK3Login())
                .data("Пин код", "1570")
                .data("Код подтверждения","000000")
                .action("Авторизоваться с помощью логина")
                .build().andThen(actions("Приложение").action("Пропустить приветствие").build());
    }
}
