package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.DeathClient;
import net.minecraft.deathclient.mods.HudMod;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.NumberSetting;
import net.minecraft.deathclient.settings.Setting;

public class ReachDisplay extends HudMod {

    public ReachDisplay() {
        super("Reach Display", "Shows your current attack reach on screen.", Category.HUD);
        this.x = 2;
        this.y = 80;
    }

    @Override
    public void draw() {
        double combatReach = getReach("Combat Reach", "Distance", 3.0);
        double blockReach  = getReach("Block Reach",  "Distance", 4.5);

        mc.fontRendererObj.drawStringWithShadow(
                String.format("Combat: §e%.1f  §fBlock: §e%.1f", combatReach, blockReach),
                this.x, this.y, 0xFF8800);
    }

    @Override
    public int getWidth() {
        return mc.fontRendererObj.getStringWidth("Combat: 6.0  Block: 8.0");
    }

    private double getReach(String modName, String settingName, double defaultVal) {
        Mod mod = DeathClient.getInstance().getModManager().getModByName(modName);
        if (mod != null && mod.isToggled()) {
            Setting s = mod.getSettingByName(settingName);
            if (s instanceof NumberSetting) return ((NumberSetting) s).getValue();
        }
        return defaultVal;
    }
}
