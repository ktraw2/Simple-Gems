package com.ktraw.simplegems.tools;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

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
            entity.addPotionEffect(effect.getNewEffectInstance());
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(IEffectProvider.EFFECTS);
        for (EffectInstanceWrapper effect : effects) {
            tooltip.add(IEffectProvider.getComponentFromEffect(effect));
        }
    }
}
