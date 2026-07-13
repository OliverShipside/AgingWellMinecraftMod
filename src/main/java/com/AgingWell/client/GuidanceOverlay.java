package com.AgingWell.client;

import com.AgingWell.guidance.GoalAdvisor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

/**
 * Renders a small on-screen HUD in the top-left corner showing the player's
 * current goal/next step.
 *
 * Toggle with: /guidance toggle   OR   the G hotkey (default).
 *
 * The overlay is OFF by default so it does not surprise new users.
 */
public class GuidanceOverlay {

    private static final GuidanceOverlay INSTANCE = new GuidanceOverlay();
    public static GuidanceOverlay getInstance() { return INSTANCE; }

    private boolean visible = false;

    private static final int MARGIN_X    = 6;
    private static final int MARGIN_Y    = 6;
    private static final int LINE_HEIGHT = 10;
    private static final int PADDING     = 4;
    private static final int BG_COLOUR   = 0x99000000;
    private static final int TITLE_COL   = 0xFFFFDD55;
    private static final int GOAL_COL    = 0xFFFFFFFF;
    private static final int HINT_COL    = 0xFFAAAAAA;

    private GuidanceOverlay() {}

    public boolean toggle() {
        visible = !visible;
        return visible;
    }

    public boolean isVisible() { return visible; }

    // Replace onRenderGuiLayer / onRenderOverlay entirely with:
    public void onRenderOverlay(CustomizeGuiOverlayEvent.Chat event) {
        if (!visible) return;
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        net.minecraft.client.gui.GuiGraphicsExtractor graphics = event.getGuiGraphics();
        Font font = mc.font;

        String title  = "\u00a7l[ Next Goal ]";
        String goal   = GoalAdvisor.getCurrentGoal(player);
        String hint = "\u00a7oPress " + KeyBindings.TOGGLE_GUIDANCE.getTranslatedKeyMessage().getString() + " to hide  \u2022  /guidance next to skip";

        int maxWidth = Math.max(font.width(title),
                Math.max(font.width(goal), font.width(hint)));
        if (!goal.isEmpty()) {
            maxWidth = Math.max(maxWidth, font.width(goal));
        }

        boolean hasDetail = !goal.isEmpty();
        int panelW = maxWidth + PADDING * 2;
        int panelH = (hasDetail ? 4 : 3) * LINE_HEIGHT + PADDING * 2;
        int x = MARGIN_X;
        int y = MARGIN_Y;

        graphics.fill(x, y, x + panelW, y + panelH, BG_COLOUR);

        int tx = x + PADDING;
        int ty = y + PADDING;

        graphics.text(font, title,  tx, ty, TITLE_COL, false);
        ty += LINE_HEIGHT;
        graphics.text(font, goal,   tx, ty, GOAL_COL,  false);
        ty += LINE_HEIGHT;

        graphics.text(font, hint,   tx, ty, HINT_COL,  false);
    }
}