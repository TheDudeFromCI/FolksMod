package me.ci.folks.ai.statemachine;

public class Transition {
    private final IState parent;
    private final IState child;
    private final TransitionActivator activator;

    public Transition(IState parent, IState child, TransitionActivator activator) {
        this.parent = parent;
        this.child = child;
        this.activator = activator;
    }

    public IState getParentState() {
        return this.parent;
    }

    public IState getChildState() {
        return this.child;
    }

    public void forceTrigger() {
        if (!this.parent.isActive())
            return;

        if (this.parent.isDone())
            this.parent.onStateExit();
        else
            this.parent.onInterrupt();

        this.parent.setActive(false);

        this.child.setActive(true);
        this.child.onStateEnter();
    }

    private boolean isTriggered() {
        return this.parent.isActive()
            && (this.parent.isInterruptible() || this.parent.isDone())
            && this.activator.requirementsMet(this);
    }

    public void tick() {
        if (isTriggered())
            forceTrigger();
    }

    public TransitionActivator getActivator() {
        return this.activator;
    }
}
