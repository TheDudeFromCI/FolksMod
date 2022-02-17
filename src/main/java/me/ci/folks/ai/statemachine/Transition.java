package me.ci.folks.ai.statemachine;

public class Transition {
    private final IState parent;
    private final IState child;
    private boolean triggered;

    public Transition(IState parent, IState child) {
        this.parent = parent;
        this.child = child;
    }

    public IState getParentState() {
        return this.parent;
    }

    public IState getChildState() {
        return this.child;
    }

    public boolean isTriggered() {
        if (!this.parent.isActive())
            return false;

        return this.parent.isDone() || this.triggered;
    }

    public void trigger() {
        if (!this.parent.isActive())
            return;

        if (this.parent.isInterruptible()) {
            this.triggered = true;
            tryTrigger();
        }
    }

    public void tick() {
        tryTrigger();
    }

    private void tryTrigger() {
        if (!isTriggered())
            return;

        if (this.parent.isDone())
            this.parent.onStateExit();
        else
            this.parent.onInterrupt();

        this.parent.setActive(false);

        this.child.setActive(true);
        this.child.onStateEnter();
    }
}
