package config.elements.common;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CustomLoader extends ClassLoader {
    private static final CustomLoader loader;

    private class ObjectWrap<T> {
        T value;

        protected ObjectWrap() {
            this.value = null;
        }

        protected ObjectWrap(T value) {
            this.value = value;
        }

        protected ObjectWrap set(T value) {
            this.value = value;
            return this;
        }

        protected T get() {
            return value;
        }
    }

    public static CustomLoader get() {
        return loader;
    }

    private CustomLoader() {

    }

    static {
        loader = new CustomLoader();
    }

    private static boolean simpleLoad(String classPath, ObjectWrap<Class> clazzWrapper) {
        if (classPath.endsWith(".class")) {
            classPath = classPath.substring(0, classPath.length() - 6);
        }
        if (StringUtils.isBlank(classPath)) {
            return false;
        }
        try {
            clazzWrapper.set(Class.forName(classPath.replaceAll("([/\\\\])", "."), false, get()));
            return true;
        } catch (Throwable e) {
            if (classPath.matches("(.+)([/\\\\])(.+)")) {
                return simpleLoad(classPath.replaceFirst("(.+?)([/\\\\])", ""), clazzWrapper);
            } else {
                return false;
            }
        }
    }

    public Class getClassFromFile(File f) {
        System.out.println(f.getPath());
        byte[] raw; // = new byte[(int) f.length()];
        //System.out.println(f.length());
        InputStream in = null;
        try {
            in = new FileInputStream(f);
            raw = in.readAllBytes();
        } catch (Exception e) {
            throw new NullPointerException();
        }
        try {
            if (in != null) {
                in.close();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectWrap<Class> clazzWrapper = new ObjectWrap<>();
        if (!simpleLoad(f.getPath(), clazzWrapper)) {
            return defineClass(null,
                    raw, 0, raw.length);
        } else {
            return clazzWrapper.get();
    }
    }
}
