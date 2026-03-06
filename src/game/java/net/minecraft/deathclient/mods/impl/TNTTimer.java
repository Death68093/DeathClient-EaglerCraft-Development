package net.minecraft.deathclient.mods.impl;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.deathclient.events.EventRender2D;
import net.minecraft.deathclient.events.EventRender3D;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.BooleanSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;

public class TNTTimer extends Mod {

    public BooleanSetting show2DList = new BooleanSetting("Show 2D List", true);
    public BooleanSetting show3DTag  = new BooleanSetting("Show 3D Tag",  true);

    public TNTTimer() {
        super("TNT Timer", "Shows countdown until TNT explodes.", Category.RENDER, false);
        this.addSetting(show2DList);
        this.addSetting(show3DTag);
    }

    // ---- 2D list in screen corner ----
    @Override
    public void onRender2D(EventRender2D event) {
        if (!show2DList.isEnabled() || mc.theWorld == null) return;

        int y = 50;
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityTNTPrimed)) continue;
            EntityTNTPrimed tnt = (EntityTNTPrimed) entity;
            float secs = tnt.fuse / 20.0f;
            int color = secs < 1.5f ? 0xFF0000 : (secs < 2.5f ? 0xFFAA00 : 0xFFFFFF);
            mc.fontRendererObj.drawStringWithShadow(
                    String.format("TNT: %.1fs", secs), 2, y, color);
            y += 10;
        }
    }

    // ---- 3D tag floating above each TNT ----
    @Override
    public void onRender3D(EventRender3D event) {
        if (!show3DTag.isEnabled() || mc.theWorld == null || mc.thePlayer == null) return;

        double renderX = mc.getRenderManager().viewerPosX;
        double renderY = mc.getRenderManager().viewerPosY;
        double renderZ = mc.getRenderManager().viewerPosZ;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityTNTPrimed)) continue;
            EntityTNTPrimed tnt = (EntityTNTPrimed) entity;
            float secs = tnt.fuse / 20.0f;

            double ex = (tnt.lastTickPosX + (tnt.posX - tnt.lastTickPosX) * event.partialTicks) - renderX;
            double ey = (tnt.lastTickPosY + (tnt.posY - tnt.lastTickPosY) * event.partialTicks) - renderY;
            double ez = (tnt.lastTickPosZ + (tnt.posZ - tnt.lastTickPosZ) * event.partialTicks) - renderZ;

            String tag = String.format("%.1fs", secs);
            int color  = secs < 1.5f ? 0xFF0000 : (secs < 2.5f ? 0xFFAA00 : 0xFFFFFF);

            renderNameTag(tag, ex, ey + tnt.height + 0.35, ez, color);
        }
    }

    /** Renders a 2D string billboard at a 3D world position. */
    private void renderNameTag(String text, double x, double y, double z, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        // Rotate to always face the camera (billboard)
        GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0, 1, 0);
        GlStateManager.rotate(mc.getRenderManager().playerViewX, 1, 0, 0);
        // Scale down to world size
        float scale = 0.025f;
        GlStateManager.scale(-scale, -scale, scale);
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        int tw = mc.fontRendererObj.getStringWidth(text);
        mc.fontRendererObj.drawStringWithShadow(text, -tw / 2, 0, color);
        GlStateManager.enableDepth();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}
