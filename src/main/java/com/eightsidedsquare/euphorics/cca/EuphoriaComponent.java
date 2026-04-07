package com.eightsidedsquare.euphorics.cca;

import dev.onyxstudios.cca.api.v3.component.sync.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import com.eightsidedsquare.euphorics.core.*;
import org.jetbrains.annotations.*;
import net.minecraft.*;

public class EuphoriaComponent implements AutoSyncedComponent
{
    public static final int MAX_SHADE_AGGRO = 20000;
    public static final int SHADES_STALK_PLAYERS_AGGRO = 6000;
    public static final int SHADES_ATTACK_PLAYERS_AGGRO = 12000;
    public static final int MAX_FORCED_ASTRAL_PLANE_TICKS = 300;
    private static final int ASTRAL_PLANE_JUMP_READY_TICKS = 80;
    private static final int BLINK_COOLDOWN_TICKS = 40;
    private final PlayerEntity player;
    private int euphoriaTicks;
    private int visualEuphoriaTicks;
    private int lastUse;
    private int blinkCooldown;
    private int shadeAggro;
    private int forcedAstralPlaneTicks;
    private boolean inAstralPlane;
    private boolean wasInAstralPlane;

    public EuphoriaComponent(final PlayerEntity player) {
        this.player = player;
    }

    public void tick() {
        if (this.lastUse - this.euphoriaTicks == 80) {
            this.player.sendMessage((Text)Text.translatable("astral_plane_jump.ready"), true);
        }
        if (this.euphoriaTicks > 0) {
            --this.euphoriaTicks;
            this.shadeAggro = Math.min(this.shadeAggro + (this.inAstralPlane ? 10 : 1), 20000);
        }
        else if (this.shadeAggro > 0) {
            this.shadeAggro = Math.max(0, this.shadeAggro - 2);
        }
        if (this.euphoriaTicks <= 80 && this.inAstralPlane) {
            this.setInAstralPlane(false);
        }
        if (this.visualEuphoriaTicks > 0) {
            --this.visualEuphoriaTicks;
        }
        if (this.blinkCooldown > 0) {
            --this.blinkCooldown;
        }
        if (this.forcedAstralPlaneTicks > 0) {
            --this.forcedAstralPlaneTicks;
        }
        final World world = this.player.world;
        if (world instanceof ServerWorld) {
            final ServerWorld serverWorld = (ServerWorld)world;
            if (isInAstralPlane(this.player) && this.player.getRandom().nextFloat() < 0.06f) {
                serverWorld.spawnParticles((ParticleEffect)EuphoricsParticles.ASTRAL_PLANE, this.player.getX(), this.player.getY() + this.player.getHeight() / 2.0f, this.player.getZ(), 1, 0.125, 0.325, 0.125, 0.025);
            }
            if (this.wasInAstralPlane != isInAstralPlane(this.player)) {
                serverWorld.spawnParticles((ParticleEffect)EuphoricsParticles.ASTRAL_PLANE, this.player.getX(), this.player.getY() + this.player.getHeight() / 2.0f, this.player.getZ(), 100, 0.125, 0.325, 0.125, 0.025);
            }
        }
        this.wasInAstralPlane = isInAstralPlane(this.player);
    }

    public static boolean isOutOfAstralPlane(@Nullable final Entity entity) {
        if (entity instanceof PlayerEntity) {
            final PlayerEntity player = (PlayerEntity)entity;
            if (isInAstralPlane(player)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isInAstralPlane(@NotNull final PlayerEntity player) {
        final EuphoriaComponent component = (EuphoriaComponent)EuphoricsEntityComponents.EUPHORIA.getNullable((Object)player);
        return component != null && (component.isInAstralPlane() || component.getForcedAstralPlaneTicks() > 0);
    }

    public boolean isReadyForAstralPlaneJump() {
        return this.getLastUse() - this.getEuphoriaTicks() >= 80 && this.getEuphoriaTicks() > 80 && this.getForcedAstralPlaneTicks() <= 0 && this.getBlinkCooldown() <= 0;
    }

    public void jumpToAstralPlane() {
        this.setInAstralPlane(true);
        this.setBlinkCooldown(40);
    }

    public void exitAstralPlane() {
        this.setInAstralPlane(false);
        this.setBlinkCooldown(40);
    }

    public void kickFromAstralPlane() {
        this.setInAstralPlane(false);
        this.setBlinkCooldown(160);
    }

    public int getEuphoriaTicks() {
        return this.euphoriaTicks;
    }

    public void setEuphoriaTicks(final int ticks) {
        this.euphoriaTicks = ticks;
    }

    public int getLastUse() {
        return this.lastUse;
    }

    public void setLastUse(final int lastUse) {
        this.lastUse = lastUse;
    }

    public boolean isInAstralPlane() {
        return this.inAstralPlane;
    }

    public void setInAstralPlane(final boolean inAstralPlane) {
        this.inAstralPlane = inAstralPlane;
    }

    public int getBlinkCooldown() {
        return this.blinkCooldown;
    }

    public void setBlinkCooldown(final int blinkCooldown) {
        this.blinkCooldown = blinkCooldown;
    }

    public int getShadeAggro() {
        return this.shadeAggro;
    }

    public void setShadeAggro(final int shadeAggro) {
        this.shadeAggro = shadeAggro;
    }

    public int getVisualEuphoriaTicks() {
        return this.visualEuphoriaTicks;
    }

    public void setVisualEuphoriaTicks(final int visualEuphoriaTicks) {
        this.visualEuphoriaTicks = visualEuphoriaTicks;
    }

    public void setForcedAstralPlaneTicks(final int forcedAstralPlaneTicks) {
        this.forcedAstralPlaneTicks = forcedAstralPlaneTicks;
    }

    public int getForcedAstralPlaneTicks() {
        return this.forcedAstralPlaneTicks;
    }

    public int getTotalEuphoriaTicks() {
        return Math.max(this.getVisualEuphoriaTicks(), this.getEuphoriaTicks());
    }

    public void readFromNbt(@NotNull final NbtCompound nbt) {
        this.setEuphoriaTicks(nbt.getInt("euphoria_ticks"));
        this.setLastUse(nbt.getInt("last_use"));
        this.setInAstralPlane(nbt.getBoolean("in_astral_plane"));
        this.setBlinkCooldown(nbt.getInt("blink_cooldown"));
        this.setShadeAggro(nbt.getInt("shade_aggro"));
        this.setForcedAstralPlaneTicks(nbt.getInt("forced_astral_plane_ticks"));
        this.setVisualEuphoriaTicks(nbt.getInt("visual_euphoria_ticks"));
    }

    public void writeToNbt(@NotNull final NbtCompound nbt) {
        nbt.putInt("euphoria_ticks", this.getEuphoriaTicks());
        nbt.putInt("last_use", this.getLastUse());
        nbt.putBoolean("in_astral_plane", this.isInAstralPlane());
        nbt.putInt("blink_cooldown", this.getBlinkCooldown());
        nbt.putInt("shade_aggro", this.getShadeAggro());
        nbt.putInt("forced_astral_plane_ticks", this.getForcedAstralPlaneTicks());
        nbt.putInt("visual_euphoria_ticks", this.getVisualEuphoriaTicks());
    }
}
