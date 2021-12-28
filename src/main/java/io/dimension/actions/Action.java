package io.dimension.actions;

import io.dimension.exceptions.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/**
 * класс оборачивающий выполнение действия в некое подобие Runnable
 */
public class Action implements IAction {
    protected IAction run;

    public Action() {
        run = empty();
    }

    Action(IAction action) {
        this.run = action;
    }

    @Override
    public void run() {
        if (run == null) {
            throw new NotImplementedException();
        }
        run.run();
    }

    @Override
    public IAction andThen(IAction action) {
        run = run.andThen(action);
        return this;
    }

    @NotNull
    public ActionBuilder dispatch(String functionalName) {
        return ActionBuilder.type(functionalName);
    }
}
