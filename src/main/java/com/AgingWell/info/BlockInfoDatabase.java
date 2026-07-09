package com.AgingWell.info;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

/**
 * Provides plain-language descriptions and tips for Minecraft blocks.
 */
public class BlockInfoDatabase {

    private static final Map<String, String> DESCRIPTIONS = new HashMap<>();
    private static final Map<String, List<String>> TIPS     = new HashMap<>();

    static {
        // --- Natural ---
        desc("grass_block",    "The green grassy surface found on most overworld terrain. Dig it with a shovel.");
        desc("dirt",           "Plain dirt. Found just below grass. Good for farming if near water.");
        desc("stone",          "The most common underground block. Mine with any pickaxe.");
        desc("deepslate",      "A harder version of stone found very deep underground (below Y=0).");
        desc("gravel",         "Falls when unsupported. Dig it with a shovel. Has a chance to drop flint.");
        desc("sand",           "Falls when unsupported. Dig with a shovel. Used to make glass in a furnace.");
        desc("bedrock",        "The indestructible bottom layer of the world. You cannot mine through it.");
        desc("obsidian",       "Extremely hard block formed when water meets a lava source. Requires a diamond pickaxe to mine.");
        desc("clay",           "Found near water. Smelt clay balls in a furnace to make bricks.");
        desc("ice",            "Slippery frozen water. Found in cold biomes. Melts near light sources.");
        desc("snow_block",     "A block of packed snow. Found in cold biomes.");
        desc("netherrack",     "The common red stone of the Nether. Breaks quickly but is not very useful.");
        desc("soul_sand",      "Slows movement. Found in the Nether. Needed to build a Wither boss.");
        desc("glowstone",      "Bright glowing block from the Nether. Provides strong light.");
        desc("end_stone",      "The pale yellow stone found in The End dimension.");

        // --- Ores ---
        desc("coal_ore",       "Contains coal. Mine with any pickaxe. Common near the surface.");
        desc("iron_ore",       "Contains raw iron. Mine with a stone pickaxe or better. Very common underground.");
        desc("gold_ore",       "Contains raw gold. Mine with an iron pickaxe or better. Also found in the Badlands biome.");
        desc("diamond_ore",    "Contains diamonds — a precious resource. Mine with an iron or diamond pickaxe. Found mostly between Y=-50 and Y=-64.");
        desc("redstone_ore",   "Contains redstone dust. Mine with an iron pickaxe or better. Acts like electrical wiring and can be used for circuits.");
        desc("lapis_ore",      "Contains lapis lazuli. Mine with a stone pickaxe or better. Used for enchanting.");
        desc("emerald_ore",    "Contains emeralds. Mine with an iron pickaxe. Found only in mountain biomes. Used to trade with villagers.");
        desc("copper_ore",     "Contains raw copper. Mine with a stone pickaxe. Turns green over time when placed (oxidise).");
        desc("ancient_debris", "Very rare Nether ore. Smelt it to get netherite scraps. Combine with gold to make ingots for netherite gear.");

        // --- Wood ---
        desc("oak_log",        "Wood from an oak tree. Chop with an axe. Used for planks, sticks, and crafting.");
        desc("birch_log",      "Light-coloured wood from a birch tree. Chop with an axe. Used for planks, sticks, and crafting.");
        desc("spruce_log",     "Dark wood from a spruce (pine) tree. Chop with an axe. Used for planks, sticks, and crafting.");
        desc("jungle_log",     "Wood from a jungle tree. Jungle trees are the tallest in the game. Chop with an axe. Used for planks, sticks, and crafting.");
        desc("oak_planks",     "Processed wood. Made from 1 log = 4 planks. Used in many recipes.");
        desc("oak_leaves",     "Leaf blocks. Break naturally overtime when the tree is removed. May drop saplings or apples.");

        // --- Structures & Crafting ---
        desc("crafting_table", "Open with right-click to access the full 3×3 crafting grid.");
        desc("furnace",        "Smelt ores or cook food. Right-click to open. Needs fuel in the bottom slot.");
        desc("chest",          "Stores up to 27 item stacks. Right-click to open.");
        desc("bed",            "Sleep at night to skip to dawn and reset your spawn point.");
        desc("door",           "Right-click to open or close. Can keep mobs out of your base.");
        desc("torch",          "A light source. Place on walls, floors, or ceilings.");
        desc("ladder",         "Place on a wall to climb. Right-click a wall block with the ladder in hand.");
        desc("glass",          "Transparent block. Made by smelting sand. Mobs can't see through tinted glass.");
        desc("bookshelf",      "Boosts nearby enchanting tables. Place 15 bookshelves around one for max enchants.");
        desc("enchanting_table", "Spend experience points to enchant tools and armour. Needs lapis lazuli.");
        desc("anvil",          "Repair or rename items, and apply enchantment books.");
        desc("brewing_stand",  "Brew potions using water bottles, nether wart, and various ingredients.");
        desc("beacon",         "A powerful late-game block that grants status effects to nearby players.");
        desc("spawner",        "Continuously spawns a specific mob. Found in dungeons and other structures.");
        desc("tnt",            "Explodes when ignited. Handle carefully!");
        desc("nether_portal",  "Step through to travel to the Nether dimension. Built from obsidian, lit with flint and steel.");

        // Tips
        tip("diamond_ore",     "Diamond ore is most common at Y=-58. Press F3 to check your Y level.");
        tip("ancient_debris",  "Ancient debris is blast-resistant. Use beds in the Nether to explode it — beds explode in the Nether!");
        tip("gravel",          "Hold a torch below a gravel column and break the bottom piece — the whole column falls.");
        tip("furnace",         "Wood logs, coal, and charcoal are all valid fuels. Coal smelts 8 items per piece.");
        tip("crafting_table",  "You can also access recipes in your recipe book (the book icon) to guide you.");
        tip("chest",           "Shift-click items to move them in and out quickly.");
        tip("enchanting_table","Surround it with 15 bookshelves (with a one block gap) to unlock level-30 enchantments.");
        tip("bed",             "A bed also works as a respawn point. If your bed is destroyed you'll respawn at world spawn.");
        tip("nether_portal",   "1 block in the Nether equals 8 blocks in the Overworld — great for fast travel!");
        tip("spawner",         "Mob Spawners spawn mobs. Place a torch on it to prevent it from spawning, or turn into an XP farm! Light the area and add water channels to funnel mobs.");
    }

    // -----------------------------------------------------------------------

    public static String describe(BlockState state) {
        String id = getPath(state);
        return DESCRIPTIONS.getOrDefault(id,
                "This is " + state.getBlock().getName().getString() + ". "
                        + "No detailed description is available yet — check the Minecraft Wiki for more info!");
    }

    public static List<String> getTips(BlockState state) {
        String id = getPath(state);
        return TIPS.getOrDefault(id, Collections.emptyList());
    }

    public static String getDescription(String id) {
        return DESCRIPTIONS.get(id);
    }

    public static List<String> getTips(String id) {
        return TIPS.getOrDefault(id, Collections.emptyList());
    }

    // -----------------------------------------------------------------------

    private static String getPath(BlockState state) {
        var key = net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(state.getBlock());
        return key == null ? "" : key.getPath();
    }

    private static void desc(String path, String description) {
        DESCRIPTIONS.put(path, description);
    }

    private static void tip(String path, String tip) {
        TIPS.computeIfAbsent(path, k -> new ArrayList<>()).add(tip);
    }
}