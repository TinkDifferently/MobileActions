package io.dimension.actions;

/**
 * функциональный интерфейс расширяющий функционал Runnable с помощью дефолтных методов empty -> возвращает пустое действие, и
 * andThen, позволяющий организовывать цепочку действий
 */
@FunctionalInterface
public interface IAction extends Runnable{
    void run();
    default IAction andThen(IAction action){
        return ()->{
            run();
            action.run();
        };
    }
    default IAction empty(){
        return ()->{};
    }
}
