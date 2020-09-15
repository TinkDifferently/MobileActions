package actions.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для специфических actions, описывающих взаимодействие с IOS
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IOSAction {
    String value();
    /**
     * true - действие не будет выполнено
     * @return
     */
    boolean ignore() default false;
}
