package com.ktraw.simplegems.tools;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public interface IEffectProvider {
    static Component EFFECTS = new TextComponent("Effects:").setStyle(Style.EMPTY.setUnderlined(true));

    public void doEffect(LivingEntity entity);

    public void addInformation(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn);

    static Component getComponentFromEffect(EffectInstanceWrapper effect) {
        return new TranslatableComponent("tooltip.simplegems.effect", new TranslatableComponent(effect.getEffect().getDescriptionId()), new TextComponent(String.valueOf(effect.getAmplifier() + 1)));
    }
}
