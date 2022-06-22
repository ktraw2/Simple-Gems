package com.ktraw.simplegems.registry;

import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.blocks.generator.GeneratorContainerMenu;
import com.ktraw.simplegems.blocks.infuser.InfuserContainerMenu;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class Menus {
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registry.MENU_REGISTRY, SimpleGems.MODID);

    public static RegistryObject<MenuType<GeneratorContainerMenu>> GENERATOR = MENUS.register("generator", () -> IForgeMenuType.create((windowId, inv, data) -> new GeneratorContainerMenu(windowId, inv)));
    public static RegistryObject<MenuType<InfuserContainerMenu>> INFUSER = MENUS.register("infuser", () -> IForgeMenuType.create(((windowId, inv, data) -> new InfuserContainerMenu(windowId, inv))));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }

}
