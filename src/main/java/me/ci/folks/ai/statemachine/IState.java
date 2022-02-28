package me.ci.folks.ai.statemachine;

import me.ci.folks.utils.StringUtils;

public interface IState {

    default String getName() {
        String className = getClass().getSimpleName();

        if (className.endsWith("State"))
            className = className.substring(0, className.length() - 5);

        return StringUtils.toSnakeCase(className);
    }

    default String getPath() {
        return getName();
    }

    default void onStateEnter() {
    }

    default void onStateTick() {
    }

    default void onStateExit() {
    }

    default void onInterrupt() {
    }

    default boolean isInterruptible() {
        return true;
    }

    boolean isDone();

    void setActive(boolean active);

    boolean isActive();
}
