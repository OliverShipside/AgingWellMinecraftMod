package com.AgingWell.guidance;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Predicate;

public class GoalAdvisor {

    private static final List<Goal> GOALS = new ArrayList<>();

    static {
        // ── Tier 0: First minutes ─────────────────────────────────────────
        GOALS.add(new Goal(
                "Gather some wood",
                "Walk up to any tree and hold left-click on the trunk. Collect at least 4 logs.",
                "Find a tree and stand close to it. Hold left-click on the trunk to break it. " +
                        "Move using W (forward), A (left), S (back), D (right). " +
                        "Hold Shift to sneak and Space to jump. Collect at least 4 logs.",
                p -> true,
                p -> hasItemCount(p, "oak_log", 4) || hasItemCount(p, "birch_log", 4)
                        || hasItemCount(p, "spruce_log", 4) || hasItemCount(p, "jungle_log", 4)
                        || hasItemCount(p, "dark_oak_log", 4) || hasItemCount(p, "acacia_log", 4)
                        || hasItemCount(p, "mangrove_log", 4) || hasItemCount(p, "cherry_log", 4)
        ));

        GOALS.add(new Goal(
                "Craft Wood Planks and a Crafting Table",
                "Open your inventory (E). Craft logs into planks, then arrange 4 planks in a 2x2 square.",
                "Press E to open your inventory. Click a log in your inventory and place it " +
                        "in the small 2x2 grid on the right to craft planks. Then arrange 4 planks " +
                        "in all 4 slots of the grid to make a Crafting Table. Click the result to collect it.",
                p -> hasItemCount(p, "oak_log", 4) || hasItemCount(p, "birch_log", 4)
                        || hasItemCount(p, "spruce_log", 4),
                p -> hasItemCount(p, "oak_planks", 4) || hasItemCount(p, "birch_planks", 4)
                        || hasItemCount(p, "spruce_planks", 4)
        ));

        GOALS.add(new Goal(
                "Craft Wooden Tools",
                "Place your Crafting Table and make a wooden pickaxe and sword.",
                "Right-click your Crafting Table to open the 3x3 crafting grid. " +
                        "Place a stick in the bottom two centre slots, then add planks above in an L-shape " +
                        "for a pickaxe, or a T-shape for a sword. Check the Recipe Book (the book icon) for help. " +
                        "Right-click to place items and left-click to pick them up.",
                p -> hasItemCount(p, "oak_planks", 4) || hasItemCount(p, "birch_planks", 4)
                        || hasItemCount(p, "spruce_planks", 4),
                p -> hasItem(p, "wooden_pickaxe") && hasItem(p, "wooden_sword")
        ));

        GOALS.add(new Goal(
                "Mine Stone and craft Stone Tools",
                "Use your wooden pickaxe to mine cobblestone, then craft a stone pickaxe and sword.",
                "Look for grey stone underground or on hillsides. Hold left-click with your " +
                        "wooden pickaxe equipped to mine it. Select items from your hotbar using " +
                        "the number keys 1-9 or scroll your mouse wheel. Collect enough cobblestone " +
                        "to craft a stone pickaxe and sword at your Crafting Table.",
                p -> hasItem(p, "wooden_pickaxe"),
                p -> hasItem(p, "stone_pickaxe") && hasItem(p, "stone_sword")
        ));

        GOALS.add(new Goal(
                "Find or build Shelter before nightfall",
                "Dig into a hillside or build 4 walls and a roof. Night brings dangerous monsters. When you are happy, use /guidance next to move on.",
                "Watch the sky — when it starts to go dark, hostile mobs will begin to spawn. " +
                        "Dig into the side of a hill using left-click, or place blocks using right-click " +
                        "to build walls around you. Make sure you have a roof and no gaps. " +
                        "When you are safe, type /guidance next to move on.",
                p -> hasItem(p, "stone_pickaxe"),
                p -> false
        ));

        GOALS.add(new Goal(
                "Craft Torches",
                "Mine coal ore (grey-streaked stone near the surface) and combine each coal with a stick.",
                "Look for coal ore underground — it is grey stone with black flecks. " +
                        "Mine it with your pickaxe. Then open your Crafting Table and place one coal " +
                        "on top of one stick to craft torches. Place torches by right-clicking on a wall " +
                        "or floor to light up dark areas and prevent mobs from spawning nearby.",
                p -> hasItem(p, "stone_pickaxe"),
                p -> hasItemCount(p, "torch", 8)
        ));

        // ── Tier 1: Early survival ────────────────────────────────────────
        GOALS.add(new Goal(
                "Build a Furnace",
                "Arrange 8 cobblestone in a ring on a Crafting Table (leave the centre empty).",
                "Mine at least 8 cobblestone blocks. Open your Crafting Table and place " +
                        "cobblestone in every slot except the centre to craft a Furnace. " +
                        "Place it on the ground by right-clicking. Furnaces can be powered with coal, " +
                        "wood or other burnable objects. You will need this to cook food and smelt ores.",
                p -> hasItemCount(p, "cobblestone", 8),
                p -> hasItem(p, "furnace")
        ));

        GOALS.add(new Goal(
                "Cook some Food",
                "Place a Furnace, put raw meat in the top slot and coal or wood in the bottom. Wait for it to cook.",
                "Right-click your Furnace to open it. Place raw meat in the top slot " +
                        "and coal or wood in the bottom slot as fuel. Wait for the arrow to fill — " +
                        "your food will appear in the slot on the right. Click it to collect it. " +
                        "Eating cooked food restores more hunger than raw food.",
                p -> hasItem(p, "furnace"),
                p -> hasItem(p, "cooked_beef", "cooked_porkchop", "cooked_chicken",
                        "cooked_salmon", "bread", "cooked_mutton")
        ));

        GOALS.add(new Goal(
                "Build a Bed",
                "Kill 3 sheep for wool (or shear them) and combine with 3 planks on a Crafting Table.",
                "Find some sheep and right-click them with shears to collect wool, or kill " +
                        "them with your sword (left-click). You need 3 wool of any colour. " +
                        "At your Crafting Table, place 3 wool in a row across the top and 3 planks " +
                        "in a row below to craft a bed. Place it and right-click it at night to sleep " +
                        "and set your respawn point.",
                p -> hasItem(p, "stone_sword"),
                p -> hasItem(p, "white_bed", "red_bed", "orange_bed", "yellow_bed",
                        "green_bed", "blue_bed", "purple_bed", "pink_bed",
                        "black_bed", "gray_bed", "light_gray_bed", "cyan_bed",
                        "light_blue_bed", "lime_bed", "magenta_bed", "brown_bed")
        ));

        // ── Tier 2: Iron age ──────────────────────────────────────────────
        GOALS.add(new Goal(
                "Mine Iron Ore",
                "Dig down to Y=16-56 or look for a cave. Look for orange-flecked stone. Mine with a stone pickaxe or better.",
                "Dig down to Y=16-56 or look for a cave. Look for orange-flecked stone. " +
                        "Mine with a stone pickaxe or better. Press F3 to see your Y coordinate. " +
                        "Watch out for lava — carry a water bucket if you have one.",
                p -> hasItem(p, "stone_pickaxe"),
                p -> hasItemCount(p, "raw_iron", 8)
        ));

        GOALS.add(new Goal(
                "Smelt Iron Ingots",
                "Put raw iron in the top slot of your Furnace and coal or wood in the bottom. You need at least 15 ingots.",
                "Right-click your Furnace and place raw iron in the top slot and coal or wood " +
                        "in the bottom slot. Wait for each piece to smelt. You will need at least 15 iron ingots " +
                        "to craft the tools and armour in the next step.",
                p -> hasItemCount(p, "raw_iron", 8),
                p -> hasItemCount(p, "iron_ingot", 15)
        ));

        GOALS.add(new Goal(
                "Craft Iron Tools and Armour",
                "Use your iron ingots to craft at least an iron pickaxe, sword, chestplate and helmet.",
                "Open your Crafting Table. Use the Recipe Book (book icon) to find the recipes for " +
                        "an iron pickaxe, sword, chestplate, and helmet. Wearing armour reduces damage taken " +
                        "from mobs and other hazards. Drag armour pieces onto your character in the inventory screen.",
                p -> hasItemCount(p, "iron_ingot", 15),
                p -> hasItem(p, "iron_pickaxe") && hasItem(p, "iron_sword")
                        && hasItem(p, "iron_chestplate") && hasItem(p, "iron_helmet")
        ));

        // ── Tier 3: Mid game ──────────────────────────────────────────────
        GOALS.add(new Goal(
                "Mine Diamonds",
                "Head deep underground to around Y=-58. Bring lots of torches and your iron pickaxe.",
                "Head deep underground to around Y=-58. Press F3 to check your Y level. " +
                        "Bring lots of torches and your iron pickaxe. Mine carefully and watch for lava. " +
                        "Try to collect at least 10 diamonds.",
                p -> hasItem(p, "iron_pickaxe"),
                p -> hasItemCount(p, "diamond", 10)
        ));

        GOALS.add(new Goal(
                "Craft Diamond Tools and Armour",
                "Use your diamonds to craft at least a diamond pickaxe, sword, chestplate and helmet.",
                "Open your Crafting Table and use the Recipe Book to craft a diamond pickaxe, " +
                        "sword, chestplate, and helmet. Diamond gear is significantly stronger than iron " +
                        "and will last much longer. Equip your armour by dragging it onto your character " +
                        "in the inventory screen.",
                p -> hasItemCount(p, "diamond", 10),
                p -> hasItem(p, "diamond_pickaxe") && hasItem(p, "diamond_sword")
                        && hasItem(p, "diamond_chestplate")
        ));

        GOALS.add(new Goal(
                "Build an Enchanting Table",
                "Craft using 4 obsidian, 2 diamonds, and 1 book. Mine obsidian where water meets lava.",
                "Mine obsidian where water meets a lava source — you need a diamond pickaxe. " +
                        "Collect 4 obsidian, 2 diamonds, and craft a book (3 paper + 1 leather). " +
                        "Open your Crafting Table and use the Recipe Book to craft the Enchanting Table. " +
                        "Place it on the ground and right-click to use it.",
                p -> hasItem(p, "diamond_pickaxe"),
                p -> hasItem(p, "enchanting_table")
        ));

        GOALS.add(new Goal(
                "Enchant your gear",
                "Right-click your Enchanting Table. Spend XP and lapis lazuli to enchant tools and armour.",
                "Right-click your Enchanting Table to open it. Place a tool or piece of armour " +
                        "in the left slot and lapis lazuli in the right slot. Three enchantment options " +
                        "will appear — hover over them to see what they do. Click one to apply it. " +
                        "Lapis lazuli can be mined underground. When you are happy, type /guidance next to move on.",
                p -> hasItem(p, "enchanting_table"),
                p -> false
        ));

        // ── Tier 4: Nether ────────────────────────────────────────────────
        GOALS.add(new Goal(
                "Enter the Nether",
                "Build a frame of 10+ obsidian (4 wide x 5 tall) and light it with flint and steel.",
                "Build a rectangular frame of obsidian at least 4 blocks wide and 5 blocks tall " +
                        "(hollow inside). Right-click the inside of the frame with flint and steel to light it. " +
                        "Step into the purple portal to travel to the Nether. Once you are there, type /guidance next to move on.",
                p -> hasItem(p, "diamond_sword") && hasItem(p, "flint_and_steel"),
                p -> false
        ));

        GOALS.add(new Goal(
                "Find a Nether Fortress",
                "Explore the Nether to find a large dark brick structure.",
                "Explore the Nether by walking in one direction. Look for a large structure made " +
                        "of dark Nether Brick — it will have towers, bridges, and corridors. " +
                        "Be careful of Ghasts (large white floating mobs that shoot fireballs) and Blazes. " +
                        "Once you find the fortress, type /guidance next to move on.",
                p -> hasItem(p, "diamond_sword"),
                p -> false
        ));

        GOALS.add(new Goal(
                "Collect Blaze Rods",
                "Kill Blazes inside the Nether Fortress. You need at least 6 Blaze Rods.",
                "Blazes are yellow flying mobs found inside the Nether Fortress near spawners. " +
                        "They shoot fireballs — dodge them and attack with your sword. " +
                        "Each Blaze drops Blaze Rods when killed. You need at least 6. " +
                        "Snowballs also deal damage to Blazes if you have any.",
                p -> hasItem(p, "diamond_sword"),
                p -> hasItemCount(p, "blaze_rod", 6)
        ));

        GOALS.add(new Goal(
                "Collect Ender Pearls",
                "Kill Endermen to collect Ender Pearls. You need at least 12.",
                "Endermen are tall dark mobs found in the Nether or Overworld. " +
                        "Do not look directly at their face or they will attack. " +
                        "Kill them with your sword to collect Ender Pearls. You need at least 12. " +
                        "Wearing a carved pumpkin on your head lets you look at them safely.",
                p -> hasItemCount(p, "blaze_rod", 6),
                p -> hasItemCount(p, "ender_pearl", 12)
        ));

        GOALS.add(new Goal(
                "Craft Eyes of Ender",
                "Craft Blaze Powder from Blaze Rods, then combine each with an Ender Pearl.",
                "Open your Crafting Table. Place a Blaze Rod in the grid to craft Blaze Powder " +
                        "(1 rod = 2 powder). Then place one Blaze Powder next to one Ender Pearl to craft " +
                        "an Eye of Ender. You need 12 Eyes of Ender in total.",
                p -> hasItemCount(p, "blaze_rod", 6) && hasItemCount(p, "ender_pearl", 12),
                p -> hasItemCount(p, "ender_eye", 12)
        ));

        GOALS.add(new Goal(
                "Brew Potions",
                "Build a Brewing Stand and brew healing and strength potions for the final fight.",
                "Craft a Brewing Stand using 1 Blaze Rod and 3 Cobblestone. Right-click it to open. " +
                        "Place Blaze Powder in the top-left fuel slot. Add water bottles in the bottom slots " +
                        "and Nether Wart in the top to start brewing. This step is optional but will make " +
                        "the final boss fight much easier. Type /guidance next when ready.",
                p -> hasItemCount(p, "blaze_rod", 2),
                p -> hasItem(p, "potion")
        ));

        // ── Tier 5: End game ──────────────────────────────────────────────
        GOALS.add(new Goal(
                "Find the Stronghold",
                "Throw an Eye of Ender and follow it — it flies toward the Stronghold.",
                "Hold an Eye of Ender and right-click to throw it. It will fly in the direction " +
                        "of the nearest Stronghold. Follow it and throw another when needed. " +
                        "When it drops into the ground, dig down to find the Stronghold beneath. " +
                        "Eyes may break when thrown so keep a good supply. Type /guidance next when you arrive.",
                p -> hasItemCount(p, "ender_eye", 12),
                p -> false
        ));

        GOALS.add(new Goal(
                "Activate the End Portal",
                "Find the End Portal room in the Stronghold and fill all 12 frame blocks with Eyes of Ender.",
                "Explore the Stronghold until you find a room with a square frame of blocks over a pool of lava. " +
                        "Right-click each frame block with an Eye of Ender to fill it. Once all 12 are filled " +
                        "the portal will activate. Step into the portal when you are ready to face the final boss. " +
                        "Type /guidance next once you are through.",
                p -> hasItemCount(p, "ender_eye", 12),
                p -> false
        ));

        GOALS.add(new Goal(
                "Defeat the Ender Dragon",
                "Destroy all End Crystals on the pillars first, then attack the dragon.",
                "When you arrive in The End, look for tall obsidian pillars with glowing crystals on top. " +
                        "Shoot the crystals with arrows or climb the pillars and break them to stop the dragon healing. " +
                        "Once all crystals are destroyed, the dragon will fly in circles over the central portal. " +
                        "Shoot it with arrows when it is flying and attack with your sword when it descends. " +
                        "When defeated, a portal home and the Dragon Egg will appear. Type /guidance next to continue.",
                p -> hasItemCount(p, "ender_eye", 12),
                p -> false
        ));

        GOALS.add(new Goal(
                "Collect the Dragon Egg",
                "Right-click the Dragon Egg to move it, then collect it.",
                "After defeating the Ender Dragon, a Dragon Egg appears on top of the End Portal. " +
                        "You cannot mine it directly as it will teleport away. Instead, right-click it once to move it " +
                        "to a nearby location, then place a torch on the block below it and break that block — " +
                        "the egg will fall onto the torch and drop as a collectible item. " +
                        "This is a trophy marking completion of the main game.",
                p -> false,
                p -> hasItem(p, "dragon_egg")
        ));

        GOALS.add(new Goal(
                "Find an End City",
                "Explore The End to find tall purple towers called End Cities.",
                "After defeating the Ender Dragon, return to The End through the portal. " +
                        "Walk away from the central island — you will need to cross a large void gap. " +
                        "Build a bridge or use Ender Pearls to reach the outer islands. " +
                        "Look for tall purple structures called End Cities. They contain excellent loot " +
                        "including Elytra (wings that let you fly) and Shulker Boxes (portable storage).",
                p -> hasItem(p, "dragon_egg"),
                p -> false
        ));

        GOALS.add(new Goal(
                "Find an Elytra",
                "Search End Ships attached to End Cities for an Elytra — wings that let you glide.",
                "End Cities sometimes have a smaller floating ship attached to them called an End Ship. " +
                        "Inside the End Ship you will find an Elytra displayed on an item frame on the wall. " +
                        "Right-click it to collect it. Equip it in your chest armour slot and jump from a height " +
                        "then press Space to glide. Use Fireworks to fly while gliding.",
                p -> hasItem(p, "dragon_egg"),
                p -> hasItem(p, "elytra")
        ));

        GOALS.add(new Goal(
                "You have completed Minecraft!",
                "You have seen everything the base game has to offer. The world is yours to explore freely.",
                "Congratulations! You have completed all of the main content Minecraft has to offer. " +
                        "From here the game is entirely yours — you could build something creative, " +
                        "try a harder difficulty, explore more of the world, or start a new adventure. " +
                        "Well done for getting this far!",
                p -> hasItem(p, "elytra"),
                p -> false
        ));
    }

    private static final Map<UUID, Integer> playerProgress = new HashMap<>();

    public static String getCurrentGoal(Player player) {
        return getActiveGoal(player).title;
    }

    public static String getCurrentGoalDetail(Player player) {
        return getActiveGoal(player).shortDetail;
    }

    public static String getCurrentGoalLongDetail(Player player) {
        return getActiveGoal(player).longDetail;
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
                "You have completed Minecraft!",
                "You have seen everything the base game has to offer. The world is yours to explore freely.",
                "Congratulations! You have completed all of the main content Minecraft has to offer. " +
                        "From here the game is entirely yours — build, explore, or challenge yourself further.",
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
        return ForgeRegistries.ITEMS.getValue(
                ResourceLocation.fromNamespaceAndPath(
                        fullPath.split(":")[0],
                        fullPath.split(":")[1]
                )
        );
    }

    private static class Goal {
        final String title;
        final String shortDetail;
        final String longDetail;
        final Predicate<Player> isUnlocked;
        final Predicate<Player> isComplete;

        Goal(String title, String shortDetail, String longDetail,
             Predicate<Player> isUnlocked, Predicate<Player> isComplete) {
            this.title       = title;
            this.shortDetail = shortDetail;
            this.longDetail  = longDetail;
            this.isUnlocked  = isUnlocked;
            this.isComplete  = isComplete;
        }
    }
}