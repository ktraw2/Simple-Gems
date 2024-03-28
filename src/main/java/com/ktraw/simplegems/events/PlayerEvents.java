package com.ktraw.simplegems.events;

import com.ktraw.simplegems.items.ChargedEmeraldDust;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEvents {
    @SubscribeEvent
    public void entityInteract(final PlayerInteractEvent.EntityInteract event) {
        final Player player = event.getEntity();
        final ItemStack heldItem = player.getItemInHand(event.getHand());
        final Entity target = event.getTarget();

        if ((target instanceof Villager || target instanceof WanderingTrader) && heldItem.getItem() instanceof ChargedEmeraldDust) {
            // Play firework launch sound
            final ResourceLocation location = new ResourceLocation("minecraft", "entity.firework_rocket.launch");
            final SoundEvent soundEvent = SoundEvent.createVariableRangeEvent(location);
            player.playSound(soundEvent, 100, 1);

            // Launch villager and give potion effect
            target.push(0, 1, 0); // TODO: addVelocity?
            ((Mob) target).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 500, 5));

            // Remove 1 item if not in creative mode
            if (!player.isCreative()) {
                heldItem.setCount(heldItem.getCount() - 1);
            }
        }
    }

    @SubscribeEvent
    public void playerTick(final TickEvent.PlayerTickEvent event) {
        // fix for health not resetting
        final float maxHealth = event.player.getMaxHealth();
        if (event.player.getHealth() > maxHealth) {
            event.player.setHealth(maxHealth);
        }
    }
}
