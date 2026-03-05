package net.minecraft.deathclient;

import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.mods.ModManager;

public class DeathClient {

    private static DeathClient instance = new DeathClient();

    public final String CLIENT_NAME    = "DeathClient";
    public final String CLIENT_VERSION = "1.0.1";
    public final String CREATOR        = "Death68093";

    private ModManager modManager;
    private boolean legitMode = false;

    public static DeathClient getInstance() {
        return instance;
    }

    public void startClient() {
        System.out.println("Starting " + CLIENT_NAME + " v" + CLIENT_VERSION + " by " + CREATOR);
        modManager = new ModManager();
    }

    public ModManager getModManager() {
        return modManager;
    }

    public boolean isLegitMode() {
        return legitMode;
    }

    public void setLegitMode(boolean legitMode) {
        this.legitMode = legitMode;

        // When Legit Mode is turned ON, automatically disable all cheat mods
        if (legitMode && modManager != null) {
            for (Mod mod : modManager.mods) {
                if (mod.isCheat() && mod.isToggled()) {
                    mod.setToggled(false); // force disable (setToggled false is always allowed)
                }
            }
        }
    }
}