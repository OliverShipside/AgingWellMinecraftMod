package com.AgingWell;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps plain-English resource names (as a player might type them) to
 * Minecraft block registry IDs used by Baritone's mine process.
 *
 * Keys are lower-case; matching is done after lower-casing the user input.
 */
public class ResourceAliases {

    private static final Map<String, String> BLOCK_MAP = new HashMap<>();

    static {
        // --- Ores ---
        put("diamond",         "minecraft:diamond_ore",       "deepslate diamond ore");
        put("diamonds",        "minecraft:diamond_ore");
        put("iron",            "minecraft:iron_ore");
        put("iron ore",        "minecraft:iron_ore");
        put("gold",            "minecraft:gold_ore");
        put("gold ore",        "minecraft:gold_ore");
        put("coal",            "minecraft:coal_ore");
        put("coal ore",        "minecraft:coal_ore");
        put("redstone",        "minecraft:redstone_ore");
        put("lapis",           "minecraft:lapis_ore");
        put("lapis lazuli",    "minecraft:lapis_ore");
        put("emerald",         "minecraft:emerald_ore");
        put("copper",          "minecraft:copper_ore");
        put("netherite",       "minecraft:ancient_debris");
        put("ancient debris",  "minecraft:ancient_debris");
        put("quartz",          "minecraft:nether_quartz_ore");

        // --- Wood / trees ---
        put("wood",            "minecraft:oak_log");
        put("logs",            "minecraft:oak_log");
        put("oak",             "minecraft:oak_log");
        put("oak log",         "minecraft:oak_log");
        put("oak logs",        "minecraft:oak_log");
        put("birch",           "minecraft:birch_log");
        put("spruce",          "minecraft:spruce_log");
        put("jungle wood",     "minecraft:jungle_log");
        put("acacia",          "minecraft:acacia_log");
        put("dark oak",        "minecraft:dark_oak_log");
        put("cherry",          "minecraft:cherry_log");
        put("mangrove",        "minecraft:mangrove_log");

        // --- Stone / earth ---
        put("stone",           "minecraft:stone");
        put("cobblestone",     "minecraft:cobblestone");
        put("dirt",            "minecraft:dirt");
        put("gravel",          "minecraft:gravel");
        put("sand",            "minecraft:sand");
        put("clay",            "minecraft:clay");
        put("granite",         "minecraft:granite");
        put("diorite",         "minecraft:diorite");
        put("andesite",        "minecraft:andesite");
        put("deepslate",       "minecraft:deepslate");
        put("obsidian",        "minecraft:obsidian");
        put("bedrock",         "minecraft:bedrock");   // Baritone will simply not find it

        // --- Plants / food ---
        put("wheat",           "minecraft:wheat");
        put("potatoes",        "minecraft:potatoes");
        put("carrots",         "minecraft:carrots");
        put("beetroot",        "minecraft:beetroots");
        put("sugar cane",      "minecraft:sugar_cane");
        put("sugarcane",       "minecraft:sugar_cane");
        put("bamboo",          "minecraft:bamboo");
        put("cactus",          "minecraft:cactus");
        put("mushroom",        "minecraft:brown_mushroom");
        put("brown mushroom",  "minecraft:brown_mushroom");
        put("red mushroom",    "minecraft:red_mushroom");

        // --- Miscellaneous ---
        put("flint",           "minecraft:gravel");   // gravel drops flint
        put("glowstone",       "minecraft:glowstone");
        put("soul sand",       "minecraft:soul_sand");
        put("netherrack",      "minecraft:netherrack");
        put("end stone",       "minecraft:end_stone");
        put("ice",             "minecraft:ice");
        put("snow",            "minecraft:snow_block");
    }

    /** Register a single alias → primary block id. */
    private static void put(String alias, String blockId) {
        BLOCK_MAP.put(alias.toLowerCase(), blockId);
    }

    /** Register an alias with one extra (ignored) comment argument for readability. */
    private static void put(String alias, String blockId, @SuppressWarnings("unused") String comment) {
        BLOCK_MAP.put(alias.toLowerCase(), blockId);
    }

    /**
     * Resolve a player-typed resource name to a Minecraft block registry ID.
     *
     * @param name  plain-English name, e.g. "diamonds", "oak logs"
     * @return      block id string, or {@code null} if not recognised
     */
    public static String resolveBlock(String name) {
        if (name == null) return null;
        String lower = name.toLowerCase().trim();

        // Direct match
        if (BLOCK_MAP.containsKey(lower)) return BLOCK_MAP.get(lower);

        // Partial / contains match as fallback
        for (Map.Entry<String, String> entry : BLOCK_MAP.entrySet()) {
            if (lower.contains(entry.getKey()) || entry.getKey().contains(lower)) {
                return entry.getValue();
            }
        }
        return null;
    }
}