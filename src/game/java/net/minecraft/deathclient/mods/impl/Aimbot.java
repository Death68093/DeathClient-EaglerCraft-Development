package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.BooleanSetting;
import net.minecraft.deathclient.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class Aimbot extends Mod {

    public NumberSetting range = new NumberSetting("Range", 6.0, 1.0, 20.0, 0.5);
    public NumberSetting smoothing = new NumberSetting("Smoothing", 1.0, 1.0, 10.0, 0.5);
    public BooleanSetting onlyPlayers = new BooleanSetting("Only Players", false);
    public BooleanSetting ignoreDead = new BooleanSetting("Ignore Dead", true);

    public Aimbot() {
        super("Aimbot", "Snaps your aim to the closest entity.", Category.COMBAT, true);
        this.addSetting(range);
        this.addSetting(smoothing);
        this.addSetting(onlyPlayers);
        this.addSetting(ignoreDead);
    }

    @Override
    public void onUpdate(EventUpdate event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;

        Entity closest = null;
        float closestDist = (float) range.getValue();

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == mc.thePlayer || !(entity instanceof EntityLivingBase)) continue;
            if (onlyPlayers.isEnabled() && !(entity instanceof EntityPlayer)) continue;
            EntityLivingBase living = (EntityLivingBase) entity;
            if (ignoreDead.isEnabled() && living.getHealth() <= 0) continue;

            float dist = mc.thePlayer.getDistanceToEntity(entity);
            if (dist < closestDist) {
                closestDist = dist;
                closest = entity;
            }
        }

        if (closest != null) {
            double diffX = closest.posX - mc.thePlayer.posX;
            double diffY = closest.posY + closest.getEyeHeight()
                    - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
            double diffZ = closest.posZ - mc.thePlayer.posZ;

            double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);

            float targetYaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
            float targetPitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

            float smooth = (float) smoothing.getValue();
            mc.thePlayer.rotationYaw += (targetYaw - mc.thePlayer.rotationYaw) / smooth;
            mc.thePlayer.rotationPitch += (targetPitch - mc.thePlayer.rotationPitch) / smooth;
        }
    }
}
