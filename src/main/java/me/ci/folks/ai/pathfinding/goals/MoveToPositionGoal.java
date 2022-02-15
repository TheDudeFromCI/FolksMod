package me.ci.folks.ai.pathfinding.goals;

import me.ci.folks.Folks;
import me.ci.folks.ai.pathfinding.IPathfindingGoal;
import me.ci.folks.ai.pathfinding.Node;
import net.minecraft.dispenser.IPosition;
import net.minecraft.util.math.BlockPos;

public class MoveToPositionGoal implements IPathfindingGoal {
    private final BlockPos position;
    private final float radius;

    public MoveToPositionGoal(BlockPos position) {
        this(position, 0.5f);
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
        return node.getPosition().distSqr(this.position.getX(), this.position.getY(), this.position.getZ(),
            false) <= this.radius * this.radius;
    }

}
