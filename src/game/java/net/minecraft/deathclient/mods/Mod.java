package net.minecraft.deathclient.mods;

import net.minecraft.client.Minecraft;

public class Mod {
    protected Minecraft mc = Minecraft.getMinecraft();
    public java.util.List<net.minecraft.deathclient.settings.Setting> settings = new java.util.ArrayList<>();
    private String name;
    private String description;
    private Category category;
    private boolean toggled;
    private boolean isCheat;
    private int key;
    private boolean holdToActivate; // If true, mod only activates while key is held down

    public int getKey() { return key; }
    public void setKey(int key) { this.key = key; }
    public boolean isHoldToActivate() { return holdToActivate; }
    public void setHoldToActivate(boolean holdToActivate) { this.holdToActivate = holdToActivate; }

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

    public void onUpdate(net.minecraft.deathclient.events.EventUpdate event) {}
    public void onRender2D(net.minecraft.deathclient.events.EventRender2D event) {}
    public void onRender3D(net.minecraft.deathclient.events.EventRender3D event) {}

    public net.minecraft.deathclient.settings.Setting getSettingByName(String name) {
        for (net.minecraft.deathclient.settings.Setting setting : this.settings) {
            if (setting.name.equalsIgnoreCase(name)) return setting;
        }
        return null;
    }

    /** Called when a key is pressed. Respects holdToActivate mode. */
    public void toggle() {
        if (isCheat && net.minecraft.deathclient.DeathClient.getInstance().isLegitMode()) {
            // If already on and legit mode just activated, turn it off
            if (this.toggled) setToggled(false);
            return;
        }
        setToggled(!this.toggled);
    }

    /** Directly sets toggled state. Use this instead of toggle() when forcing a state. */
    public void setToggled(boolean value) {
        if (value == this.toggled) return;
        if (value && isCheat && net.minecraft.deathclient.DeathClient.getInstance().isLegitMode()) return;
        this.toggled = value;
        if (this.toggled) onEnable();
        else onDisable();
    }

    protected void onEnable() {}
    protected void onDisable() {}

    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isToggled() { return toggled; }
    public boolean isCheat() { return isCheat; }
    public Category getCategory() { return category; }

    public enum Category {
        MOVEMENT, RENDER, PLAYER, COMBAT, WORLD, HUD
    }
}