package io.dimension.utils;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.dimension.config.session.DriverController;

/**
 * Класс для работы логгера.
 */
public final class CustomLogger {

    public static final String EMPTY_STRING = "%ПУСТОЕ ЗНАЧЕНИЕ%";
    public static final String NON_EMPTY_STRING = "%НЕПУСТОЕ ЗНАЧЕНИЕ%";
    private static final Logger logger;

    /**
     *
     */
    static {
        logger = LoggerFactory.getLogger(CustomLogger.class);
    }

    /**
     * Конструктор.
     */
    private CustomLogger() {
    }

    /**
     * Выводит информационное сообщение о действии.
     */
    @Step("{0}")
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * Выводит сообщение об ошибке в действии.
     */
    @Step("Получена ошибка {0}")
    //Комментарий изменять нельзя - на него завязаны "игнорируемые" ошибки
    public static void warn(String message) {
        logger.warn(message);
        attachPageSource();
        attachScreenshot();
    }

    /**
     * Выводит сообщение об ошибке в действии и саму ошибку.
     */
    public static void warn(String message, Throwable e) {
        logger.warn(message, e);
        attachPageSource();
        attachScreenshot();
    }

    /**
     * Выводит сообщение о фатальной ошибке в действии.
     */
    @Step("{0}")
    public static void fail(String message) {
        logger.error(message);
        attachPageSource();
        attachScreenshot();
        throw new AssertionError(message);
    }

    /**
     * Выводит сообщение о фатальной ошибке в действии. так же выводит сообщение об ошибке
     */
    @Step("{0}")
    public static void fail(String message, Throwable e) {
        logger.error(message, e);
        attachPageSource();
        attachScreenshot();
        throw new AssertionError(message, e);
    }

    /**
     * Проверяет, что значения идентичны.
     *
     * @param paramText проверяемый параметр
     * @param checked значение, полученное в тесте
     * @param expected ожидаемое значение парметра
     */
    public static void equals(String paramText, Object checked, Object expected) {
        //если ожидается пустое значение
        if (EMPTY_STRING.equals(expected)) {
            if (checked == null || checked.equals("")) {
                info(String.format("Параметр '%s' ожидаемо пустой", paramText));
            } else {
                warn(String
                        .format("Параметр '%s' неожиданно непустой, зафиксировано значение '%s'", paramText,
                                checked));
            }
            return;
        }

        if (NON_EMPTY_STRING.equals(expected)) {
            if (checked == null || checked.equals("")) {
                warn(String.format("Параметр '%s' неожиданно пустой", paramText));
            } else {
                info(String
                        .format("Параметр '%s' ожидаемо непустой, зафиксировано значение '%s'", paramText,
                                checked));
            }
            return;
        }

        if (Objects.equals(checked, expected)) {
            info(String.format("Параметр '%s' ожидаемо равен '%s'", paramText, String.valueOf(checked)));
        } else {
            warn(String
                    .format("Параметр '%s' равен '%s', ожидалось: '%s'", paramText, String.valueOf(checked),
                            String.valueOf(expected)));
        }
    }

    /**
     * Проверяет, что значения отличаются.
     *
     * @param paramText проверяемый параметр
     * @param expected "неверное" значение параметра
     * @param checked значение, полученное в тесте
     */
    public static void differs(String paramText, Object checked, Object expected) {
        if (Objects.equals(checked, expected)) {
            warn(
                    String.format("Параметр '%s' неожиданно равен '%s'", paramText,
                            String.valueOf(checked)));
        } else {
            info(String.format("Параметр '%s' со значением '%s' ожидаемо не равен '%s'", paramText,
                    String.valueOf(checked), String.valueOf(expected)));
        }
    }

    /**
     * Создает скриншот страницы.
     */
    @Attachment(value = "Page screenshot", type = "image/jpg")
    public static byte[] attachScreenshot() {
        try {
            TakesScreenshot screenMaker = DriverController.getInstance().getDriver();
            if (screenMaker!=null)
            return screenMaker.getScreenshotAs(OutputType.BYTES);
        } catch (Exception ignored){

        }
        return new byte[0];
    }

    /**
     * Создает Page source страницы.
     */
    @Attachment(value = "Page source", type = "text/xml")
    public static byte[] attachPageSource() {
        try {
            RemoteWebDriver webDriver=DriverController.getInstance().getDriver();
            if (webDriver!=null)
                return webDriver.getScreenshotAs(OutputType.BYTES);
        } catch (Exception ignored) {

        }
        return new byte[0];
    }

    /**
     * Проверяет, что значения соотвествует формату.
     *
     * @param paramText проверяемый параметр
     * @param patternRegexp regexp для проверки
     * @param expectedPattern ожидаемый формат значения
     */
    public static void matcher(String paramText, String patternRegexp, String expectedPattern) {
        Pattern patternCompile = Pattern.compile(patternRegexp);
        if (patternCompile.matcher(paramText).matches()) {
            info(String.format("Значение '%s' соответствует формату '%s'", paramText, expectedPattern));
        } else {
            warn(
                    String.format("Значение '%s' не соответствует формату '%s'", paramText, expectedPattern));
        }
    }

    /**
     * Проверяет, что значения соотвествует предзаготовленному формату.
     *
     * @param paramName проверяемый параметр
     * @param checkedText текст для проверки
     * @param pattern регулярно используемый паттерн
     */
//    public static void matches(String paramName, String checkedText, Patterns pattern) {
//        if (pattern.matches(checkedText)) {
//            info(
//                    String.format("Параметр '%s' со значением '%s' соответствует формату '%s'", paramName, checkedText, pattern.displayedName));
//        } else {
//            warn(
//                    (String.format("Параметр '%s' со значением '%s' не соответствует формату '%s'", paramName, checkedText,
//                            pattern.displayedName)));
//        }
//    }

    /**
     * Проверяет вхождение параметра.
     *
     * @param paramText проверяемый параметр
     * @param checked значение, полученное в тесте
     * @param expected проверяемое значение параметра
     */
    public static void contains(String paramText, String checked, String expected) {
        if (checked.contains(expected)) {
            info(String.format("Параметр '%s' содержит значение '%s'", checked, expected));
        } else {
            warn(String
                    .format("Параметр '%s' со значением '%s' не содержит значение '%s'", paramText, checked,
                            expected));
        }
    }


    /**
     * Логгирует результат, ожидаем или нет.
     */
    public static void parameterizedCheck(String s, boolean real, Boolean expected) {
        if (s == null) {
            return;
        }
        String replacer = real == expected ? "ожидаемо" : "неожиданно";
        if (!real) {
            replacer += " не";
        }
        s = s.replace("$placeholder$", replacer);
        Consumer<String> logger = expected == real ? CustomLogger::info : CustomLogger::warn;
        logger.accept(s);
    }
}
