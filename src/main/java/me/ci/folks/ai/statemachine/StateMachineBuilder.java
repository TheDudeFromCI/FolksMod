package me.ci.folks.ai.statemachine;

import java.util.HashMap;

public class StateMachineBuilder {

    private final HashMap<String, IState> states = new HashMap<>();
    private final ComplexState stateMachine;

    public StateMachineBuilder(String name) {
        this.stateMachine = new ComplexState(name);
        this.states.put("ENTER", this.stateMachine.getEntryState());
        this.states.put("EXIT", this.stateMachine.getExitState());
    }

    public StateMachineBuilder addState(String identifier, IState state) {
        if (this.states.containsKey(identifier))
            throw new IllegalArgumentException("Identifier '" + identifier + "' already exists!");

        this.stateMachine.addState(state);
        this.states.put(identifier, state);

        return this;
    }

    public StateMachineBuilder addTransition(String parent, String child, TransitionActivator activator) {
        IState parentState = this.states.get(parent);
        IState childState = this.states.get(child);
        Transition transition = new Transition(parentState, childState, activator);
        this.stateMachine.addTransition(transition);

        return this;
    }

    public StateMachineBuilder addManualTransition(String parent, String child, String trigger) {
        IState parentState = this.states.get(parent);
        IState childState = this.states.get(child);
        Transition transition = new Transition(parentState, childState, TransitionActivator.NEVER);
        this.stateMachine.addTransition(transition);
        this.stateMachine.addManualTransition(trigger, transition);

        return this;
    }

    public ComplexState build() {
        return this.stateMachine;
    }
}
