package com.AgingWell.commands;

import com.AgingWell.info.BlockInfoDatabase;
import com.AgingWell.info.EntityInfoDatabase;
import com.AgingWell.info.ItemInfoDatabase;
import com.AgingWell.info.InfoLookup;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.List;

/**
 * /info command — gives the player plain-language information about:
 *
 *   /info            — describes the item currently held in the main hand
 *   /info block      — describes the block the player is looking at
 *   /info entity     — describes the entity the player is looking at
 *   /info <name>     — looks up a specific block or item by name keyword
 *
 * All output is written to the chat so it does not interrupt gameplay.
 */
public class InfoCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("info")
                        .executes(InfoCommand::describeHeldItem)

                        .then(Commands.literal("block")
                                .executes(InfoCommand::describeLookedAtBlock))

                        .then(Commands.literal("entity")
                                .executes(InfoCommand::describeLookedAtEntity))

                        .then(Commands.argument("query", StringArgumentType.greedyString())
                                .executes(ctx ->
                                        describeByName(ctx, StringArgumentType.getString(ctx, "query"))))
        );
    }

    private static int describeHeldItem(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        ItemStack held = player.getMainHandItem();
        if (held.isEmpty()) {
            send(player, "Your hand is empty. Try picking up an item first!");
            return 1;
        }

        send(player, "\u00a7e\u00a7l" + held.getHoverName().getString() + "\u00a7r");
        send(player, ItemInfoDatabase.describe(held));

        List<String> tips = ItemInfoDatabase.getTips(held);
        if (!tips.isEmpty()) {
            send(player, "\u00a77Tips:");
            tips.forEach(tip -> send(player, "\u00a77  \u2022 " + tip));
        }
        return 1;
    }

    private static int describeLookedAtBlock(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        HitResult hit = player.pick(5.0, 0f, false);
        if (hit.getType() != HitResult.Type.BLOCK) {
            send(player, "You're not looking at a block. Point your crosshair at one and try again.");
            return 1;
        }

        BlockPos pos     = ((BlockHitResult) hit).getBlockPos();
        BlockState state = player.level().getBlockState(pos);

        send(player, "\u00a7e\u00a7lBlock: " + state.getBlock().getName().getString()
                + "\u00a7r  \u00a77(at " + pos.toShortString() + ")");
        send(player, BlockInfoDatabase.describe(state));

        List<String> tips = BlockInfoDatabase.getTips(state);
        if (!tips.isEmpty()) {
            send(player, "\u00a77Tips:");
            tips.forEach(tip -> send(player, "\u00a77  \u2022 " + tip));
        }
        return 1;
    }

    private static int describeLookedAtEntity(CommandContext<CommandSourceStack> ctx) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        HitResult blockHit = player.pick(10.0, 0f, false);
        double maxDist     = blockHit.getLocation().distanceTo(player.getEyePosition());

        Entity target = null;
        for (Entity e : player.level().getEntities(player,
                player.getBoundingBox().inflate(10.0))) {
            double dist = e.distanceTo(player);
            if (dist < maxDist) { maxDist = dist; target = e; }
        }

        if (target == null) {
            send(player, "No entity nearby. Walk closer or look directly at a creature.");
            return 1;
        }

        send(player, "\u00a7e\u00a7l" + target.getName().getString()
                + "\u00a7r  \u00a77(" + target.getType().getDescription().getString() + ")");
        send(player, EntityInfoDatabase.describe(target));

        List<String> tips = EntityInfoDatabase.getTips(target);
        if (!tips.isEmpty()) {
            send(player, "\u00a77Tips:");
            tips.forEach(tip -> send(player, "\u00a77  \u2022 " + tip));
        }
        return 1;
    }

    private static int describeByName(CommandContext<CommandSourceStack> ctx, String query) {
        Player player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        String result = InfoLookup.lookupByName(query);
        if (result == null) {
            send(player, "Sorry, I don't have information about \"" + query + "\" yet.");
            send(player, "\u00a77Try /info (held item), /info block, or /info entity.");
        } else {
            send(player, "\u00a7e\u00a7l" + query + "\u00a7r");
            send(player, result);
        }
        return 1;
    }

    private static void send(Player player, String message) {
        player.sendSystemMessage(Component.literal(message));
    }
}