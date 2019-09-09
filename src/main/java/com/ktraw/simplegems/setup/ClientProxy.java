package com.ktraw.simplegems.setup;

import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.blocks.generator.GeneratorScreen;
import com.ktraw.simplegems.blocks.infuser.InfuserContainer;
import com.ktraw.simplegems.blocks.infuser.InfuserScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {
    @Override
    public void init() {
        ScreenManager.registerFactory(ModBlocks.GENERATOR_CONTAINER, GeneratorScreen::new);
        ScreenManager.registerFactory(ModBlocks.INFUSER_CONTAINER, InfuserScreen::new);
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @Override
    public PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
