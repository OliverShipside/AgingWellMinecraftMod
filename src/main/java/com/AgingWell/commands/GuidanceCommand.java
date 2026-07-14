package com.AgingWell.commands;

import com.AgingWell.client.GuidanceOverlay;
import com.AgingWell.client.KeyBindings;
import com.AgingWell.guidance.GoalAdvisor;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

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

                        .executes(GuidanceCommand::showCurrentGoal)
        );
    }

    private static int showCurrentGoal(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        String goal   = GoalAdvisor.getCurrentGoal(player);
        String detail = GoalAdvisor.getCurrentGoalLongDetail(player);

        send(player, "\u00a7e\u00a7l=== Current Goal ===");
        send(player, "\u00a7f" + goal);
        if (detail != null && !detail.isEmpty()) {
            send(player, "\u00a77" + detail);
        }
        send(player, "\u00a77Tip: use \u00a7f/guidance toggle\u00a77 to pin this to your screen.");
        return 1;
    }

    private static int toggleOverlay(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        boolean nowVisible = GuidanceOverlay.getInstance().toggle();
        send(player, nowVisible
                ? "\u00a7aGuidance overlay shown. It will appear in the top-left of your screen."
                : "\u00a7eGuidance overlay hidden. Type /guidance to check goals in chat.");
        return 1;
    }

    private static int nextGoal(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        GoalAdvisor.advanceGoal(player);
        String next = GoalAdvisor.getCurrentGoal(player);
        send(player, "\u00a7aMoving on! New goal:");
        send(player, "\u00a7f" + next);
        return 1;
    }

    private static int showHelp(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        send(player, "\u00a7e\u00a7l=== Guidance Help ===");
        send(player, "\u00a7fThe guidance system suggests what to do next in Minecraft.");
        send(player, "\u00a7f/guidance         \u00a77\u2014 show your current goal in chat");
        send(player, "\u00a7f/guidance toggle  \u00a77\u2014 pin/unpin the goal display on-screen");
        send(player, "\u00a7f/guidance next    \u00a77\u2014 skip to the next suggested goal");
        send(player, "\u00a7f/guidance help    \u00a77\u2014 show this message");
        send(player, " ");
        send(player, "\u00a77Goals are based on your inventory and progress.");
        send(player, "\u00a77You can also press \u00a7f" +
                KeyBindings.TOGGLE_GUIDANCE.getTranslatedKeyMessage().getString() +
                "\u00a77 to toggle the overlay.");
        return 1;
    }

    private static void send(Player player, String message) {
        player.sendSystemMessage(Component.literal(message));
    }
}