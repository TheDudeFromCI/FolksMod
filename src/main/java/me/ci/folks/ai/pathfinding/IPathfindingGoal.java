package me.ci.folks.ai.pathfinding;

public interface IPathfindingGoal {
    double getHeuristic(Node node);

    boolean isGoal(Node node);
}
