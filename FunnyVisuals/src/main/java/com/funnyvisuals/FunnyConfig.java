package com.funnyvisuals;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

/** Настройки мода. Хранятся в config/funnyvisuals.properties. */
public final class FunnyConfig {
    public static final List<String> FEATURES = List.of("trail", "hud", "sky", "rain");

    public static boolean trail = true;
    public static boolean hud = true;
    public static boolean sky = true;
    public static boolean rain = false;
    /** Интенсивность частиц, 1..10. */
    public static int intensity = 5;

    private FunnyConfig() {}

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("funnyvisuals.properties");
    }

    public static boolean get(String feature) {
        return switch (feature) {
            case "trail" -> trail;
            case "hud" -> hud;
            case "sky" -> sky;
            case "rain" -> rain;
            default -> false;
        };
    }

    /** Переключает функцию и возвращает новое состояние (null, если такой функции нет). */
    public static Boolean toggle(String feature) {
        switch (feature) {
            case "trail" -> trail = !trail;
            case "hud" -> hud = !hud;
            case "sky" -> sky = !sky;
            case "rain" -> rain = !rain;
            default -> { return null; }
        }
        save();
        return get(feature);
    }

    public static String label(String feature) {
        return switch (feature) {
            case "trail" -> "радужный след";
            case "hud" -> "HUD";
            case "sky" -> "радужное небо";
            case "rain" -> "конфетти-дождь";
            default -> feature;
        };
    }

    public static void load() {
        Path f = file();
        if (!Files.exists(f)) {
            save();
            return;
        }
        Properties p = new Properties();
        try (Reader r = Files.newBufferedReader(f, StandardCharsets.UTF_8)) {
            p.load(r);
            trail = Boolean.parseBoolean(p.getProperty("trail", "true"));
            hud = Boolean.parseBoolean(p.getProperty("hud", "true"));
            sky = Boolean.parseBoolean(p.getProperty("sky", "true"));
            rain = Boolean.parseBoolean(p.getProperty("rain", "false"));
            intensity = Math.max(1, Math.min(10, Integer.parseInt(p.getProperty("intensity", "5"))));
        } catch (IOException | NumberFormatException e) {
            System.err.println("[FunnyVisuals] Не удалось прочитать конфиг: " + e);
        }
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("trail", String.valueOf(trail));
        p.setProperty("hud", String.valueOf(hud));
        p.setProperty("sky", String.valueOf(sky));
        p.setProperty("rain", String.valueOf(rain));
        p.setProperty("intensity", String.valueOf(intensity));
        try (Writer w = Files.newBufferedWriter(file(), StandardCharsets.UTF_8)) {
            p.store(w, "FunnyVisuals");
        } catch (IOException e) {
            System.err.println("[FunnyVisuals] Не удалось сохранить конфиг: " + e);
        }
    }
}
