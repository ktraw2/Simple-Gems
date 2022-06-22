package com.ktraw.simplegems.util.mobeffects;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public interface IMobEffectProvider {
    static Component EFFECTS = Component.literal("Effects:").setStyle(Style.EMPTY.withUnderlined(true));

    public void doEffect(LivingEntity entity);

    public void addInformation(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn);

    static Component getComponentFromEffect(MobEffectInstanceWrapper effect) {
        return Component.translatable("tooltip.simplegems.effect", Component.translatable(effect.getEffect().getDescriptionId()), Component.literal(String.valueOf(effect.getAmplifier() + 1)));
    }
}
