package me.ci.folks.registry;

import me.ci.folks.Folks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Folks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModCommon {

    @SubscribeEvent
    public static void onCommonSetupEvent(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(ModCommands.class);
    }

    private ModCommon() {
        // Hide constructor
    }
}
