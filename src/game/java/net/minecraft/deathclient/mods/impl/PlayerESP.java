package net.minecraft.deathclient.mods.impl;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.deathclient.events.EventRender2D;
import net.minecraft.deathclient.events.EventRender3D;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.BooleanSetting;
import net.minecraft.deathclient.settings.ModeSetting;
import net.minecraft.deathclient.settings.NumberSetting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;

public class PlayerESP extends Mod {

    public ModeSetting colorMode   = new ModeSetting("Color Mode", "Static", "Static", "Health", "Rainbow");
    public BooleanSetting showNames = new BooleanSetting("Show Names",    true);
    public BooleanSetting showHP    = new BooleanSetting("Show Health",   true);
    public BooleanSetting tracers   = new BooleanSetting("Tracers",       false);
    public NumberSetting  red       = new NumberSetting("R", 255, 0, 255, 1);
    public NumberSetting  green     = new NumberSetting("G", 0,   0, 255, 1);
    public NumberSetting  blue      = new NumberSetting("B", 0,   0, 255, 1);

    public PlayerESP() {
        super("Player ESP", "Draws boxes around players through walls.", Category.COMBAT, false);
        this.addSetting(colorMode);
        this.addSetting(showNames);
        this.addSetting(showHP);
        this.addSetting(tracers);
        this.addSetting(red);
        this.addSetting(green);
        this.addSetting(blue);
    }

    // ---- 3D boxes ----
    @Override
    public void onRender3D(EventRender3D event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;

        double renderX = mc.getRenderManager().viewerPosX;
        double renderY = mc.getRenderManager().viewerPosY;
        double renderZ = mc.getRenderManager().viewerPosZ;

        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);

        for (Object obj : mc.theWorld.playerEntities) {
            if (!(obj instanceof EntityPlayer)) continue;
            EntityPlayer player = (EntityPlayer) obj;
            if (player == mc.thePlayer) continue;

            double x = (player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks) - renderX;
            double y = (player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks) - renderY;
            double z = (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks) - renderZ;

            float[] c = getColor(player);
            GlStateManager.color(c[0], c[1], c[2], 1.0f);

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            float w = player.width / 2.0f;
            AxisAlignedBB bbox = new AxisAlignedBB(-w, 0, -w, w, player.height, w);
            RenderGlobal.func_181561_a(bbox);
            GlStateManager.popMatrix();
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
    }

    // ---- 2D name tags and health bars ----
    @Override
    public void onRender2D(EventRender2D event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        if (!showNames.isEnabled() && !showHP.isEnabled() && !tracers.isEnabled()) return;

        ScaledResolution sr = new ScaledResolution(mc);
        int sw = sr.getScaledWidth();
        int sh = sr.getScaledHeight();

        for (Object obj : mc.theWorld.playerEntities) {
            if (!(obj instanceof EntityPlayer)) continue;
            EntityPlayer player = (EntityPlayer) obj;
            if (player == mc.thePlayer) continue;

            // Project world position to screen
            int[] screen = worldToScreen(player.posX, player.posY + player.height, player.posZ, sw, sh);
            if (screen == null) continue;

            int sx = screen[0], sy = screen[1];
            float[] c = getColor(player);
            int argb = toARGB(c[0], c[1], c[2], 1.0f);

            if (showNames.isEnabled()) {
                String name = player.getGameProfile().getName();
                int tw = mc.fontRendererObj.getStringWidth(name);
                mc.fontRendererObj.drawStringWithShadow(name, sx - tw / 2, sy - 10, argb);
            }

            if (showHP.isEnabled()) {
                float hp  = player.getHealth();
                float max = player.getMaxHealth();
                String hpStr = String.format("%.1f", hp);
                int hw = mc.fontRendererObj.getStringWidth(hpStr);
                int hpColor = hp > max * 0.6f ? 0x00FF00 : (hp > max * 0.3f ? 0xFFAA00 : 0xFF0000);
                mc.fontRendererObj.drawStringWithShadow(hpStr, sx - hw / 2, sy, hpColor);
            }

            if (tracers.isEnabled()) {
                // Draw a line from screen centre to the player
                int[] col = { (int)(c[0]*255), (int)(c[1]*255), (int)(c[2]*255) };
                drawLine2D(sw / 2, sh, sx, sy, toARGB(c[0], c[1], c[2], 0.8f));
            }
        }
    }

    // Simple 2D line using individual pixels (no GL immediate mode required)
    private void drawLine2D(int x1, int y1, int x2, int y2, int color) {
        int dx = Math.abs(x2 - x1), dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1, sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;
        while (true) {
            net.minecraft.client.gui.Gui.drawRect(x1, y1, x1 + 1, y1 + 1, color);
            if (x1 == x2 && y1 == y2) break;
            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x1 += sx; }
            if (e2 <  dx) { err += dx; y1 += sy; }
        }
    }

    private float[] getColor(EntityPlayer player) {
        switch (colorMode.getMode()) {
            case "Health": {
                float r = player.getHealth() / player.getMaxHealth();
                return new float[]{ 1f - r, r, 0f };
            }
            case "Rainbow": {
                float t = (System.currentTimeMillis() % 3000) / 3000.0f;
                return hsvToRgb(t, 1f, 1f);
            }
            default:
                return new float[]{
                        (float) red.getValue()   / 255f,
                        (float) green.getValue() / 255f,
                        (float) blue.getValue()  / 255f };
        }
    }

    private float[] hsvToRgb(float h, float s, float v) {
        int i = (int)(h * 6); float f = h * 6 - i;
        float p = v * (1 - s), q = v * (1 - f * s), t = v * (1 - (1 - f) * s);
        switch (i % 6) {
            case 0: return new float[]{v, t, p};
            case 1: return new float[]{q, v, p};
            case 2: return new float[]{p, v, t};
            case 3: return new float[]{p, q, v};
            case 4: return new float[]{t, p, v};
            default: return new float[]{v, p, q};
        }
    }

    private int toARGB(float r, float g, float b, float a) {
        return ((int)(a*255) << 24) | ((int)(r*255) << 16) | ((int)(g*255) << 8) | (int)(b*255);
    }

    /**
     * Projects a world position to 2D screen coordinates.
     * Returns null if the point is behind the camera.
     */
    private int[] worldToScreen(double wx, double wy, double wz, int sw, int sh) {
        try {
            net.minecraft.util.Vec3 pos = new net.minecraft.util.Vec3(
                    wx - mc.getRenderManager().viewerPosX,
                    wy - mc.getRenderManager().viewerPosY,
                    wz - mc.getRenderManager().viewerPosZ);

            // Rough frustum check
            net.minecraft.util.Vec3 look = mc.thePlayer.getLookVec();
            double dot = look.xCoord * pos.xCoord + look.yCoord * pos.yCoord + look.zCoord * pos.zCoord;
            if (dot < 0) return null;

            // Use Minecraft's projection (requires GL readback - expensive, so we use a simple angle approximation)
            float yaw   = (float) Math.toRadians(mc.thePlayer.rotationYaw + 90);
            float pitch = (float) Math.toRadians(mc.thePlayer.rotationPitch);

            double dx = pos.xCoord, dy = pos.yCoord, dz = pos.zCoord;

            double cosYaw = Math.cos(yaw), sinYaw = Math.sin(yaw);
            double cosPitch = Math.cos(pitch), sinPitch = Math.sin(pitch);

            double rx = dx * cosYaw - dz * sinYaw;
            double ry = dy * cosPitch - (dx * sinYaw + dz * cosYaw) * sinPitch;
            double rz = dy * sinPitch + (dx * sinYaw + dz * cosYaw) * cosPitch;

            if (rz < 0.1) return null;

            float fov = (float) Math.toRadians(mc.gameSettings.fovSetting);
            float aspect = (float) sw / sh;
            double projX = rx / (rz * Math.tan(fov / 2) * aspect);
            double projY = ry / (rz * Math.tan(fov / 2));

            int screenX = (int) ((projX + 1) * sw / 2);
            int screenY = (int) ((1 - projY) * sh / 2);
            return new int[]{screenX, screenY};
        } catch (Exception e) {
            return null;
        }
    }
}
