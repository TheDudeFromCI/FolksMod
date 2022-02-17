package me.ci.folks.ai.statemachine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import me.ci.folks.ai.states.IdleState;

public abstract class StateMachine {
    protected final List<IState> states = new ArrayList<>();
    protected final List<Transition> transitions = new ArrayList<>();
    protected final Map<String, Transition> manualTransitions = new HashMap<>();
    protected final IState entryState = new IdleState();

    protected StateMachine() {
        addState(this.entryState);

        getEntryState().setActive(true);
        getEntryState().onStateEnter();
    }

    public IState getEntryState() {
        return this.entryState;
    }

    public void addState(IState state) {
        this.states.add(state);
    }

    public void addTransition(Transition transition) {
        this.transitions.add(transition);
    }

    public void tick() {
        for (IState state : this.states) {
            if (state.isActive())
                state.onStateTick();
        }

        for (Transition transition : this.transitions) {
            if (transition.getParentState().isActive())
                transition.tick();
        }
    }

    public IState getActiveState() {
        Optional<IState> state = this.states.stream()
            .filter(IState::isActive)
            .findFirst();

        if (!state.isPresent())
            throw new IllegalStateException("No active states!");

        return state.get();
    }

    public void triggerTransition(String name) {
        int seperatorIndex = name.indexOf('.');
        if (seperatorIndex > -1) {

            String stateName = name.substring(0, seperatorIndex);
            String triggerName = name.substring(seperatorIndex + 1);

            Optional<ComplexState> state = this.states.stream()
                .filter(ComplexState.class::isInstance)
                .map(ComplexState.class::cast)
                .filter(s -> s.getName().equals(stateName))
                .findFirst();

            if (!state.isPresent())
                throw new IllegalArgumentException("Unknown nested state: " + stateName);

            state.get().triggerTransition(triggerName);
            return;
        }

        if (!this.manualTransitions.containsKey(name))
            throw new IllegalArgumentException("Unknown trigger: " + name);

        this.manualTransitions.get(name).forceTrigger();
    }

    public void addManualTransition(String name, Transition transition) {
        if (!name.matches("[a-z0-9_]+"))
            throw new IllegalArgumentException(
                "Trigger name should only contain lowercase alphanumeric character and underscores!");

        this.manualTransitions.put(name, transition);
    }
}
