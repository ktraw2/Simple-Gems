package com.ktraw.simplegems.blocks.infuser;

import com.ktraw.simplegems.util.containers.SimpleGemsContainerScreen;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;

public class InfuserScreen extends SimpleGemsContainerScreen<InfuserContainerMenu> {
    public static final String textureLocation = "textures/gui/infuser_gui.png";

    public static final int ARROW_WIDTH = 22;
    public static final int ARROW_HEIGHT = 17;

    public InfuserScreen(
            final InfuserContainerMenu screenContainer,
            final Inventory inv,
            final Component titleIn
    ) {
        super(screenContainer, inv, titleIn, "Infuser", textureLocation);
    }

    @Override
    protected void renderBg(
            @Nonnull final GuiGraphics graphics,
            final float partialTicks,
            final int mouseX,
            final int mouseY
    ) {
        final Pair<Integer, Integer> shiftedXY = super.renderBaseBg(graphics, partialTicks, mouseX, mouseY);

        final int timer = typedContainerMenu.getDataAt(InfuserBlockEntity.INT_TIMER);
        if (timer > 0) {
            final int processTime = typedContainerMenu.getDataAt(InfuserBlockEntity.INT_RECIPE_PROCESS_TIME);
            final int scaledProgress = (timer) / (processTime / ARROW_WIDTH);

            graphics.blit(
                    gui,
                    shiftedXY.getFirst() + 82,
                    shiftedXY.getSecond() + 34,
                    177,
                    0,
                    ARROW_WIDTH - scaledProgress,
                    ARROW_HEIGHT
            );
        }
    }
}
