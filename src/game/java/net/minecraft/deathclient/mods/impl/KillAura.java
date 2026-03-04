package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class KillAura extends Mod {

    public KillAura() {
        super("KillAura", "Automatically attacks nearby entities.", Category.COMBAT, true);
    }

    @Override
    public void onUpdate(EventUpdate event) {
        for (Entity entity : mc.theWorld.loadedEntityList) {
            // Make sure we aren't attacking ourselves, and it's actually a living thing
            if (entity != mc.thePlayer && entity instanceof EntityLivingBase) {
                // 4.0F is the attack radius
                if (mc.thePlayer.getDistanceToEntity(entity) <= 4.0F) {
                    
                    // Swing the arm visually
                    mc.thePlayer.swingItem();
                    
                    // Send the attack packet to the server
                    mc.playerController.attackEntity(mc.thePlayer, entity);
                    
                    // Break out of the loop so we only attack one entity per tick
                    break;
                }
            }
        }
    }
}