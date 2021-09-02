package ru.vtb.messengers;

import actions.Action;
import actions.ActionBuilder;
import actions.IAction;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

/**
 * общий предок всех тестов, обеспечивающий  запуски исполняемых потоков и установку драйвера
 */
public class BaseTest {
    private IAction action;

    @NotNull
    protected BaseTest join(@NotNull IAction action) {
        this.action.andThen(action);
        return this;
    }

    @BeforeSuite
    @Parameters({"device", "app"})
    public void buildPages(@NotNull String device, @NotNull String app) {
//        DriverController.getInstance().createDriver(app, device);
        action = new Action();
    }

    @NotNull
    protected ActionBuilder actions(@NotNull String actionGroupName) {
        return ActionBuilder.type(actionGroupName);
    }

    @Test
    public final void test() {
        action.run();
    }
}
