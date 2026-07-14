package com.AgingWell.client;

import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

public class KeyBindings {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final KeyMapping TOGGLE_GUIDANCE = new KeyMapping(
            "key.agingwell.guidance",
            GLFW.GLFW_KEY_G,
            "key.categories.misc"
    );

    public static final KeyMapping TOGGLE_TARGET_LOCK = new KeyMapping(
            "key.agingwell.targetlock",
            GLFW.GLFW_KEY_R,
            "key.categories.misc"
    );

    public static final KeyMapping INFO = new KeyMapping(
            "key.agingwell.info",
            GLFW.GLFW_KEY_I,
            "key.categories.misc"
    );

    public static final KeyMapping GUIDANCE_SHOW = new KeyMapping(
            "key.agingwell.guidance_show",
            GLFW.GLFW_KEY_H,
            "key.categories.misc"
    );

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_GUIDANCE);
        event.register(TOGGLE_TARGET_LOCK);
        event.register(INFO);
        event.register(GUIDANCE_SHOW);

        MinecraftForge.EVENT_BUS.register(new KeyInputHandler());

        LOGGER.info("[AgingWell] Keybindings registered.");
    }

    public static class KeyInputHandler {

        @SubscribeEvent
        public void onKeyInput(InputEvent.Key event) {

            while (TOGGLE_GUIDANCE.consumeClick()) {
                GuidanceOverlay.getInstance().toggle();
            }

            while (TOGGLE_TARGET_LOCK.consumeClick()) {
                TargetingSystem.getInstance().toggleLock();
            }

            while (INFO.consumeClick()) {
                net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
                if (mc.player != null && mc.level != null) {

                    boolean entityNearby = mc.level.getEntities(
                            mc.player,
                            mc.player.getBoundingBox().inflate(5.0)
                    ).stream().anyMatch(e -> e != mc.player);

                    HitResult hit = mc.player.pick(5.0, 0f, false);
                    boolean lookingAtBlock = hit.getType() == HitResult.Type.BLOCK;

                    if (entityNearby) {
                        mc.player.connection.sendCommand("info entity");
                    } else if (lookingAtBlock) {
                        mc.player.connection.sendCommand("info block");
                    } else if (!mc.player.getMainHandItem().isEmpty()) {
                        mc.player.connection.sendCommand("info");
                    } else {
                        mc.player.sendSystemMessage(Component.literal(
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
}