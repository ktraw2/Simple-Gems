package com.ktraw.simplegems.blocks.infuser;

import com.ktraw.simplegems.SimpleGems;
import com.mojang.blaze3d.matrix.MatrixStack;
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

        final int timer = typedContainer.getDataAt(InfuserTile.INT_TIMER);
        if (timer > 0) {
            final int processTime = typedContainer.getDataAt(InfuserTile.INT_RECIPE_PROCESS_TIME);
            final int scaledProgress = (timer) / (processTime / ARROW_WIDTH);

            blit(stack, shiftedX + 82, shiftedY + 34, 177, 0, ARROW_WIDTH - scaledProgress, ARROW_HEIGHT);
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
        font.drawString(stack,"Infuser", 8f, 6f, 4210752);
        drawString(stack, Minecraft.getInstance().fontRenderer,"Energy: " + container.getEnergy() + " FE", 8, 70, 0x00FF00);
    }
}
