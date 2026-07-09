package com.AgingWell.info;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

/**
 * Provides plain-language descriptions and usage tips for Minecraft items,
 * intended to help players who are new to the game understand what things do.
 */
public class ItemInfoDatabase {

    // item registry path → description
    private static final Map<String, String> DESCRIPTIONS = new HashMap<>();
    // item registry path → list of tips
    private static final Map<String, List<String>> TIPS = new HashMap<>();

    static {
        // --- Tools ---
        desc("wooden_pickaxe",  "A basic pickaxe made of wood. Used to mine stone and coal ore. Breaks quickly.");
        desc("stone_pickaxe",   "A stone pickaxe. Mines iron ore and most common blocks. Much sturdier than wood.");
        desc("iron_pickaxe",    "An iron pickaxe. Can mine gold, redstone and lapis ore. A reliable all-rounder.");
        desc("diamond_pickaxe", "A diamond pickaxe — one of the best tools. Can mine obsidian and all ores.");
        desc("wooden_axe",      "A wooden axe. Chops logs and wood blocks. Can also be used as a weapon in a pinch.");
        desc("wooden_shovel",   "A wooden shovel. Digs dirt, sand, gravel and snow quickly.");
        desc("wooden_sword",    "A basic wooden sword. Deals a little more damage than your fist.");
        desc("iron_sword",      "A solid iron sword. Deals decent damage to most enemies.");
        desc("diamond_sword",   "A powerful diamond sword. One of the strongest melee weapons in the game.");
        desc("bow",             "A ranged weapon that fires arrows. Hold right-click to draw, release to shoot.");
        desc("fishing_rod",     "Cast into water with right-click, wait for the bobber to dip, then right-click again to reel in.");
        desc("flint_and_steel", "Creates fire. Used to light Nether portals, fireplaces, and TNT.");
        desc("compass",         "Points toward your original spawn point. Useful if you get lost.");
        desc("clock",           "Shows the time of day. Gold face = day, dark face = night.");
        desc("map",             "Displays a top-down view of the terrain around you when you made it.");
        desc("shears",          "Harvest wool from sheep without killing them. Also cuts leaves and cobwebs.");

        // --- Food ---
        desc("bread",           "A filling food item. Craft from 3 wheat in a row across a crafting table.");
        desc("apple",           "A light snack. Found in structure chests or dropped by oak leaves.");
        desc("cooked_beef",     "Cooked steak — one of the most filling foods. Cook raw beef in a furnace.");
        desc("cooked_porkchop", "Cooked pork chop. Very filling. Cook raw porkchop in a furnace.");
        desc("golden_apple",    "A special apple that gives you absorption (extra hearts) and regeneration briefly.");
        desc("cooked_chicken",  "Cooked chicken. Good food. Cook raw chicken in a furnace.");
        desc("cooked_salmon",   "Cooked salmon. Good food. Cook raw salmon in a furnace.");

        // --- Materials ---
        desc("stick",           "A basic crafting ingredient. Made from 2 planks stacked vertically.");
        desc("iron_ingot",      "Smelted from iron ore. Used to craft tools, armour, buckets, and more.");
        desc("gold_ingot",      "Smelted from gold ore. Used for powered rails, clocks, and golden tools.");
        desc("diamond",         "A rare gem found deep underground. Used for the best tools and armour.");
        desc("coal",            "Burns as fuel in furnaces. Also crafts torches with a stick.");
        desc("string",          "Dropped by spiders. Used to craft bows, fishing rods, and wool.");
        desc("feather",         "Dropped by chickens. Used to craft arrows and books.");
        desc("leather",         "Dropped by cows. Used to craft basic armour and books.");
        desc("wool",            "Sheared from sheep. Used for beds, carpets and decoration.");
        desc("torch",           "Provides light. Prevents hostile mobs from spawning nearby at night.");
        desc("ladder",          "Place on the side of a block to climb up or down safely.");
        desc("chest",           "Stores up to 27 stacks of items. Place two side-by-side for a large chest (54 slots).");
        desc("crafting_table",  "Unlocks the 3×3 crafting grid, needed for most recipes.");
        desc("furnace",         "Smelts ores into ingots and cooks raw food. Needs fuel (coal, wood, etc.).");
        desc("bed",             "Sleep in it at night to skip to dawn and set your spawn point.");
        desc("bucket",          "Carries water or lava. Fill by right-clicking on a source block.");
        desc("ender_pearl",     "Thrown to teleport to where it lands. Takes a little damage on arrival.");
        desc("blaze_rod",       "Dropped by Blazes in the Nether. Used to brew potions and make Eyes of Ender.");

        // --- Armour ---
        desc("iron_helmet",     "Protects your head. Part of an iron armour set.");
        desc("iron_chestplate", "Protects your torso — the largest armour piece. Part of an iron set.");
        desc("iron_leggings",   "Protects your legs. Part of an iron armour set.");
        desc("iron_boots",      "Protects your feet. Part of an iron armour set.");
        desc("diamond_helmet",  "Top-tier head protection.");
        desc("shield",          "Hold in your offhand and right-click to block incoming attacks and projectiles.");

        // ---- Tips ----
        tip("bread",           "Craft 3 wheat in a row on a crafting table.");
        tip("torch",           "Place torches on your left wall when exploring caves — on the way back they will be on your right, helping you navigate home.");
        tip("bed",             "You must be near the bed at night and no monsters can be nearby for it to work.");
        tip("compass",         "Does not point to your current bed — only your original world spawn.");
        tip("bow",             "Arrows are the ammunition. Make them from a flint, stick, and feather.");
        tip("crafting_table",  "Right-click to open the full 3×3 crafting grid.");
        tip("furnace",         "Coal is the most efficient common fuel. Wood logs work too.");
        tip("chest",           "Shift-click an item to move it quickly between your inventory and the chest.");
        tip("ender_pearl",     "Be careful — you take damage each time you teleport.");
        tip("iron_pickaxe",    "You need at least an iron pickaxe to mine gold, diamond, redstone, and lapis.");
        tip("diamond_pickaxe", "Enchant it with Fortune III to get more diamonds per ore block.");
        tip("shield",          "It completely blocks skeleton arrows if you right-click just before they hit.");
    }

    // -----------------------------------------------------------------------

    public static String describe(ItemStack stack) {
        String id = getPath(stack);

        String description = DESCRIPTIONS.get(id);
        if (description != null) {
            return description;
        }

        description = BlockInfoDatabase.getDescription(id);
        if (description != null) {
            return description;
        }

        return "This is " + stack.getHoverName().getString() + ". "
                + "No detailed description is available yet — try looking it up on the Minecraft Wiki!";
    }

    public static List<String> getTips(ItemStack stack) {
        String id = getPath(stack);
        return TIPS.getOrDefault(id, Collections.emptyList());
    }

    // -----------------------------------------------------------------------

    private static String getPath(ItemStack stack) {
        var key = ForgeRegistries.ITEMS.getKey(stack.getItem());
        return key == null ? "" : key.getPath();
    }

    private static void desc(String path, String description) {
        DESCRIPTIONS.put(path, description);
    }

    private static void tip(String path, String tip) {
        TIPS.computeIfAbsent(path, k -> new ArrayList<>()).add(tip);
    }
}