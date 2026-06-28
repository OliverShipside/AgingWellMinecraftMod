package com.AgingWell;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

/**
 * AgingWell mod configuration.
 * Values are stored in config/agingwellassistancemod-common.toml
 * and can be edited there between sessions.
 */
public class Config {

    // -----------------------------------------------------------------------
    // Config spec builder
    // -----------------------------------------------------------------------

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public  static final ForgeConfigSpec         SPEC;

    // -----------------------------------------------------------------------
    // Values
    // -----------------------------------------------------------------------

    /** Log the dirt block registration key on startup (kept from original template). */
    public static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK;

    /** A magic number kept from the original template for reference. */
    public static final ForgeConfigSpec.IntValue MAGIC_NUMBER;

    /** Item list (kept from original template). */
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_LIST;

    // -----------------------------------------------------------------------
    // AgingWell-specific config
    // -----------------------------------------------------------------------

    /** Maximum range (blocks) for the target-lock system. */
    public static final ForgeConfigSpec.IntValue TARGET_LOCK_RANGE;

    /** Whether the guidance overlay starts visible when the player joins. */
    public static final ForgeConfigSpec.BooleanValue OVERLAY_ON_BY_DEFAULT;

    // -----------------------------------------------------------------------
    // Convenience accessors (kept from original template)
    // -----------------------------------------------------------------------

    public static boolean   logDirtBlock;
    public static int       magicNumber;
    public static String    magicNumberIntroduction;
    public static List<? extends String> items;

    // -----------------------------------------------------------------------
    // Static initialiser
    // -----------------------------------------------------------------------

    static {
        BUILDER.comment("General settings").push("general");

        LOG_DIRT_BLOCK = BUILDER
                .comment("If true, the registry key for the Dirt block is logged on startup.")
                .define("logDirtBlock", true);

        MAGIC_NUMBER = BUILDER
                .comment("A magic number (from the original template).")
                .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

        ITEM_LIST = BUILDER
                .comment("A list of items that the original template logged on startup.")
                .defineListAllowEmpty("items", Lists.newArrayList("minecraft:iron_ingot"),
                        obj -> obj instanceof String);

        BUILDER.pop();

        // -------------------------------------------------------------------
        BUILDER.comment("AgingWell assistance settings").push("agingwell");

        TARGET_LOCK_RANGE = BUILDER
                .comment("Maximum distance (blocks) at which target lock will acquire and track a mob.")
                .defineInRange("targetLockRange", 32, 4, 128);

        OVERLAY_ON_BY_DEFAULT = BUILDER
                .comment("If true, the guidance overlay is shown as soon as the player loads in.")
                .define("overlayOnByDefault", false);

        BUILDER.pop();

        SPEC = BUILDER.build();

        // Cache primitives for convenience (mirrors original template style)
        logDirtBlock          = true;
        magicNumber           = 42;
        magicNumberIntroduction = "The magic number is: ";
        items                 = Lists.newArrayList("minecraft:iron_ingot");
    }
}