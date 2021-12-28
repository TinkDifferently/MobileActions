package ru.vtb.app.chat.tests.pack;

import io.dimension.BaseTest;
import io.dimension.actions.IAction;

public abstract class VtbTest extends BaseTest implements Login {
    private volatile static boolean isFirst = true;

    protected IAction login = () -> {
        if (isFirst) {
            loginAuthorise().run();
            isFirst = false;
        } else {
            pinAuthorise().run();
        }
    };
}

