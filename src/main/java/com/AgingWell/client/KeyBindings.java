package com.AgingWell.client;

import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
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

    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_GUIDANCE);
        event.register(TOGGLE_TARGET_LOCK);
        MinecraftForge.EVENT_BUS.addListener(KeyBindings::onKeyInput);
        LOGGER.info("[AgingWell] Keybindings registered.");
    }

    private static void onKeyInput(InputEvent.Key event) {
        while (TOGGLE_GUIDANCE.consumeClick()) {
            GuidanceOverlay.getInstance().toggle();
        }
        while (TOGGLE_TARGET_LOCK.consumeClick()) {
            TargetingSystem.getInstance().toggleLock();
        }
    }
}