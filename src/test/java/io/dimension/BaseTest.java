package io.dimension;

import io.dimension.actions.Action;
import io.dimension.actions.ActionBuilder;
import io.dimension.actions.IAction;
import io.dimension.config.session.DriverController;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import org.jetbrains.annotations.NotNull;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

/**
 * общий предок всех тестов, обеспечивающий  запуски исполняемых потоков и установку драйвера
 */
public abstract class BaseTest implements UseActions{
    private IAction action;

    @BeforeTest
    @Parameters({"device", "app"})
    public final void mount(@NotNull String device, @NotNull String app){
        DriverController.getInstance().createDriver(app, device);
        action = new Action();
        mount();
    }

    protected abstract void mount();

    @NotNull
    protected BaseTest join(@NotNull IAction action) {
        this.action.andThen(action);
        return this;
    }

    @Test
    public final void test(ITestContext context) {
        AllureLifecycle lifecycle= Allure.getLifecycle();
        lifecycle.updateTestCase(testResult -> testResult.setName(context.getName()));
        action.run();
    }
}
