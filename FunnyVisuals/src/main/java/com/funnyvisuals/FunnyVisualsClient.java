package com.funnyvisuals;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class FunnyVisualsClient implements ClientModInitializer {
    public static final String MOD_ID = "funnyvisuals";

    private static KeyBinding trailKey;
    private static KeyBinding hudKey;
    private static KeyBinding skyKey;
    private static KeyBinding rainKey;
    private static KeyBinding burstKey;

    @Override
    public void onInitializeClient() {
        FunnyConfig.load();

        trailKey = register("trail", GLFW.GLFW_KEY_J);
        hudKey = register("hud", GLFW.GLFW_KEY_K);
        skyKey = register("sky", GLFW.GLFW_KEY_L);
        rainKey = register("rain", GLFW.GLFW_KEY_H);
        burstKey = register("burst", GLFW.GLFW_KEY_G);

        ClientTickEvents.END_CLIENT_TICK.register(FunnyVisualsClient::onTick);
        HudRenderCallback.EVENT.register(HudOverlay::render);
        ClientCommandRegistrationCallback.EVENT.register(FunnyCommands::register);
    }

    private static KeyBinding register(String name, int key) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key." + MOD_ID + "." + name,
                InputUtil.Type.KEYSYM,
                key,
                "category." + MOD_ID));
    }

    private static void onTick(MinecraftClient client) {
        while (trailKey.wasPressed()) announce(client, "trail");
        while (hudKey.wasPressed()) announce(client, "hud");
        while (skyKey.wasPressed()) announce(client, "sky");
        while (rainKey.wasPressed()) announce(client, "rain");
        while (burstKey.wasPressed()) ParticleFx.burst(client);

        ParticleFx.tick(client);
    }

    private static void announce(MinecraftClient client, String feature) {
        Boolean state = FunnyConfig.toggle(feature);
        if (client.player != null && state != null) {
            client.player.sendMessage(Text.literal(
                    "FunnyVisuals: " + FunnyConfig.label(feature) + " — " + (state ? "вкл" : "выкл")), true);
        }
    }
}
