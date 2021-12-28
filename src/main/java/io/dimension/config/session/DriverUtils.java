package io.dimension.config.session;

import io.dimension.elements.mobile.ByExecutor;
import io.appium.java_client.MobileBy;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.dimension.utils.CustomLogger;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Утилитиарный класс для работы с драйвером.
 */
public final class DriverUtils {

    /**
     * Конструктор.
     */
    private DriverUtils() {
    }

    /**
     * Выполняет микроскролл.
     *
     * @param side true - down
     */
    private static void microScroll(boolean side) {
        int from = 200;
        int to = side ? 50 : 350;
        //String s = DriverManager.get().getPageSource();
        new TouchAction((PerformsTouchActions) DriverController.getInstance().getDriver()).press(PointOption.point(10, from))
            .waitAction(WaitOptions.waitOptions(Duration.ofMillis(800)))
            .moveTo(PointOption.point(10, to)).perform().release();
    }

    /**
     * Производит скролл до элемента.
     *
     * @param by локатор элемента
     * @param side направление скролла, true - вниз
     * @return результат скролла, true - скролл выполнен
     */
    public static boolean scrollTo(By by, boolean side) {
        //Добавить проверку поиска
        while (findWithoutWait(null, by).size() == 0) {
            microScroll(side);
        }
        return true;
    }

    /**
     * Производит скролл до элемента, учитывая контекст.
     *
     * @param context элемент для скролла
     * @param by локатор элемента
     * @param side направление скролла, true - вниз
     * @return результат скролла, true - скролл выполнен
     */
    public static boolean scrollToInContext(WebElement context, By by, boolean side) {
        while (!findWithoutWait(context, by).get(0).isDisplayed()) {
            microScroll(side);
        }
        return true;
    }

    /**
     * Производит скролл до элемента c выбором направления скролла.
     *
     * @param element элемент для скролла
     * @param side направление скролла
     * @return результат скролла, true - скролл выполнен
     */
    public static boolean scrollTo(ByExecutor element, Screen.Where side) {
        for (int i = 0; i < 50; i++) {
            if (element == null) {
                return false;
            }
            if (Screen.get().isInside(element)) {
                return true;
            }
            switch (side) {
                case UNDEFINED: {
                    if (element.getLocation().getY() < 88) {
                        scrollUp();
                    } else {
                        scrollDown();
                    }
                    break;
                }
                case UP:
                    scrollUp();
                    break;
                case DOWN:
                    scrollDown();
                    break;
            }
        }
        return false;
    }

    /**
     * Выполняет скролл вниз.
     */
    public static void scrollDown() {
        microScroll(true);
    }

    /**
     * Выполняет скролл вверх.
     */
    public static void scrollUp() {
        microScroll(false);
    }

    /**
     * Выключает неявное ожидание.
     */
    private static void disableImplicitWait() {
        DriverController.getInstance().getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }

    /**
     * Включает неявное ожидание.
     */
    private static void enableImplicitWait() {
        DriverController.getInstance().getDriver().manage().timeouts()
            .implicitlyWait(Config.getGlobalTimeout(), TimeUnit.SECONDS);
    }

    /**
     * Применяет функцию без неявного ожидания.
     *
     * @param function функция
     * @param <V> дженерик
     * @return результат выполнения функции
     */
    public static <V> V runWithoutTimeout(Supplier<V> function) {
        disableImplicitWait();
        V result = function.get();
        enableImplicitWait();
        return result;
    }

    /**
     * Ожидание операций или элементов.
     *
     * @param seconds таймаут в секундах
     * @param isTrue фунция ожидаиня, дролжа вернуть true, ожидание оеончено
     * @param <V> результат работы функции
     * @return результат работы функции
     * @throws TimeoutException если за вресмя ожидания ничего не было найдено
     */
    public static <V> V waitFor(int seconds, Function<? super WebDriver, V> isTrue) {
        try {
            disableImplicitWait();
            return new WebDriverWait(DriverController.getInstance().getDriver(), seconds).until(isTrue);
        } finally {
            enableImplicitWait();
        }
    }

    /**
     * Ожидание операций или элементов. В случае, если элемента нет - выбрасывает исключение.
     *
     * @param seconds время ожидания
     * @param isTrue функция
     * @param <V> дженерик
     * @return null - заглушка
     */
    public static <V> V waitForOrFail(int seconds, Function<? super WebDriver, V> isTrue) {
        try {
            return waitFor(seconds, isTrue);
        } catch (TimeoutException e) {
            CustomLogger.fail("Не удалось дождаться события", e);
        }
        return null;
    }

    /**
     * Ожидание операций или элементов с таймаутом по-умолчанию.
     *
     * @param isTrue фунция ожидаиня, дролжа вернуть true, ожидание оеончено
     * @param <V> результат работы функции
     * @return результат работы функции
     */
    public static <V> V waitFor(Function<? super WebDriver, V> isTrue) {
        return new WebDriverWait(DriverController.getInstance().getDriver(), Config.getGlobalTimeout()).until(isTrue);
    }

    /**
     * Поиск элемента с отлюченным неявным ожиданием.
     *
     * @param context контект поиска или null, если элемент ищется по всему DOM
     * @param by локатор поиска (см. {@link By})
     * @return список найденных элементов. Если элементов не было найдено, возвращается пустой список
     */
    public static List<WebElement> findWithoutWait(WebElement context, By by) {
        SearchContext searchContext = context == null ? DriverController.getInstance().getDriver() : context;
        return runWithoutTimeout(() -> searchContext.findElements(by));
    }

    /**
     * Проверка существует ли элемент в DOM в текущий момент (ожидания отключены).
     *
     * @param context контект поиска или null, если элемент ищется по всему DOM
     * @param by локатор поиска (см. {@link By})
     * @return true, если элементы приисутсвует в DOM
     */
    public static boolean isElementExist(WebElement context, By by) {
        try {
            return findWithoutWait(context, by).size() != 0;
        } catch (WebDriverException e) {
            return false;
        }
    }

    /**
     * Вспомогательный метод для поиска элементов по стратегии ClassChain в нативном контексте драйвера (iOS)
     *
     * @param classChain описание ряда элементов внутри которых будет выполнен сдвиг
     * @param preElementDescription описание элемента внутри этого ряда
     * @param postElementDescription внутренний путь до элемента после сдвига. если значение пустое - результатом будет сам элемент сдвига
     * @return элемент найденный с помощью сдвига
     */
    public static WebElement findIosFollowing(String classChain, String preElementDescription, String postElementDescription) {
        if (StringUtils.isEmpty(classChain) || StringUtils.isEmpty(preElementDescription) || null == postElementDescription) {
            CustomLogger.fail("Не удалось найти элемент по описанию");
        }
        try {
            List<WebElement> nodes = DriverController.getInstance().getDriver().findElements(MobileBy.iOSClassChain(classChain + "/*"));
            WebElement node = DriverUtils
                .findNullableElement(MobileBy.iOSClassChain(String.format("%s/%s", classChain, preElementDescription)));
            if (node == null) {
                return null;
            }
            int i = nodes.indexOf(node);
            if (i == -1) {
                return null;
            }
            String finalSearch = "".equals(postElementDescription)
                ? String.format("%s/*[%d]", classChain, i + 2)
                : String.format("%s/*[%d]/**/%s", classChain, i + 2, postElementDescription);
            return DriverUtils.findNullableElement(MobileBy.iOSClassChain(finalSearch));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Ищет элемент, если не находит - ищет элемент, отталкиваясь от контекста.
     *
     * @param context элемент для поиска
     * @param by локатор
     * @return результат поиска
     */
    public static WebElement findElementOrElse(WebElement context, By by, WebElement defaultValue) {
        try {
            return context == null ? DriverController.getInstance().getDriver().findElement(by) : context.findElement(by);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * Ищет элемент по локатору вне условий контекста.
     *
     * @param by локатор
     * @return результат поиска
     */
    public static WebElement findNullableElement(By by) {
        return findElementOrElse(null, by, null);
    }

}