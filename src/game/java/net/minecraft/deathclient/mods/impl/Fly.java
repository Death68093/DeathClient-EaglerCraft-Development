package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.Mod;

public class Fly extends Mod {

    // Default 2.0, Min 0.5, Max 10.0
    public net.minecraft.deathclient.settings.NumberSetting speed = new net.minecraft.deathclient.settings.NumberSetting("Speed", 2.0, 0.5, 10.0, 0.5);
    // Vanilla (Creative flight) vs Motion (forces velocity)
    public net.minecraft.deathclient.settings.ModeSetting mode = new net.minecraft.deathclient.settings.ModeSetting("Mode", "Vanilla", "Vanilla", "Motion");

    public Fly() {
        super("Fly", "Allows you to fly like in creative mode.", Category.MOVEMENT, false);
        this.addSetting(speed);
        this.addSetting(mode);
    }

    @Override
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer == null) return;

        if (mode.is("Vanilla")) {
            // Vanilla Mode: Just turn on creative flight capabilities
            mc.thePlayer.capabilities.isFlying = true;
            mc.thePlayer.capabilities.setFlySpeed((float) (speed.getValue() * 0.05f)); 
        } 
        else if (mode.is("Motion")) {
            // Motion Mode: Suspend the player in the air
            mc.thePlayer.motionY = 0.0;
            
            // Handle going up and down
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.thePlayer.motionY += speed.getValue() * 0.5;
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.thePlayer.motionY -= speed.getValue() * 0.5;
            }

            // Math to handle directional movement based on where you are looking
            float yaw = mc.thePlayer.rotationYaw;
            double forward = mc.thePlayer.movementInput.moveForward;
            double strafe = mc.thePlayer.movementInput.moveStrafe;

            if (forward == 0.0D && strafe == 0.0D) {
                mc.thePlayer.motionX = 0.0D;
                mc.thePlayer.motionZ = 0.0D;
            } else {
                if (forward != 0.0D) {
                    if (strafe > 0.0D) {
                        yaw += (float)(forward > 0.0D ? -45 : 45);
                    } else if (strafe < 0.0D) {
                        yaw += (float)(forward > 0.0D ? 45 : -45);
                    }
                    strafe = 0.0D;
                    if (forward > 0.0D) {
                        forward = 1.0D;
                    } else if (forward < 0.0D) {
                        forward = -1.0D;
                    }
                }
                
                // Calculate the final X and Z velocities using trigonometry
                double cos = Math.cos(Math.toRadians(yaw + 90.0F));
                double sin = Math.sin(Math.toRadians(yaw + 90.0F));
                mc.thePlayer.motionX = forward * speed.getValue() * cos + strafe * speed.getValue() * sin;
                mc.thePlayer.motionZ = forward * speed.getValue() * sin - strafe * speed.getValue() * cos;
            }
        }
    }

    @Override
    public void onDisable() {
        if (mc.thePlayer == null) return;
        
        // Reset everything when we turn the mod off so we fall normally
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.capabilities.setFlySpeed(0.05f); // Vanilla default
        
        // Stop all momentum instantly so you don't shoot forward
        mc.thePlayer.motionX = 0.0;
        mc.thePlayer.motionZ = 0.0;
    }
}