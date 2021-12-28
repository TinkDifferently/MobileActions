package io.dimension.elements.base.annotations;

import org.atteo.classindex.IndexAnnotated;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Аннотация для определения имени страницы.
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Pages.class)
@IndexAnnotated
public @interface Page {

    String value();
}
