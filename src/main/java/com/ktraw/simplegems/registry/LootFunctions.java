package com.ktraw.simplegems.registry;

import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.functions.CopyNbt;
import com.ktraw.simplegems.functions.Lore;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class LootFunctions {
    private static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, SimpleGems.MODID);

    public static final RegistryObject<LootItemFunctionType> LORE = LOOT_FUNCTIONS.register("lore", () -> new LootItemFunctionType(new Lore.Serializer()));
    public static final RegistryObject<LootItemFunctionType> COPY_NBT = LOOT_FUNCTIONS.register("copy_nbt", () -> new LootItemFunctionType(new CopyNbt.Serializer()));

    public static void register(final IEventBus eventBus) {
        LOOT_FUNCTIONS.register(eventBus);
    }
}
