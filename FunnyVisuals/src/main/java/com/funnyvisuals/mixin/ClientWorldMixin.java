package com.funnyvisuals.mixin;

import com.funnyvisuals.FunnyColors;
import com.funnyvisuals.FunnyConfig;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Подменяет цвет неба на плавно меняющуюся радугу. */
@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "getSkyColor", at = @At("RETURN"), cancellable = true)
    private void funnyvisuals$rainbowSky(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Integer> cir) {
        if (FunnyConfig.sky) {
            cir.setReturnValue(FunnyColors.rainbow(0f));
        }
    }
}
