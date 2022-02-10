package me.ci.folks.client.renderers.models;

import me.ci.folks.npc.NPCEntity;
import net.minecraft.client.renderer.entity.model.PlayerModel;

public class NPCModel extends PlayerModel<NPCEntity> {

    public NPCModel(boolean slim) {
        super(0f, slim);
    }
}
