package com.AgingWell;

import com.AgingWell.commands.InfoCommand;
import com.AgingWell.commands.AutomationCommand;
import com.AgingWell.commands.GuidanceCommand;
import com.AgingWell.client.KeyBindings;
import com.AgingWell.client.GuidanceOverlay;
import com.AgingWell.client.TargetingSystem;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


/**
 * AgingWell Assistance Mod
 *
 * Provides in-game help, automation, targeting, and guidance features
 * to make Minecraft more accessible for older or less experienced players.
 *
 * Features:
 *   - /info command   : describe held item, looked-at block, or named entity/block
 *   - /automate command : issue plain-language automation tasks via Baritone
 *   - /guidance command : show next steps / goals based on progress
 *   - Hotkey (default G) : toggle the Guidance Overlay HUD
 *   - Hotkey (default T) : lock camera onto the nearest hostile mob
 */
@Mod(AgingWell.MODID)
public final class AgingWell {

    public static final String MODID = "agingwellassistancemod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public AgingWell(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(KeyBindings::onRegisterKeyMappings);
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
        LOGGER.info("[AgingWell] Mod initialising...");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("[AgingWell] Common setup complete.");
    }

    public void onRegisterCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        InfoCommand.register(dispatcher);
        AutomationCommand.register(dispatcher);
        GuidanceCommand.register(dispatcher);
        LOGGER.info("[AgingWell] Commands registered.");
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MinecraftForge.EVENT_BUS.register(GuidanceOverlay.getInstance());
            MinecraftForge.EVENT_BUS.register(TargetingSystem.getInstance());
            LOGGER.info("[AgingWell] Client setup complete.");
        }
    }
}