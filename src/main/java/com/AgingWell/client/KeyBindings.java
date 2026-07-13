package com.AgingWell.client;

import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

public class KeyBindings {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final KeyMapping TOGGLE_GUIDANCE = new KeyMapping(
            "key.agingwell.guidance",
            GLFW.GLFW_KEY_G,
            KeyMapping.Category.MISC
    );

    public static final KeyMapping TOGGLE_TARGET_LOCK = new KeyMapping(
            "key.agingwell.targetlock",
            GLFW.GLFW_KEY_R,
            KeyMapping.Category.MISC
    );

    public static final KeyMapping INFO = new KeyMapping(
            "key.agingwell.info",
            GLFW.GLFW_KEY_I,
            KeyMapping.Category.MISC
    );

    public static final KeyMapping GUIDANCE_SHOW = new KeyMapping(
            "key.agingwell.guidance_show",
            GLFW.GLFW_KEY_H,
            KeyMapping.Category.MISC
    );

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_GUIDANCE);
        event.register(TOGGLE_TARGET_LOCK);
        event.register(INFO);
        event.register(GUIDANCE_SHOW);

        InputEvent.Key.BUS.addListener(KeyBindings::onKeyInput);

        LOGGER.info("[AgingWell] Keybindings registered.");
    }

    private static void onKeyInput(InputEvent.Key event) {
        while (TOGGLE_GUIDANCE.consumeClick()) {
            GuidanceOverlay.getInstance().toggle();
        }
        while (TOGGLE_TARGET_LOCK.consumeClick()) {
            TargetingSystem.getInstance().toggleLock();
        }
        while (INFO.consumeClick()) {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.player != null) {
                // Check for nearby entity first
                boolean entityNearby = mc.level.getEntities(
                        mc.player,
                        mc.player.getBoundingBox().inflate(5.0)
                ).stream().anyMatch(e -> e != mc.player);

                // Check if looking at a block
                net.minecraft.world.phys.HitResult hit = mc.player.pick(5.0, 0f, false);
                boolean lookingAtBlock = hit.getType() == net.minecraft.world.phys.HitResult.Type.BLOCK;

                if (entityNearby) {
                    mc.player.connection.sendCommand("info entity");
                } else if (lookingAtBlock) {
                    mc.player.connection.sendCommand("info block");
                } else if (!mc.player.getMainHandItem().isEmpty()) {
                    mc.player.connection.sendCommand("info");
                } else {
                    mc.player.sendSystemMessage(net.minecraft.network.chat.Component.literal(
                            "\u00a7e[AgingWell] The I key describes things around you. " +
                                    "Hold an item, look at a block, or stand near a creature and press I again."
                    ));
                }
            }
        }

        while (GUIDANCE_SHOW.consumeClick()) {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.player != null) {
                mc.player.connection.sendCommand("guidance");
            }
        }
    }
}