package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.BooleanSetting;

public class FPSBoost extends Mod {

    public BooleanSetting noParticles  = new BooleanSetting("No Particles",    true);
    public BooleanSetting noClouds     = new BooleanSetting("No Clouds",        true);
    public BooleanSetting noSky        = new BooleanSetting("No Sky",           false);
    public BooleanSetting noFog        = new BooleanSetting("No Fog",           true);
    public BooleanSetting fastGraphics = new BooleanSetting("Fast Graphics",    false);

    private int  savedParticles;
    private int  savedClouds;
    private boolean savedFancy;
    private boolean savedFog;

    public FPSBoost() {
        super("FPS Boost", "Reduces visual effects to improve frame rate.", Category.RENDER, false);
        this.addSetting(noParticles);
        this.addSetting(noClouds);
        this.addSetting(noSky);
        this.addSetting(noFog);
        this.addSetting(fastGraphics);
    }

    @Override
    public void onEnable() {
        if (mc.gameSettings == null) return;
        savedParticles = mc.gameSettings.particleSetting;
        savedClouds    = mc.gameSettings.clouds;
        savedFancy     = mc.gameSettings.fancyGraphics;
        savedFog       = mc.gameSettings.fog;
    }

    @Override
    public void onUpdate(EventUpdate event) {
        if (mc.gameSettings == null) return;
        if (noParticles.isEnabled())  mc.gameSettings.particleSetting = 2;   // Minimal
        if (noClouds.isEnabled())     mc.gameSettings.clouds = 0;            // Off
        if (noFog.isEnabled())        mc.gameSettings.fog = false;          // Disable fog
        if (fastGraphics.isEnabled()) mc.gameSettings.fancyGraphics = false;
    }

    @Override
    public void onDisable() {
        if (mc.gameSettings == null) return;
        mc.gameSettings.particleSetting = savedParticles;
        mc.gameSettings.clouds          = savedClouds;
        mc.gameSettings.fancyGraphics   = savedFancy;
        mc.gameSettings.fog             = savedFog;
    }
}
