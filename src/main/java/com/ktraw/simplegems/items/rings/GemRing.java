package com.ktraw.simplegems.items.rings;

import com.google.common.collect.Multimap;
import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.tools.IEffectProvider;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;

public class GemRing extends Item {
    private static final int ENERGY_PER_TICK = 10;
    private static final Style GREEN_STYLE = new Style().setColor(TextFormatting.GREEN);
    private static final Style HINT_STYLE = new Style().setItalic(true).setColor(TextFormatting.GRAY);

    private static final ITextComponent BLANK_LINE = new StringTextComponent("");
    private static final ITextComponent PRESS_CTRL = new StringTextComponent("Press <Shift>").setStyle(HINT_STYLE);

    private IEffectProvider ringEffect;
    private ITextComponent firstLineOfTooltip;
    private Multimap<String, AttributeModifier> attributeModifierMultimap;

    public GemRing(String registryName, @Nullable IEffectProvider ringEffect, @Nullable Multimap<String, AttributeModifier> attributeModifierMultimap, @Nullable ITextComponent firstLineOfTooltip) {
        this(registryName, ringEffect);
        this.attributeModifierMultimap = attributeModifierMultimap;
        this.firstLineOfTooltip = firstLineOfTooltip;
    }

    public GemRing(String registryName, @Nullable IEffectProvider ringEffect) {
        super(new Properties()
                .group(SimpleGems.setup.getCreativeTab())
                .maxStackSize(1));

        this.ringEffect = ringEffect;
        setRegistryName(registryName);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {

        return (ringEffect != null) ? new GemRingCapabilityProvider(stack) : null;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
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
    public boolean showDurabilityBar(ItemStack stack) {
        return ringEffect != null;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        if (ringEffect == null) {
            return 0.0;
        }

        return stack.getCapability(CapabilityEnergy.ENERGY).map(e -> {
            double stored = e.getEnergyStored();
            double max = e.getMaxEnergyStored();
            return (max - stored) / max;
        }).orElse(1.0);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slot, ItemStack stack) {
        Multimap<String, AttributeModifier> multimap = super.getAttributeModifiers(slot, stack);

        if (attributeModifierMultimap == null) {
            return multimap;
        }

        if (slot == EquipmentSlotType.MAINHAND || slot == EquipmentSlotType.OFFHAND) {
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> {
                CompoundNBT tag = stack.getOrCreateTag();
                if (e.getEnergyStored() > 0 && tag.getBoolean("active")) {
                    multimap.putAll(attributeModifierMultimap);
                }
            });
        }
        return multimap;
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0x00FF00;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (ringEffect == null) {
            return;
        }

        if (firstLineOfTooltip != null) {
            tooltip.add(firstLineOfTooltip);
        }

        if (Screen.hasShiftDown()) {
            tooltip.add(new StringTextComponent("Energy: " + stack.getOrCreateTag().getInt("energy") + " FE").setStyle(GREEN_STYLE));
            tooltip.add(BLANK_LINE);
            ringEffect.addInformation(stack, worldIn, tooltip, flagIn);
        }
        else {
            tooltip.add(PRESS_CTRL);
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (ringEffect == null) {
            return;
        }

        // check is a living entity
        if (entityIn instanceof LivingEntity) {
            LivingEntity casted = (LivingEntity) entityIn;

            // do a check for max health TODO specific fix for 1 ring, try to genericize this checking behavior, method reference?
            float maxHealth = casted.getMaxHealth();
            if (casted.getHealth() > maxHealth) {
                casted.setHealth(maxHealth);
            }

            // get energy capability
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> {
                CompoundNBT tag = stack.getOrCreateTag();
                    if (tag.getBoolean("active")) {
                        if ((isSelected || casted.getHeldItemOffhand().equals(stack)) && (e.getEnergyStored() >= ENERGY_PER_TICK || entityIsCreative(casted))) {
                            // add potion effects only if conditions are met
                            ringEffect.doEffect(casted);

                            if (!entityIsCreative(casted)) {
                                // subtract energy from survival players
                                e.extractEnergy(ENERGY_PER_TICK, false);
                            }
                        }
                    }

            });
        }
    }

    private boolean entityIsCreative(Entity entity) {
        if (entity instanceof PlayerEntity) {
            PlayerEntity casted = (PlayerEntity)entity;
            return casted.isCreative();
        }
        else {
            return false;
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (ringEffect == null) {
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }

        ItemStack currentStack = playerIn.getHeldItem(handIn);
        CompoundNBT tag = currentStack.getOrCreateTag();

        tag.putBoolean("active", !tag.getBoolean("active"));
        tag.putInt("energy", tag.getInt("energy"));

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
