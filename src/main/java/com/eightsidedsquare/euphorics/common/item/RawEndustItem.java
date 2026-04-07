package com.eightsidedsquare.euphorics.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import com.eightsidedsquare.euphorics.core.*;
import net.minecraft.*;
import com.eightsidedsquare.euphorics.cca.*;

public class RawEndustItem extends AliasedBlockItem
{
    public static final int VISUAL_EUPHORIA_TICKS_INCREMENT = 200;

    public RawEndustItem(final Block block, final Item.Settings settings) {
        super(block, settings);
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

    public ItemStack finishUsing(final ItemStack stack, final World world, final LivingEntity user) {
        super.finishUsing(stack, world, user);
        if (user instanceof PlayerEntity) {
            final PlayerEntity player = (PlayerEntity)user;
            final EuphoriaComponent component = (EuphoriaComponent)EuphoricsEntityComponents.EUPHORIA.get((Object)player);
            final boolean alreadyEuphoric = component.getTotalEuphoriaTicks() > 0;
            component.setVisualEuphoriaTicks(component.getVisualEuphoriaTicks() + 200);
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
}
