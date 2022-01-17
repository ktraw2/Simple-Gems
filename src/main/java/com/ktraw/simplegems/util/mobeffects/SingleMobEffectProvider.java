package com.ktraw.simplegems.util.mobeffects;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public record SingleMobEffectProvider(MobEffectInstanceWrapper effect) implements IMobEffectProvider {
    @Override
    public void doEffect(LivingEntity entity) {
        entity.addEffect(effect.getNewEffectInstance());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(IMobEffectProvider.EFFECTS);
        tooltip.add(IMobEffectProvider.getComponentFromEffect(effect));
    }
}
