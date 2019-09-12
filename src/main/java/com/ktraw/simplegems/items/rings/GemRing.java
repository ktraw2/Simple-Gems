package com.ktraw.simplegems.items.rings;

import com.google.common.collect.Multimap;
import com.ktraw.simplegems.SimpleGems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;
import java.util.List;

public class GemRing extends Item {
    private static int ENERGY_PER_TICK = 10;
    private static Style GREEN_STYLE = new Style().setColor(TextFormatting.GREEN);
    private EffectInstance ringEffect = new EffectInstance(Effects.HASTE, 20);

    public GemRing() {
        super(new Properties()
                .group(SimpleGems.setup.getCreativeTab())
                .maxStackSize(1));

        setRegistryName("gem_ring");
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new GemRingCapabilityProvider(stack);
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean("active");
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
        return true;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY).map(e -> {
            double stored = e.getEnergyStored();
            double max = e.getMaxEnergyStored();
            return (max - stored) / max;
        }).orElse(1.0);
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        return 0x00FF00;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new StringTextComponent("Energy: " + stack.getOrCreateTag().getInt("energy") + " FE").setStyle(GREEN_STYLE));
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(e -> {
            if (stack.hasTag()) {
                CompoundNBT tag = stack.getTag();
                if (entityIn instanceof PlayerEntity) {
                    if (tag.getBoolean("active")) {
                        PlayerEntity casted = (PlayerEntity) entityIn;
                        if ((isSelected || casted.getHeldItemOffhand().equals(stack)) && e.getEnergyStored() >= ENERGY_PER_TICK) {
                            casted.addPotionEffect(ringEffect);
                            if (!casted.isCreative()) {
                                e.extractEnergy(ENERGY_PER_TICK, false);
                            }
                        }
                    }
                }
                else {
                    tag.putBoolean("active", false);
                }



            }
        });
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack currentStack = playerIn.getHeldItem(handIn);
        CompoundNBT tag = currentStack.getOrCreateTag();

        tag.putBoolean("active", !tag.getBoolean("active"));
        tag.putInt("energy", tag.getInt("energy"));

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
