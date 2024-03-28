package com.ktraw.simplegems.registry;

import com.ktraw.simplegems.SimpleGems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CreativeModeTabs {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SimpleGems.MODID);

    public static final RegistryObject<CreativeModeTab> SIMPLE_GEMS = CREATIVE_MODE_TABS.register(
            "main_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(Blocks.EMERALD_LAMP.get()))
                    .title(Component.translatable("tabs.simplegems.main_tab"))
                    .displayItems((parameters, output) -> {
                        output.acceptAll(Items.getAllRegisteredItems());
                    })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
