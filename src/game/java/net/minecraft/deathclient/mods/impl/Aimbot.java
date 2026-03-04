package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

public class Aimbot extends Mod {

    public Aimbot() {
        super("Aimbot", "Snaps your aim to the closest entity.", Category.COMBAT, true);
    }

    @Override
    public void onUpdate(EventUpdate event) {
        Entity closest = null;
        float closestDist = 100.0F; // Arbitrary large starting distance

        // Find the closest entity
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity != mc.thePlayer && entity instanceof EntityLivingBase) {
                float dist = mc.thePlayer.getDistanceToEntity(entity);
                if (dist < closestDist) {
                    closestDist = dist;
                    closest = entity;
                }
            }
        }

        // If we found someone within 6 blocks, aim at them
        if (closest != null && closestDist <= 6.0F) {
            double diffX = closest.posX - mc.thePlayer.posX;
            // Calculate Y difference based on eye heights
            double diffY = closest.posY + closest.getEyeHeight() - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
            double diffZ = closest.posZ - mc.thePlayer.posZ;

            double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
            
            // Calculate the required Yaw and Pitch
            float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
            float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

            // Apply rotations
            mc.thePlayer.rotationYaw = yaw;
            mc.thePlayer.rotationPitch = pitch;
        }
    }
}