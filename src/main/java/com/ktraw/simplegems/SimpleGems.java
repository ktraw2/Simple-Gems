package com.ktraw.simplegems;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ktraw.simplegems.events.PlayerEvents;
import com.ktraw.simplegems.registry.BlockEntities;
import com.ktraw.simplegems.registry.Blocks;
import com.ktraw.simplegems.registry.CreativeModeTabs;
import com.ktraw.simplegems.registry.Items;
import com.ktraw.simplegems.registry.LootFunctions;
import com.ktraw.simplegems.registry.Menus;
import com.ktraw.simplegems.registry.RecipeSerializers;
import com.ktraw.simplegems.registry.RecipeTypes;
import com.ktraw.simplegems.setup.ClientProxy;
import com.ktraw.simplegems.setup.ModSetup;
import com.ktraw.simplegems.setup.ServerProxy;
import com.ktraw.simplegems.setup.SidedSetupHandler;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("simplegems")
public class SimpleGems
{
    public static final String MODID = "simplegems";

    public static SidedSetupHandler proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);

    public static ModSetup setup = ModSetup.getSetup();

    public static final int POTION_TICKS = 20;

    public static final Multimap<Attribute, AttributeModifier> heavyRingAttributes = HashMultimap.create();
    private static final UUID MAX_HEALTH_UUID = UUID.fromString("5aabe6ee-9d50-408d-a46d-e74375bbf931");
    static {
        heavyRingAttributes.put(Attributes.MAX_HEALTH, new AttributeModifier(MAX_HEALTH_UUID, "Ring modifier", 10.0, AttributeModifier.Operation.ADDITION));
    }

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public SimpleGems() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the setup method for modloading
        eventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(new PlayerEvents());

        Blocks.register(eventBus);
        BlockEntities.register(eventBus);
        Menus.register(eventBus);
        RecipeTypes.register(eventBus);
        RecipeSerializers.register(eventBus);
        Items.register(eventBus);
        LootFunctions.register(eventBus);
        CreativeModeTabs.register(eventBus);
    }

    private void setup(final FMLCommonSetupEvent event) {
        setup.init();
        proxy.init();
    }
}