package com.ktraw.simplegems.setup;

import com.ktraw.simplegems.blocks.generator.GeneratorContainerMenu;
import com.ktraw.simplegems.registry.Blocks;
import com.ktraw.simplegems.blocks.generator.GeneratorScreen;
import com.ktraw.simplegems.blocks.infuser.InfuserScreen;
import com.ktraw.simplegems.registry.Menus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ClientProxy implements IProxy {
    @Override
    public void init() {
        MenuScreens.register(Menus.GENERATOR.get(), GeneratorScreen::new);
        MenuScreens.register(Menus.INFUSER.get(), InfuserScreen::new);
    }
}
