package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.Mod;

public class Fly extends Mod {

    public Fly() {
        // True for isCheat, because this is definitely not vanilla survival behavior!
        super("Fly", "Allows you to fly like in Creative mode.", Category.MOVEMENT, true);
    }

    @Override
    protected void onEnable() {
        if (mc.thePlayer != null) {
            mc.thePlayer.capabilities.isFlying = true;
        }
    }

    @Override
    public void onUpdate(EventUpdate event) {
        // We enforce the flying state every tick. 
        // This prevents the game (or the server) from dragging you back down to the ground.
        if (mc.thePlayer != null) {
            mc.thePlayer.capabilities.isFlying = true;
        }
    }

    @Override
    protected void onDisable() {
        if (mc.thePlayer != null) {
            // We only want to disable flight if they aren't actually in Creative or Spectator mode.
            // Otherwise, turning off the mod would break their legitimate creative flight!
            if (!mc.playerController.isInCreativeMode() && !mc.thePlayer.isSpectator()) {
                mc.thePlayer.capabilities.isFlying = false;
            }
        }
    }
}