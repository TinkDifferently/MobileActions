package io.dimension.elements.base;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

import java.lang.reflect.Field;

/**
 * Factory class for search elements from context.
 */
public class SearchContextElementLocatorFactory implements ElementLocatorFactory {

    private DefaultElementLocatorFactory defaultElementLocatorFactory;

    /**
     * Constructor.
     *
     * @param searchContext context to search from
     */
    public SearchContextElementLocatorFactory(SearchContext searchContext) {
        defaultElementLocatorFactory = new DefaultElementLocatorFactory(searchContext);
    }

    /**
     * Creates locator for searching from fields annotations.
     *
     * @param field field with annotation
     * @return location for elements
     */
    @Override
    public ElementLocator createLocator(Field field) {
        return defaultElementLocatorFactory.createLocator(field);
    }
}
