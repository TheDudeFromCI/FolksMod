package me.ci.folks.ai.pathfinding;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import me.ci.folks.ai.pathfinding.movements.BasicMovement;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

public class AsyncPathfinder {

    public enum State {
        NO_GOAL, FIND_PRE_PATH, FINDING_PATH, WAITING_FOR_GOAL_UPDATE
    }

    private static final int MAX_ITERATIONS_PER_TICK = 64;

    private final List<IMovement> movementTypes = new ArrayList<>();
    private final Entity entity;
    private IPathfindingGoal goal;
    private PathfindingTask task;
    private State state;
    private Path path;

    public AsyncPathfinder(Entity entity) {
        this.entity = entity;
        this.state = State.NO_GOAL;
        addMovementType(new BasicMovement(this.entity));
    }

    public void addMovementType(IMovement movement) {
        this.movementTypes.add(movement);
    }

    public void removeMovementType(IMovement movement) {
        this.movementTypes.remove(movement);
    }

    public void tick() {
        if (this.state == State.FIND_PRE_PATH) {

            updateTask();
            if (this.task.getPath() == null) {
                this.path = this.task.getPartialPath();

                BlockPos taskStart = this.path.getSize() > 0 ? this.path.getEnd().getPosition()
                    : new BlockPos(this.entity.position());

                this.task = new PathfindingTask(taskStart, goal, this.movementTypes);
                this.state = State.FINDING_PATH;
            } else {
                this.path = this.task.getPath();
                this.state = State.WAITING_FOR_GOAL_UPDATE;
            }

        } else if (this.state == State.FINDING_PATH) {

            updateTask();
            if (this.task.getPath() != null) {
                this.path = this.task.getPath();
                this.state = State.WAITING_FOR_GOAL_UPDATE;
            }

        } else if (this.state == State.WAITING_FOR_GOAL_UPDATE
            && this.goal.hasGoalMoved()) {
            setGoal(this.goal);
        }
    }

    private void updateTask() {
        for (int i = 0; i < MAX_ITERATIONS_PER_TICK && this.task.getPath() == null; i++) {
            this.task.tick();
        }
    }

    public void setGoal(@Nullable IPathfindingGoal goal) {
        this.goal = goal;
        this.path = null;

        if (this.goal == null) {
            this.task = null;
            this.state = State.NO_GOAL;
            return;
        }

        BlockPos start = new BlockPos(this.entity.position());
        this.task = new PathfindingTask(start, goal, this.movementTypes);
        this.state = State.FIND_PRE_PATH;
    }

    public IPathfindingGoal getGoal() {
        return this.goal;
    }

    @Nullable
    public Path getPath() {
        return this.path;
    }

    public boolean isDone() {
        return this.state == State.NO_GOAL || this.state == State.WAITING_FOR_GOAL_UPDATE;
    }
}
