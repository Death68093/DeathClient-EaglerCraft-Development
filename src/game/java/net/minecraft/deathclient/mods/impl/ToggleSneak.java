package net.minecraft.deathclient.mods.impl;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.HudMod;

public class ToggleSneak extends HudMod {

    public net.minecraft.deathclient.settings.BooleanSetting omniSneak =
            new net.minecraft.deathclient.settings.BooleanSetting("Omni Sneak", false);

    public ToggleSneak() {
        super("Toggle Sneak", "Keeps you sneaking automatically.", Category.PLAYER);
        this.addSetting(omniSneak);
    }

    @Override
    public void draw() {
        mc.fontRendererObj.drawStringWithShadow("[Sneaking]", this.x, this.y, 0xFFAA00);
    }

    @Override
    public int getWidth() {
        return mc.fontRendererObj.getStringWidth("[Sneaking]");
    }

    @Override
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer == null) return;
        // Omni Sneak = sneak always. Otherwise only sneak while moving.
        boolean shouldSneak = omniSneak.isEnabled()
                || mc.thePlayer.movementInput.moveForward != 0
                || mc.thePlayer.movementInput.moveStrafe != 0;
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), shouldSneak);
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer != null && !mc.gameSettings.keyBindSneak.isKeyDown()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
    }
}