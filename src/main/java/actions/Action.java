package actions;

import exceptions.NotImplementedException;

/**
 * класс оборачивающий выполнение действия в некое подобие Runnable
 */
public class Action implements IAction{
    protected IAction run;
    public Action(){
        run=empty();
    }
    Action(IAction action){
        this.run=action;
    }
    @Override
    public void run() {
        if (run==null)
            throw new NotImplementedException();
        run.run();
    }

    @Override
    public IAction andThen(IAction action) {
        run=run.andThen(action);
        return this;
    }
}
