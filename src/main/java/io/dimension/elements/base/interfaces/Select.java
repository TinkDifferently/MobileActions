package io.dimension.elements.base.interfaces;

import io.dimension.exceptions.BadTypeException;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public interface Select<T extends SelectableItem> {
    Collection<T> getOptions();

    private void select(SelectableItem item, @NotNull String strategy) {
        switch (strategy) {
            case "click":
                item.click();
                return;
            case "longPress":
                if (item instanceof LongPress) {
                    ((LongPress) item).longPress();
                    return;
                }
                throw new BadTypeException(String.format("'%s' does not support strategy '%s'", item.getClass(), strategy));
            default:
                throw new BadTypeException(String.format("Unknown strategy '%s'", strategy));
        }
    }

    default void select(int index, String strategy) {
        for (T t : getOptions()) {
            if (index-- == 0) {
                select(t, strategy);
                return;
            }
        }
        throw new RuntimeException(String.format("Could not select item with index '%d'", index));
    }

    default void select(String title, String strategy) {
        var result = getOptions().stream().filter(option -> title.equals(option.getTitle())).findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("No such item '%s'", title)));
        select(result, strategy);
    }

    default void selectAny(String strategy) {
        var options = getOptions();
        var index = ThreadLocalRandom.current().nextInt(options.size());
        for (var option : options) {
            if (index-- == 0) {
                select(option, strategy);
            }
        }
    }

    default void select(int index) {
        select(index, "click");
    }

    default void select(String title) {
        select(title, "click");
    }

    default void selectAny() {
        selectAny("click");
    }



}
