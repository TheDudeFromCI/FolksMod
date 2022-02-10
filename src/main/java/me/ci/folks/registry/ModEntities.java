package me.ci.folks.registry;

import me.ci.folks.Folks;
import me.ci.folks.client.renderers.NPCRenderer;
import me.ci.folks.npc.NPCEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Mod.EventBusSubscriber(modid = Folks.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final EntityType<NPCEntity> NPC = (EntityType) EntityType.Builder
            .of((type, world) -> new NPCEntity(world, "Unnamed"), EntityClassification.CREATURE)
            .sized(0.6F, 1.8F)
            .build("npc")
            .setRegistryName(new ResourceLocation(Folks.MOD_ID, "npc"));

    public static void register(Register<EntityType<?>> event) {
        event.getRegistry().register(NPC);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenderers() {
        RenderingRegistry.registerEntityRenderingHandler(NPC, NPCRenderer::new);
    }

    @SubscribeEvent
    public static void initializeAttributes(EntityAttributeCreationEvent event) {
        event.put(NPC, NPCEntity.createNPCAttributes().build());
    }

    private ModEntities() {
        // Hide constructor
    }
}
