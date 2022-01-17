package com.ktraw.simplegems.setup;

import com.ktraw.simplegems.blocks.ModBlocks;
import com.ktraw.simplegems.blocks.generator.GeneratorScreen;
import com.ktraw.simplegems.blocks.infuser.InfuserScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public class ClientProxy implements IProxy {
    @Override
    public void init() {
        MenuScreens.register(ModBlocks.GENERATOR_CONTAINER, GeneratorScreen::new);
        MenuScreens.register(ModBlocks.INFUSER_CONTAINER, InfuserScreen::new);
    }

//    @Override
//    public World getClientWorld() {
//        return Minecraft.getInstance().world;
//    }
//
//    @Override
//    public PlayerEntity getClientPlayer() {
//        return Minecraft.getInstance().player;
//    }
}
