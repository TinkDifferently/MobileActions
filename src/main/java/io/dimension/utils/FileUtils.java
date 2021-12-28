package io.dimension.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Утилитарный класс для работы с файлами.
 */
public final class FileUtils {

    /**
     * Конструктор
     */
    private FileUtils() {
    }

    /**
     * Из текста который в файле делает стрингу.
     */
    protected static String read(BufferedReader bufferedReader) {
        String line = "";
        StringBuilder builder = new StringBuilder();
        while (true) {
            try {
                if (((line = bufferedReader.readLine()) == null)) {
                    break;
                }
            } catch (IOException ignored) {
                //todo ИСКЛЮЧЕНИЕ + проверка на nullpointer!
            }
            builder.append(line).append("\n\r");
        }
        return builder.toString();
    }

    /**
     * Читает весь файл в строку.
     *
     * @param path путь до файда
     * @return строка с прочитанным файлм.
     */
    public static String read(String path) {
        try {
            return read(new BufferedReader(new FileReader(path)));
        } catch (FileNotFoundException ignored) {
            //
        }
        return "";
    }

    /**Обертка для чтения файла
     *
     * @param file - файл
     * @return
     */
    public static String read(File file) {
        try {
            return read(new BufferedReader(new FileReader(file)));
        } catch (FileNotFoundException ignored) {
            //
        }
        return "";
    }

    /** Создает универсальную коллекцию файлов.
     *
     * @param all - флаг
     * @param path - путь до файла
     * @return
     */
    public static Set<File> filesFromPath(boolean all, String path) {
        File file = new File(path);
        int length = 0;
        Set<String> fileNames = Arrays.stream(file.listFiles()).map(File::getPath).collect(Collectors.toCollection(TreeSet::new));
        if (all) {
            while (fileNames.size() != length) {
                length = fileNames.size();
                List<Set<String>> pFileNames = new ArrayList<>();
                fileNames.forEach(o -> {
                    try {
                        pFileNames.add(Arrays.stream(new File(o).listFiles()).map(File::getPath).collect(Collectors.toSet()));
                    } catch (Exception ignored) {

                    }
                });
                pFileNames.forEach(fileNames::addAll);
            }
        }
        return fileNames.stream().map(File::new).collect(Collectors.toSet());
    }

    /**Возвращает коллекцию.
     *
     * @param all
     * @return
     */
    public static Set<File> filesFromPath(boolean all) {
        return filesFromPath(all, System.getProperty("user.dir"));
    }

    public static void write(String path,String text){
        File file = new File(path);
        try (FileWriter writer = new FileWriter(path)){
            file.mkdir();
            file.createNewFile();
            writer.write(text);
            writer.flush();
        } catch(Exception e) {

        }
    }
}
