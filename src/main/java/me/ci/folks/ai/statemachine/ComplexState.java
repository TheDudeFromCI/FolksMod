package me.ci.folks.ai.statemachine;

import me.ci.folks.ai.states.IdleState;

public class ComplexState extends StateMachine implements IState {

    private final IState exitState = new IdleState("end");
    private final String name;
    private boolean active;

    public ComplexState(String name) {
        super();
        this.name = name;

        addState(this.exitState);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(getName());

        if (isActive()) {
            sb.append(" > ");
            sb.append(getActiveState().getName());
        }

        return sb.toString();
    }

    public IState getExitState() {
        return this.exitState;
    }

    @Override
    public boolean isDone() {
        return this.exitState.isActive();
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void onStateEnter() {
        resetState();
        getActiveState().onStateEnter();
    }

    @Override
    public void onStateExit() {
        getActiveState().onStateExit();
        resetState();
    }

    @Override
    public void onInterrupt() {
        getActiveState().onInterrupt();
        resetState();
    }

    private void resetState() {
        this.states.forEach(s -> s.setActive(false));
        this.entryState.setActive(true);
    }

    @Override
    public void onStateTick() {
        tick();
    }

    @Override
    public boolean isInterruptible() {
        return getActiveState().isInterruptible();
    }

}
