package me.ci.folks.npc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.ci.folks.ai.commands.ICommand;
import me.ci.folks.ai.pathfinding.EntityPathHandler;
import me.ci.folks.ai.pathfinding.IPathfindingGoal;
import me.ci.folks.ai.statemachine.StateMachine;
import me.ci.folks.ai.statemachine.StateMachineBuilder;
import me.ci.folks.ai.statemachine.TransitionActivator;
import me.ci.folks.ai.states.IdleState;
import me.ci.folks.ai.states.NavigationState;

public class NPCController {
    private final List<ICommand> commands = new ArrayList<>();
    private final List<String[]> commandQueue = new LinkedList<>();
    private final StateMachine stateMachine;
    private final EntityPathHandler pathHandler;
    private final NPCEntity npc;
    private final IdleState idleState;
    private final NavigationState navigationState;

    public NPCController(NPCEntity npc) {
        this.npc = npc;
        this.pathHandler = new EntityPathHandler(npc);

        this.idleState = new IdleState();
        this.navigationState = new NavigationState(this.npc, this.pathHandler);

        this.stateMachine = new StateMachineBuilder("root")
            .addState("idle", idleState)
            .addState("nav", navigationState)
            .addTransition("ENTER", "idle", TransitionActivator.NEXT_AVAILABLE)
            .addTransition("idle", "nav", t -> this.navigationState.hasGoal())
            .addTransition("nav", "idle", TransitionActivator.ON_DONE)
            .build();
    }

    public NPCEntity getNPC() {
        return this.npc;
    }

    public StateMachine getStateMachine() {
        return this.stateMachine;
    }

    public void tick() {
        this.stateMachine.tick();
        this.pathHandler.tick();

        if (this.stateMachine.getActiveState() == this.idleState
            && !this.commandQueue.isEmpty()) {

            String[] args = this.commandQueue.remove(0);
            ICommand cmd = this.commands.stream()
                .filter(c -> c.getName().equals(args[0]))
                .findFirst().orElseThrow(() -> new IllegalStateException(args[0]));

            cmd.execute(this.npc, args);
        }
    }

    public void addCommand(ICommand command) {
        this.commands.add(command);
    }

    public void removeCommand(ICommand command) {
        this.commands.remove(command);
    }

    public void runCommand(String... args) {
        if (args.length == 0)
            throw new IllegalArgumentException("Command not specified!");

        this.commandQueue.add(args);
    }

    public void setNavigationGoal(IPathfindingGoal goal) {
        this.navigationState.setGoal(goal);
    }
}
