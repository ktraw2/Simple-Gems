package com.ktraw.mymod.blocks.generator;

import com.ktraw.mymod.MyMod;
import com.ktraw.mymod.blocks.generator.GeneratorContainer;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class GeneratorScreen extends ContainerScreen<GeneratorContainer> {

    private static final int FLAME_SIDE = 13;

    private ResourceLocation GUI = new ResourceLocation(MyMod.MODID, "textures/gui/generator_gui.png");

    public GeneratorScreen(GeneratorContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
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

        final TileEntity tileEntity = container.getTileEntity();
        if (tileEntity instanceof GeneratorTile) {
            final GeneratorTile casted = (GeneratorTile) tileEntity;
            if (casted.isProcessing()) {
                final int timer = casted.getCounter();
                final int scaledProgress = (timer) / (GeneratorTile.PROCESS_TICKS / FLAME_SIDE);

                blit(shiftedX + 81 - 1, shiftedY + 51 + scaledProgress - 1, 176, scaledProgress, FLAME_SIDE, FLAME_SIDE);
            }
        }

    }
}
