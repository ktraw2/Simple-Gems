package com.ktraw.simplegems.tools;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class SingleEffectProvider implements IEffectProvider {
    private EffectInstanceWrapper effect;

    public SingleEffectProvider(EffectInstanceWrapper effect) {
        this.effect = effect;
    }

    @Override
    public void doEffect(LivingEntity entity) {
        entity.addEffect(effect.getNewEffectInstance());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(IEffectProvider.EFFECTS);
        tooltip.add(IEffectProvider.getComponentFromEffect(effect));
    }
}
