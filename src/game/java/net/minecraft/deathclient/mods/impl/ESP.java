package net.minecraft.deathclient.mods.impl;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.deathclient.events.EventRender3D;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.BooleanSetting;
import net.minecraft.deathclient.settings.ModeSetting;
import net.minecraft.deathclient.settings.NumberSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class ESP extends Mod {

    public ModeSetting targetMode = new ModeSetting("Target", "Mobs", "Mobs", "Animals", "Players", "All Entities");
    public BooleanSetting throughWalls = new BooleanSetting("Through Walls", true);
    public NumberSetting red   = new NumberSetting("R", 255, 0, 255, 1);
    public NumberSetting green = new NumberSetting("G", 0, 0, 255, 1);
    public NumberSetting blue  = new NumberSetting("B", 0, 0, 255, 1);

    public ESP() {
        super("ESP", "Draws boxes around entities through walls.", Category.COMBAT, false);
        this.addSetting(targetMode);
        this.addSetting(throughWalls);
        this.addSetting(red);
        this.addSetting(green);
        this.addSetting(blue);
    }

    @Override
    public void onRender3D(EventRender3D event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;

        double renderX = mc.getRenderManager().viewerPosX;
        double renderY = mc.getRenderManager().viewerPosY;
        double renderZ = mc.getRenderManager().viewerPosZ;

        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        if (throughWalls.isEnabled()) {
            GlStateManager.disableDepth();
            GlStateManager.depthMask(false);
        }
        GlStateManager.color(
                (float) red.getValue() / 255f,
                (float) green.getValue() / 255f,
                (float) blue.getValue() / 255f, 1.0f);

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == mc.thePlayer) continue;
            if (!shouldTarget(entity)) continue;

            double x = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks) - renderX;
            double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks) - renderY;
            double z = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks) - renderZ;

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);

            float w = entity.width / 2.0F;
            float h = entity.height;
            AxisAlignedBB bbox = new AxisAlignedBB(-w, 0, -w, w, h, w);
            RenderGlobal.func_181561_a(bbox);

            GlStateManager.popMatrix();
        }

        if (throughWalls.isEnabled()) {
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
        }
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
    }

    private boolean shouldTarget(Entity entity) {
        if (!(entity instanceof EntityLivingBase)) return false;
        switch (targetMode.getMode()) {
            case "Mobs":        return entity instanceof EntityMob;
            case "Animals":     return entity instanceof EntityAnimal;
            case "Players":     return entity instanceof EntityPlayer;
            case "All Entities":return true;
            default:            return false;
        }
    }
}
