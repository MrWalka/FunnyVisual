package com.funnyvisuals;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public final class FunnyCommands {
    private FunnyCommands() {}

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess registryAccess) {
        LiteralArgumentBuilder<FabricClientCommandSource> root = ClientCommandManager.literal("funny")
                .executes(ctx -> {
                    status(ctx.getSource());
                    return 1;
                });

        // /funny trail | hud | sky | rain — переключение функции
        for (String feature : FunnyConfig.FEATURES) {
            root.then(ClientCommandManager.literal(feature).executes(ctx -> {
                Boolean state = FunnyConfig.toggle(feature);
                ctx.getSource().sendFeedback(Text.literal(
                        "FunnyVisuals: " + FunnyConfig.label(feature) + " — " + (state ? "вкл" : "выкл")));
                return 1;
            }));
        }

        // /funny burst
        root.then(ClientCommandManager.literal("burst").executes(ctx -> {
            ParticleFx.burst(MinecraftClient.getInstance());
            return 1;
        }));

        // /funny intensity <1-10>
        root.then(ClientCommandManager.literal("intensity")
                .then(ClientCommandManager.argument("value", IntegerArgumentType.integer(1, 10))
                        .executes(ctx -> {
                            FunnyConfig.intensity = IntegerArgumentType.getInteger(ctx, "value");
                            FunnyConfig.save();
                            ctx.getSource().sendFeedback(Text.literal(
                                    "FunnyVisuals: интенсивность = " + FunnyConfig.intensity));
                            return 1;
                        })));

        dispatcher.register(root);
    }

    private static void status(FabricClientCommandSource src) {
        StringBuilder sb = new StringBuilder("FunnyVisuals:");
        for (String f : FunnyConfig.FEATURES) {
            sb.append("\n  ").append(f).append(" — ").append(FunnyConfig.get(f) ? "вкл" : "выкл");
        }
        sb.append("\n  intensity — ").append(FunnyConfig.intensity);
        sb.append("\nКоманды: /funny <trail|hud|sky|rain|burst> и /funny intensity <1-10>");
        src.sendFeedback(Text.literal(sb.toString()));
    }
}
