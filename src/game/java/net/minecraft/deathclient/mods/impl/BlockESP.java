package net.minecraft.deathclient.mods.impl;

import net.lax1dude.eaglercraft.v1_8.opengl.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.deathclient.events.EventRender3D;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.BooleanSetting;
import net.minecraft.deathclient.settings.ModeSetting;
import net.minecraft.deathclient.settings.NumberSetting;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class BlockESP extends Mod {

    // Whitelist = only show selected blocks; Blacklist = show everything except selected blocks
    public ModeSetting     mode          = new ModeSetting("Mode",     "Whitelist", "Whitelist", "Blacklist");
    public NumberSetting   scanRadius    = new NumberSetting("Radius", 15, 5, 30, 1);
    // Whitelist block toggles
    public BooleanSetting  chests        = new BooleanSetting("Chests",          true);
    public BooleanSetting  spawners      = new BooleanSetting("Spawners",        true);
    public BooleanSetting  furnaces      = new BooleanSetting("Furnaces",        false);
    public BooleanSetting  craftingTables= new BooleanSetting("Crafting Tables", false);
    public BooleanSetting  beds          = new BooleanSetting("Beds",            false);
    public BooleanSetting  enderChests   = new BooleanSetting("Ender Chests",    true);
    // Color
    public NumberSetting   red           = new NumberSetting("R", 255, 0, 255, 1);
    public NumberSetting   green         = new NumberSetting("G", 165, 0, 255, 1);
    public NumberSetting   blue          = new NumberSetting("B", 0,   0, 255, 1);

    public BlockESP() {
        super("Block ESP", "Highlights specific blocks through walls.", Category.RENDER, false);
        this.addSetting(mode);
        this.addSetting(scanRadius);
        this.addSetting(chests);
        this.addSetting(spawners);
        this.addSetting(furnaces);
        this.addSetting(craftingTables);
        this.addSetting(beds);
        this.addSetting(enderChests);
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

        int radius = (int) scanRadius.getValue();
        int px = (int) mc.thePlayer.posX;
        int py = (int) mc.thePlayer.posY;
        int pz = (int) mc.thePlayer.posZ;

        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GlStateManager.color(
                (float) red.getValue()   / 255f,
                (float) green.getValue() / 255f,
                (float) blue.getValue()  / 255f, 1.0f);

        for (int x = px - radius; x <= px + radius; x++) {
            for (int y = Math.max(0, py - radius); y <= Math.min(255, py + radius); y++) {
                for (int z = pz - radius; z <= pz + radius; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = mc.theWorld.getBlockState(pos).getBlock();
                    if (!shouldHighlight(block)) continue;

                    double bx = x - renderX, by = y - renderY, bz = z - renderZ;
                    AxisAlignedBB aabb = new AxisAlignedBB(
                            bx - 0.01, by - 0.01, bz - 0.01,
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

    private boolean shouldHighlight(Block block) {
        if (block == Blocks.air) return false;

        if (mode.is("Whitelist")) {
            if (chests.isEnabled()         && (block == Blocks.chest || block == Blocks.trapped_chest)) return true;
            if (spawners.isEnabled()        && block == Blocks.mob_spawner)                              return true;
            if (furnaces.isEnabled()        && (block == Blocks.furnace || block == Blocks.lit_furnace)) return true;
            if (craftingTables.isEnabled()  && block == Blocks.crafting_table)                           return true;
            if (beds.isEnabled()            && block == Blocks.bed)                                      return true;
            if (enderChests.isEnabled()     && block == Blocks.ender_chest)                              return true;
            return false;
        } else {
            // Blacklist: hide common terrain, show everything else
            return block != Blocks.stone && block != Blocks.dirt && block != Blocks.grass
                && block != Blocks.gravel && block != Blocks.sand && block != Blocks.water
                && block != Blocks.lava   && block != Blocks.bedrock && block != Blocks.flowing_water
                && block != Blocks.flowing_lava && block != Blocks.netherrack;
        }
    }
}
