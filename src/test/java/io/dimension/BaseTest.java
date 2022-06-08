package io.dimension;

import io.dimension.actions.Action;
import io.dimension.actions.ActionBuilder;
import io.dimension.actions.IAction;
import io.dimension.config.session.DriverController;
import io.dimension.config.session.DriverUtils;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Attachment;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * общий предок всех тестов, обеспечивающий  запуски исполняемых потоков и установку драйвера
 */
public abstract class BaseTest implements UseActions {
    private IAction action;

    @BeforeClass
    @Parameters({"device", "app"})
    public final void mount(@NotNull String device, @NotNull String app) {
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

    @Attachment(value = "Attachment Screenshot", type = "image/png")
    public byte[] makeScreenshot() {
        return ((TakesScreenshot) DriverController.getInstance().getDriver())
                .getScreenshotAs(OutputType.BYTES);
    }

    @Test
    public final void test(ITestContext context) {
        AllureLifecycle lifecycle = Allure.getLifecycle();
        Allure.epic(context.getName());
        Optional.ofNullable(this.getClass().getAnnotation(DisplayName.class))
                .ifPresent(o -> lifecycle.updateTestCase(testResult -> testResult.setName(o.value())));
        try {
            action.run();
        } catch (Exception any){
            try {
                makeScreenshot();
            } catch (Exception ignored){
                System.out.println("Could not make screenshot");
            }
            throw any;
        }
    }


    @AfterClass
    public void unmount(ITestContext context) throws InterruptedException {
        TimeUnit.SECONDS.sleep(7);

        DriverController.getInstance().unmount();
    }
}
