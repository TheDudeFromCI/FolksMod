package me.ci.folks.ai.statemachine;

public interface TransitionActivator {
    public static final TransitionActivator NEXT_AVAILABLE = t -> true;
    public static final TransitionActivator NEVER = t -> false;
    public static final TransitionActivator ON_DONE = t -> t.getParentState().isDone();

    boolean requirementsMet(Transition transition);
}
