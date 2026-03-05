package net.minecraft.deathclient.events;

import net.minecraft.deathclient.DeathClient;
import net.minecraft.deathclient.mods.Mod;

public class EventKey extends Event {
    private int key;

    public EventKey(int key) {
        this.key = key;
    }

    public int getKey() { return key; }

    /**
     * Called when a key is pressed. Checks all mods for matching keybinds.
     * For hold-to-activate mods, enables them on key press.
     * For normal mods, toggles them on key press.
     *
     * NOTE: For hold-to-activate mods to work correctly, you must also create an
     * EventKeyRelease and call it from Minecraft.java on key release, which will
     * call setToggled(false) on matching mods with holdToActivate = true.
     */
    public void call() {
        if (DeathClient.getInstance() == null || DeathClient.getInstance().getModManager() == null) return;
        for (Mod mod : DeathClient.getInstance().getModManager().mods) {
            if (mod.getKey() == this.key && this.key != 0) {
                if (mod.isHoldToActivate()) {
                    mod.setToggled(true); // Enable while held
                } else {
                    mod.toggle(); // Normal toggle
                }
            }
        }
    }
}