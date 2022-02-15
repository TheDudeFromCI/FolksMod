package me.ci.folks.ai.pathfinding.movements;

import java.util.Collection;

import me.ci.folks.ai.pathfinding.IMovement;
import me.ci.folks.ai.pathfinding.Node;
import me.ci.folks.ai.pathfinding.NodeEdge;
import net.minecraft.util.Direction;

public class BasicMovement implements IMovement {

    @Override
    public void getNeighbors(Collection<NodeEdge> neighbors, Node currentNode) {
        neighbors.add(new NodeEdge(this, currentNode, new Node(
            currentNode.getPosition().relative(Direction.NORTH, 1)), 1));

        neighbors.add(new NodeEdge(this, currentNode, new Node(
            currentNode.getPosition().relative(Direction.EAST, 1)), 1));

        neighbors.add(new NodeEdge(this, currentNode, new Node(
            currentNode.getPosition().relative(Direction.SOUTH, 1)), 1));

        neighbors.add(new NodeEdge(this, currentNode, new Node(
            currentNode.getPosition().relative(Direction.WEST, 1)), 1));
    }

}
