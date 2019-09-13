package com.ktraw.simplegems.tools;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SingleEffectProvider implements IEffectProvider {
    private EffectInstanceWrapper effect;

    public SingleEffectProvider(EffectInstanceWrapper effect) {
        this.effect = effect;
    }

    @Override
    public void doEffect(LivingEntity entity) {
        entity.addPotionEffect(effect.getNewEffectInstance());
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(IEffectProvider.EFFECTS);
        tooltip.add(IEffectProvider.getComponentFromEffect(effect));
    }
}
