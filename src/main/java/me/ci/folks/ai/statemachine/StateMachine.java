package me.ci.folks.ai.statemachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import me.ci.folks.ai.states.IdleState;

public class StateMachine {
    protected final List<IState> states = new ArrayList<>();
    protected final List<Transition> transitions = new ArrayList<>();
    protected final IState entryState = new IdleState();

    public StateMachine() {
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

        if (state.isEmpty())
            throw new IllegalStateException("No active states!");

        return state.get();
    }
}
