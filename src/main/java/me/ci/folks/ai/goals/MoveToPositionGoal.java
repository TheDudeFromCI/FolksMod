package me.ci.folks.ai.goals;

import java.util.EnumSet;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;

public class MoveToPositionGoal extends Goal {

    protected final CreatureEntity mob;
    protected double speedModifier = 1.0;
    protected boolean hasTarget = false;
    protected boolean isRunning = false;
    protected double targetX;
    protected double targetY;
    protected double targetZ;

    public MoveToPositionGoal(CreatureEntity mob) {
        this.mob = mob;
        setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.isVehicle())
            return false;

        return this.hasTarget;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone() && !this.mob.isVehicle();
    }

    @Override
    public void start() {
        super.start();
        this.isRunning = true;
        recalculatePath();
    }

    @Override
    public void stop() {
        if (this.mob.getNavigation().isDone())
            this.hasTarget = false;

        this.mob.getNavigation().stop();
        this.isRunning = false;
        super.stop();
    }

    public void setTarget(double x, double y, double z) {
        this.targetX = x;
        this.targetY = y;
        this.targetZ = z;
        this.hasTarget = true;
        recalculatePath();
    }

    public void setSpeedModifier(double speedModifier) {
        this.speedModifier = speedModifier;
        recalculatePath();
    }

    private void recalculatePath() {
        if (this.isRunning && this.hasTarget) {
            this.mob.getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, this.speedModifier);
        }
    }
}
