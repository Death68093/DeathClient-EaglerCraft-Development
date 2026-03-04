package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.Mod;

public class ToggleSprint extends Mod {

    public ToggleSprint() {
        // False for isCheat, because Toggle Sprint is generally allowed in "Legit" mode
        super("ToggleSprint", "Keeps you sprinting continuously.", Category.MOVEMENT, false);
    }

    @Override
    public void onUpdate(EventUpdate event) {
        // If the player is moving forward, not colliding with a wall, and not sneaking
        if (mc.thePlayer.moveForward > 0 && !mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isSneaking()) {
            mc.thePlayer.setSprinting(true);
        }
    }

    @Override
    protected void onDisable() {
        mc.thePlayer.setSprinting(false);
    }
}