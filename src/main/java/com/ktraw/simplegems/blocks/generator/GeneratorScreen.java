package com.ktraw.simplegems.blocks.generator;

import com.ktraw.simplegems.util.containers.SimpleGemsContainerScreen;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GeneratorScreen extends SimpleGemsContainerScreen<GeneratorContainerMenu> {
    public static final String textureLocation = "textures/gui/generator_gui.png";

    public static final int FLAME_SIDE = 14;

    public GeneratorScreen(GeneratorContainerMenu screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn, "Generator", textureLocation);
    }

    @Override
    protected void renderBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        Pair<Integer, Integer> shiftedXY = super.renderBaseBg(stack, partialTicks, mouseX, mouseY);

        final int timer = typedContainerMenu.getDataAt(GeneratorBlockEntity.INT_TIMER);
        if (timer > 0) {
            final int scaledProgress = (timer) / (GeneratorBlockEntity.PROCESS_TICKS / FLAME_SIDE);

            blit(stack, shiftedXY.getFirst() + 80, shiftedXY.getSecond() + 50 + scaledProgress, 176, scaledProgress, FLAME_SIDE, FLAME_SIDE);
        }
    }
}
