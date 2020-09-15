package actions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для специфических actions, описывающих взаимодействие с Android
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AndroidAction {
    String value();

    /**
     * true - действие не будет выполнено
     *
     * @return
     */
    boolean ignore() default false;
}
