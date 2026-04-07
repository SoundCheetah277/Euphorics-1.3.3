package com.eightsidedsquare.euphorics.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import com.eightsidedsquare.euphorics.cca.*;
import com.eightsidedsquare.euphorics.core.*;
import net.minecraft.*;

public class CrystallizedEndustItem extends Item
{
    public static final int EUPHORIA_TICKS_INCREMENT = 800;

    public CrystallizedEndustItem(final Item.Settings settings) {
        super(settings);
    }

    public ItemStack finishUsing(final ItemStack stack, final World world, final LivingEntity user) {
        super.finishUsing(stack, world, user);
        if (user instanceof PlayerEntity) {
            final PlayerEntity player = (PlayerEntity)user;
            final EuphoriaComponent component = (EuphoriaComponent)EuphoricsEntityComponents.EUPHORIA.get((Object)player);
            final boolean alreadyEuphoric = component.getTotalEuphoriaTicks() > 0;
            component.setEuphoriaTicks(component.getEuphoriaTicks() + 800);
            if (!alreadyEuphoric) {
                component.setLastUse(component.getTotalEuphoriaTicks());
            }
            else {
                component.setLastUse(component.getTotalEuphoriaTicks() + 100);
            }
            player.getItemCooldownManager().set((Item)this, 20);
            EuphoricsEntityComponents.EUPHORIA.sync((Object)player);
        }
        if (user instanceof PlayerEntity) {
            final PlayerEntity player = (PlayerEntity)user;
            if (player.getAbilities().creativeMode) {
                return stack;
            }
        }
        stack.decrement(1);
        return stack;
    }

    public int getMaxUseTime(final ItemStack stack) {
        return 24;
    }

    public UseAction getUseAction(final ItemStack stack) {
        return UseAction.DRINK;
    }

    public SoundEvent getDrinkSound() {
        return EuphoricsSounds.ITEM_CRYSTALLIZED_ENDUST_SNORT;
    }

    public SoundEvent getEatSound() {
        return this.getDrinkSound();
    }

    public TypedActionResult<ItemStack> use(final World world, final PlayerEntity user, final Hand hand) {
        return (TypedActionResult<ItemStack>) ItemUsage.consumeHeldItem(world, user, hand);
    }
}
