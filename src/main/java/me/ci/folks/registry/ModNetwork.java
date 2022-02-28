package me.ci.folks.registry;

import me.ci.folks.Folks;
import me.ci.folks.network.NPCDebugInfoPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

@Mod.EventBusSubscriber(modid = Folks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModNetwork {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(Folks.MOD_ID, "network"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals);

    @SubscribeEvent
    @SuppressWarnings({ "unused" })
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        int packetID = 0;

        INSTANCE.registerMessage(packetID++,
            NPCDebugInfoPacket.class,
            NPCDebugInfoPacket::encode,
            NPCDebugInfoPacket::decode,
            NPCDebugInfoPacket::handle);
    }

}
