package me.ci.folks.ai.states;

import me.ci.folks.ai.statemachine.IState;

public class IdleState implements IState {

    private final String name;
    private boolean active;

    public IdleState() {
        this("idle");
    }

    public IdleState(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

}
