package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.mods.Mod;

public class Fullbright extends Mod {
    
    private float oldGamma;

    public Fullbright() {
        // True for isCheat, since standard survival doesn't let you see in pitch black
        super("Fullbright", "Removes darkness.", Category.RENDER, true);
    }

    @Override
    protected void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 100.0f;
    }

    @Override
    protected void onDisable() {
        mc.gameSettings.gammaSetting = oldGamma;
    }
}