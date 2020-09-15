package config.session;

import config.session.parsed.ApplicationConfig;
import config.session.parsed.DeviceConfig;
import config.session.parsed.DeviceInstance;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.jetbrains.annotations.Contract;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import utils.CustomLogger;

import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;

/**
 * Класс, отвечающий за выбор драйвера
 */
public final class DriverController {
    private RemoteWebDriver driver;
    private Platform currentPlatform;

    private final static DriverController instance=new DriverController();


    @Contract(pure = true)
    private DriverController(){
    }

    @Contract(pure = true)
    public static DriverController getInstance(){
        return instance;
    }

    @Contract(pure = true)
    public RemoteWebDriver getDriver(){
        return driver;
    }

    public Platform getCurrentPlatform(){
        return currentPlatform;
    }

    /**
     * Создает и кэширует драйвер для заданного устройства и приложения
     * @param applicationBind имя приложения из файла applications.xmf
     * @param deviceBind имя девайса из файла devices.xml
     * @return драйвер
     */
    public RemoteWebDriver createDriver(String applicationBind, String deviceBind){
        if (driver!=null)
            driver.quit();
        DeviceInstance device= DeviceConfig.getInstance().getDeviceInstance(deviceBind);
        CustomLogger.info(String.format("Obtaining container for %s", device));
        DesiredCapabilities capabilities = ApplicationConfig.getInstance().forDevice(applicationBind,device);
        currentPlatform=Platform.valueOf(capabilities.getCapability("platformName").toString().toUpperCase());
        capabilities.setCapability("newCommandTimeout", "6000");
        BiFunction<URL, DesiredCapabilities, AppiumDriver> driverCreator = currentPlatform==Platform.IOS ? IOSDriver::new : AndroidDriver::new;
        driver=driverCreator.apply(Config.getHubUrl(), capabilities);
        driver.manage().timeouts()
                .implicitlyWait(Config.getGlobalTimeout(), TimeUnit.SECONDS);
        return driver;
    }

}
