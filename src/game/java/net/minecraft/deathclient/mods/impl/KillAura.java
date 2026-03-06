package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.BooleanSetting;
import net.minecraft.deathclient.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class KillAura extends Mod {

    public NumberSetting range = new NumberSetting("Range", 4.0, 1.0, 6.0, 0.1);
    public NumberSetting reach = new NumberSetting("Reach", 3.0, 1.0, 6.0, 0.1);
    public BooleanSetting onlyPlayers = new BooleanSetting("Only Players", false);
    public BooleanSetting ignoreDead = new BooleanSetting("Ignore Dead", true);

    public KillAura() {
        super("KillAura", "Automatically attacks nearby entities.", Category.COMBAT, true);
        this.addSetting(range);
        this.addSetting(reach);
        this.addSetting(onlyPlayers);
        this.addSetting(ignoreDead);
    }

    @Override
    public void onUpdate(EventUpdate event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == mc.thePlayer) continue;
            if (!(entity instanceof EntityLivingBase)) continue;
            if (onlyPlayers.isEnabled() && !(entity instanceof EntityPlayer)) continue;

            EntityLivingBase living = (EntityLivingBase) entity;
            if (ignoreDead.isEnabled() && living.getHealth() <= 0) continue;

            if (mc.thePlayer.getDistanceToEntity(entity) <= (float) range.getValue()) {
                mc.thePlayer.swingItem();
                mc.playerController.attackEntity(mc.thePlayer, entity);
                break; // Only one target per tick
            }
        }
    }
}
