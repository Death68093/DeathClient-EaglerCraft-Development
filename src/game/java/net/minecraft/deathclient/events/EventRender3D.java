package net.minecraft.deathclient.events;

import net.minecraft.deathclient.DeathClient;
import net.minecraft.deathclient.mods.Mod;

public class EventRender3D extends Event {
    public float partialTicks;

    public EventRender3D(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public void call() {
        if (DeathClient.getInstance() != null && DeathClient.getInstance().getModManager() != null) {
            for (Mod mod : DeathClient.getInstance().getModManager().mods) {
                if (mod.isToggled()) {
                    // We will add this method to your Mod class in Step 3!
                    mod.onRender3D(this); 
                }
            }
        }
    }
}