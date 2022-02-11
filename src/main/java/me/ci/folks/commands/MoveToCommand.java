package me.ci.folks.commands;

import com.mojang.brigadier.context.CommandContext;

import me.ci.folks.Folks;
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
    public static int execute(CommandContext<CommandSource> context) {

        try {
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

            NPCEntity npc = (NPCEntity) target;
            npc.walkTo(pos.getX(), pos.getY(), pos.getZ());

            sender.sendMessage(new StringTextComponent("Destination set.")
                    .withStyle(TextFormatting.GRAY), Util.NIL_UUID);

            return 1;
        } catch (Exception e) {
            Folks.LOGGER.error("Failed to execute command!", e);
            return 0;
        }
    }
}
