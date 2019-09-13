package com.ktraw.simplegems.tools;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

import javax.annotation.Nonnull;

public class EffectInstanceWrapper {
    private Effect effect;
    private int duration;
    private int amplifier;

    public EffectInstanceWrapper(Effect effect, int duration, int amplifier) {
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public EffectInstanceWrapper(Effect effect, int duration) {
        this(effect, 0, 0);
    }

    public EffectInstanceWrapper(Effect effect) {
        this(effect, 0);
    }

    @Nonnull
    public EffectInstance getNewEffectInstance() {
        return new EffectInstance(effect, duration, amplifier);
    }

    public Effect getEffect() {
        return effect;
    }

    public int getDuration() {
        return duration;
    }

    public int getAmplifier() {
        return amplifier;
    }
}
