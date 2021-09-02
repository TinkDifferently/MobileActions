package config.elements.common;


import config.elements.common.annotations.Page;
import config.elements.common.annotations.Pages;
import exceptions.LoggedException;
import utils.CustomReflection;
import utils.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Класс для хранения объектов страниц.
 */
@SuppressWarnings("RedundantSuppression")
public final class PageHandler {

    private final ThreadLocal<Map<Class<? extends CommonPage>, Object>> pageStorage;
    private final ThreadLocal<CommonPage> activePage;
    private final ThreadLocal<CommonPage> previousPage;
    private final HashMap<String, Class<? extends CommonPage>> pageNames = new HashMap<>();

    private PageHandler() {
        pageStorage = new ThreadLocal<>();
        pageStorage.set(new HashMap<>());

        activePage = new ThreadLocal<>();
        previousPage = new ThreadLocal<>();
        try {
            populatePages();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static PageHandler instance = new PageHandler();

    public static PageHandler getInstance() {
        return instance;
    }

    /**
     * собирает ссылки на все существующие страницы
     */
    @SuppressWarnings("unchecked")
    private void populatePages() throws ClassNotFoundException {
        Set<File> allFiles = FileUtils.filesFromPath(true);
        Set<File> allClasses = allFiles.stream().filter(o -> o.getName().endsWith(".class"))
                .collect(Collectors.toSet());
        final Boolean[] f = {true};
        Set<Class> classes = new HashSet<>();
        for (int i = 0; i < 50; i++) {
            classes.clear();
            f[0] = true;
            allClasses.forEach(o -> {
                try {
                    classes.add(CustomLoader.get().getClassFromFile(o));
                } catch (Throwable e) {
                    f[0] = false;
                }
            });
            if (f[0]) {
                break;
            }
        }
        BiConsumer<Page, Class> biConsumer = (key, value) ->
                pageNames.put(key.value(), value);
        classes.forEach(o -> {
            Pages pages = (Pages) o.getAnnotation(Pages.class);
            if (pages != null) {
                for (Page page : pages.value())
                    biConsumer.accept(page, o);
                return;
            }
            Page page = (Page) o.getAnnotation(Page.class);
            if (page != null) {
                biConsumer.accept(page, o);
            }
        });
    }

    /**
     * Получает объект страницы из хранилища или пытается создать, если страница еще не была инициализирована.
     *
     * @param mustCreate флаг, нужно ли создавать новую страницу, даже если она уже была создана
     * @param pageClass  класс искомой страницы
     * @param <P>        тип искомой страницы
     * @return объект страницы
     */
    @SuppressWarnings("unchecked")
    public <P extends CommonPage> P getPage(boolean mustCreate, Class<P> pageClass) {
        if (activePage != null && activePage.get() != null && !Objects.equals(activePage.get().getClass(), pageClass)) {
            activePage.get().afterClose();
            previousPage.set(activePage.get());
        }
        Optional<P> optionalPage = Optional.ofNullable((P) pageStorage.get().get(pageClass));
        P page;
        page = mustCreate || optionalPage.isEmpty() ? CustomReflection.createNewInstanceOr(pageClass, null) : optionalPage.get();
        activePage.set(page);
        return page;
    }

    /**
     * Получает объект страницы из хранилища или пытается создать, если страница еще не была инициализирована.
     *
     * @param pageClass класс искомой страницы
     * @param <P>       тип искомой страницы
     * @return объект страницы
     */
    @SuppressWarnings("unchecked")
    public <P extends CommonPage> P getPage(Class<P> pageClass) {
        return getPage(false, pageClass);
    }

    /**
     * Получает объект страницы из хранилища по его имени или пытается создать, если страница еще не была инициализирована.
     *
     * @param name имя искомой страницы
     * @param <P>  тип искомой страницы
     * @return объект страницы
     */
    @SuppressWarnings("unchecked")
    public <P extends CommonPage> P getPage(String name) {
        Class<P> pageClass = (Class<P>) pageNames.get(name);
        if (pageClass == null) {
            throw new RuntimeException(String.format("Page '%s' is not registered", name));
        }
        return getPage(pageClass);
    }

    /**
     * Создает новую страницу по ее имени, даже если она уже была создана.
     *
     * @param pageClass класс страницы, указывается в аннотации {@link Page}
     * @param <P>       тип страницы
     * @return объект страницы
     */
    @SuppressWarnings("unchecked")
    public <P extends CommonPage> P createPage(Class<P> pageClass) {
        return getPage(true, pageClass);
    }

    /**
     * Создает новую страницу по ее имени, даже если она уже была создана.
     *
     * @param name имя страницы, указывается в аннотации {@link Page}
     * @param <P>  тип страницы
     * @return объект страницы
     */
    @SuppressWarnings("unchecked")
    public <P extends CommonPage> P createPage(String name) {
        Class<P> pageClass = (Class<P>) Optional.ofNullable(pageNames.get(name))
                .orElseThrow(() -> new LoggedException(name));
        return createPage(pageClass);
    }

    /**
     * Возвращает предыдущую использовавшеюся страницу.
     *
     * @param <P> тип страницы
     * @return объект страницы
     */
    @SuppressWarnings("unchecked")
    public <P extends CommonPage> P loadPreviousPage() {
        if (previousPage.get() != null) {
            activePage.set(previousPage.get());
        }
        return (P) activePage.get();
    }

    /**
     * Сохраняет объект страницы в хранилище.
     *
     * @param page объекст страницы
     */
    public void putPage(CommonPage page) {
        pageStorage.get().put(page.getClass(), page);
        activePage.set(page);
    }

    /**
     * Возвращает текущую страницу.
     *
     * @param <P> тип страницы
     * @return обект страницы
     */
    @SuppressWarnings("unchecked")
    public <P extends CommonPage> P getActivePage() {
        return (P) activePage.get();
    }
}
