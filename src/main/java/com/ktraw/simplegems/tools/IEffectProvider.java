package com.ktraw.simplegems.tools;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public interface IEffectProvider {
    static ITextComponent EFFECTS = new StringTextComponent("Effects:").setStyle(new Style().setUnderlined(true));

    public void doEffect(LivingEntity entity);

    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn);

    static ITextComponent getComponentFromEffect(EffectInstanceWrapper effect) {
        return new TranslationTextComponent("tooltip.simplegems.effect", new TranslationTextComponent(effect.getEffect().getName()), new StringTextComponent(String.valueOf(effect.getAmplifier() + 1)));
    }
}
