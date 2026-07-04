package com.AgingWell.info;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles /info &lt;name&gt; free-text lookups.
 * Combines block, item, and entity knowledge into a single searchable map.
 */
public class InfoLookup {

    private static final Map<String, String> LOOKUP = new HashMap<>();

    static {
        // Gameplay concepts
        add("hunger",       "Your hunger bar is the row of drumsticks. Keep it above 6 (3 drumsticks) to heal. "
                + "Eat food to restore it. You'll eventually starve on Hard difficulty.");
        add("health",       "Your health bar is the row of hearts. You regenerate health slowly when your hunger is nearly full. "
                + "Potions and golden apples can restore it faster.");
        add("experience",   "Green orbs dropped by mobs and ores. Fill your XP bar to level up. "
                + "Spend levels at an enchanting table or anvil.");
        add("enchanting",   "Use an enchanting table to add special powers to tools and armour. "
                + "Costs XP levels and lapis lazuli. Surround the table with bookshelves for stronger enchants.");
        add("crafting",     "Open a crafting table (right-click) to access the 3x3 crafting grid. "
                + "Arrange ingredients in patterns to make items. Check the recipe book for help.");
        add("smelting",     "Place a furnace, put ore or raw food in the top slot, and fuel (coal, wood) in the bottom. "
                + "Wait for the arrow to fill and collect your item.");
        add("brewing",      "Use a brewing stand with blaze powder as fuel, a water bottle, and nether wart to start. "
                + "Add ingredients to create potions with different effects.");
        add("farming",      "Till dirt near water with a hoe, plant seeds, and wait for crops to grow. "
                + "Use bone meal to speed up growth.");
        add("nether",       "A dangerous dimension accessed through an obsidian portal lit with flint and steel. "
                + "1 block here = 8 blocks in the Overworld. Home to blazes, ghasts, and nether fortresses.");
        add("end",          "The final dimension, reached through a stronghold End Portal activated with Eyes of Ender. "
                + "The Ender Dragon lives here. Defeating it gives huge XP and unlocks End Cities.");
        add("spawn",        "Where you appear when you first join or die. Set a new spawn by sleeping in a bed. "
                + "Your current spawn coordinates show in the F3 screen.");
        add("respawn",      "When you die, you respawn at your bed (if it exists) or your original world spawn. "
                + "Items are dropped where you died — return quickly to collect them.");
        add("inventory",    "Press E to open your inventory. You have 27 storage slots and 4 armour slots. "
                + "The hotbar at the bottom holds 9 items accessible with number keys 1-9.");
        add("hotbar",       "The 9 item slots at the bottom of the screen. "
                + "Press 1-9 to select them, or scroll your mouse wheel.");
        add("night",        "Hostile mobs spawn at night in open areas. Sleep in a bed to skip it, "
                + "or light up your area with torches so mobs can't spawn.");
        add("sleep",        "Right-click a bed at night to sleep. All players on a server must sleep for it to work. "
                + "Skips to dawn and resets your spawn point.");
        add("coordinates",  "Press F3 to open the debug screen showing X, Y, Z. "
                + "X = east/west, Y = height, Z = north/south. Sea level is Y=63.");
        add("f3",           "The debug screen. Shows coordinates, biome, FPS, and other technical info. "
                + "Press F3 to toggle it on and off.");
        add("stronghold",   "An underground stone structure containing a portal to The End. "
                + "Find it by throwing Eyes of Ender — they fly toward it.");
        add("village",      "A naturally generated settlement of villagers. Contains useful loot chests "
                + "and villagers you can trade with for items.");
        add("nether fortress", "A large Nether structure made of Nether Bricks. "
                + "Home to blazes and wither skeletons. Contains blaze spawners and rare loot chests.");
        add("bastion",      "A large Nether structure made of blackstone. Home to Piglins and Hoglins. "
                + "Contains excellent loot including netherite.");

        // Key mechanics
        add("sprint",       "Hold Ctrl (or double-tap W) to sprint. Uses hunger faster but moves you 30% quicker.");
        add("sneak",        "Hold Shift to sneak. You move slowly but won't fall off edges, "
                + "and other players can't see your nametag from far away.");
        add("swim",         "Hold space to swim upward in water. Breath metre appears — surface before it empties.");
        add("fall damage",  "Falling more than 3 blocks deals damage. Crouch-land, use water, "
                + "or use Feather Falling boots to reduce it. A water pool breaks any fall.");
        add("fire",         "Standing in fire or lava deals damage rapidly. Carry a bucket of water to extinguish yourself.");
        add("hunger bar",   "The 10 drumstick icons. Eat food to keep it filled. Healing requires at least 9/10 filled.");
    }

    /**
     * Look up a free-text query and return a description, or null if not found.
     */
    public static String lookupByName(String query) {
        if (query == null) return null;
        String lower = query.toLowerCase().trim();

        // Exact match
        if (LOOKUP.containsKey(lower)) return LOOKUP.get(lower);

        // Contains match
        for (Map.Entry<String, String> entry : LOOKUP.entrySet()) {
            if (lower.contains(entry.getKey()) || entry.getKey().contains(lower)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private static void add(String key, String description) {
        LOOKUP.put(key.toLowerCase(), description);
    }
}