package me.ci.folks.events;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import me.ci.folks.npc.NPCEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings({ "resource" })
public final class DebugNPC {

    @SubscribeEvent
    public static void debugNPCText(RenderGameOverlayEvent.Text event) {
        if (!Minecraft.getInstance().options.renderDebug)
            return;

        NPCEntity npc = getTargetNPC();
        if (npc == null)
            return;

        String npcState = npc.getDebugInfo().getState();

        event.getRight().add("");
        event.getRight().add(String.format("NPC: %s", npc.getNPCName()));
        event.getRight().add(String.format("State: %s", npcState));
    }

    @SubscribeEvent
    public static void debugNPCPathfinding(RenderWorldLastEvent event) {
        if (!Minecraft.getInstance().options.renderDebug)
            return;

        NPCEntity npc = getTargetNPC();
        if (npc == null)
            return;

        float[] path = npc.getDebugInfo().getPath();
        if (path.length > 0)
            renderPath(event.getMatrixStack(), path);
    }

    @Nullable
    private static NPCEntity getTargetNPC() {
        Entity entity = Minecraft.getInstance().crosshairPickEntity;
        if (entity instanceof NPCEntity)
            return (NPCEntity) entity;

        return null;
    }

    private static void renderPath(MatrixStack matrixStack, float[] path) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        Vector3d projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        matrixStack.pushPose();
        matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);
        RenderSystem.lineWidth(2);
        RenderSystem.disableDepthTest();

        Matrix4f matrix = matrixStack.last().pose();

        bufferbuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        for (int i = 0; i < path.length; i += 3) {
            bufferbuilder.vertex(matrix, path[i + 0], path[i + 1], path[i + 2])
                .color(0f, 0f, 1f, 1f)
                .endVertex();
        }
        tessellator.end();

        RenderSystem.lineWidth(1);
        RenderSystem.enableDepthTest();
        matrixStack.popPose();
    }

    private DebugNPC() {
    }

}
