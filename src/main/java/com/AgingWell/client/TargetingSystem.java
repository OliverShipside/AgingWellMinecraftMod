package com.AgingWell.client;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


/**
 * Target-lock system.
 *
 * When active, rotates the local player's view each tick to keep it pointed
 * at the locked target entity.  Lock is automatically released when:
 *   - The target dies or despawns
 *   - The target moves out of range (>32 blocks)
 *   - The player presses the T hotkey again
 *   - The player types /automate stop
 *
 * Activate via the T hotkey (KeyBindings) or {@link #lockOnNearest(Player)}.
 */
public class TargetingSystem {

    private static final TargetingSystem INSTANCE = new TargetingSystem();
    public static TargetingSystem getInstance() { return INSTANCE; }

    private static final double MAX_RANGE = 32.0;

    private Entity  lockedTarget = null;
    private boolean active       = false;

    private TargetingSystem() {}

    public void toggleLock() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        if (com.AgingWell.automation.BaritoneAdapter.isActive(player)) {
            send(player, "\u00a7eTarget lock is unavailable while automation is running.");
            send(player, "\u00a77Type /automate stop first.");
            return;
        }

        if (active && lockedTarget != null) {
            releaseLock(player, "Target lock released.");
        } else {
            lockOnNearest(player);
        }
    }

    public void lockOnNearest(Player player) {
        Entity nearest = findNearestHostile(player);
        if (nearest == null) {
            send(player, "\u00a7eNo hostile mobs found within " + (int) MAX_RANGE + " blocks.");
            return;
        }
        lockedTarget = nearest;
        active       = true;
        send(player, "\u00a7aTarget locked: \u00a7f" + nearest.getName().getString()
                + " \u00a77(Press " + KeyBindings.TOGGLE_TARGET_LOCK.getTranslatedKeyMessage().getString() + " to release)");
    }

    public void clearLock() {
        lockedTarget = null;
        active       = false;
    }

    public boolean isActive() { return active; }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        if (!active || lockedTarget == null) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) { clearLock(); return; }

        if (!lockedTarget.isAlive()) {
            releaseLock(player, "\u00a7eTarget defeated! Lock released.");
            return;
        }

        if (lockedTarget.distanceTo(player) > MAX_RANGE) {
            releaseLock(player, "\u00a7eTarget out of range. Lock released.");
            return;
        }

        rotateToward(mc, player, lockedTarget);
    }

    private void rotateToward(Minecraft mc, Player player, Entity target) {
        Vec3 eye  = player.getEyePosition();
        Vec3 tEye = target.getEyePosition();

        double dx = tEye.x - eye.x;
        double dy = tEye.y - eye.y;
        double dz = tEye.z - eye.z;
        double hd = Math.sqrt(dx * dx + dz * dz);

        float wantYaw   = (float) Math.toDegrees(Math.atan2(-dx, dz));
        float wantPitch = (float) Math.toDegrees(-Math.atan2(dy, hd));

        float curYaw = player.getYRot();
        float delta  = wrapDegrees(wantYaw - curYaw);

        float newYaw   = curYaw + delta * 0.30f;
        float newPitch = player.getXRot() + (wantPitch - player.getXRot()) * 0.30f;
        newPitch = Math.max(-90f, Math.min(90f, newPitch));

        player.setYRot(newYaw);
        player.setXRot(newPitch);

        if (mc.getCameraEntity() == player) {
            mc.getCameraEntity().setYRot(newYaw);
            mc.getCameraEntity().setXRot(newPitch);
        }
    }

    private Entity findNearestHostile(Player player) {
        Entity nearest = null;
        double minDist = MAX_RANGE;

        for (Entity e : player.level().getEntities(player,
                player.getBoundingBox().inflate(MAX_RANGE))) {
            if (!(e instanceof Monster)) continue;
            if (!e.isAlive()) continue;
            double d = e.distanceTo(player);
            if (d < minDist) { minDist = d; nearest = e; }
        }
        return nearest;
    }

    private void releaseLock(Player player, String msg) {
        lockedTarget = null;
        active       = false;
        send(player, msg);
    }

    private static float wrapDegrees(float d) {
        d %= 360f;
        if (d >= 180f) d -= 360f;
        if (d < -180f) d += 360f;
        return d;
    }

    private static void send(Player player, String msg) {
        player.sendSystemMessage(Component.literal(msg));
    }
}