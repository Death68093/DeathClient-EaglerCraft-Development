package net.minecraft.deathclient.api;

public class BridgeHelper {
    // This is a "functional interface" - basically a placeholder
    public interface TickListener {
        void onTick();
    }

    public static TickListener tickHandler = null;

    public static void update() {
        if (tickHandler != null) {
            tickHandler.onTick();
        }
    }
}