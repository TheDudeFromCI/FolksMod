package me.ci.folks.registry;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import me.ci.folks.Folks;
import me.ci.folks.commands.FolksCommand;
import me.ci.folks.commands.MoveToCommand;
import me.ci.folks.commands.ShowPathCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ModCommands {

    public static final LiteralArgumentBuilder<CommandSource> FOLKS = Commands.literal("folks")
        .requires(source -> source.hasPermission(1))

        // Usage: /folks moveTo <npc> <position>
        .then(Commands.literal("moveTo")
            .then(Commands.argument("npc", EntityArgument.entity())
                .then(Commands.argument("position", BlockPosArgument.blockPos())
                    .executes(wrapCommand(MoveToCommand::execute)))))

        // Usage: /folks showPath <position>
        .then(Commands.literal("showPath")
            .then(Commands.argument("position", BlockPosArgument.blockPos())
                .executes(wrapCommand(ShowPathCommand::execute))))

        // Usage: /folks
        .executes(FolksCommand::execute);

    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        event.getDispatcher().register(FOLKS);
    }

    private static Command<CommandSource> wrapCommand(Command<CommandSource> command) {
        return context -> {
            try {
                return command.run(context);
            } catch (Exception e) {
                context.getSource().getEntity().sendMessage(new StringTextComponent(
                    "An internal error has occured while executing this command! Please see console for more information.")
                        .withStyle(TextFormatting.RED),
                    Util.NIL_UUID);

                Folks.LOGGER.error("Failed to execute command!", e);
                return 0;
            }
        };
    }

    private ModCommands() {
        // Hide constructor
    }
}
