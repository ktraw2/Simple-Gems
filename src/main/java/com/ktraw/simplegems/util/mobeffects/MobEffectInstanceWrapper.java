package com.ktraw.simplegems.util.mobeffects;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Getter
public class MobEffectInstanceWrapper {
    private final MobEffect effect;
    private final int duration;
    private final int amplifier;

    public MobEffectInstanceWrapper(MobEffect effect, int duration) {
        this(effect, duration, 0);
    }

    public MobEffectInstanceWrapper(MobEffect effect) {
        this(effect, 0);
    }

    @Nonnull
    public MobEffectInstance getNewEffectInstance() {
        return new MobEffectInstance(effect, duration, amplifier);
    }
}
