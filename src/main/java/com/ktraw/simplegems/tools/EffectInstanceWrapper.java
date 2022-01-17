package com.ktraw.simplegems.tools;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import javax.annotation.Nonnull;

public class EffectInstanceWrapper {
    private MobEffect effect;
    private int duration;
    private int amplifier;

    public EffectInstanceWrapper(MobEffect effect, int duration, int amplifier) {
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public EffectInstanceWrapper(MobEffect effect, int duration) {
        this(effect, 0, 0);
    }

    public EffectInstanceWrapper(MobEffect effect) {
        this(effect, 0);
    }

    @Nonnull
    public MobEffectInstance getNewEffectInstance() {
        return new MobEffectInstance(effect, duration, amplifier);
    }

    public MobEffect getEffect() {
        return effect;
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }
}
