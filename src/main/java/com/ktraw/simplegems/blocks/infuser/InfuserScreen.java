package com.ktraw.simplegems.blocks.infuser;

import com.ktraw.simplegems.SimpleGems;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class InfuserScreen extends ContainerScreen<InfuserContainer> {
    private static final int ARROW_WIDTH = 22;
    private static final int ARROW_HEIGHT = 17;

    private ResourceLocation GUI = new ResourceLocation(SimpleGems.MODID, "textures/gui/infuser_gui.png");
    private InfuserContainer typedContainer;

    public InfuserScreen(InfuserContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        typedContainer = screenContainer;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1f, 1f, 1f, 1f);
        minecraft.getTextureManager().bindTexture(GUI);
        final int shiftedX = (width - xSize) / 2;
        final int shiftedY = (height - ySize) / 2;
        blit(shiftedX, shiftedY, 0, 0, xSize, ySize);

        final int timer = typedContainer.getDataAt(InfuserTile.INT_TIMER);
        if (timer > 0) {
            final int processTime = typedContainer.getDataAt(InfuserTile.INT_RECIPE_PROCESS_TIME);
            final int scaledProgress = (timer) / (processTime / ARROW_WIDTH);

            blit(shiftedX + 82, shiftedY + 34, 177, 0, ARROW_WIDTH - scaledProgress, ARROW_HEIGHT);
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        font.drawString("Infuser", 8f, 6f, 4210752);
        drawString(Minecraft.getInstance().fontRenderer,"Energy: " + container.getEnergy() + " FE", 8, 70, 0x00FF00);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX, mouseY);
    }
}
