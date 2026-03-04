package net.minecraft.deathclient.mods.impl;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.deathclient.events.EventRender3D;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class ESP extends Mod {

    public net.minecraft.deathclient.settings.BooleanSetting playersOnly = new net.minecraft.deathclient.settings.BooleanSetting("Players Only", true);

    public ESP() {
        super("ESP", "Draws boxes around entities through walls.", Category.RENDER, false);
        this.addSetting(playersOnly);
    }

    @Override
    public void onRender3D(EventRender3D event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;

        // 1. Get the camera's current position from the RenderManager
        double renderX = mc.getRenderManager().viewerPosX;
        double renderY = mc.getRenderManager().viewerPosY;
        double renderZ = mc.getRenderManager().viewerPosZ;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity == mc.thePlayer || (playersOnly.isEnabled() && !(entity instanceof EntityPlayer))) continue;

            // 2. Calculate the entity's "Interpolated" position (Smooth movement)
            double x = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks) - renderX;
            double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks) - renderY;
            double z = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks) - renderZ;

            // 3. START DRAWING
            GlStateManager.pushMatrix();
            
            // This is the fix: We move the OpenGL "cursor" to the entity's feet
            GlStateManager.translate(x, y, z);
            
            // Now we define the box starting from 0,0,0 (the entity's feet)
            float w = entity.width / 2.0F;
            float h = entity.height;
            AxisAlignedBB bbox = new AxisAlignedBB(-w, 0, -w, w, h, w);

            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth(); // See through walls
            GlStateManager.depthMask(false);
            
            GlStateManager.color(1.0F, 0.0F, 0.0F, 1.0F); // Red
            
            // Draw the box at its new 0,0,0 origin
            RenderGlobal.func_181561_a(bbox);

            // 4. CLEAN UP
            GlStateManager.depthMask(true);
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
}