package io.dimension.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Регулярные выражения для суммы, сумма + валюта, количество дней, процентная ставка.
 */
public enum Patterns {

    COMBINATION_PATTERN("", "");


    public String displayedName;
    public Pattern pattern;

    /**
     * Конструктор.
     *
     * @param displayedName отображаемое имя шаблона
     * @param regex регекс выражение
     */
    Patterns(String displayedName, String regex) {
        this.displayedName = displayedName;
        this.pattern = Pattern.compile(regex);
    }

    /**
     * Метод для сборки комбинированного паттерна.
     *
     * @param displayedName Проверяемый формат, например, 'XXX.XX - XXX XXX.XX$'
     * @param regex регулярное выражение
     * @return комбинированный паттерн
     */
    public static Patterns compile(String displayedName, String regex) {
        COMBINATION_PATTERN.displayedName = displayedName;
        COMBINATION_PATTERN.pattern = Pattern.compile(regex);
        return COMBINATION_PATTERN;
    }

    /**
     * Метод для сборки комбинированного паттерна переменной длины.
     *
     * @param displayedName Проверяемый формат
     * @param format формат регулярного выражения
     * @param args регулярные выражения
     * @return комбинированный паттерн
     */
    public static Patterns compile(String displayedName, String format, String... args) {
        COMBINATION_PATTERN.displayedName = displayedName;
        COMBINATION_PATTERN.pattern = Pattern.compile(String.format(format, args));
        return COMBINATION_PATTERN;
    }

    /**
     * Возвращает паттерн по его имени.
     *
     * @param name имя паттерна
     * @return сохраненный паттерн
     */
    public static Patterns getPattern(String name) {
        return Patterns.valueOf(name);
    }


    /**
     * Проверка строки с помощью регулярного выражения.
     *
     * @param value проверяемая строка
     * @return true/false
     */
    public boolean matches(String value) {
        try {
            Matcher matcher = pattern.matcher(value);
            return matcher.matches();
        } catch (Exception any) {
            return false;
        }
    }

    @Override
    public String toString() {
        return pattern.pattern();
    }

    /**
     * Вытаскивает значение по шаблону и возвращает его.
     *
     * @param value значение
     * @return возвращает одно или несколько значений
     */
    public String extractFirst(String value) {
        Matcher matcher = this.pattern.matcher(value);
        return matcher.find() ? matcher.group() : "";
    }
}
