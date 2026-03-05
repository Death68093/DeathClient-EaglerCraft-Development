package net.minecraft.deathclient.events;

import net.minecraft.deathclient.DeathClient;
import net.minecraft.deathclient.mods.Mod;

/**
 * Fired when a key is RELEASED. Used to disable hold-to-activate mods.
 *
 * To hook this up, in your Minecraft.java (or wherever key events are handled),
 * add alongside your EventKey call:
 *   new EventKeyRelease(keyCode).call();
 */
public class EventKeyRelease extends Event {
    private int key;

    public EventKeyRelease(int key) {
        this.key = key;
    }

    public int getKey() { return key; }

    public void call() {
        if (DeathClient.getInstance() == null || DeathClient.getInstance().getModManager() == null) return;
        for (Mod mod : DeathClient.getInstance().getModManager().mods) {
            if (mod.getKey() == this.key && this.key != 0 && mod.isHoldToActivate()) {
                mod.setToggled(false); // Disable when key is released
            }
        }
    }
}