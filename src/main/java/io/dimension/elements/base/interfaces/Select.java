package io.dimension.elements.base.interfaces;

import java.util.Collection;

public interface Select<T extends SelectableItem> {
    Collection<T> getOptions();

    default void select(int index) {
        for (T t : getOptions()) {
            if (index-- == 0) {
                t.click();
                return;
            }
        }
        throw new RuntimeException(String.format("Could not select item with index '%d'", index));
    }

    default void select(String title) {
        getOptions().stream().filter(option -> title.equals(option.getTitle())).findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("No such item '%s'", title)))
                .click();
    }
}
