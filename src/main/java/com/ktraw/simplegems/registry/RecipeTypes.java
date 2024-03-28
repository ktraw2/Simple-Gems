package com.ktraw.simplegems.registry;

import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.blocks.infuser.InfuserRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeTypes {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, SimpleGems.MODID);

    public static final RegistryObject<RecipeType<InfuserRecipe>> INFUSER_RECIPE_TYPE = RECIPE_TYPES.register("infuser", () -> RecipeType.simple(new ResourceLocation(SimpleGems.MODID, "infuser")));

    public static void register(final IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
    }
}
