package me.ci.folks.commands;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.context.CommandContext;

import me.ci.folks.ai.pathfinding.IMovement;
import me.ci.folks.ai.pathfinding.IPathfindingGoal;
import me.ci.folks.ai.pathfinding.Path;
import me.ci.folks.ai.pathfinding.PathfindingTask;
import me.ci.folks.ai.pathfinding.debug.PathRenderingDebug;
import me.ci.folks.ai.pathfinding.goals.MoveToPositionGoal;
import me.ci.folks.ai.pathfinding.movements.BasicMovement;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.ILocationArgument;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class ShowPathCommand {
    public static int execute(CommandContext<CommandSource> context) {

        BlockPos start = new BlockPos(context.getSource().getPosition());

        ILocationArgument posArg = context.getArgument("position", ILocationArgument.class);
        BlockPos targetPos = posArg.getBlockPos(context.getSource());
        IPathfindingGoal goal = new MoveToPositionGoal(targetPos);

        List<IMovement> movementTypes = new ArrayList<>();
        movementTypes.add(new BasicMovement(context.getSource().getLevel()));

        PathfindingTask task = new PathfindingTask(start, goal, movementTypes);

        long startTime = System.currentTimeMillis();

        Path path = null;
        while (path == null) {
            task.tick();
            path = task.getPath();
        }

        long ms = System.currentTimeMillis() - startTime;

        PathRenderingDebug.renderPath(path);

        context.getSource().getEntity()
            .sendMessage(
                new StringTextComponent("Found path with " + path.getSize() + " nodes in " + ms + "ms. Path type: "
                    + path.getPathType().toString().toLowerCase())
                        .withStyle(TextFormatting.GREEN),
                Util.NIL_UUID);

        return 1;
    }
}
