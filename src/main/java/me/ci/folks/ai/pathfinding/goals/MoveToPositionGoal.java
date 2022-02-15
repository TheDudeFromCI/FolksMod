package me.ci.folks.ai.pathfinding.goals;

import me.ci.folks.ai.pathfinding.IPathfindingGoal;
import me.ci.folks.ai.pathfinding.Node;
import net.minecraft.util.math.BlockPos;

public class MoveToPositionGoal implements IPathfindingGoal {
    private final BlockPos position;
    private final float radius;

    public MoveToPositionGoal(BlockPos position) {
        this(position, 0f);
    }

    public MoveToPositionGoal(BlockPos position, float radius) {
        this.position = position;
        this.radius = radius;
    }

    @Override
    public double getHeuristic(Node node) {
        return Math.sqrt(node.getPosition().distSqr(this.position));
    }

    @Override
    public boolean isGoal(Node node) {
        return node.getPosition().distSqr(this.position) <= this.radius * this.radius;
    }

}
