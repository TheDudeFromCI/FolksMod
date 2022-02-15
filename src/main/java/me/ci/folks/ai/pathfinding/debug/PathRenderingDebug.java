package me.ci.folks.ai.pathfinding.debug;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import org.lwjgl.opengl.GL11;

import me.ci.folks.Folks;
import me.ci.folks.ai.pathfinding.Path;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class PathRenderingDebug {
    private static Path pathToRender;

    public static void renderPath(Path path) {
        PathRenderingDebug.pathToRender = path;
        Folks.LOGGER.debug("Updated debug render path. Rendering {} nodes.", path.getSize());
    }

    @SubscribeEvent
    public static void renderPath(RenderWorldLastEvent event) {
        if (PathRenderingDebug.pathToRender == null)
            return;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        MatrixStack matrixStack = event.getMatrixStack();
        Vector3d projectedView = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        matrixStack.pushPose();
        matrixStack.translate(-projectedView.x, -projectedView.y, -projectedView.z);
        RenderSystem.lineWidth(2);

        Matrix4f matrix = matrixStack.last().pose();

        bufferbuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        drawPathLine(matrix, bufferbuilder);
        tessellator.end();

        RenderSystem.lineWidth(1);
        matrixStack.popPose();
    }

    private static void drawPathLine(Matrix4f matrix, BufferBuilder buffer) {
        BlockPos pos = PathRenderingDebug.pathToRender.getElement(0).parent.getPosition();
        buffer.vertex(matrix, pos.getX() + 0.5f, pos.getY() + 0.01f, pos.getZ() + 0.5f)
            .color(0f, 0f, 1f, 1f)
            .endVertex();

        for (int i = 0; i < PathRenderingDebug.pathToRender.getSize(); i++) {
            pos = PathRenderingDebug.pathToRender.getElement(i).child.getPosition();
            buffer.vertex(matrix, pos.getX() + 0.5f, pos.getY() + 0.01f, pos.getZ() + 0.5f)
                .color(0f, 0f, 1f, 1f)
                .endVertex();
        }
    }
}
