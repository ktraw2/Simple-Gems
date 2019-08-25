package com.ktraw.mymod.setup;

import com.ktraw.mymod.blocks.ModBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModSetup {
    private static ModSetup setup = new ModSetup();
    private ItemGroup creativeTab;

    private ModSetup() {
        creativeTab = new ItemGroup("mymod") {
            @Override
            public ItemStack createIcon() {
                return new ItemStack(ModBlocks.EMERALDLAMP);
            }
        };
    }

    public static ModSetup getSetup() {
        return setup;
    }

    public ItemGroup getCreativeTab() {
        return creativeTab;
    }

    public void init() {

    }
}
