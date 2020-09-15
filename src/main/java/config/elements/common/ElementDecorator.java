package config.elements.common;

import io.appium.java_client.HasSessionDetails;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.pagefactory.interceptors.InterceptorOfASingleElement;
import io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import utils.CustomLogger;
import utils.CustomReflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static io.appium.java_client.internal.ElementMap.getElementClass;
import static io.appium.java_client.pagefactory.utils.ProxyFactory.getEnhancedProxy;

/**
 * Decorator for custom elements.
 */
public class ElementDecorator extends AppiumFieldDecorator {

    private final Class[] supportedAnnotations = {FindBy.class, FindAll.class, FindBys.class, AndroidFindBy.class,
            iOSXCUITFindBy.class};
    private WebDriver webDriver;
    private int failCount = 0;

    /**
     * Constructor.
     *
     * @param context  searchContext to search from
     * @param duration search timeout
     */
    public ElementDecorator(SearchContext context, Duration duration) {
        super(context, duration);
        webDriver = WebDriverUnpackUtility.unpackWebDriverFromSearchContext(context);
    }

    /**
     * Constructor using default timeout.
     *
     * @param context searchContext to search from
     */
    public ElementDecorator(SearchContext context) {
        this(context, DEFAULT_WAITING_TIMEOUT);
    }


    /**
     * Constructor using default timeout.
     *
     * @param loader loader
     * @param field  field
     */
    @Override
    public Object decorate(ClassLoader loader, Field field) {

        if (!isDecoratableField(field) && !isDecoratableList(field)) {
            return null;
        }
        ElementLocatorFactory factory;
        try {
            Field factoryFiled = this.getClass().getSuperclass().getDeclaredField("widgetLocatorFactory");
            factoryFiled.setAccessible(true);
            factory = (ElementLocatorFactory) factoryFiled.get(this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        ElementLocator locator = factory.createLocator(field);
        if (locator == null) {
            return null;
        }
        if (CommonElement.class.isAssignableFrom(field.getType())) {
            WebElement webElement = proxyForLocator(locator);
            return createInstance(field.getType(), webElement);
        }
        return decorateList(field, locator);
    }

    /**
     * Проверяет? следует ли является ли поле лимтом и следует ли его декорировать
     *
     * @param field проверяемое поле
     * @return true, если следует декорировать
     * Исключения обрабатываются на стороне вызова
     */
    private boolean isDecoratableList(Field field) {
        if (!List.class.isAssignableFrom(field.getType())) {
            return false;
        }
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            return false;
        }
        Class<?> listTypeClass;
        Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
        try {
            listTypeClass = Class.forName(listType.getTypeName());
        } catch (ClassNotFoundException e) {
            return false;
        }
        if (!CommonElement.class.isAssignableFrom(listTypeClass)) {
            return false;
        }
        return isSupportedAnnotationPresent(field);
    }


    /**
     * Проверяет, следует ли декорировать поле.
     *
     * @param field проверяемое поле
     * @return true, если поле следует декорировать
     */
    private boolean isDecoratableField(Field field) {
        return isSupportedAnnotationPresent(field) && CommonElement.class.isAssignableFrom(field.getType());
    }

    /**
     * Проверяет у поля наличие используемых для поиска элементов аннотаций.
     *
     * @param field проверяемое поле
     * @return true, если аннотация присутствует
     * Исключения обрабатываются на стороне вызова
     */
    @SuppressWarnings("unchecked")
    private boolean isSupportedAnnotationPresent(Field field) {
        for (Class annotation : supportedAnnotations) {
            if (field.getDeclaredAnnotation(annotation) != null) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private List<? extends CommonElement> decorateList(Field field, ElementLocator locator) {
        Type genericType = field.getGenericType();
        Class<? extends CommonElement> listTypeClass;
        Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
        try {
            listTypeClass = (Class<? extends CommonElement>) Class.forName(listType.getTypeName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<? extends CommonElement> proxiedList = proxyForListLocator(locator, listTypeClass);

        return Collections.unmodifiableList(proxiedList);
    }

    /**
     * Создание объекта элемента.
     *
     * @param klass   объект Class элемента
     * @param element элемент
     * @param <T>
     * @return объект элемента
     */
    @SuppressWarnings("unchecked")
    private <T extends CommonElement> T createInstance(Class<?> klass, WebElement element) {
        try {
            return (T) klass.getConstructor(WebElement.class).newInstance(element);
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private WebElement proxyForLocator(ElementLocator locator) {
        try {
            HasSessionDetails webDriver = (HasSessionDetails) this.webDriver;
            ElementInterceptor elementInterceptor = new ElementInterceptor(locator, this.webDriver);
            return getEnhancedProxy(getElementClass(webDriver.getPlatformName(), webDriver.getAutomationName()), elementInterceptor);
        } catch (Exception e) {
            ElementInterceptor elementInterceptor = new ElementInterceptor(locator, this.webDriver);
            return getEnhancedProxy(getElementClass("ARM - htmlunit", ""), elementInterceptor);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends CommonElement> List<T> proxyForListLocator(ElementLocator locator, Class<T> klass) {
        return (List<T>) Proxy.newProxyInstance(ElementDecorator.class.getClassLoader(),
                new Class[]{List.class},
                new ElementsListInvocationHandler<>(locator, klass));
    }


    /**
     * Infocation handler for list of custom elements.
     *
     * @param <T> type of elements in list
     */
    private final class ElementsListInvocationHandler<T extends CommonElement> implements InvocationHandler {

        private List<T> elementsCache;
        private ElementLocator locator;
        private Class<T> listGenericClass;

        private ElementsListInvocationHandler(ElementLocator locator, Class<T> listGenericClass) {
            this.locator = locator;
            this.listGenericClass = listGenericClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            List<WebElement> webElements;
            if (elementsCache == null) {
                elementsCache = new LinkedList<>();
                webElements = locator.findElements();
                webElements.forEach(we -> elementsCache.add(createInstance(listGenericClass, we)));
            }
            try {
                return method.invoke(elementsCache, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Custom elements invocation handler.
     */
    private final class ElementInterceptor extends InterceptorOfASingleElement {

        private ElementInterceptor(ElementLocator locator, WebDriver driver) {
            super(locator, driver);
        }

        @Override
        protected Object getObject(WebElement element, Method method, Object[] args) {
            try {
                if (!method.getDeclaringClass().isAssignableFrom(element.getClass())) {
                    return CustomReflection.invokeOr(element, method.getName(), null, args);
                }
                return method.invoke(element, args);
            } catch (IllegalAccessException t) {
                CustomLogger.fail("Внутренняя ошибка", t);
            } catch (InvocationTargetException t) {
                failCount++;
                if (failCount < 3) {
                    return getObject(element, method, args);
                } else {
                    CustomLogger.fail("Не удалось выполнить действие");
                    throw new RuntimeException(t);
                }
            }
            return null;
        }
    }
}
