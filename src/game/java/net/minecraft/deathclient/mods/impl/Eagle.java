package net.minecraft.deathclient.mods.impl;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.item.ItemBlock;

public class Eagle extends Mod {

    public Eagle() {
        // False for isCheat, as it simulates standard key inputs
        super("Eagle", "Automatically sneaks at the edge of blocks.", Category.MOVEMENT, false);
    }

    @Override
    public void onUpdate(EventUpdate event) {
        // Only trigger if the player is holding a block
        if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock) {
            
            // Check the block directly below the player
            BlockPos blockBelow = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
            
            if (mc.theWorld.getBlockState(blockBelow).getBlock() == Blocks.air) {
                // We are over the edge, press the sneak key
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
            } else {
                // We are safely on a block, release the sneak key
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
            }
        }
    }

    @Override
    protected void onDisable() {
        // Ensure we don't get stuck sneaking when we turn the mod off
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
    }
}