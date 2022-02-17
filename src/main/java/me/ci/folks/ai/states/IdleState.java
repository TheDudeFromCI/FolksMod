package me.ci.folks.ai.states;

import me.ci.folks.ai.statemachine.IState;

public class IdleState implements IState {

    private boolean active;

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
