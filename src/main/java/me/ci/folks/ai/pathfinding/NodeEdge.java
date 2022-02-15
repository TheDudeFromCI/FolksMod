package me.ci.folks.ai.pathfinding;

public class NodeEdge {
    public IMovement movement;
    public Node parent;
    public Node child;
    public double distance;

    public NodeEdge(IMovement movement, Node parent, Node child, double distance) {
        this.movement = movement;
        this.parent = parent;
        this.child = child;
        this.distance = distance;
    }
}
