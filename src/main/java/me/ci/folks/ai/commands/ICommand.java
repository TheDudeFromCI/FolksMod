package me.ci.folks.ai.commands;

import me.ci.folks.npc.NPCEntity;

public interface ICommand {
    String getName();

    void execute(NPCEntity npc, String[] args);
}
