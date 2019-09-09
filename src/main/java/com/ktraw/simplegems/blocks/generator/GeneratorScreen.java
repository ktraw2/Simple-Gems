package com.ktraw.simplegems.blocks.generator;

import com.ktraw.simplegems.SimpleGems;
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

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        font.drawString("Generator", 8f, 6f, 4210752);
        drawString(Minecraft.getInstance().fontRenderer, "Energy: " + container.getEnergy() + " FE", 8, 70, 0x00FF00);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        minecraft.getTextureManager().bindTexture(GUI);
        final int shiftedX = (width - xSize) / 2;
        final int shiftedY = (height - ySize) / 2;
        blit(shiftedX, shiftedY, 0, 0, xSize, ySize);

        final int timer = typedContainer.getDataAt(GeneratorTile.INT_TIMER);
        if (timer > 0) {
            final int scaledProgress = (timer) / (GeneratorTile.PROCESS_TICKS / FLAME_SIDE);

            // blit(xCoord, yCoord, copyFromX, copyFromY, copyFromWidth, copyFromHeight)
            blit(shiftedX + 80, shiftedY + 50 + scaledProgress, 176, scaledProgress, FLAME_SIDE, FLAME_SIDE);
        }
    }
}
