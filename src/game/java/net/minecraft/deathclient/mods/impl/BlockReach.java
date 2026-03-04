package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.NumberSetting;

public class BlockReach extends Mod {
    
    // Default 5.0, Min 4.5 (vanilla is 4.5), Max 8.0
    public NumberSetting reachDistance = new NumberSetting("Distance", 5.0, 4.5, 8.0, 0.1);

    public BlockReach() {
        super("Block Reach", "Extends your range for placing and breaking blocks.", Category.PLAYER, true);
        this.addSetting(reachDistance);
    }
}