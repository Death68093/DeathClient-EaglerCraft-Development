package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.DeathClient;
import net.minecraft.deathclient.events.EventRender2D;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.BooleanSetting;

public class HUD extends Mod {

    // A toggle setting that defaults to true
    public BooleanSetting showWatermark = new BooleanSetting("Show Watermark", true);

    public HUD() {
        super("HUD", "Draws the client interface.", Category.RENDER, false);
        // this.setToggled(true);
        this.addSetting(showWatermark); // Register the setting!
    }

    // public void setToggled(Boolean val) {
    //     showWatermark = val;
    // }

    @Override
    public void onRender2D(EventRender2D event) {
        // Only draw the watermark if the setting is checked!
        if (this.showWatermark.isEnabled()) {
            mc.fontRendererObj.drawStringWithShadow("DeathClient", 2, 2, 0xFF0000); 
        }

        int yOffset = 2;
        for (Mod mod : DeathClient.getInstance().getModManager().mods) {
            if (mod.isToggled()) {
                int screenWidth = new net.minecraft.client.gui.ScaledResolution(mc).getScaledWidth();
                int textWidth = mc.fontRendererObj.getStringWidth(mod.getName());
                
                mc.fontRendererObj.drawStringWithShadow(mod.getName(), screenWidth - textWidth - 2, yOffset, 0x00FF00);
                yOffset += 10;
            }
        }
    }
}