package net.minecraft.deathclient.mods;

import net.minecraft.client.Minecraft;



public class Mod {
    protected Minecraft mc = Minecraft.getMinecraft();
    public java.util.List<net.minecraft.deathclient.settings.Setting> settings = new java.util.ArrayList<>();
    private String name;
    private String description;
    private Category category;
    private boolean toggled;
    private boolean isCheat; // Used for your Legit/Cracked mode feature
    private int key; // The LWJGL key code (e.g., Keyboard.KEY_R)

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }
    public Mod(String name, String description, Category category, boolean isCheat) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.isCheat = isCheat;
        this.toggled = false;
    }

    public void addSetting(net.minecraft.deathclient.settings.Setting setting) {
        this.settings.add(setting);
    }

    public void onUpdate(net.minecraft.deathclient.events.EventUpdate event) {
        // Override this in specific mod classes that need to run code every tick
    }

    public void onRender2D(net.minecraft.deathclient.events.EventRender2D event) {
        // Override this in specific mod classes that need to draw on the screen
    }
    public void onRender3D(net.minecraft.deathclient.events.EventRender3D event) {}

    public net.minecraft.deathclient.settings.Setting getSettingByName(String name) {
        for (net.minecraft.deathclient.settings.Setting setting : this.settings) {
            if (setting.name.equalsIgnoreCase(name)) {
                return setting;
            }
        }
        return null; // Return null if the setting isn't found
    }

    public void toggle() {
        // Prevent enabling if it's a cheat and Legit Mode is active
        if (isCheat && net.minecraft.deathclient.DeathClient.getInstance().isLegitMode()) {
            return; 
        }
        
        this.toggled = !this.toggled;
        if (this.toggled) {
            onEnable();
        } else {
            onDisable();
        }
    }

    protected void onEnable() {
        // Override in specific mod classes
    }

    protected void onDisable() {
        // Override in specific mod classes
    }

    public String getName() { return name; }
    public boolean isToggled() { return toggled; }
    public boolean isCheat() { return isCheat; }
    
    public enum Category {
        MOVEMENT, RENDER, PLAYER, COMBAT, WORLD, HUD
    }
}