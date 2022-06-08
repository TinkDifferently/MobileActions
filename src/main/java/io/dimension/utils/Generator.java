package io.dimension.utils;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {


    private final static char[] ru = new char[33];
    private final static char[] en = new char[28];
    private final static char[] num = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};

    static {
        char a = 'а';
        for (char i = 0; a < 'я'; i++) {
            ru[i] = a++;
        }
        ru[32] = 'ё';
        a = 'a';
        for (char i = 0; a < 'z'; i++) {
            en[i] = a++;
        }
    }

    public static @NotNull String generateString(String type) {
        return generateString(type, -1);
    }

    public static @NotNull String generateString(String type, int length) {
        return generateString(type, length, true);
    }

    private static char generateChar(char @NotNull [] source, boolean useUpperCase) {
        var result = source[ThreadLocalRandom.current().nextInt(0, source.length)];
        return useUpperCase
                ? Character.toUpperCase(result)
                : result;
    }

    public static @NotNull String generateString(String type, int length, boolean useUpperCase) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            var upperCase = useUpperCase && ThreadLocalRandom.current().nextBoolean();
            switch (type) {
                case "ru": {
                    builder.append(generateChar(ru, upperCase));
                }
                break;
                case "en": {
                    builder.append(generateChar(en, upperCase));
                }
                break;
                case "num": {
                    builder.append(generateChar(num, upperCase));
                }
                break;
                case "any": {
                    int ran = ThreadLocalRandom.current().nextInt(0, 3);
                    switch (ran) {
                        case 0:
                            builder.append(generateChar(ru, upperCase));
                            break;
                        case 1:
                            builder.append(generateChar(en, upperCase));
                            break;
                        case 2:
                            builder.append(generateChar(num, upperCase));
                            break;
                        default:
                    }
                }
                default:
            }
        }
        return builder.toString();
    }


    public static String generateName() {
        var names = List.of("Сильвестр", "Пиастр", "Лурц", "Джон", "Лондо", "Магуайр", "Галадриэль", "Палпатин");
        return names.get(ThreadLocalRandom.current().nextInt(names.size()));
    }

    public static String generateSurname() {
        var names = List.of("Чан", "Сапфир", "Бэггинс", "Ланистер", "Синклер", "Пушкаш", "Иванова", "Кеноби");
        return names.get(ThreadLocalRandom.current().nextInt(names.size()));
    }

    public static String generatePatronymic() {
        var names = List.of("ИбнХаттаб", "АльСауди", "Бьорнсон", "Рорихович");
        return names.get(ThreadLocalRandom.current().nextInt(names.size()));
    }

    public static @NotNull LocalDate generateBirthDate(int minAge, int maxAge) {
        var current = LocalDate.now();
        var period = LocalDate.now().getYear() - maxAge;
        var year = ThreadLocalRandom.current().nextInt(period, current.getYear() - minAge);
        var month = ThreadLocalRandom.current().nextInt(1, 13);
        var day = ThreadLocalRandom.current().nextInt(1, Month.of(month).length(Year.isLeap(year)));
        return LocalDate.of(
                year,
                month,
                day
        );
    }


}
