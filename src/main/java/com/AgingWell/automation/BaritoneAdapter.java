package com.AgingWell.automation;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class BaritoneAdapter {

    private static boolean baritoneAvailable = false;

    static {
        try {
            Class.forName("baritone.api.BaritoneAPI");
            baritoneAvailable = true;
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            baritoneAvailable = false;
        }
    }

    public static void goToCoordinates(Player player, double x, double y, double z) {
        if (!checkBaritone(player)) return;
        try {
            Object baritone = getPrimaryBaritone();
            Object goal = Class.forName("baritone.api.pathing.goals.GoalBlock")
                    .getConstructor(int.class, int.class, int.class)
                    .newInstance((int) x, (int) y, (int) z);
            Object process = baritone.getClass().getMethod("getCustomGoalProcess").invoke(baritone);
            process.getClass().getMethod("setGoalAndPath",
                    Class.forName("baritone.api.pathing.goals.Goal")).invoke(process, goal);
        } catch (Throwable e) {
            handleError(player, e);
        }
    }

    public static void goToSpawn(Player player) {
        if (!checkBaritone(player)) return;
        try {
            Object baritone = getPrimaryBaritone();
            net.minecraft.core.BlockPos spawnPos = player.level().getLevelData().getSpawnPos();
            Object goal = Class.forName("baritone.api.pathing.goals.GoalBlock")
                    .getConstructor(int.class, int.class, int.class)
                    .newInstance(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
            Object process = baritone.getClass().getMethod("getCustomGoalProcess").invoke(baritone);
            process.getClass().getMethod("setGoalAndPath",
                    Class.forName("baritone.api.pathing.goals.Goal")).invoke(process, goal);
        } catch (Throwable e) {
            handleError(player, e);
        }
    }

    public static void mineBlock(Player player, String blockId) {
        if (!checkBaritone(player)) return;
        try {
            Object baritone = getPrimaryBaritone();
            Object process = baritone.getClass().getMethod("getMineProcess").invoke(baritone);
            process.getClass().getMethod("mineByName", String[].class)
                    .invoke(process, (Object) new String[]{blockId});
        } catch (Throwable e) {
            handleError(player, e);
        }
    }

    public static void followClosestPlayer(Player player) {
        if (!checkBaritone(player)) return;
        send(player, "\u00a7cFollow is not supported in this version of Baritone.");
    }

    public static void goToSurface(Player player) {
        if (!checkBaritone(player)) return;
        try {
            Object baritone = getPrimaryBaritone();
            Object goal = Class.forName("baritone.api.pathing.goals.GoalYLevel")
                    .getConstructor(int.class)
                    .newInstance(255);
            Object process = baritone.getClass().getMethod("getCustomGoalProcess").invoke(baritone);
            process.getClass().getMethod("setGoalAndPath",
                    Class.forName("baritone.api.pathing.goals.Goal")).invoke(process, goal);
        } catch (Throwable e) {
            handleError(player, e);
        }
    }

    public static void stop(Player player) {
        if (!baritoneAvailable) return;
        try {
            Object baritone = getPrimaryBaritone();
            Object pathingBehavior = baritone.getClass()
                    .getMethod("getPathingBehavior").invoke(baritone);
            pathingBehavior.getClass().getMethod("cancelEverything").invoke(pathingBehavior);
            send(player, "\u00a7e[AgingWell] Automation stopped.");
        } catch (Throwable e) {
            handleError(player, e);
        }
    }

    public static String getStatus(Player player) {
        if (!baritoneAvailable) {
            return "Baritone not installed \u2014 automation unavailable.";
        }
        try {
            Object baritone = getPrimaryBaritone();
            Object pathingBehavior = baritone.getClass()
                    .getMethod("getPathingBehavior").invoke(baritone);
            boolean isPathing = (boolean) pathingBehavior.getClass()
                    .getMethod("isPathing").invoke(pathingBehavior);
            return isPathing ? "Currently pathing..." : "Idle";
        } catch (Throwable e) {
            return "Baritone status unavailable.";
        }
    }


    private static Object getPrimaryBaritone() throws Throwable {
        Object api = Class.forName("baritone.api.BaritoneAPI")
                .getMethod("getProvider").invoke(null);
        return api.getClass().getMethod("getPrimaryBaritone").invoke(api);
    }

    private static boolean checkBaritone(Player player) {
        if (!baritoneAvailable) {
            send(player, "\u00a7c[AgingWell] Baritone is not installed.");
            send(player, "\u00a77Place baritone-api-1.11.2.jar in your mods folder.");
            return false;
        }
        return true;
    }

    private static void handleError(Player player, Throwable e) {
        send(player, "\u00a7c[AgingWell] Automation error: " + e.getMessage());
    }

    private static void send(Player player, String message) {
        player.sendSystemMessage(Component.literal(message));
    }
}