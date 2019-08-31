package com.ktraw.mymod.events;

import com.ktraw.mymod.items.ChargedEmeraldDust;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class PlayerEvents {
    @SubscribeEvent
    public void entityInteract(PlayerInteractEvent.EntityInteract event) {
        PlayerEntity player = event.getPlayer();
        ItemStack heldItem = player.getHeldItem(event.getHand());
        Entity target = event.getTarget();

        if ((target instanceof VillagerEntity || target instanceof WanderingTraderEntity) && heldItem.getItem() instanceof ChargedEmeraldDust) {
            // Play firework launch sound
            ResourceLocation location = new ResourceLocation("minecraft", "entity.firework_rocket.launch");
            SoundEvent soundEvent = new SoundEvent(location);
            player.playSound(soundEvent, 100, 1);

            // Launch villager and give potion effect
            target.addVelocity(0, 1, 0);
            ((MobEntity) target).addPotionEffect(new EffectInstance(Effects.SPEED, 500, 5));

            // Remove 1 item if not in creative mode
            if (!player.isCreative()) {
                heldItem.setCount(heldItem.getCount() - 1);
            }
        }
    }
}
