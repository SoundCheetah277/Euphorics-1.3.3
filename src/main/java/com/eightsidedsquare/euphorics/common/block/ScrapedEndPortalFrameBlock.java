package com.eightsidedsquare.euphorics.common.block;

import com.eightsidedsquare.euphorics.core.EuphoricsSounds;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.*;

public class ScrapedEndPortalFrameBlock extends EndPortalFrameBlock
{
    public ScrapedEndPortalFrameBlock(final AbstractBlock.Settings settings) {
        super(settings);
    }

    public void scheduledTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random) {
        this.regenerate(state, world, pos);
    }

    public void randomTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random) {
        this.regenerate(state, world, pos);
    }

    protected void regenerate(final BlockState state, final ServerWorld world, final BlockPos pos) {
        if (world.getBlockState(pos).isOf((Block)this)) {
            world.setBlockState(pos, Blocks.END_PORTAL_FRAME.getStateWithProperties(state));
            final Vec3d soundPos = Vec3d.ofCenter((Vec3i)pos);
            world.playSound((PlayerEntity)null, soundPos.x, soundPos.y, soundPos.z, EuphoricsSounds.BLOCK_SCRAPED_END_PORTAL_FRAME_REGENERATE, SoundCategory.BLOCKS, 1.0f, 2.0f);
            world.spawnParticles((ParticleEffect) ParticleTypes.SMOKE, soundPos.x, (double)(pos.getY() + 0.8125f), soundPos.z, 10, 0.125, 0.0, 0.125, 0.0);
        }
    }

    public boolean hasRandomTicks(final BlockState state) {
        return true;
    }
}
