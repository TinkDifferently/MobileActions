package io.dimension.actions;

import io.dimension.actions.annotations.AndroidAction;
import io.dimension.actions.annotations.MobileAction;
import io.dimension.actions.annotations.IOSAction;
import io.dimension.config.session.DriverUtils;
import io.dimension.elements.base.PageHandler;
import io.dimension.elements.base.interfaces.Clickable;
import io.dimension.elements.mobile.AbstractMobileElement;
import io.dimension.config.session.DriverController;
import io.dimension.exceptions.NoSuchActionException;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import io.dimension.utils.CustomLogger;
import io.dimension.utils.CustomReflection;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

/**
 * Общий предок для всех action-ов описывающих взаимодействие со страницами
 */
public class PageAction extends Action {
    /**
     * переключается к заданной страенице
     *
     * @param page имя страницы
     */
    protected void switchTo(String page) {
        PageHandler.getInstance().getPage(page);
    }

    /**
     * перезагружает заданную страницу
     *
     * @param page имя страницы
     */
    protected void reloadPage(String page) {
        PageHandler.getInstance().createPage(page);
    }

    /**
     * возвращается на предыдущую страницу (двойное выполнение этого
     * действия невозможно, в кэше хранится только одна страница)
     */
    protected void switchBack() {
        PageHandler.getInstance().loadPreviousPage();
    }

    /**
     * конструктор генерирующий исполняемый поток, последовательно прикрепляя к нему
     * переданные io.dimension.actions
     *
     * @param actions имена выполняемых действий
     */
    public PageAction(String... actions) {
        Arrays.stream(actions).forEach(this::loadAction);
    }

    public PageAction(IAction action) {
        super(action);
    }

    /**
     * метод возвращающий значение с заданной страницы, с произвольным типом
     *
     * @param bind имя элемента
     * @param <T>
     * @return элемент
     */
    protected <T> T get(String bind) {
        return (T) PageHandler.getInstance().getActivePage().getElement(bind);
    }

    /**
     * метод возвращающий элемент с заданной страницы
     *
     * @param bind имя элемента
     * @param <T>
     * @return элемент
     */
    protected <T extends AbstractMobileElement> T element(String bind) {
        return (T) get(bind);
    }

    /**
     * выполняет клик по заданному элементу на текущей странице
     *
     * @param target привязка элемента текущей страницы
     */
    protected void click(String target) {
        element(target)
                .as(Clickable.class)
                .click();
    }

    /**
     */
    protected void dismissOptionalAlert() {
        var text = DriverUtils.findNullableElement(By.id("ru.vtb.mobilebanking.android.rc:id/alertTitle"));
        if (text != null) {
            DriverUtils.findWithoutWait(null, By.xpath("//androidx.appcompat.widget.LinearLayoutCompat//android.widget.Button"))
                    .get(0).click();
        }
    }


    /**
     * Добавляет действие к потоку исполнения, в зависимости от платформы
     *
     * @param actionName имя действия из аннотации
     */
    protected void loadAction(String actionName) {
        andThen(() -> {
            try {
                Optional<Method> invokable;
                if (DriverController.getInstance().getCurrentPlatform().is(Platform.IOS)) {
                    invokable = CustomReflection.getAnnotatedMethods(this.getClass(), IOSAction.class).stream()
                            .filter(o -> o.getAnnotation(IOSAction.class).value().equals(actionName)).findFirst();
                    if (invokable.isPresent() && invokable.get().getAnnotation(IOSAction.class).ignore())
                        return;
                } else {
                    invokable = CustomReflection.getAnnotatedMethods(this.getClass(), AndroidAction.class).stream()
                            .filter(o -> o.getAnnotation(AndroidAction.class).value().equals(actionName)).findFirst();
                    if (invokable.isPresent() && invokable.get().getAnnotation(AndroidAction.class).ignore())
                        return;
                }
                invokable.or(() -> CustomReflection.getAnnotatedMethods(this.getClass(), MobileAction.class).stream()
                        .filter(o -> o.getAnnotation(MobileAction.class).value().equals(actionName)).findFirst())
                        .orElseThrow(NoSuchActionException::new).invoke(this);
            } catch (Exception e) {
                CustomLogger.fail(String.format("Не удалось выполнить действие '%s'", actionName), e);
            }
        });
    }
}
