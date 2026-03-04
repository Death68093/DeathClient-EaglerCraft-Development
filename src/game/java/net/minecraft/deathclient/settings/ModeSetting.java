package net.minecraft.deathclient.settings;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting {
    public List<String> modes;
    public int index;

    public ModeSetting(String name, String defaultMode, String... modes) {
        super(name);
        this.modes = Arrays.asList(modes);
        this.index = this.modes.indexOf(defaultMode);
        if (this.index == -1) this.index = 0; // Fallback just in case
    }

    public String getMode() {
        return modes.get(index);
    }

    public boolean is(String mode) {
        return getMode().equalsIgnoreCase(mode);
    }

    public void cycle() {
        if (index < modes.size() - 1) {
            index++;
        } else {
            index = 0;
        }
    }
}