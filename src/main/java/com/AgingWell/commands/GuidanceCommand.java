package com.AgingWell.commands;

import com.AgingWell.client.GuidanceOverlay;
import com.AgingWell.guidance.GoalAdvisor;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

/**
 * /guidance command — shows the player what they should do next.
 *
 *   /guidance         — print the current suggested goal(s) to chat
 *   /guidance toggle  — show/hide the persistent on-screen Guidance Overlay HUD
 *   /guidance next    — skip the current goal and suggest the next one
 *   /guidance help    — show the full guidance help message
 */
public class GuidanceCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                Commands.literal("guidance")

                        .then(Commands.literal("toggle")
                                .executes(GuidanceCommand::toggleOverlay))

                        .then(Commands.literal("next")
                                .executes(GuidanceCommand::nextGoal))

                        .then(Commands.literal("help")
                                .executes(GuidanceCommand::showHelp))

                        // /guidance with no args — show current goal in chat
                        .executes(GuidanceCommand::showCurrentGoal)
        );
    }

    // -----------------------------------------------------------------------

    private static int showCurrentGoal(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        String goal = GoalAdvisor.getCurrentGoal(player);
        String detail = GoalAdvisor.getCurrentGoalDetail(player);

        send(player, "§e§l=== Current Goal ===");
        send(player, "§f" + goal);
        if (detail != null && !detail.isEmpty()) {
            send(player, "§7" + detail);
        }
        send(player, "§7Tip: use §f/guidance toggle§7 to pin this to your screen.");
        return 1;
    }

    private static int toggleOverlay(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        boolean nowVisible = GuidanceOverlay.getInstance().toggle();
        send(player, nowVisible
                ? "§aGuidance overlay shown. It will appear in the top-left of your screen."
                : "§eGuidance overlay hidden. Type /guidance to check goals in chat.");
        return 1;
    }

    private static int nextGoal(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        GoalAdvisor.advanceGoal(player);
        String next = GoalAdvisor.getCurrentGoal(player);
        send(player, "§aMoving on! New goal:");
        send(player, "§f" + next);
        return 1;
    }

    private static int showHelp(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        send(player, "§e§l=== Guidance Help ===");
        send(player, "§fThe guidance system suggests what to do next in Minecraft.");
        send(player, "§f/guidance         §7— show your current goal in chat");
        send(player, "§f/guidance toggle  §7— pin/unpin the goal display on-screen");
        send(player, "§f/guidance next    §7— skip to the next suggested goal");
        send(player, "§f/guidance help    §7— show this message");
        send(player, " ");
        send(player, "§7Goals are based on your inventory and achievements.");
        send(player, "§7You can also press §fG§7 (default) to toggle the overlay.");
        return 1;
    }

    private static void send(Player player, String message) {
        player.sendSystemMessage(Component.literal(message));
    }
}