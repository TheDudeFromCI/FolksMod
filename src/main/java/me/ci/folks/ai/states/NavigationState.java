package me.ci.folks.ai.states;

import me.ci.folks.ai.pathfinding.AsyncPathfinder;
import me.ci.folks.ai.pathfinding.EntityPathHandler;
import me.ci.folks.ai.pathfinding.IPathfindingGoal;

public class NavigationState extends IdleState {

    private final EntityPathHandler pathHandler;
    private final AsyncPathfinder pathfinder;
    private IPathfindingGoal goal;

    public NavigationState(EntityPathHandler pathHandler, AsyncPathfinder pathfinder) {
        this("navigation", pathHandler, pathfinder);
    }

    public NavigationState(String name, EntityPathHandler pathHandler, AsyncPathfinder pathfinder) {
        super(name);

        this.pathHandler = pathHandler;
        this.pathfinder = pathfinder;
    }

    @Override
    public void onStateEnter() {
        this.pathfinder.setGoal(this.goal);
    }

    @Override
    public void onStateExit() {
        this.goal = null;
        this.pathfinder.setGoal(null);
    }

    @Override
    public void onInterrupt() {
        onStateExit();
    }

    @Override
    public boolean isDone() {
        return this.pathfinder.isDone() && this.pathHandler.isDone();
    }

    public void setGoal(IPathfindingGoal goal) {
        this.goal = goal;
        this.pathfinder.setGoal(goal);
    }

    public boolean hasGoal() {
        return this.goal != null;
    }

}
