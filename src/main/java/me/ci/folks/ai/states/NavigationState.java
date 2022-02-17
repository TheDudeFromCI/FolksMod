package me.ci.folks.ai.states;

import java.util.ArrayList;
import java.util.List;

import me.ci.folks.ai.pathfinding.EntityPathHandler;
import me.ci.folks.ai.pathfinding.IMovement;
import me.ci.folks.ai.pathfinding.IPathfindingGoal;
import me.ci.folks.ai.pathfinding.Path;
import me.ci.folks.ai.pathfinding.PathfindingTask;
import me.ci.folks.ai.pathfinding.movements.BasicMovement;
import me.ci.folks.ai.statemachine.IState;
import me.ci.folks.npc.NPCEntity;
import net.minecraft.util.math.BlockPos;

public class NavigationState implements IState {

    private static final int PATHFINDING_NODES_PER_TICK = 64;

    private final EntityPathHandler pathHandler;
    private final NPCEntity npc;
    private final List<IMovement> movementTypes = new ArrayList<>();
    private PathfindingTask pathfinder;
    private IPathfindingGoal goal;
    private boolean active;

    public NavigationState(NPCEntity npc, EntityPathHandler pathHandler) {
        this.npc = npc;
        this.pathHandler = pathHandler;
        addMovementType(new BasicMovement(npc));
    }

    public void addMovementType(IMovement movement) {
        this.movementTypes.add(movement);
        resetPath();
    }

    public void removeMovementType(IMovement movement) {
        this.movementTypes.remove(movement);
        resetPath();
    }

    @Override
    public void onStateEnter() {
        resetPath();
        onStateTick();
    }

    @Override
    public void onStateExit() {
        this.pathHandler.setPath(null);
        this.pathfinder = null;
        this.goal = null;
    }

    @Override
    public void onInterrupt() {
        onStateExit();
    }

    @Override
    public void onStateTick() {
        if (this.goal.hasGoalMoved())
            resetPath();

        if (this.pathfinder == null)
            return;

        Path path = null;
        for (int i = 0; i < PATHFINDING_NODES_PER_TICK && path == null; i++) {
            this.pathfinder.tick();
            path = this.pathfinder.getPath();
        }

        if (path == null) {
            Path currentPath = this.pathHandler.getPath();
            if (currentPath != null && currentPath.getEnd().equals(this.pathfinder.getBestNode()))
                return; // Keep on current partial path
            else {
                path = this.pathfinder.getPartialPath();
                resetPath();
            }
        } else {
            this.pathfinder = null;
        }

        this.pathHandler.setPath(path);
    }

    @Override
    public boolean isDone() {
        return this.pathHandler.getPath() == null;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    public void setGoal(IPathfindingGoal goal) {
        this.goal = goal;
        resetPath();
    }

    private void resetPath() {
        if (this.goal == null) {
            this.pathfinder = null;
            return;
        }

        BlockPos start = new BlockPos(this.npc.position());
        this.pathfinder = new PathfindingTask(start, goal, this.movementTypes);
    }

    public boolean hasGoal() {
        return this.goal != null;
    }

}
