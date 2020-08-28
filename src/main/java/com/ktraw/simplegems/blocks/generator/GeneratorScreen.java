package com.ktraw.simplegems.blocks.generator;

import com.ktraw.simplegems.SimpleGems;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GeneratorScreen extends ContainerScreen<GeneratorContainer> {

    private static final int FLAME_SIDE = 13;

    private ResourceLocation GUI = new ResourceLocation(SimpleGems.MODID, "textures/gui/generator_gui.png");
    private GeneratorContainer typedContainer;

    public GeneratorScreen(GeneratorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        typedContainer = screenContainer;
    }

    /**
     * Simply calls drawGuiContainerBackgroundLayer
     * @param stack
     * @param partialTicks
     * @param mouseX
     * @param mouseY
     */
    @Override
    protected void func_230450_a_(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        drawGuiContainerBackgroundLayer(stack, partialTicks, mouseX, mouseY);
    }

    protected void drawGuiContainerBackgroundLayer(MatrixStack stack, float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        minecraft.getTextureManager().bindTexture(GUI);
        final int shiftedX = (width - xSize) / 2;
        final int shiftedY = (height - ySize) / 2;
        blit(stack, shiftedX, shiftedY, 0, 0, xSize, ySize);

        final int timer = typedContainer.getDataAt(GeneratorTile.INT_TIMER);
        if (timer > 0) {
            final int scaledProgress = (timer) / (GeneratorTile.PROCESS_TICKS / FLAME_SIDE);

            // blit(matrixStack, xCoord, yCoord, copyFromX, copyFromY, copyFromWidth, copyFromHeight);
            blit(stack, shiftedX + 80, shiftedY + 50 + scaledProgress, 176, scaledProgress, FLAME_SIDE, FLAME_SIDE);
        }
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderHoveredToolTip(stack, mouseX, mouseY);
    }

    protected void renderHoveredToolTip(MatrixStack stack, int mouseX, int mouseY) {
        func_230459_a_(stack, mouseX, mouseY);
    }

    @Override
    protected void func_230451_b_(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        drawGuiContainerForegroundLayer(p_230451_1_, p_230451_2_, p_230451_3_);
    }

    protected void drawGuiContainerForegroundLayer(MatrixStack stack, int mouseX, int mouseY) {
        font.drawString(stack, "Generator", 8f, 6f, 4210752);
        drawString(stack, Minecraft.getInstance().fontRenderer, "Energy: " + container.getEnergy() + " FE", 8, 70, 0x00FF00);
    }
}
