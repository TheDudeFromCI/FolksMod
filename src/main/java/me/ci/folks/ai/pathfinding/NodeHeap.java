package me.ci.folks.ai.pathfinding;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import javax.annotation.Nullable;

public class NodeHeap {
    private final PriorityQueue<Node> heap = new PriorityQueue<>();
    private final Set<Node> openSet = new HashSet<>();

    public void add(Node node) {
        if (this.openSet.contains(node))
            return;

        this.heap.add(node);
        this.openSet.add(node);
    }

    @Nullable
    public Node get() {
        Node node = this.heap.poll();

        if (node != null)
            this.openSet.remove(node);

        return node;
    }

    public boolean isEmpty() {
        return this.openSet.isEmpty();
    }

    public Node getOrDefault(Node def) {
        if (this.openSet.contains(def)) {
            for (Node n : this.openSet) {
                if (n.equals(def))
                    return n;
            }
        }

        return def;
    }
}
