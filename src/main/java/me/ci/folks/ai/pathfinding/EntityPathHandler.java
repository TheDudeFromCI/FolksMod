package me.ci.folks.ai.pathfinding;

import javax.annotation.Nullable;

import net.minecraft.entity.MobEntity;
import net.minecraft.util.math.vector.Vector3d;

public class EntityPathHandler {

    private final MobEntity mob;

    @Nullable
    private Path path;
    private int nodeIndex;

    public EntityPathHandler(MobEntity mob) {
        this.mob = mob;
    }

    public void setPath(Path path) {
        if (path.getSize() == 0)
            path = null;

        this.path = path;
        this.nodeIndex = 0;
    }

    public void tick() {
        if (this.path == null)
            return;

        Node node = this.path.getElement(this.nodeIndex).child;
        Vector3d target = node.toVector();
        this.mob.getMoveControl().setWantedPosition(target.x, target.y, target.z, 1);

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
        return this.mob.position().distanceToSqr(node.toVector()) < 0.1;
    }
}
