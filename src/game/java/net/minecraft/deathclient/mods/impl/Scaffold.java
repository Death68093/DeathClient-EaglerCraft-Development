package net.minecraft.deathclient.mods.impl;

import net.minecraft.block.Block;
import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

public class Scaffold extends Mod {

    public Scaffold() {
        super("Scaffold", "Automatically places blocks under you.", Category.WORLD, false);
    }

    @Override
    public void onUpdate(EventUpdate event) {
        // 1. Find the position directly under our feet
        BlockPos blockUnder = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);

        // 2. Only act if there is currently air under us
        if (mc.theWorld.getBlockState(blockUnder).getBlock() == Blocks.air) {
            
            // 3. Find a valid block in the hotbar
            int blockSlot = getBlockSlot();
            if (blockSlot == -1) return; // No blocks found!

            // Save old slot to switch back later
            int oldSlot = mc.thePlayer.inventory.currentItem;
            mc.thePlayer.inventory.currentItem = blockSlot;

            // 4. Find a block neighbor to click against
            BlockData data = getBlockData(blockUnder);
            if (data != null) {
                // 5. Place the block!
                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem(), data.pos, data.face, getVec3(data.pos, data.face))) {
                    mc.thePlayer.swingItem();
                }
            }

            // Switch back to the item we were holding
            mc.thePlayer.inventory.currentItem = oldSlot;
        }
    }

    private int getBlockSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof ItemBlock) {
                return i;
            }
        }
        return -1;
    }

    // Helper class to store which block we are clicking and on which side
    private class BlockData {
        public BlockPos pos;
        public EnumFacing face;
        public BlockData(BlockPos pos, EnumFacing face) {
            this.pos = pos;
            this.face = face;
        }
    }

    private BlockData getBlockData(BlockPos pos) {
        if (mc.theWorld.getBlockState(pos.add(0, -1, 0)).getBlock() != Blocks.air)
            return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
        if (mc.theWorld.getBlockState(pos.add(-1, 0, 0)).getBlock() != Blocks.air)
            return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
        if (mc.theWorld.getBlockState(pos.add(1, 0, 0)).getBlock() != Blocks.air)
            return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
        if (mc.theWorld.getBlockState(pos.add(0, 0, -1)).getBlock() != Blocks.air)
            return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
        if (mc.theWorld.getBlockState(pos.add(0, 0, 1)).getBlock() != Blocks.air)
            return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
        return null;
    }

    private Vec3 getVec3(BlockPos pos, EnumFacing face) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.5;
        double z = pos.getZ() + 0.5;
        x += (double)face.getFrontOffsetX() * 0.5;
        y += (double)face.getFrontOffsetY() * 0.5;
        z += (double)face.getFrontOffsetZ() * 0.5;
        return new Vec3(x, y, z);
    }
}