package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.NumberSetting;

public class CombatReach extends Mod {
    
    // Name, Default Value, Minimum, Maximum, Increment (0.1 means 1 decimal place)
    public NumberSetting reachDistance = new NumberSetting("Distance", 4.0, 3.0, 6.0, 0.1);

    public CombatReach() {
        super("Combat Reach", "Extends your attack range against entities.", Category.COMBAT, true);
        this.addSetting(reachDistance); // Register the setting!
    }
}