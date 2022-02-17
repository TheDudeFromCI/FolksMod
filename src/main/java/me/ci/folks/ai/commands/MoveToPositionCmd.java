package me.ci.folks.ai.commands;

import me.ci.folks.ai.pathfinding.IPathfindingGoal;
import me.ci.folks.ai.pathfinding.goals.MoveToPositionGoal;
import me.ci.folks.npc.NPCEntity;
import net.minecraft.util.math.BlockPos;

public class MoveToPositionCmd implements ICommand {

    @Override
    public String getName() {
        return "move_to";
    }

    @Override
    public void execute(NPCEntity npc, String[] args) {
        BlockPos pos = new BlockPos(
            Integer.valueOf(args[1]),
            Integer.valueOf(args[2]),
            Integer.valueOf(args[3]));

        IPathfindingGoal goal = new MoveToPositionGoal(pos);
        npc.getController().setNavigationGoal(goal);
    }

}
