package com.ktraw.simplegems.tools;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class MultiEffectProvider implements IEffectProvider {
    private List<EffectInstanceWrapper> effects;

    public MultiEffectProvider(List<EffectInstanceWrapper> effects) {
        this.effects = effects;
    }

    @Override
    public void doEffect(LivingEntity entity) {
        for (EffectInstanceWrapper effect : effects) {
            entity.addEffect(effect.getNewEffectInstance());
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(IEffectProvider.EFFECTS);
        for (EffectInstanceWrapper effect : effects) {
            tooltip.add(IEffectProvider.getComponentFromEffect(effect));
        }
    }
}
