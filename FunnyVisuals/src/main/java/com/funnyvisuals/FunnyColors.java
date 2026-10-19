package com.funnyvisuals;

import net.minecraft.util.math.MathHelper;

public final class FunnyColors {
    private FunnyColors() {}

    /** Цвет радуги, зависящий от времени. offset сдвигает оттенок (0..1). Возвращает ARGB с полной непрозрачностью. */
    public static int rainbow(float offset) {
        float h = ((System.currentTimeMillis() % 6000L) / 6000f + offset) % 1f;
        if (h < 0) h += 1f;
        return 0xFF000000 | MathHelper.hsvToRgb(h, 0.75f, 1f);
    }

    public static int red(int argb) { return (argb >> 16) & 0xFF; }
    public static int green(int argb) { return (argb >> 8) & 0xFF; }
    public static int blue(int argb) { return argb & 0xFF; }
}
