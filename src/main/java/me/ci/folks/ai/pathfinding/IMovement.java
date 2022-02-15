package me.ci.folks.ai.pathfinding;

import java.util.Collection;

public interface IMovement {
    void getNeighbors(Collection<NodeEdge> neighbors, Node currentNode);
}
