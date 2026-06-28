package com.AgingWell.info;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

/**
 * Provides plain-language descriptions and combat/interaction tips for entities.
 */
public class EntityInfoDatabase {

    private static final Map<String, String> DESCRIPTIONS = new HashMap<>();
    private static final Map<String, List<String>> TIPS     = new HashMap<>();

    static {
        // --- Passive mobs ---
        desc("cow",         "A peaceful cow. Drops raw beef and leather when killed. Can be bred with wheat.");
        desc("sheep",       "A peaceful sheep. Drops wool when sheared (no harm!) or raw mutton when killed. Breed with wheat.");
        desc("pig",         "A peaceful pig. Drops raw porkchop. Breed with carrots, beetroot, or potatoes.");
        desc("chicken",     "A small peaceful bird. Drops raw chicken and feathers. Lays eggs. Breed with seeds.");
        desc("horse",       "Can be tamed and ridden. Approach slowly, keep right-clicking to tame. Add a saddle to ride.");
        desc("donkey",      "Like a horse, but can carry a chest of items for you. Very useful for hauling gear.");
        desc("villager",    "Trades items for emeralds. Each profession offers different goods. Protect them!");
        desc("cat",         "Tame with raw fish. Scares away Creepers and Phantoms. Great base companion.");
        desc("wolf",        "Tame with bones to make a loyal dog that attacks enemies. Heal with any meat.");
        desc("bee",         "Pollinates flowers and crops. Produces honey. Don't hit one — the whole hive will attack!");
        desc("axolotl",     "Lives in water. Will attack drowned and guardians for you. Carry in a bucket.");
        desc("fox",         "Shy creature. Breed two foxes to get a baby fox that trusts you from birth.");
        desc("rabbit",      "Small and fast. Drops rabbit hide and raw rabbit. Can be bred with dandelions or carrots.");
        desc("turtle",      "Lays eggs on beaches. Drops scutes when they grow up, used to craft a turtle helmet.");

        // --- Hostile mobs ---
        desc("zombie",      "A slow undead mob that attacks you. Burns in sunlight. Drops rotten flesh.");
        desc("skeleton",    "Fires arrows at range. Burns in sunlight. Drops bones and arrows. Watch for headshots!");
        desc("creeper",     "Approaches silently and EXPLODES near you. Very dangerous. Keep distance or use a sword.");
        desc("spider",      "Climbs walls. Attacks at night or in darkness. Drops string and spider eyes.");
        desc("enderman",    "Tall dark mob. Only attacks if you look directly at its face. Drops ender pearls.");
        desc("witch",       "Throws harmful potions. Drink milk to cure their effects. Drops potions and glowstone.");
        desc("drowned",     "A zombie that lives underwater. Some carry tridents. Burns in sunlight.");
        desc("husk",        "A desert zombie. Does NOT burn in sunlight. Causes Hunger when it hits you.");
        desc("stray",       "A skeleton found in cold biomes. Shoots arrows that slow you. Drops slowness arrows.");
        desc("phantom",     "Attacks players who haven't slept in 3+ days. Drops membranes for Slow Falling potion.");
        desc("slime",       "Bouncy green mob found in swamps or deep underground. Splits into smaller slimes. Drops slimeballs.");
        desc("magma_cube",  "The Nether version of a slime. Found near lava. Drops magma cream.");
        desc("blaze",       "Flies and shoots fireballs in the Nether. Drops blaze rods — essential for potions.");
        desc("ghast",       "Large flying mob in the Nether that shoots explosive fireballs. Deflect them with a sword!");
        desc("wither_skeleton", "Tough skeleton in the Nether. Causes Wither effect on hit. Drops wither skeleton skulls.");
        desc("cave_spider", "Smaller, faster spider found in mineshafts. Its bite is venomous. Use milk to cure poison.");
        desc("silverfish",  "Tiny mob that hides in stone blocks near strongholds. Calls others when attacked.");
        desc("guardian",    "Underwater fish-like mob near ocean monuments. Fires a laser beam. Drops prismarine.");
        desc("elder_guardian", "Boss version of the guardian. Gives you Mining Fatigue, making mining very slow.");
        desc("endermite",   "Tiny hostile mob occasionally spawned by ender pearls. Endermen attack them.");
        desc("shulker",     "Found in End Cities. Shoots projectiles that cause Levitation. Drops shulker shells.");
        desc("pillager",    "Humanoid raider that shoots crossbows. Part of Raids. Drops crossbows and arrows.");
        desc("ravager",     "Large beast in Raids. Very tough — get some distance and fight carefully.");

        // --- Boss mobs ---
        desc("ender_dragon", "THE final boss of Minecraft. Lives in The End. Destroy the End Crystals on the pillars first, then attack.");
        desc("wither",      "A player-summoned boss. Extremely dangerous. Has 3 heads that fire explosive skulls.");

        // --- Tips ---
        tip("creeper",      "Back away the moment you hear the hissing sound — you have about 1.5 seconds.");
        tip("creeper",      "Cats scare creepers away. Keep a tamed cat nearby.");
        tip("skeleton",     "Rush up close — skeletons can't easily shoot you at point blank.");
        tip("skeleton",     "Use a shield to block the skeletons arrows");
        tip("enderman",     "Wear a carved pumpkin on your head to look at endermen safely.");
        tip("zombie",       "They can break down wooden doors on Hard difficulty. Use iron doors instead.");
        tip("spider",       "Spiders cannot climb glass-smooth surfaces (like glass panes).");
        tip("blaze",        "Snowballs deal damage to blazes! Bring a stack into the fortress.");
        tip("ghast",        "A ghast's fireball can be deflected back with a sword, bow, or even your fist.");
        tip("witch",        "Drink milk to remove their potion effects instantly.");
        tip("phantom",      "Sleep in a bed to reset the phantom timer and stop them spawning.");
        tip("ender_dragon", "Bring lots of arrows. Destroy all End Crystals before focusing the dragon.");
        tip("horse",        "After taming, you still need a saddle (found in dungeons/chests) to control it.");
        tip("wolf",         "A tamed wolf fights for you. Sitting wolves (right-click) won't follow you into danger.");
        tip("villager",     "Curing a zombie villager (golden apple + weakness potion) makes them greatful. They will give very cheap trades.");
    }

    // -----------------------------------------------------------------------

    public static String describe(Entity entity) {
        String id = getPath(entity);
        return DESCRIPTIONS.getOrDefault(id,
                entity.getName().getString() + " — no description available yet. "
                        + "Check the Minecraft Wiki for details on this entity.");
    }

    public static List<String> getTips(Entity entity) {
        String id = getPath(entity);
        return TIPS.getOrDefault(id, Collections.emptyList());
    }

    // -----------------------------------------------------------------------

    private static String getPath(Entity entity) {
        var key = net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        return key == null ? "" : key.getPath();
    }

    private static void desc(String path, String description) {
        DESCRIPTIONS.put(path, description);
    }

    private static void tip(String path, String tip) {
        TIPS.computeIfAbsent(path, k -> new ArrayList<>()).add(tip);
    }
}