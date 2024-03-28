package com.ktraw.simplegems.registry;

import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.blocks.infuser.InfuserRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeSerializers {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, SimpleGems.MODID);
    public static final RegistryObject<RecipeSerializer<InfuserRecipe>> INFUSER_SERIALIZER = RECIPE_SERIALIZERS.register("infuser", InfuserRecipe.Serializer::new);

    public static void register(final IEventBus eventBus) {
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
