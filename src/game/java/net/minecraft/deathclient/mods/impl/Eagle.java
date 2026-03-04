package net.minecraft.deathclient.mods.impl;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockPos;

public class Eagle extends Mod {

    public net.minecraft.deathclient.settings.BooleanSetting onlyBlocks = new net.minecraft.deathclient.settings.BooleanSetting("Only Holding Blocks", true);
    public net.minecraft.deathclient.settings.ModeSetting condition = new net.minecraft.deathclient.settings.ModeSetting("Condition", "Always", "Always", "While Sneaking", "While Blocking");

    public Eagle() {
        super("Eagle", "Automatically sneaks at the edge of blocks.", Category.PLAYER, false);
        this.addSetting(onlyBlocks);
        this.addSetting(condition);
    }

    @Override
    public void onUpdate(EventUpdate event) {
        if (mc.thePlayer == null || mc.theWorld == null) return;

        // 1. Check "Only Holding Blocks" setting
        if (this.onlyBlocks.isEnabled()) {
            if (mc.thePlayer.getCurrentEquippedItem() == null || !(mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock)) {
                return; // Stop right here if we aren't holding a block!
            }
        }

        // 2. Check the "Condition" setting
        boolean shouldEagle = false;
        String currentMode = this.condition.getMode();

        if (currentMode.equals("Always")) {
            shouldEagle = true;
        } else if (currentMode.equals("While Sneaking")) {
            // Only activate if the user is actually pressing their sneak key
            shouldEagle = mc.gameSettings.keyBindSneak.isKeyDown(); 
        } else if (currentMode.equals("While Blocking")) {
            // Only activate if they are holding right-click with a sword/shield
            shouldEagle = mc.thePlayer.isUsingItem();
        }

        // 3. The Core Eagle Logic
        if (shouldEagle && mc.thePlayer.onGround) {
            // Get the block directly underneath the player
            BlockPos posBelow = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0D, mc.thePlayer.posZ);
            boolean overAir = mc.theWorld.getBlockState(posBelow).getBlock() == Blocks.air;

            if (overAir) {
                // If we are over air, force the player to sneak so they don't fall
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), true);
            } else {
                // If we are safely on a block, force them to un-sneak so they walk fast!
                // (Only un-sneak them if they aren't physically holding the key in "Always" or "While Blocking" modes)
                if (!mc.gameSettings.keyBindSneak.isKeyDown()) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        // When we turn the mod off, make sure we don't get stuck sneaking
        if (mc.thePlayer != null && !mc.gameSettings.keyBindSneak.isKeyDown()) {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
        }
    }
}