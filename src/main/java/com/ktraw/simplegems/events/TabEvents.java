package com.ktraw.simplegems.events;

import com.ktraw.simplegems.registry.Blocks;
import com.ktraw.simplegems.registry.Items;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static com.ktraw.simplegems.SimpleGems.MODID;

public class TabEvents {
    @SubscribeEvent
    public void registerTabs(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MODID, "main_tab"),
                builder ->
                        builder
                                .icon(() -> new ItemStack(Blocks.EMERALD_LAMP.get()))
                                .title(Component.translatable("tabs.simplegems.main_tab"))
                                .displayItems((featureFlags, output, hasOp) -> {
                                    output.acceptAll(Items.getAllRegisteredItems());
                                })
                                .build()
        );
    }
}
