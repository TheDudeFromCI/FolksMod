package me.ci.folks.ai.pathfinding.movements;

import java.util.Collection;

import me.ci.folks.ai.pathfinding.IMovement;
import me.ci.folks.ai.pathfinding.Node;
import me.ci.folks.ai.pathfinding.NodeEdge;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

public class BasicMovement implements IMovement {

    private static final int MAX_FALL_DISTANCE = 3;
    private final World world;

    public BasicMovement(World world) {
        this.world = world;
    }

    @Override
    public void getNeighbors(Collection<NodeEdge> neighbors, Node currentNode) {

        BlockPos pos = currentNode.getPosition();
        tryAddPosition(neighbors, currentNode, pos.relative(Direction.NORTH, 1), 1);
        tryAddPosition(neighbors, currentNode, pos.relative(Direction.EAST, 1), 1);
        tryAddPosition(neighbors, currentNode, pos.relative(Direction.SOUTH, 1), 1);
        tryAddPosition(neighbors, currentNode, pos.relative(Direction.WEST, 1), 1);

        pos = pos.relative(Direction.UP, 1);
        tryAddPosition(neighbors, currentNode, pos.relative(Direction.NORTH, 1), 1.414);
        tryAddPosition(neighbors, currentNode, pos.relative(Direction.EAST, 1), 1.414);
        tryAddPosition(neighbors, currentNode, pos.relative(Direction.SOUTH, 1), 1.414);
        tryAddPosition(neighbors, currentNode, pos.relative(Direction.WEST, 1), 1.414);

        for (int i = 1; i <= MAX_FALL_DISTANCE; i++) {
            pos = currentNode.getPosition().relative(Direction.DOWN, i);
            tryAddPosition(neighbors, currentNode, pos.relative(Direction.NORTH, 1), 0.414 + i);
            tryAddPosition(neighbors, currentNode, pos.relative(Direction.EAST, 1), 0.414 + i);
            tryAddPosition(neighbors, currentNode, pos.relative(Direction.SOUTH, 1), 0.414 + i);
            tryAddPosition(neighbors, currentNode, pos.relative(Direction.WEST, 1), 0.414 + i);
        }
    }

    private void tryAddPosition(Collection<NodeEdge> neighbors, Node currentNode, BlockPos pos, double distance) {
        if (!canStandAt(pos))
            return;

        Node node = new Node(pos);
        NodeEdge edge = new NodeEdge(this, currentNode, node, distance);
        neighbors.add(edge);
    }

    private boolean canStandAt(BlockPos pos) {
        return canStandOnBlock(pos.relative(Direction.DOWN, 1))
            && canStandInBlock(pos)
            && canStandInBlock(pos.relative(Direction.UP, 1));
    }

    private boolean canStandOnBlock(BlockPos pos) {
        VoxelShape col = this.world.getBlockState(pos).getCollisionShape(this.world, pos);
        double maxY = col.max(Axis.Y);
        return maxY >= 0.9 && maxY <= 1.1;
    }

    private boolean canStandInBlock(BlockPos pos) {
        VoxelShape col = this.world.getBlockState(pos).getCollisionShape(this.world, pos);
        return col.isEmpty();
    }

}
