package me.ci.folks.ai.pathfinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Path {

    public enum PathType {
        COMPLETE, NO_PATH, TOO_FAR
    }

    private final List<NodeEdge> nodes = new ArrayList<>();
    private final PathType type;

    public Path(PathType type, Node leafNode) {
        this.type = type;

        NodeEdge edge = leafNode.getParent();
        while (edge != null) {
            this.nodes.add(edge);
            edge = edge.parent.getParent();
        }

        Collections.reverse(this.nodes);
    }

    public PathType getPathType() {
        return this.type;
    }

    public int getSize() {
        return nodes.size();
    }

    public NodeEdge getElement(int index) {
        return nodes.get(index);
    }
}
