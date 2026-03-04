package net.minecraft.deathclient;

import net.minecraft.deathclient.mods.ModManager;

public class DeathClient {
    
    private static DeathClient instance = new DeathClient();
    
    public final String CLIENT_NAME = "DeathClient";
    public final String CLIENT_VERSION = "1.0.1";
    public final String CREATOR = "Death68093";
    
    private ModManager modManager;
    private boolean legitMode = false; // Toggle for Cracked/Legit modes

    public static DeathClient getInstance() {
        return instance;
    }

    public void startClient() {
        System.out.println("Starting " + CLIENT_NAME + " v" + CLIENT_VERSION + " by " + CREATOR);
        
        // Initialize systems
        modManager = new ModManager();
        
        // TODO: Initialize Event System, GUI, and Config Manager
    }
    
    public ModManager getModManager() {
        return modManager;
    }

    public boolean isLegitMode() {
        return legitMode;
    }

    public void setLegitMode(boolean legitMode) {
        this.legitMode = legitMode;
        // Logic to automatically disable "cracked" mods when legit mode is turned on
    }
}