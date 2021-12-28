package io.dimension.elements.base;


import io.dimension.elements.base.annotations.Page;
import io.dimension.elements.base.annotations.Pages;
import io.dimension.exceptions.LoggedException;
import io.dimension.utils.CustomReflection;
import org.atteo.classindex.ClassIndex;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

/**
 * Класс для хранения объектов страниц.
 */
@SuppressWarnings("RedundantSuppression")
public final class PageHandler {

    private final Map<Class<? extends IPage>, Object> pageStorage;
    private IPage activePage;
    private IPage previousPage;
    private final HashMap<String, Class<? extends IPage>> pageNames = new HashMap<>();

    private PageHandler() {
        pageStorage = new HashMap<>();
        activePage = new IPage() {
            @Override
            public void waitForLoadFinished() {

            }

            @Override
            public <T> T getElement(String name) {
                throw new RuntimeException("Page was not initialized");
            }

            @Override
            public void afterClose(Object... args) {

            }

        };
        populatePages();
    }

    private static final ThreadLocal<PageHandler> pInstance = ThreadLocal.withInitial(PageHandler::new);

    public static PageHandler getInstance() {
        return pInstance.get();
    }

    /**
     * собирает ссылки на все существующие страницы
     */
    @SuppressWarnings("unchecked")
    private void populatePages() {
        BiConsumer<Page, Class<? extends IPage>> biConsumer = (key, value) ->
                pageNames.put(key.value(), value);
        ClassIndex.getAnnotated(Page.class).forEach(o -> {
            Pages pages = o.getAnnotation(Pages.class);
            if (pages != null) {
                for (Page page : pages.value()) {
                    biConsumer.accept(page, (Class<? extends IPage>) o);
                }
                return;
            }
            Page page = o.getAnnotation(Page.class);
            if (page != null) {
                biConsumer.accept(page, (Class<? extends IPage>) o);
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
    public <P extends IPage> P getPage(boolean mustCreate, Class<P> pageClass) {
        if (activePage != null && !Objects.equals(activePage.getClass(), pageClass)) {
            activePage.afterClose();
            previousPage = activePage;
        }
        Optional<P> optionalPage = Optional.ofNullable((P) pageStorage.get(pageClass));
        P page;
        page = mustCreate || optionalPage.isEmpty() ? CustomReflection.createNewInstanceOr(pageClass, null) : optionalPage.get();
        activePage = page;
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
    public <P extends IPage> P getPage(Class<P> pageClass) {
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
    public <P extends IPage> P getPage(String name) {
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
    public <P extends IPage> P createPage(Class<P> pageClass) {
        return getPage(true, pageClass);
    }

    /**
     * Создает новую страницу по ее имени, даже если она уже была создана.
     *
     * @param <P>  тип страницы
     * @param name имя страницы, указывается в аннотации {@link Page}
     */
    @SuppressWarnings("unchecked")
    public <P extends IPage> void createPage(String name) {
        Class<P> pageClass = (Class<P>) Optional.ofNullable(pageNames.get(name))
                .orElseThrow(() -> new LoggedException(name));
        createPage(pageClass);
    }

    /**
     * Возвращает предыдущую использовавшеюся страницу.
     */
    @SuppressWarnings("unchecked")
    public void loadPreviousPage() {
        if (previousPage != null) {
            activePage = previousPage;
        }
    }

    /**
     * Сохраняет объект страницы в хранилище.
     *
     * @param page объекст страницы
     */
    public void putPage(IPage page) {
        pageStorage.put(page.getClass(), page);
        activePage = page;
    }

    /**
     * Возвращает текущую страницу.
     *
     * @param <P> тип страницы
     * @return обект страницы
     */
    @SuppressWarnings("unchecked")
    public <P extends IPage> P getActivePage() {
        return (P) activePage;
    }
}
