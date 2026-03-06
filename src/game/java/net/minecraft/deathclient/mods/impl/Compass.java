package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.mods.HudMod;
import net.minecraft.deathclient.settings.BooleanSetting;

public class Compass extends HudMod {

    public BooleanSetting showCoords = new BooleanSetting("Show Coords", true);

    public Compass() {
        super("Compass", "Shows your current facing direction and coordinates.", Category.HUD);
        this.x = 2;
        this.y = 100;
        this.addSetting(showCoords);
    }

    @Override
    public void draw() {
        if (mc.thePlayer == null) return;

        float yaw = mc.thePlayer.rotationYaw % 360;
        if (yaw < 0) yaw += 360;

        String dir    = getCardinal(yaw);
        String facing = "Facing: §e" + dir + " §7(" + (int) yaw + "°)";
        mc.fontRendererObj.drawStringWithShadow(facing, this.x, this.y, 0xFFFFFF);

        if (showCoords.isEnabled()) {
            String coords = String.format("§7X: §f%d §7Y: §f%d §7Z: §f%d",
                    (int) mc.thePlayer.posX,
                    (int) mc.thePlayer.posY,
                    (int) mc.thePlayer.posZ);
            mc.fontRendererObj.drawStringWithShadow(coords, this.x, this.y + 10, 0xFFFFFF);
        }
    }

    /**
     * Converts Minecraft yaw (0 = South, 90 = West, 180 = North, 270 = East)
     * to a cardinal direction string.
     */
    private String getCardinal(float yaw) {
        if (yaw >= 337.5f || yaw < 22.5f)  return "S";
        if (yaw < 67.5f)                    return "SW";
        if (yaw < 112.5f)                   return "W";
        if (yaw < 157.5f)                   return "NW";
        if (yaw < 202.5f)                   return "N";
        if (yaw < 247.5f)                   return "NE";
        if (yaw < 292.5f)                   return "E";
        return "SE";
    }

    @Override
    public int getWidth() {
        return mc.fontRendererObj.getStringWidth("Facing: SW (337°)");
    }

    @Override
    public int getHeight() {
        return showCoords.isEnabled() ? 20 : 10;
    }
}
