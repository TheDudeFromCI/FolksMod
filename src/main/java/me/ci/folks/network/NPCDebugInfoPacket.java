package me.ci.folks.network;

import java.util.UUID;
import java.util.function.Supplier;

import me.ci.folks.npc.NPCEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class NPCDebugInfoPacket {

    public static void encode(NPCDebugInfoPacket instance, PacketBuffer buffer) {
        buffer.writeUUID(instance.npcUuid);
        buffer.writeUtf(instance.npcName);
        buffer.writeUtf(instance.state);

        buffer.writeInt(instance.path.length);
        for (int i = 0; i < instance.path.length; i++)
            buffer.writeFloat(instance.path[i]);
    }

    public static NPCDebugInfoPacket decode(PacketBuffer buffer) {
        UUID npcUuid = buffer.readUUID();
        String npcName = buffer.readUtf();
        String state = buffer.readUtf();

        float[] path = new float[buffer.readInt()];
        for (int i = 0; i < path.length; i++)
            path[i] = buffer.readFloat();

        return new NPCDebugInfoPacket(npcUuid, npcName, state, path);
    }

    public static void handle(NPCDebugInfoPacket instance, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(instance)));
        ctx.get().setPacketHandled(true);
    }

    @SuppressWarnings({ "resource" })
    private static void handleClient(NPCDebugInfoPacket instance) {

        for (Entity entity : Minecraft.getInstance().level.entitiesForRendering()) {
            if (entity.getUUID().equals(instance.npcUuid)) {
                NPCEntity npc = (NPCEntity) entity;
                npc.setNPCName(instance.npcName);
                npc.getDebugInfo().setState(instance.state);
                npc.getDebugInfo().setPath(instance.path);
                return;
            }
        }
    }

    private final UUID npcUuid;
    private final String npcName;
    private final String state;
    private final float[] path;

    public NPCDebugInfoPacket(UUID npcUuid, String npcName, String state, float[] path) {
        this.npcUuid = npcUuid;
        this.npcName = npcName;
        this.state = state;
        this.path = path;
    }

}
