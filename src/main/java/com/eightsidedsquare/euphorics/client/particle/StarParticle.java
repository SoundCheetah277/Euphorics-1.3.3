package com.eightsidedsquare.euphorics.client.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.Util;
import com.google.common.collect.*;
import java.util.*;
import org.jetbrains.annotations.*;
import net.minecraft.*;

public class StarParticle extends SpriteBillboardParticle
{
    private final float rotationSpeed;

    public StarParticle(final ClientWorld clientWorld, final double x, final double y, final double z, final double velocityX, final double velocityY, final double velocityZ, final Vector3f color) {
        super(clientWorld, x, y, z);
        this.gravityStrength = 0.0f;
        this.setVelocity(velocityX, velocityY, velocityZ);
        this.setColor(color.x, color.y, color.z);
        this.rotationSpeed = (float)(Math.random() - 0.5) * 0.1f;
        this.angle = (float)(Math.random() * 3.141592653589793 * 2.0);
        this.maxAge += 30;
    }

    public void tick() {
        super.tick();
        if (!this.dead) {
            this.prevAngle = this.angle;
            this.angle += (float)(3.141592653589793 * this.rotationSpeed * 2.0);
        }
        this.alpha = Math.min(1.0f, (this.maxAge - this.age) / 20.0f);
    }

    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class RawEndustFactory implements ParticleFactory<DefaultParticleType>
    {
        protected final SpriteProvider spriteProvider;
        protected final ImmutableList<Vector3f> colors;

        public RawEndustFactory(final SpriteProvider spriteProvider) {
            this.colors = ImmutableList.of(new Vector3f(220.0f, 228.0f, 166.0f), new Vector3f(199.0f, 207.0f, 136.0f), new Vector3f(165.0f, 190.0f, 117.0f), new Vector3f(133.0f, 172.0f, 97.0f), new Vector3f(103.0f, 165.0f, 98.0f), new Vector3f(96.0f, 160.0f, 92.0f), new Vector3f(48.0f, 150.0f, 102.0f));
            this.spriteProvider = spriteProvider;
            this.colors.forEach(vec -> vec.div(255.0f));
        }

        @Nullable
        public Particle createParticle(final DefaultParticleType parameters, final ClientWorld world, final double x, final double y, final double z, final double velocityX, final double velocityY, final double velocityZ) {
            final StarParticle particle = new StarParticle(world, x, y, z, velocityX, velocityY, velocityZ, Util.getRandom(this.colors, world.random));
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }

    public static class AstralPlaneFactory implements ParticleFactory<DefaultParticleType>
    {
        protected final SpriteProvider spriteProvider;
        protected final ImmutableList<Vector3f> colors;

        public AstralPlaneFactory(final SpriteProvider spriteProvider) {
            this.colors = ImmutableList.of(new Vector3f(25.0f, 56.0f, 100.0f), new Vector3f(50.0f, 62.0f, 127.0f), new Vector3f(13.0f, 76.0f, 100.0f), new Vector3f(43.0f, 101.0f, 147.0f), new Vector3f(43.0f, 135.0f, 152.0f), new Vector3f(143.0f, 75.0f, 170.0f), new Vector3f(154.0f, 84.0f, 182.0f), new Vector3f(163.0f, 96.0f, 175.0f), new Vector3f(218.0f, 107.0f, 213.0f), new Vector3f(244.0f, 140.0f, 212.0f), new Vector3f(254.0f, 180.0f, 207.0f));
            this.spriteProvider = spriteProvider;
            this.colors.forEach(vec -> vec.div(255.0f));
        }

        @Nullable
        public Particle createParticle(final DefaultParticleType parameters, final ClientWorld world, final double x, final double y, final double z, final double velocityX, final double velocityY, final double velocityZ) {
            final StarParticle particle = new StarParticle(world, x, y, z, velocityX, velocityY, velocityZ, Util.getRandom(this.colors, world.random));
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
