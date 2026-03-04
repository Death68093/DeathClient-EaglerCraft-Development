package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.HudMod; // IMPORT HUDMOD

public class ToggleSprint extends HudMod { // EXTEND HUDMOD

    public net.minecraft.deathclient.settings.BooleanSetting omniSprint = new net.minecraft.deathclient.settings.BooleanSetting("Omni Sprint", false);

    public ToggleSprint() {
        super("Toggle Sprint", "Keeps you sprinting automatically.", Category.PLAYER); // Removed 'false' if HudMod doesn't use it
        this.setKey(47); // 'V'
        this.addSetting(omniSprint);
    }

    // --- NEW HUD METHODS ---
    @Override
    public void draw() {
        mc.fontRendererObj.drawStringWithShadow("[Sprint (Toggled)]", this.x, this.y, 0x00FF00); // Green text
    }

    @Override
    public int getWidth() {
        return mc.fontRendererObj.getStringWidth("[Sprint (Toggled)]");
    }
    // -----------------------

    @Override
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer != null && mc.thePlayer.movementInput.moveForward > 0 && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally) {
            mc.thePlayer.setSprinting(true);
        }
    }
    
    @Override
    public void onDisable() {
        if (mc.thePlayer != null) {
            mc.thePlayer.setSprinting(false);
        }
    }
}