package io.dimension.config.session;

import io.dimension.elements.mobile.AbstractMobileElement;
import io.dimension.elements.mobile.ByExecutor;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import io.dimension.utils.CustomLogger;

/**
 * Класс для описания дисплея устройства.
 */
public class Screen {

    private static Screen screen;
    private Rectangle rectangle;

    /**
     * Вложенный класс для описания направления скролла.
     */
    public enum Where {
        UP("вверх"),
        DOWN("вниз"),
        UNDEFINED("неизвестно");

        private String description;

        Where(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    /**
     * Возвращает инстантс объекта.
     */
    public static Screen get() {
        return screen;
    }

    static {
        screen = new Screen();
    }

    {
        rectangle = null;
    }

    /**
     * Возвращает элемент-родитель для последующего поиска.
     *
     * @return элемент-родитель
     */
    private WebElement getRootElement() {
        return DriverController.getInstance().getCurrentPlatform() == Platform.IOS
                ? DriverController.getInstance().getDriver().findElement(MobileBy.iOSClassChain("XCUIElementTypeAny"))
                : DriverController.getInstance().getDriver().findElement(By.xpath("//*"));
    }

    /**
     * Возвращает положение элемента на экране в виде сущности Rectangle.
     *
     * @return положение элемента на экране
     */
    private Rectangle getRectangle() {
        try {
            if (rectangle == null) {
                rectangle = getRootElement().getRect();
            }
        } catch (Exception e) {
            CustomLogger.fail("Невозможно получить размеры экрана. Драйвер был поврежден", e);
        }
        return rectangle;
    }

    /**
     * Проверяет, находится ли элемент в пределах его Rectangle по его координатам.
     *
     * @param element элемент (WebElement)
     * @return true, если элемент в пределах
     */
    public boolean isInside(WebElement element) {
        if (element == null) {
            return false;
        }
        Rectangle rectangle = element.getRect();
        boolean result = rectangle.getX() + 5 >= getRectangle().getX();
        result &= rectangle.getY() >= getRectangle().getY();
        result &= rectangle.getX() + rectangle.getWidth() - 5 < getRectangle().getX() + getRectangle().getWidth();
        result &= rectangle.getY() + rectangle.getHeight() < getRectangle().getY() + getRectangle().getHeight();
        return result;
    }

    /**
     * @param element элемент (ByExecutor)
     * @return true, если элемент в пределах
     * @see Screen#isInside(WebElement)
     */
    public boolean isInside(ByExecutor element) {
        return element != null && isInside(element.getInitialElement());
    }

    /**
     * @param element элемент (AbstractElement)
     * @return true, если элемент в пределах
     * @see Screen#isInside(WebElement)
     */
    public boolean isInside(AbstractMobileElement element) {
        return element != null && isInside(element.$());
    }
}
