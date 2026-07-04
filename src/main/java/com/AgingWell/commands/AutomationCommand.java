package com.AgingWell.commands;

import com.AgingWell.automation.BaritoneAdapter;
import com.AgingWell.ResourceAliases;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

/**
 * /automate command — converts plain-language requests into Baritone actions.
 *
 * Usage examples:
 *   /automate mine diamonds
 *   /automate go home
 *   /automate gather wood
 *   /automate go to 100 64 -200
 *   /automate stop
 *
 * The command parses the free-text argument and maps it to a BaritoneAdapter
 * method.  Because Baritone is a client-side library, all actual path-finding
 * and mining logic runs on the client thread via the adapter.
 */
public class AutomationCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal("automate")

                        // /automate stop  — cancel any current Baritone task
                        .then(Commands.literal("stop")
                                .executes(AutomationCommand::stopAutomation))

                        // /automate status — report what Baritone is currently doing
                        .then(Commands.literal("status")
                                .executes(AutomationCommand::statusAutomation))

                        // /automate <free text>  — parse and dispatch
                        .then(Commands.argument("task", StringArgumentType.greedyString())
                                .executes(ctx -> handleTask(ctx,
                                        StringArgumentType.getString(ctx, "task"))))

                        // /automate with no args — show help
                        .executes(AutomationCommand::showHelp)
        );
    }

    // -----------------------------------------------------------------------
    // Handlers
    // -----------------------------------------------------------------------

    private static int handleTask(CommandContext<CommandSourceStack> ctx, String task) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        String lower = task.toLowerCase().trim();

        // --- Navigation ---
        if (lower.startsWith("go to") || lower.startsWith("walk to") || lower.startsWith("navigate to")) {
            String coords = lower.replaceFirst("(go to|walk to|navigate to)\\s*", "").trim();
            return handleGoTo(player, coords);
        }

        if (lower.equals("go home") || lower.equals("go to home") || lower.equals("home")) {
            return handleGoHome(player);
        }

        // --- Mining ---
        if (lower.startsWith("mine ") || lower.startsWith("dig ")) {
            String target = lower.replaceFirst("(mine|dig)\\s*", "").trim();
            return handleMine(player, target);
        }

        // --- Gathering ---
        if (lower.startsWith("gather ") || lower.startsWith("collect ") || lower.startsWith("get ")) {
            String resource = lower.replaceFirst("(gather|collect|get)\\s*", "").trim();
            return handleGather(player, resource);
        }

        // --- Following ---
        if (lower.startsWith("follow")) {
            return handleFollow(player);
        }

        // --- Surface / escape ---
        if (lower.contains("surface") || lower.contains("get out") || lower.contains("go up")) {
            return handleSurface(player);
        }

        send(player, "§cI didn't understand \"" + task + "\".");
        send(player, "§7Type /automate for a list of things I can do.");
        return 0;
    }

    private static int handleGoTo(Player player, String coords) {
        String[] parts = coords.split("\\s+");
        if (parts.length == 3) {
            try {
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                double z = Double.parseDouble(parts[2]);
                send(player, "§aHeading to (" + (int)x + ", " + (int)y + ", " + (int)z + ")...");
                BaritoneAdapter.goToCoordinates(player, x, y, z);
                return 1;
            } catch (NumberFormatException ignored) { }
        }
        send(player, "§cPlease give three numbers, e.g.  /automate go to 100 64 -200");
        return 0;
    }

    private static int handleGoHome(Player player) {
        send(player, "§aNavigating to world spawn...");
        BaritoneAdapter.goToSpawn(player);
        return 1;
    }

    private static int handleMine(Player player, String target) {
        String blockId = ResourceAliases.resolveBlock(target);
        if (blockId == null) {
            send(player, "§cI don't know how to mine \"" + target + "\". "
                    + "Try something like: diamonds, iron, coal, stone.");
            return 0;
        }
        send(player, "§aLooking for " + target + " to mine...");
        BaritoneAdapter.mineBlock(player, blockId);
        return 1;
    }

    private static int handleGather(Player player, String resource) {
        String blockId = ResourceAliases.resolveBlock(resource);
        if (blockId == null) {
            send(player, "§cI don't know how to gather \"" + resource + "\". "
                    + "Try: wood, logs, sand, gravel, dirt.");
            return 0;
        }
        send(player, "§aGathering " + resource + "...");
        BaritoneAdapter.mineBlock(player, blockId);
        return 1;
    }

    private static int handleFollow(Player player) {
        send(player, "§aFollowing the nearest player...");
        BaritoneAdapter.followClosestPlayer(player);
        return 1;
    }

    private static int handleSurface(Player player) {
        send(player, "§aHeading to the surface...");
        BaritoneAdapter.goToSurface(player);
        return 1;
    }

    private static int stopAutomation(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;
        send(player, "§eAutomation stopped.");
        BaritoneAdapter.stop(player);
        return 1;
    }

    private static int statusAutomation(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;
        String status = BaritoneAdapter.getStatus(player);
        send(player, "§7Automation status: §f" + status);
        return 1;
    }

    private static int showHelp(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;
        send(player, "§e§l=== Automation Commands ===");
        send(player, "§f/automate go to <x> <y> <z>  §7— Walk to coordinates");
        send(player, "§f/automate go home             §7— Navigate to world spawn");
        send(player, "§f/automate mine <material>     §7— Mine for a resource (e.g. diamonds, iron)");
        send(player, "§f/automate gather <material>   §7— Collect surface resources (e.g. wood)");
        send(player, "§f/automate follow              §7— Follow the nearest other player");
        send(player, "§f/automate surface             §7— Find a path back to the surface");
        send(player, "§f/automate stop                §7— Cancel whatever is running");
        send(player, "§f/automate status              §7— Check what automation is active");
        return 1;
    }

    // -----------------------------------------------------------------------
    // Utility
    // -----------------------------------------------------------------------

    private static void send(Player player, String message) {
        player.sendSystemMessage(Component.literal(message));
    }
}