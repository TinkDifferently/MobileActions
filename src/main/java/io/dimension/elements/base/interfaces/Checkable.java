package io.dimension.elements.base.interfaces;

public interface Checkable {


    boolean getState();

    void switchState();

    default void check() {
        setState(true);
    }

    default void uncheck() {
        setState(false);
    }

    default void setState(boolean state) {
        if (getState() != state) {
            switchState();
        }
    }
}
