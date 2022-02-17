package me.ci.folks.commands;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import me.ci.folks.ai.pathfinding.IMovement;
import me.ci.folks.ai.pathfinding.IPathfindingGoal;
import me.ci.folks.ai.pathfinding.Path;
import me.ci.folks.ai.pathfinding.PathfindingTask;
import me.ci.folks.ai.pathfinding.goals.MoveToPositionGoal;
import me.ci.folks.ai.pathfinding.movements.BasicMovement;
import me.ci.folks.npc.NPCEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.command.arguments.ILocationArgument;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class MoveToCommand {
    public static int execute(CommandContext<CommandSource> context) throws CommandSyntaxException {
        EntitySelector targetSelector = context.getArgument("npc", EntitySelector.class);
        Entity sender = context.getSource().getEntity();
        Entity target = targetSelector.findSingleEntity(context.getSource());

        if (!(target instanceof NPCEntity)) {
            sender.sendMessage(new StringTextComponent("Target entity is not an NPC!")
                .withStyle(TextFormatting.RED), Util.NIL_UUID);

            return 0;
        }

        ILocationArgument posArg = context.getArgument("position", ILocationArgument.class);
        BlockPos pos = posArg.getBlockPos(context.getSource());

        List<IMovement> movementTypes = new ArrayList<>();
        movementTypes.add(new BasicMovement(target));

        BlockPos start = new BlockPos(target.position());
        IPathfindingGoal goal = new MoveToPositionGoal(pos);
        PathfindingTask task = new PathfindingTask(start, goal, movementTypes);

        Path path = null;
        while (path == null) {
            task.tick();
            path = task.getPath();
        }

        NPCEntity npc = (NPCEntity) target;
        npc.setCurrentPath(path);

        sender.sendMessage(new StringTextComponent("Destination set.")
            .withStyle(TextFormatting.GRAY), Util.NIL_UUID);

        return 1;
    }
}
