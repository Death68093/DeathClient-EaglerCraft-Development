package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.Mod;

/**
 * No Dynamic FOV - Prevents the base FOV setting from drifting.
 *
 * NOTE: The sprint/speed FOV zoom is applied as a multiplier inside
 * EntityRenderer.getFOVModifier(), which requires a separate MCP hook to fully
 * suppress. This class locks the base fovSetting value and is the correct place
 * to add that hook. Add this line in EntityRenderer.getFOVModifier():
 *
 *   if (DeathClient.getInstance().getModManager()
 *           .getModByName("No Dynamic FOV").isToggled()) return 1.0F;
 */
public class NoDynamicFOV extends Mod {

    private float lockedFOV;

    public NoDynamicFOV() {
        super("No Dynamic FOV", "Prevents FOV from changing when sprinting/using items.", Category.RENDER, false);
    }

    @Override
    public void onEnable() {
        if (mc.gameSettings != null) {
            lockedFOV = mc.gameSettings.fovSetting;
        }
    }

    @Override
    public void onUpdate(EventUpdate event) {
        if (mc.gameSettings == null) return;
        // Lock the base FOV value so it can't drift
        mc.gameSettings.fovSetting = lockedFOV;
    }
}
