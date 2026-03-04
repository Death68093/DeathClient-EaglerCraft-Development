package net.minecraft.deathclient.events;

import net.minecraft.deathclient.DeathClient;
import net.minecraft.deathclient.mods.Mod;

public class EventRender2D extends Event {
    
    // This method fires the event and tells all active mods to draw on the screen
    public void call() {
        if (DeathClient.getInstance() != null && DeathClient.getInstance().getModManager() != null) {
            for (Mod mod : DeathClient.getInstance().getModManager().mods) {
                if (mod.isToggled()) {
                    mod.onRender2D(this);
                }
            }
        }
    }
}