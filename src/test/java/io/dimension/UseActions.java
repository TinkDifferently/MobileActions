package io.dimension;

import io.dimension.actions.ActionBuilder;
import org.jetbrains.annotations.NotNull;

public interface UseActions {
    @NotNull
    default ActionBuilder actions(@NotNull String actionGroupName) {
        return ActionBuilder.type(actionGroupName);
    }
}
