package com.ktraw.simplegems.setup;

import com.ktraw.simplegems.blocks.generator.GeneratorScreen;
import com.ktraw.simplegems.blocks.infuser.InfuserScreen;
import com.ktraw.simplegems.registry.Menus;
import net.minecraft.client.gui.screens.MenuScreens;

public class ClientProxy implements SidedSetupHandler {
    @Override
    public void init() {
        MenuScreens.register(Menus.GENERATOR.get(), GeneratorScreen::new);
        MenuScreens.register(Menus.INFUSER.get(), InfuserScreen::new);
    }
}
