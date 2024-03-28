package com.ktraw.simplegems.items.rings;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.ktraw.simplegems.util.mobeffects.IMobEffectProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class GemRing extends Item {
    private static final int ENERGY_PER_TICK = 100;
    private static final Style GREEN_STYLE = Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GREEN));
    private static final Style HINT_STYLE = Style.EMPTY.withItalic(true).withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY));

    private static final Component BLANK_LINE = Component.literal("");
    private static final Component PRESS_CTRL = Component.literal("Press <Shift>").setStyle(HINT_STYLE);

    private final IMobEffectProvider ringEffect;
    private Component firstLineOfTooltip;
    private Multimap<Attribute, AttributeModifier> attributeModifierMultimap;

    public GemRing(
            @Nullable final IMobEffectProvider ringEffect,
            @Nullable final Multimap<Attribute, AttributeModifier> attributeModifierMultimap,
            @Nullable final Component firstLineOfTooltip
    ) {
        this(ringEffect);
        this.attributeModifierMultimap = attributeModifierMultimap;
        this.firstLineOfTooltip = firstLineOfTooltip;
    }

    public GemRing(@Nullable final IMobEffectProvider ringEffect) {
        super(new Properties()
                .stacksTo(1));

        this.ringEffect = ringEffect;
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(
            final ItemStack stack,
            @Nullable final CompoundTag nbt
    ) {
        return (ringEffect != null) ? new GemRingCapabilityProvider(stack) : null;
    }

    @Override
    public boolean isFoil(@Nonnull final ItemStack stack) {
        return (ringEffect != null) && (stack.getOrCreateTag().getBoolean("active"));
    }

    @Override
    public boolean shouldCauseReequipAnimation(
            final ItemStack oldStack,
            final ItemStack newStack,
            final boolean slotChanged
    ) {
        return slotChanged;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(
            final ItemStack oldStack,
            final ItemStack newStack
    ) {
        return false;
    }

    @Override
    public boolean isBarVisible(@Nonnull final ItemStack stack) {
        return ringEffect != null;
    }

    @Override
    public int getBarWidth(@Nonnull final ItemStack stack) {
        if (ringEffect == null) {
            return 0;
        }

        return stack.getCapability(ForgeCapabilities.ENERGY)
                .map(e -> Math.min(13 * e.getEnergyStored() / e.getMaxEnergyStored(), 13))
                .orElse(0);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(
            final EquipmentSlot slot,
            final ItemStack stack
    ) {
        final Multimap<Attribute, AttributeModifier> multimap = ArrayListMultimap.create();

        if (attributeModifierMultimap == null) {
            return multimap;
        }

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(e -> {
                final CompoundTag tag = stack.getOrCreateTag();
                if (e.getEnergyStored() > 0 && tag.getBoolean("active")) {
                    multimap.putAll(attributeModifierMultimap);
                }
            });
        }
        return multimap;
    }

    @Override
    public int getBarColor(@Nonnull final ItemStack stack) {
        return 0x00FF00;
    }

    @Override
    public void appendHoverText(
            @Nonnull final ItemStack stack,
            @Nullable final Level worldIn,
            @Nonnull final List<Component> tooltip,
            @Nonnull final TooltipFlag flagIn
    ) {
        if (ringEffect == null) {
            return;
        }

        if (firstLineOfTooltip != null) {
            tooltip.add(firstLineOfTooltip);
        }

        if (Screen.hasShiftDown()) {
            tooltip.add(Component.literal("Energy: " + stack.getOrCreateTag().getInt("energy") + " FE").setStyle(GREEN_STYLE));
            tooltip.add(BLANK_LINE);
            ringEffect.addInformation(stack, worldIn, tooltip, flagIn);
        } else {
            tooltip.add(PRESS_CTRL);
        }
    }

    @Override
    public void inventoryTick(
            @Nonnull final ItemStack stack,
            @Nonnull final Level worldIn,
            @Nonnull final Entity entityIn,
            final int itemSlot,
            final boolean isSelected
    ) {
        if (ringEffect == null) {
            return;
        }

        // check is a living entity
        if (entityIn instanceof LivingEntity casted) {
            // get energy capability
            stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(e -> {
                final CompoundTag tag = stack.getOrCreateTag();
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
                    } else if (energyStored < ENERGY_PER_TICK && !isCreative) {
                        // set to inactive so glow goes away
                        stack.getOrCreateTag().putBoolean("active", false);
                    }
                }
            });
        }
    }

    private boolean entityIsCreative(final Entity entity) {
        if (entity instanceof Player casted) {
            return casted.isCreative();
        }

        return false;
    }

    @Nonnull
    @Override
    public InteractionResultHolder<ItemStack> use(
            @Nonnull final Level worldIn,
            @Nonnull final Player playerIn,
            @Nonnull final InteractionHand handIn
    ) {
        if (ringEffect == null) {
            return super.use(worldIn, playerIn, handIn);
        }

        final ItemStack currentStack = playerIn.getItemInHand(handIn);
        final CompoundTag tag = currentStack.getOrCreateTag();

        tag.putBoolean("active", !tag.getBoolean("active"));
        tag.putInt("energy", tag.getInt("energy"));

        return super.use(worldIn, playerIn, handIn);
    }

}
