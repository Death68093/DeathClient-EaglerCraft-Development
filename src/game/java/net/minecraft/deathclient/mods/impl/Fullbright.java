package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.Mod;

public class Fullbright extends Mod {

    // Default 10.0 (max brightness), Min 1.0, Max 15.0
    public net.minecraft.deathclient.settings.NumberSetting gamma = new net.minecraft.deathclient.settings.NumberSetting("Gamma Level", 10.0, 1.0, 15.0, 1.0);
    
    // We need a place to store your normal brightness so we can restore it later!
    private float oldGamma;

    public Fullbright() {
        super("Fullbright", "Lights up the world.", Category.RENDER, false);
        this.addSetting(gamma);
    }

    @Override
    public void onEnable() {
        if (mc.gameSettings != null) {
            // Save whatever your brightness is currently at before we change it
            this.oldGamma = mc.gameSettings.gammaSetting;
        }
    }

    @Override
    public void onUpdate(EventUpdate event) {
        if (mc.gameSettings != null) {
            // Constantly force the game's gamma to match your slider
            mc.gameSettings.gammaSetting = (float) this.gamma.getValue();
        }
    }

    @Override
    public void onDisable() {
        if (mc.gameSettings != null) {
            // Put the brightness back to normal when you turn the mod off
            mc.gameSettings.gammaSetting = this.oldGamma;
        }
    }
}