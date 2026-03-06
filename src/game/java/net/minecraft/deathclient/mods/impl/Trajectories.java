package net.minecraft.deathclient.mods.impl;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.deathclient.events.EventRender3D;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.BooleanSetting;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.lax1dude.eaglercraft.v1_8.opengl.EaglercraftGPU;
import net.lax1dude.eaglercraft.v1_8.opengl.WorldRenderer;
import net.minecraft.util.BlockPos;

public class Trajectories extends Mod {

    public BooleanSetting showBow        = new BooleanSetting("Bow",         true);
    public BooleanSetting showThrowables = new BooleanSetting("Throwables",  true);

    public Trajectories() {
        super("Trajectories", "Shows the arc of projectiles you are about to throw.", Category.RENDER, false);
        this.addSetting(showBow);
        this.addSetting(showThrowables);
    }

    @Override
    public void onRender3D(EventRender3D event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        ItemStack held = mc.thePlayer.getCurrentEquippedItem();
        if (held == null) return;

        Item item = held.getItem();
        boolean isBow       = item instanceof ItemBow && mc.thePlayer.isUsingItem();
        boolean isThrowable = item instanceof ItemSnowball
                           || item instanceof ItemEgg
                           || item instanceof ItemEnderPearl;

        if (isBow       && !showBow.isEnabled())        return;
        if (isThrowable && !showThrowables.isEnabled())  return;
        if (!isBow && !isThrowable)                      return;

        double renderX = mc.getRenderManager().viewerPosX;
        double renderY = mc.getRenderManager().viewerPosY;
        double renderZ = mc.getRenderManager().viewerPosZ;

        // --- Initial velocity ---
        float yaw   = mc.thePlayer.rotationYaw;
        float pitch = mc.thePlayer.rotationPitch;
        double speed, gravity, drag;

        if (isBow) {
            int charge = held.getMaxItemUseDuration() - mc.thePlayer.getItemInUseCount();
            float ratio = Math.min(1.0f, charge / 20.0f);
            speed   = ratio * 3.0;
            gravity = 0.05;
            drag    = 0.99;
        } else if (item instanceof ItemEnderPearl) {
            speed   = 1.5;
            gravity = 0.03;
            drag    = 0.99;
        } else {
            speed   = 1.5;
            gravity = 0.03;
            drag    = 0.99;
        }

        double yawRad   = Math.toRadians(yaw);
        double pitchRad = Math.toRadians(pitch);
        double vx = -Math.sin(yawRad) * Math.cos(pitchRad) * speed;
        double vy = -Math.sin(pitchRad) * speed;
        double vz =  Math.cos(yawRad)  * Math.cos(pitchRad) * speed;

        // Start from eye position (relative to render origin)
        double px = mc.thePlayer.posX - renderX;
        double py = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - renderY;
        double pz = mc.thePlayer.posZ - renderZ;

        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.color(1.0f, 1.0f, 0.0f, 0.9f); // Yellow

        EaglercraftGPU.glLineWidth(2.0f);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer wr = tessellator.getWorldRenderer();
        wr.begin(Tessellator.GL_LINE_STRIP, DefaultVertexFormats.POSITION);

        for (int i = 0; i < 120; i++) {
            wr.pos(px, py, pz).endVertex();

            px += vx; py += vy; pz += vz;
            vy -= gravity;
            vx *= drag; vz *= drag;

            // Stop on block collision
            BlockPos blockPos = new BlockPos(px + renderX, py + renderY, pz + renderZ);
            if (mc.theWorld.getBlockState(blockPos).getBlock() != Blocks.air) {
                wr.pos(px, py, pz).endVertex();
                break;
            }
        }

        tessellator.draw();
        EaglercraftGPU.glLineWidth(1.0f);

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
    }
}
