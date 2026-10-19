package com.funnyvisuals;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.ThreadLocalRandom;

public final class ParticleFx {
    private ParticleFx() {}

    /** Вызывается каждый клиентский тик. */
    public static void tick(MinecraftClient c) {
        ClientWorld world = c.world;
        ClientPlayerEntity player = c.player;
        if (world == null || player == null || c.isPaused()) return;

        ThreadLocalRandom r = ThreadLocalRandom.current();
        int power = FunnyConfig.intensity;

        // Радужный след: частицы NOTE красятся через velocityX (это оттенок 0..1).
        if (FunnyConfig.trail) {
            Vec3d v = player.getVelocity();
            boolean moving = v.x * v.x + v.z * v.z > 1.0E-4;
            if (moving) {
                int count = 1 + power / 3;
                for (int i = 0; i < count; i++) {
                    world.addParticle(ParticleTypes.NOTE,
                            player.getX() + (r.nextDouble() - 0.5) * 0.6,
                            player.getY() + 0.1,
                            player.getZ() + (r.nextDouble() - 0.5) * 0.6,
                            r.nextDouble(), 0, 0);
                }
                if (player.isSprinting()) {
                    for (int i = 0; i < power / 2; i++) {
                        world.addParticle(ParticleTypes.FIREWORK,
                                player.getX() + (r.nextDouble() - 0.5) * 0.5,
                                player.getY() + 0.2,
                                player.getZ() + (r.nextDouble() - 0.5) * 0.5,
                                (r.nextDouble() - 0.5) * 0.05, 0.05, (r.nextDouble() - 0.5) * 0.05);
                    }
                }
            }
        }

        // Конфетти-дождь вокруг игрока.
        if (FunnyConfig.rain) {
            int count = power * 3;
            for (int i = 0; i < count; i++) {
                double x = player.getX() + (r.nextDouble() - 0.5) * 24;
                double z = player.getZ() + (r.nextDouble() - 0.5) * 24;
                double y = player.getY() + 4 + r.nextDouble() * 6;
                if (r.nextBoolean()) {
                    world.addParticle(ParticleTypes.CHERRY_LEAVES, x, y, z, 0, -0.05, 0);
                } else {
                    world.addParticle(ParticleTypes.FIREWORK, x, y, z, 0, -0.12, 0);
                }
            }
        }
    }

    /** Залп конфетти вокруг игрока. */
    public static void burst(MinecraftClient c) {
        ClientWorld world = c.world;
        ClientPlayerEntity player = c.player;
        if (world == null || player == null) return;

        ThreadLocalRandom r = ThreadLocalRandom.current();
        Vec3d p = player.getEyePos();
        int count = 20 + FunnyConfig.intensity * 10;
        for (int i = 0; i < count; i++) {
            double vx = (r.nextDouble() - 0.5) * 0.8;
            double vy = r.nextDouble() * 0.6;
            double vz = (r.nextDouble() - 0.5) * 0.8;
            if (i % 2 == 0) {
                world.addParticle(ParticleTypes.FIREWORK, p.x, p.y, p.z, vx, vy, vz);
            } else {
                world.addParticle(ParticleTypes.NOTE, p.x + vx, p.y + vy, p.z + vz, r.nextDouble(), 0, 0);
            }
        }
    }
}
