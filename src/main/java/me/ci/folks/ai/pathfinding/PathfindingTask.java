package me.ci.folks.ai.pathfinding;

import java.util.Collection;
import java.util.LinkedList;

import javax.annotation.Nullable;

import me.ci.folks.ai.pathfinding.Path.PathType;
import net.minecraft.util.math.BlockPos;

public class PathfindingTask {

    private final NodeHeap heap = new NodeHeap();
    private final Collection<IMovement> movementTypes;
    private final IPathfindingGoal goal;
    private final int maxNodes;
    private int nodesChecked = 0;
    private Path path;
    private Node bestNode;

    public PathfindingTask(BlockPos start, IPathfindingGoal goal, Collection<IMovement> movementTypes) {
        this(start, goal, movementTypes, 8192);
    }

    public PathfindingTask(BlockPos start, IPathfindingGoal goal, Collection<IMovement> movementTypes, int maxNodes) {
        this.goal = goal;
        this.movementTypes = movementTypes;
        this.maxNodes = maxNodes;

        Node startNode = new Node(start);
        startNode.setGScore(0);
        startNode.setHeuristic(this.goal.getHeuristic(startNode));
        this.heap.add(startNode);
    }

    public void tick() {
        if (this.path != null)
            return;

        if (heap.isEmpty()) {
            this.path = new Path(PathType.NO_PATH, this.bestNode);
            return;
        }

        Node current = this.heap.get();
        this.nodesChecked++;

        if (this.bestNode == null || current.getHeuristic() < this.bestNode.getHeuristic())
            this.bestNode = current;

        if (this.goal.isGoal(current)) {
            this.path = new Path(PathType.COMPLETE, current);
            return;
        }

        if (this.nodesChecked >= this.maxNodes) {
            this.path = new Path(PathType.TOO_FAR, this.bestNode);
            return;
        }

        Collection<NodeEdge> neighbors = new LinkedList<>();
        for (IMovement movementType : this.movementTypes) {
            movementType.getNeighbors(neighbors, current);
        }

        for (NodeEdge edge : neighbors) {

            edge.child = this.heap.getOrDefault(edge.child);
            double tScore = current.getGScore() + edge.distance;

            if (tScore < edge.child.getGScore()) {
                edge.child.setIncomingEdge(edge);
                edge.child.setGScore(tScore);
                edge.child.setHeuristic(this.goal.getHeuristic(edge.child));
                this.heap.add(edge.child);
            }
        }

    }

    @Nullable
    public Path getPath() {
        return this.path;
    }
}
