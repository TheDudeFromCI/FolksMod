package me.ci.folks.registry;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import me.ci.folks.commands.FolksCommand;
import me.ci.folks.commands.MoveToCommand;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ModCommands {

    public static final LiteralArgumentBuilder<CommandSource> FOLKS = Commands.literal("folks")
            .requires(source -> source.hasPermission(1))

            // Usage: /folks moveTo <npc> <position>
            .then(Commands.literal("moveTo")
                    .then(Commands.argument("npc", EntityArgument.entity())
                            .then(Commands.argument("position", BlockPosArgument.blockPos())
                                    .executes(MoveToCommand::execute))))

            // Usage: /folks
            .executes(FolksCommand::execute);

    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        event.getDispatcher().register(FOLKS);
    }

    private ModCommands() {
        // Hide constructor
    }
}
