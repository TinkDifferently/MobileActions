package io.dimension.actions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для платформонезависимых io.dimension.actions
 * методы, помеченные этой аннотацией будут выполняться только если нет методов,
 * помеченных аннотацией с тем же именем для соответствующей платформы
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MobileAction {
    String value();
}
