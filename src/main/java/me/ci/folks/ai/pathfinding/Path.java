package me.ci.folks.ai.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

public class Path {

    public enum PathType {
        COMPLETE, NO_PATH, TOO_FAR, PARTIAL
    }

    private final List<NodeEdge> nodes = new ArrayList<>();
    private final PathType type;

    public Path(PathType type, @Nonnull Node leafNode) {
        this.type = type;

        NodeEdge edge = leafNode.getIncomingEdge();
        while (edge != null) {
            this.nodes.add(edge);
            edge = edge.parent.getIncomingEdge();
        }

        Collections.reverse(this.nodes);
    }

    public PathType getPathType() {
        return this.type;
    }

    public int getSize() {
        return this.nodes.size();
    }

    public NodeEdge getElement(int index) {
        return this.nodes.get(index);
    }

    public Node getStart() {
        return this.nodes.get(0).parent;
    }

    public Node getEnd() {
        return this.nodes.get(this.nodes.size() - 1).child;
    }
}
