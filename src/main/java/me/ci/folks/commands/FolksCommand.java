package me.ci.folks.commands;

import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class FolksCommand {
    public static int execute(CommandContext<CommandSource> context) {

        IFormattableTextComponent text = new StringTextComponent("Usage:\n").withStyle(TextFormatting.GRAY)
                .append("/folks").withStyle(TextFormatting.GOLD)
                .append(" moveTo").withStyle(TextFormatting.GRAY)
                .append(" <x>").withStyle(TextFormatting.AQUA)
                .append(" <y>").withStyle(TextFormatting.AQUA)
                .append(" <z>").withStyle(TextFormatting.AQUA);

        Entity sender = context.getSource().getEntity();
        sender.sendMessage(text, Util.NIL_UUID);

        return 1;
    }
}
