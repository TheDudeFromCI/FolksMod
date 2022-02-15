package me.ci.folks.registry;

import me.ci.folks.Folks;
import me.ci.folks.ai.pathfinding.debug.PathRenderingDebug;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Folks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModClient {

    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(PathRenderingDebug.class);
    }

    private ModClient() {
    }
}
