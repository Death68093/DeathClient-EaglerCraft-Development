package net.minecraft.deathclient.mods.impl;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.deathclient.events.EventRender3D;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.BooleanSetting;
import net.minecraft.deathclient.settings.NumberSetting;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

/**
 * XRay - Ore ESP implementation.
 * Scans blocks in a radius and renders coloured wireframe outlines around ores
 * through walls. True opaque-block removal requires hooking the chunk renderer,
 * which is not possible via events alone. This approach is fully functional as
 * an "Ore Finder" / "Ore Radar" and is effective in practice.
 */
public class XRay extends Mod {

    public NumberSetting scanRadius = new NumberSetting("Scan Radius", 10, 5, 20, 1);
    public BooleanSetting diamonds  = new BooleanSetting("Diamond",  true);
    public BooleanSetting gold      = new BooleanSetting("Gold",     true);
    public BooleanSetting iron      = new BooleanSetting("Iron",     true);
    public BooleanSetting coal      = new BooleanSetting("Coal",     false);
    public BooleanSetting emerald   = new BooleanSetting("Emerald",  true);
    public BooleanSetting lapis     = new BooleanSetting("Lapis",    true);
    public BooleanSetting redstone  = new BooleanSetting("Redstone", true);

    public XRay() {
        super("XRay", "Highlights ores through walls.", Category.RENDER, false);
        this.addSetting(scanRadius);
        this.addSetting(diamonds);
        this.addSetting(gold);
        this.addSetting(iron);
        this.addSetting(coal);
        this.addSetting(emerald);
        this.addSetting(lapis);
        this.addSetting(redstone);
    }

    @Override
    public void onRender3D(EventRender3D event) {
        if (mc.theWorld == null || mc.thePlayer == null) return;

        double renderX = mc.getRenderManager().viewerPosX;
        double renderY = mc.getRenderManager().viewerPosY;
        double renderZ = mc.getRenderManager().viewerPosZ;

        int radius = (int) scanRadius.getValue();
        int px = (int) mc.thePlayer.posX;
        int py = (int) mc.thePlayer.posY;
        int pz = (int) mc.thePlayer.posZ;

        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);

        for (int x = px - radius; x <= px + radius; x++) {
            for (int y = Math.max(0, py - radius); y <= Math.min(255, py + radius); y++) {
                for (int z = pz - radius; z <= pz + radius; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = mc.theWorld.getBlockState(pos).getBlock();

                    int color = getOreColor(block);
                    if (color == -1) continue;

                    float r = ((color >> 16) & 0xFF) / 255.0f;
                    float g = ((color >> 8)  & 0xFF) / 255.0f;
                    float b = (color         & 0xFF) / 255.0f;
                    GlStateManager.color(r, g, b, 1.0f);

                    double bx = x - renderX;
                    double by = y - renderY;
                    double bz = z - renderZ;
                    AxisAlignedBB aabb = new AxisAlignedBB(bx - 0.01, by - 0.01, bz - 0.01,
                                                           bx + 1.01, by + 1.01, bz + 1.01);
                    RenderGlobal.func_181561_a(aabb);
                }
            }
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
    }

    private int getOreColor(Block block) {
        if (diamonds.isEnabled()  && block == Blocks.diamond_ore)                             return 0x00FFFF;
        if (gold.isEnabled()      && block == Blocks.gold_ore)                                return 0xFFD700;
        if (iron.isEnabled()      && block == Blocks.iron_ore)                                return 0xC0C0C0;
        if (coal.isEnabled()      && block == Blocks.coal_ore)                                return 0x444444;
        if (emerald.isEnabled()   && block == Blocks.emerald_ore)                             return 0x00FF44;
        if (lapis.isEnabled()     && block == Blocks.lapis_ore)                               return 0x2255FF;
        if (redstone.isEnabled()  && (block == Blocks.redstone_ore || block == Blocks.lit_redstone_ore)) return 0xFF2222;
        return -1;
    }
}
