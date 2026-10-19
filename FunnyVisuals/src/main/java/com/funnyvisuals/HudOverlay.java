package com.funnyvisuals;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public final class HudOverlay {
    private HudOverlay() {}

    public static void render(DrawContext ctx, RenderTickCounter tickCounter) {
        MinecraftClient c = MinecraftClient.getInstance();
        if (!FunnyConfig.hud || c.player == null || c.options.hudHidden) return;

        TextRenderer tr = c.textRenderer;
        long t = System.currentTimeMillis();

        // Радужный заголовок, каждая буква «качается» по волне.
        String title = "FunnyVisuals";
        int x = 6;
        for (int i = 0; i < title.length(); i++) {
            String ch = String.valueOf(title.charAt(i));
            int wave = (int) Math.round(Math.sin(t / 300.0 + i * 0.6) * 2);
            ctx.drawText(tr, ch, x, 6 + wave, FunnyColors.rainbow(i * 0.07f), true);
            x += tr.getWidth(ch);
        }

        // Состояние функций и FPS.
        int y = 20;
        for (String f : FunnyConfig.FEATURES) {
            boolean on = FunnyConfig.get(f);
            String line = FunnyConfig.label(f) + ": " + (on ? "вкл" : "выкл");
            ctx.drawText(tr, line, 6, y, on ? 0xFF7CFC00 : 0xFFAAAAAA, true);
            y += 10;
        }
        ctx.drawText(tr, c.getCurrentFps() + " FPS", 6, y + 2, 0xFFFFFFFF, true);

        // Пульсирующая радужная рамка вокруг прицела.
        int cx = ctx.getScaledWindowWidth() / 2;
        int cy = ctx.getScaledWindowHeight() / 2;
        int r = 9 + (int) Math.round(Math.sin(t / 200.0) * 2);
        int col = FunnyColors.rainbow(0.5f);
        ctx.fill(cx - r, cy - r, cx + r, cy - r + 1, col);
        ctx.fill(cx - r, cy + r - 1, cx + r, cy + r, col);
        ctx.fill(cx - r, cy - r, cx - r + 1, cy + r, col);
        ctx.fill(cx + r - 1, cy - r, cx + r, cy + r, col);
    }
}
