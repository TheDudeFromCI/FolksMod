package me.ci.folks.ai.pathfinding;

import javax.annotation.Nullable;

import me.ci.folks.npc.NPCEntity;
import net.minecraft.util.math.vector.Vector3d;

public class EntityPathHandler {

    private final NPCEntity npc;

    @Nullable
    private Path path;
    private int nodeIndex;

    public EntityPathHandler(NPCEntity npc) {
        this.npc = npc;
    }

    public void setPath(@Nullable Path path) {
        if (path == null || path.getSize() == 0)
            path = null;

        if (this.path == path)
            return;

        this.path = path;
        this.nodeIndex = 0;
        this.npc.sendDebugPacket();
    }

    @Nullable
    public Path getPath() {
        return this.path;
    }

    public void tick() {
        if (this.path == null)
            return;

        Node node = this.path.getElement(this.nodeIndex).child;
        Vector3d target = node.toVector();
        this.npc.getMoveControl().setWantedPosition(target.x, target.y, target.z, 1);

        if (isAtNode(node)) {
            this.nodeIndex++;
            if (this.nodeIndex == this.path.getSize())
                this.path = null;
        }
    }

    public boolean isDone() {
        return this.path == null;
    }

    private boolean isAtNode(Node node) {
        return this.npc.position().distanceToSqr(node.toVector()) < 0.1;
    }
}
