package net.minecraft.deathclient.mods.impl;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.deathclient.DeathClient;
import net.minecraft.deathclient.events.EventRender2D;
import net.minecraft.deathclient.mods.HudMod;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.BooleanSetting;

public class HUD extends Mod {

    public BooleanSetting showWatermark = new BooleanSetting("Show Watermark", true);
    public BooleanSetting showFPS      = new BooleanSetting("Show FPS", true);
    public BooleanSetting showPing     = new BooleanSetting("Show Ping", true);
    public BooleanSetting showModList  = new BooleanSetting("Show Mod List", true);
    public BooleanSetting showCoords   = new BooleanSetting("Show Coords", false);

    public HUD() {
        super("HUD", "Draws the client interface.", Category.HUD, false);
        this.addSetting(showWatermark);
        this.addSetting(showFPS);
        this.addSetting(showPing);
        this.addSetting(showModList);
        this.addSetting(showCoords);
    }

    @Override
    public void onRender2D(EventRender2D event) {
        if (mc.thePlayer == null) return;
        ScaledResolution sr = new ScaledResolution(mc);
        int screenWidth = sr.getScaledWidth();

        int leftY = 2;

        // --- Watermark ---
        if (showWatermark.isEnabled()) {
            String watermark = DeathClient.getInstance().CLIENT_NAME
                    + " v" + DeathClient.getInstance().CLIENT_VERSION;
            mc.fontRendererObj.drawStringWithShadow(watermark, 2, leftY, 0xFF0000);
            leftY += 10;
        }

        // --- FPS ---
        if (showFPS.isEnabled()) {
            int fps = net.minecraft.client.Minecraft.getDebugFPS();
            int fpsColor = fps >= 60 ? 0x00FF00 : (fps >= 30 ? 0xFFAA00 : 0xFF0000);
            mc.fontRendererObj.drawStringWithShadow("FPS: " + fps, 2, leftY, fpsColor);
            leftY += 10;
        }

        // --- Ping ---
        if (showPing.isEnabled() && mc.getNetHandler() != null) {
            try {
                net.minecraft.client.network.NetworkPlayerInfo info =
                        mc.getNetHandler().getPlayerInfo(mc.thePlayer.getGameProfile().getId());
                if (info != null) {
                    int ping = info.getResponseTime();
                    int pingColor = ping < 80 ? 0x00FF00 : (ping < 150 ? 0xFFAA00 : 0xFF0000);
                    mc.fontRendererObj.drawStringWithShadow("Ping: " + ping + "ms", 2, leftY, pingColor);
                    leftY += 10;
                }
            } catch (Exception ignored) {}
        }

        // --- Coords ---
        if (showCoords.isEnabled()) {
            String coords = String.format("XYZ: %d / %d / %d",
                    (int) mc.thePlayer.posX, (int) mc.thePlayer.posY, (int) mc.thePlayer.posZ);
            mc.fontRendererObj.drawStringWithShadow(coords, 2, leftY, 0xFFFFFF);
        }

        // --- Mod list (right side, skip HudMods to avoid clutter) ---
        if (showModList.isEnabled()) {
            int yOffset = 2;
            for (Mod mod : DeathClient.getInstance().getModManager().mods) {
                if (mod.isToggled() && !(mod instanceof HudMod)) {
                    int textWidth = mc.fontRendererObj.getStringWidth(mod.getName());
                    mc.fontRendererObj.drawStringWithShadow(
                            mod.getName(), screenWidth - textWidth - 2, yOffset, 0x00FF00);
                    yOffset += 10;
                }
            }
        }
    }
}
