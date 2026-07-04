package com.AgingWell.guidance;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Predicate;

/**
 * Analyses a player's inventory and advancement state to suggest the most
 * relevant next goal.
 *
 * Goals are ordered in a rough progression from "just spawned" through to
 * "late game".  The advisor scans the list from the current position and
 * picks the first goal whose "unlock condition" is met (i.e. the player
 * seems ready for it) but whose "completion condition" is not yet satisfied.
 *
 * Per-player progress is tracked in a simple map keyed on the player's UUID.
 * This resets each session (no persistence yet) — file-based saving can be
 * added later.
 */

public class GoalAdvisor {

    private static final List<Goal> GOALS = new ArrayList<>();

    static {
        // ── Tier 0: First minutes ─────────────────────────────────────────
        GOALS.add(new Goal(
                "Gather some wood",
                "Walk up to any tree and hold left-click on the trunk. Collect at least 4 logs.",
                p -> true,
                p -> hasItemCount(p, "oak_log", 4) || hasItemCount(p, "birch_log", 4)
                        || hasItemCount(p, "spruce_log", 4) || hasItemCount(p, "jungle_log", 4)
                        || hasItemCount(p, "dark_oak_log", 4) || hasItemCount(p, "acacia_log", 4)
                        || hasItemCount(p, "mangrove_log", 4) || hasItemCount(p, "cherry_log", 4)
        ));

        GOALS.add(new Goal(
                "Craft Wood Planks and a Crafting Table",
                "Open your inventory (E). Craft logs into planks, then arrange 4 planks in a 2x2 square.",
                p -> hasItemCount(p, "oak_log", 4) || hasItemCount(p, "birch_log", 4)
                        || hasItemCount(p, "spruce_log", 4),
                p -> hasItemCount(p, "oak_planks", 4) || hasItemCount(p, "birch_planks", 4)
                        || hasItemCount(p, "spruce_planks", 4)
        ));

        GOALS.add(new Goal(
                "Craft Wooden Tools",
                "Place your Crafting Table and make a wooden pickaxe and sword.",
                p -> hasItemCount(p, "oak_planks", 4) || hasItemCount(p, "birch_planks", 4)
                        || hasItemCount(p, "spruce_planks", 4),
                p -> hasItem(p, "wooden_pickaxe") && hasItem(p, "wooden_sword")
        ));

        GOALS.add(new Goal(
                "Mine Stone and craft Stone Tools",
                "Use your wooden pickaxe to mine cobblestone, then craft a stone pickaxe and sword.",
                p -> hasItem(p, "wooden_pickaxe"),
                p -> hasItem(p, "stone_pickaxe") && hasItem(p, "stone_sword")
        ));

        GOALS.add(new Goal(
                "Find or build Shelter before nightfall",
                "Dig into a hillside or build 4 walls and a roof. Night brings dangerous monsters. When you are happy, go to the next goal.",
                p -> hasItem(p, "stone_pickaxe"),
                p -> false
        ));

        GOALS.add(new Goal(
                "Craft Torches",
                "Mine coal ore (grey-streaked stone near the surface) and combine each coal with a stick.",
                p -> hasItem(p, "stone_pickaxe"),
                p -> hasItemCount(p, "torch", 8)
        ));

        // ── Tier 1: Early survival ────────────────────────────────────────
        GOALS.add(new Goal(
                "Build a Furnace",
                "Arrange 8 cobblestone in a ring on a Crafting Table (leave the centre empty). Furnaces can be powered with coal, wood or other burnable objects. You can even use lava buckets!",
                p -> hasItemCount(p, "cobblestone", 8),
                p -> hasItem(p, "furnace")
        ));

        GOALS.add(new Goal(
                "Cook some Food",
                "Place a Furnace, put raw meat in the top slot and coal or wood in the bottom. Wait for it to cook.",
                p -> hasItem(p, "furnace"),
                p -> hasItem(p, "cooked_beef", "cooked_porkchop", "cooked_chicken",
                        "cooked_salmon", "bread", "cooked_mutton")
        ));

        GOALS.add(new Goal(
                "Build a Bed",
                "Kill 3 sheep for wool (or shear them) and combine with 3 planks on a Crafting Table. Sleep in the bed to spawn at it when you die.",
                p -> hasItem(p, "stone_sword"),
                p -> hasItem(p, "white_bed", "red_bed", "orange_bed", "yellow_bed",
                        "green_bed", "blue_bed", "purple_bed", "pink_bed",
                        "black_bed", "gray_bed", "light_gray_bed", "cyan_bed",
                        "light_blue_bed", "lime_bed", "magenta_bed", "brown_bed")
        ));

        // ── Tier 2: Iron age ──────────────────────────────────────────────
        GOALS.add(new Goal(
                "Mine Iron Ore",
                "Dig down to Y=16-56 or look for a cave. Look for orange-flecked stone. Mine with a stone pickaxe or better. Watch out for lava though.",
                p -> hasItem(p, "stone_pickaxe"),
                p -> hasItemCount(p, "raw_iron", 8)
        ));

        GOALS.add(new Goal(
                "Smelt Iron Ingots",
                "Now put raw iron in the top slot of your Furnace and coal or wood in the bottom. You'll need at least 15 ingots.",
                p -> hasItemCount(p, "raw_iron", 8),
                p -> hasItemCount(p, "iron_ingot", 15)
        ));

        GOALS.add(new Goal(
                "Craft Iron Tools and Armour",
                "Use your iron ingots to craft at least an iron pickaxe, sword, chestplate and helmet.",
                p -> hasItemCount(p, "iron_ingot", 15),
                p -> hasItem(p, "iron_pickaxe") && hasItem(p, "iron_sword")
                        && hasItem(p, "iron_chestplate") && hasItem(p, "iron_helmet")
        ));

        // ── Tier 3: Mid game ──────────────────────────────────────────────
        GOALS.add(new Goal(
                "Mine Diamonds",
                "Head deep underground to around Y=-58. Bring lots of torches and your iron pickaxe. Try to get at least 10",
                p -> hasItem(p, "iron_pickaxe"),
                p -> hasItemCount(p, "diamond", 10)
        ));

        GOALS.add(new Goal(
                "Craft Diamond Tools and Armour",
                "Use your diamonds to craft at least a diamond pickaxe, sword, chestplate and helmet.",
                p -> hasItemCount(p, "diamond", 10),
                p -> hasItem(p, "diamond_pickaxe") && hasItem(p, "diamond_sword")
                        && hasItem(p, "diamond_chestplate")
        ));

        GOALS.add(new Goal(
                "Build an Enchanting Table",
                "Craft using 4 obsidian, 2 diamonds, and 1 book. Mine obsidian where water meets lava (use a diamond pickaxe).",
                p -> hasItem(p, "diamond_pickaxe"),
                p -> hasItem(p, "enchanting_table")
        ));

        GOALS.add(new Goal(
                "Enchant your gear",
                "Right-click your Enchanting Table. Spend XP levels and lapis lazuli to enchant tools and armour. Lapis lazuli can be mined underground. Once you are happy, move on to the next goal.",
                p -> hasItem(p, "enchanting_table"),
                p -> false
        ));

        // ── Tier 4: Nether ────────────────────────────────────────────────
        GOALS.add(new Goal(
                "Enter the Nether",
                "Build a frame of 10+ obsidian (4 wide x 5 tall, hollow inside) and light it with flint and steel. Once you are there, move on to the next goal.",
                p -> hasItem(p, "diamond_sword") && hasItem(p, "flint_and_steel"),
                p -> false
        ));

        GOALS.add(new Goal(
                "Find a Nether Fortress",
                "Explore the Nether to find a large dark brick structure. It contains Blazes and rare loot chests. Once you are find one, move on to the next goal.",
                p -> hasItem(p, "diamond_sword"),
                p -> false
        ));

        GOALS.add(new Goal(
                "Collect Blaze Rods",
                "Kill Blazes inside the Nether Fortress. They drop Blaze Rods needed for brewing and Eyes of Ender. You'll need at least 6, but more will be useful",
                p -> hasItem(p, "diamond_sword"),
                p -> hasItemCount(p, "blaze_rod", 6)
        ));

        GOALS.add(new Goal(
                "Collect Ender Pearls",
                "Kill Endermen (tall dark mobs) in the Nether or Overworld. They drop Ender Pearls. You'll need at least 12",
                p -> hasItemCount(p, "blaze_rod", 6),
                p -> hasItemCount(p, "ender_pearl", 12)
        ));

        GOALS.add(new Goal(
                "Craft Eyes of Ender",
                "Craft Blaze Powder from Blaze Rods (1 rod = 2 powder). Then combine each powder with an Ender Pearl.",
                p -> hasItemCount(p, "blaze_rod", 6) && hasItemCount(p, "ender_pearl", 12),
                p -> hasItemCount(p, "ender_eye", 12)
        ));

        GOALS.add(new Goal(
                "Brew Potions",
                "It's a good idea to build a Brewing Stand (1 Blaze Rod + 3 Cobblestone). Brew healing and strength potions for the final fight. This is not mandatory but will make the upcoming fights easier.",
                p -> hasItemCount(p, "blaze_rod", 2),
                p -> hasItem(p, "potion")
        ));

        // ── Tier 5: End game ──────────────────────────────────────────────
        GOALS.add(new Goal(
                "Find the Stronghold",
                "Throw an Eye of Ender — it flies toward the Stronghold. Follow it until it drops into the ground. Eyes may break so having a good supply is recommended. Strongholds are underground mazes made of grey brick. Once you are there, move on to the next goal.",
                p -> hasItemCount(p, "ender_eye", 12),
                p -> false
        ));

        GOALS.add(new Goal(
                "Activate the End Portal",
                "Inside the Stronghold, find the End Portal room. Fill all 12 frame blocks with Eyes of Ender to open it. Go through the portal when you are ready to face the final boss. Once you are through, move on to the next goal.",
                p -> hasItemCount(p, "ender_eye", 12),
                p -> false
        ));

        GOALS.add(new Goal(
                "Defeat the Ender Dragon",
                "Destroy all End Crystals on the obsidian pillars first, then attack the dragon when it hovers over the portal or shoot it with a bow.",
                p -> hasItemCount(p, "ender_eye", 12),
                p -> false
        ));
    }

    private static final Map<UUID, Integer> playerProgress = new HashMap<>();

    public static String getCurrentGoal(Player player) {
        return getActiveGoal(player).title;
    }

    public static String getCurrentGoalDetail(Player player) {
        return getActiveGoal(player).detail;
    }

    public static void advanceGoal(Player player) {
        int current = playerProgress.getOrDefault(player.getUUID(), 0);
        playerProgress.put(player.getUUID(), Math.min(current + 1, GOALS.size() - 1));
    }

    private static Goal getActiveGoal(Player player) {
        int start = playerProgress.getOrDefault(player.getUUID(), 0);

        for (int i = start; i < GOALS.size(); i++) {
            Goal g = GOALS.get(i);

            if (g.isComplete.test(player)) {
                playerProgress.put(player.getUUID(), i + 1);
                continue;
            }

            if (!g.isUnlocked.test(player)) continue;

            playerProgress.put(player.getUUID(), i);
            return g;
        }

        return new Goal(
                "You've completed all suggested goals!",
                "Explore, build, or challenge yourself with a harder difficulty.",
                p -> true, p -> false
        );
    }

    private static boolean hasItem(Player player, String... itemPaths) {
        for (String path : itemPaths) {
            Item item = getItem(path);
            if (item == null) continue;
            if (player.getInventory().countItem(item) > 0) return true;
        }
        return false;
    }

    private static boolean hasItemCount(Player player, String itemPath, int count) {
        Item item = getItem(itemPath);
        if (item == null) return false;
        return player.getInventory().countItem(item) >= count;
    }

    private static Item getItem(String path) {
        String fullPath = path.contains(":") ? path : "minecraft:" + path;
        String[] parts = fullPath.split(":");
        return ForgeRegistries.ITEMS.getValue(
                ResourceLocation.fromNamespaceAndPath(parts[0], parts[1])
        );
    }

    private static class Goal {
        final String title;
        final String detail;
        final Predicate<Player> isUnlocked;
        final Predicate<Player> isComplete;

        Goal(String title, String detail,
             Predicate<Player> isUnlocked, Predicate<Player> isComplete) {
            this.title      = title;
            this.detail     = detail;
            this.isUnlocked = isUnlocked;
            this.isComplete = isComplete;
        }
    }
}