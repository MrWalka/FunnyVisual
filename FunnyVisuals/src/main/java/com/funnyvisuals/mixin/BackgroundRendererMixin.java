package com.funnyvisuals.mixin;

import com.funnyvisuals.FunnyColors;
import com.funnyvisuals.FunnyConfig;
import net.minecraft.client.render.BackgroundRenderer;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Красит туман в сдвинутый по фазе цвет радуги, чтобы он отличался от неба. */
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Inject(method = "getFogColor", at = @At("RETURN"), cancellable = true)
    private static void funnyvisuals$rainbowFog(CallbackInfoReturnable<Vector4f> cir) {
        if (FunnyConfig.sky) {
            int c = FunnyColors.rainbow(0.15f);
            cir.setReturnValue(new Vector4f(
                    FunnyColors.red(c) / 255f,
                    FunnyColors.green(c) / 255f,
                    FunnyColors.blue(c) / 255f,
                    1f));
        }
    }
}
