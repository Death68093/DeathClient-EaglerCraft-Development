package net.minecraft.deathclient.events;

public class EventKey extends Event {
    private int key;

    public EventKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

    // Add this method so Minecraft.java doesn't crash!
    public void call() {
        // We will use this later if we need mods to run specific code the moment a key is pressed
    }
}