package com.ktraw.simplegems.items.rings;

import com.google.common.collect.Multimap;
import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.tools.IEffectProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;

public class GemRing extends Item {
    private static final int ENERGY_PER_TICK = 10;
    private static final Style GREEN_STYLE = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GREEN));
    private static final Style HINT_STYLE = Style.EMPTY.withItalic(true).withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY));

    private static final Component BLANK_LINE = new TextComponent("");
    private static final Component PRESS_CTRL = new TextComponent("Press <Shift>").setStyle(HINT_STYLE);

    private IEffectProvider ringEffect;
    private Component firstLineOfTooltip;
    private Multimap<Attribute, AttributeModifier> attributeModifierMultimap;

    public GemRing(String registryName, @Nullable IEffectProvider ringEffect, @Nullable Multimap<Attribute, AttributeModifier> attributeModifierMultimap, @Nullable Component firstLineOfTooltip) {
        this(registryName, ringEffect);
        this.attributeModifierMultimap = attributeModifierMultimap;
        this.firstLineOfTooltip = firstLineOfTooltip;
    }

    public GemRing(String registryName, @Nullable IEffectProvider ringEffect) {
        super(new Properties()
                .tab(SimpleGems.setup.getCreativeTab())
                .stacksTo(1));

        this.ringEffect = ringEffect;
        setRegistryName(registryName);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {

        return (ringEffect != null) ? new GemRingCapabilityProvider(stack) : null;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return (ringEffect != null) && (stack.getOrCreateTag().getBoolean("active"));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return ringEffect != null;
    }


//    @Override
//    public double getDurabilityForDisplay(ItemStack stack) {
//        if (ringEffect == null) {
//            return 0.0;
//        }
//
//        return stack.getCapability(CapabilityEnergy.ENERGY).map(e -> {
//            double stored = e.getEnergyStored();
//            double max = e.getMaxEnergyStored();
//            return (max - stored) / max;
//        }).orElse(1.0);
//    }

    @Override
    public int getBarWidth(ItemStack stack) {
        if (ringEffect == null) {
            return 0;
        }

        return stack.getCapability(CapabilityEnergy.ENERGY)
                .map(e -> Math.min(13 * e.getEnergyStored() / e.getMaxEnergyStored(), 13))
                .orElse(0);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

        if (attributeModifierMultimap == null) {
            return multimap;
        }

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> {
                CompoundTag tag = stack.getOrCreateTag();
                if (e.getEnergyStored() > 0 && tag.getBoolean("active")) {
                    multimap.putAll(attributeModifierMultimap);
                }
            });
        }
        return multimap;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x00FF00;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (ringEffect == null) {
            return;
        }

        if (firstLineOfTooltip != null) {
            tooltip.add(firstLineOfTooltip);
        }

        if (Screen.hasShiftDown()) {
            tooltip.add(new TextComponent("Energy: " + stack.getOrCreateTag().getInt("energy") + " FE").setStyle(GREEN_STYLE));
            tooltip.add(BLANK_LINE);
            ringEffect.addInformation(stack, worldIn, tooltip, flagIn);
        }
        else {
            tooltip.add(PRESS_CTRL);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (ringEffect == null) {
            return;
        }

        // check is a living entity
        if (entityIn instanceof LivingEntity) {
            LivingEntity casted = (LivingEntity) entityIn;

            // get energy capability
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> {
                CompoundTag tag = stack.getOrCreateTag();
                final boolean isCreative = entityIsCreative(casted);
                final int energyStored = e.getEnergyStored();
                if (tag.getBoolean("active")) {
                    if ((isSelected || casted.getOffhandItem().equals(stack)) && (energyStored >= ENERGY_PER_TICK || isCreative)) {
                        // add potion effects only if conditions are met
                        ringEffect.doEffect(casted);

                        if (!isCreative) {
                            // subtract energy from survival players
                            e.extractEnergy(ENERGY_PER_TICK, false);

                            // check if we hit 0
                            if (e.getEnergyStored() < ENERGY_PER_TICK) {
                                // set to inactive so glow goes away
                                stack.getOrCreateTag().putBoolean("active", false);
                            }
                        }
                    }
                    else if (energyStored < ENERGY_PER_TICK && !isCreative) {
                        // set to inactive so glow goes away
                        stack.getOrCreateTag().putBoolean("active", false);
                    }
                }
            });
        }
    }

    private boolean entityIsCreative(Entity entity) {
        if (entity instanceof Player) {
            Player casted = (Player)entity;
            return casted.isCreative();
        }
        else {
            return false;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (ringEffect == null) {
            return super.use(worldIn, playerIn, handIn);
        }

        ItemStack currentStack = playerIn.getItemInHand(handIn);
        CompoundTag tag = currentStack.getOrCreateTag();

        tag.putBoolean("active", !tag.getBoolean("active"));
        tag.putInt("energy", tag.getInt("energy"));

        return super.use(worldIn, playerIn, handIn);
    }

}
