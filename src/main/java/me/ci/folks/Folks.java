package me.ci.folks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Folks.MOD_ID)
public class Folks {

    public static final String MOD_ID = "folks";
    public static final Logger LOGGER = LogManager.getLogger();

    public Folks() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::onCommonSetup);
        bus.addListener(this::onClientSetup);
        bus.addListener(this::onServerSetup);

        this.onRegister();
    }

    private void onRegister() {
        Folks.LOGGER.info("Initializing Folks mod.");
        Folks.LOGGER.info("Finished initializing.");
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        // Setup on both client and server
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        // Setup on client
    }

    private void onServerSetup(FMLDedicatedServerSetupEvent event) {
        // Setup on dedicated physical server
    }
}
