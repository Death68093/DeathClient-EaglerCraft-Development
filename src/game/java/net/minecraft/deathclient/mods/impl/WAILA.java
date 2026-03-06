package net.minecraft.deathclient.mods.impl;

import net.minecraft.block.Block;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.deathclient.events.EventRender2D;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.BooleanSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MovingObjectPosition;

public class WAILA extends Mod {

    public BooleanSetting showBlocks  = new BooleanSetting("Show Blocks",  true);
    public BooleanSetting showEntities= new BooleanSetting("Show Entities", true);
    public BooleanSetting showCoords  = new BooleanSetting("Show Coords",   true);
    public BooleanSetting showHealth  = new BooleanSetting("Show Health",   true);

    public WAILA() {
        super("WAILA", "Shows what you are looking at.", Category.RENDER, false);
        this.addSetting(showBlocks);
        this.addSetting(showEntities);
        this.addSetting(showCoords);
        this.addSetting(showHealth);
    }

    @Override
    public void onRender2D(EventRender2D event) {
        if (mc.theWorld == null || mc.thePlayer == null || mc.objectMouseOver == null) return;
        if (mc.currentScreen != null) return; // Don't show while a GUI is open

        ScaledResolution sr = new ScaledResolution(mc);
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();

        String primaryText = null;
        String secondaryText = null;
        int primaryColor = 0xFFFFFF;

        MovingObjectPosition mop = mc.objectMouseOver;

        // ---- Block ----
        if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && showBlocks.isEnabled()) {
            Block block = mc.theWorld.getBlockState(mop.getBlockPos()).getBlock();
            primaryText = block.getLocalizedName();
            primaryColor = 0xFFFFFF;
            if (showCoords.isEnabled()) {
                secondaryText = String.format("(%d, %d, %d)",
                        mop.getBlockPos().getX(), mop.getBlockPos().getY(), mop.getBlockPos().getZ());
            }
        }

        // ---- Entity ----
        else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && showEntities.isEnabled()) {
            Entity entity = mop.entityHit;
            primaryText = entity.getName();
            primaryColor = 0xFFFF55;
            if (showHealth.isEnabled() && entity instanceof EntityLivingBase) {
                EntityLivingBase living = (EntityLivingBase) entity;
                float hp  = living.getHealth();
                float max = living.getMaxHealth();
                float ratio = hp / max;
                int hpColor = ratio > 0.6f ? 0x00FF00 : (ratio > 0.3f ? 0xFFAA00 : 0xFF0000);
                secondaryText = String.format("HP: %.1f / %.1f", hp, max);
                // Override secondary color inline via colour codes is not available, but we draw separately
            }
        }

        if (primaryText == null) return;

        // ---- Render tooltip box above hotbar ----
        int boxW = Math.max(
                mc.fontRendererObj.getStringWidth(primaryText),
                secondaryText != null ? mc.fontRendererObj.getStringWidth(secondaryText) : 0) + 10;
        int boxH = secondaryText != null ? 22 : 12;
        int bx = sw / 2 - boxW / 2;
        int by = sh - 65 - boxH;

        // Background
        Gui.drawRect(bx - 2, by - 2, bx + boxW + 2, by + boxH + 2, 0xBB000000);
        // Top accent line (red for client brand)
        Gui.drawRect(bx - 2, by - 2, bx + boxW + 2, by - 1, 0xFFCC0000);

        mc.fontRendererObj.drawStringWithShadow(primaryText, bx + 2, by + 2, primaryColor);
        if (secondaryText != null) {
            mc.fontRendererObj.drawStringWithShadow(secondaryText, bx + 2, by + 12, 0xAAAAAA);
        }
    }
}
