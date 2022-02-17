package me.ci.folks.ai.statemachine;

import me.ci.folks.utils.StringUtils;

public interface IState {

    default String getName() {
        return StringUtils.toCamelCase(getClass().getSimpleName());
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
