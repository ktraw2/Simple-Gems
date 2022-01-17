package com.ktraw.simplegems.util.mobeffects;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public record MultiMobEffectProvider(List<MobEffectInstanceWrapper> effects) implements IMobEffectProvider {
    @Override
    public void doEffect(LivingEntity entity) {
        for (MobEffectInstanceWrapper effect : effects) {
            entity.addEffect(effect.getNewEffectInstance());
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(IMobEffectProvider.EFFECTS);
        for (MobEffectInstanceWrapper effect : effects) {
            tooltip.add(IMobEffectProvider.getComponentFromEffect(effect));
        }
    }
}
