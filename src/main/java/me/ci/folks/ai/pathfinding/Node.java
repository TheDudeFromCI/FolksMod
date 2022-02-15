package me.ci.folks.ai.pathfinding;

import javax.annotation.Nullable;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class Node implements Comparable<Node> {

    private final BlockPos position;
    private final Vector3d vector;
    private double gScore = Double.POSITIVE_INFINITY;
    private double fScore = Double.POSITIVE_INFINITY;
    private double heuristic = 0;
    private NodeEdge parent;

    public Node(BlockPos position) {
        this.position = position;
        this.vector = new Vector3d(position.getX() + 0.5,
            position.getY(),
            position.getZ() + 0.5);
    }

    public BlockPos getPosition() {
        return this.position;
    }

    public double getGScore() {
        return this.gScore;
    }

    public double getFScore() {
        return this.fScore;
    }

    public double getHeuristic() {
        return this.heuristic;
    }

    public void setGScore(double gScore) {
        this.gScore = gScore;
    }

    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
        this.fScore = this.gScore + heuristic;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.fScore, other.fScore);
    }

    @Nullable
    public NodeEdge getIncomingEdge() {
        return this.parent;
    }

    public void setIncomingEdge(@Nullable NodeEdge parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node)) {
            return false;
        }

        Node other = (Node) obj;

        return this.position.equals(other.position);
    }

    @Override
    public int hashCode() {
        return this.position.hashCode();
    }

    public Vector3d toVector() {
        return this.vector;
    }
}
