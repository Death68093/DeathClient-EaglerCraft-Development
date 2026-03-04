package net.minecraft.deathclient.mods;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.deathclient.events.Event;
import net.minecraft.deathclient.events.EventUpdate;

public class ModManager {
    public List<Mod> mods = new ArrayList<>();

    public ModManager() {
        // Register mods
        mods.add(new net.minecraft.deathclient.mods.impl.Fullbright());
        mods.add(new net.minecraft.deathclient.mods.impl.ToggleSprint());
        mods.add(new net.minecraft.deathclient.mods.impl.Eagle());
        mods.add(new net.minecraft.deathclient.mods.impl.Fly());
        mods.add(new net.minecraft.deathclient.mods.impl.NoFall());
        mods.add(new net.minecraft.deathclient.mods.impl.KillAura());
        mods.add(new net.minecraft.deathclient.mods.impl.Aimbot());
        mods.add(new net.minecraft.deathclient.mods.impl.BlockReach());
        mods.add(new net.minecraft.deathclient.mods.impl.CombatReach());
        mods.add(new net.minecraft.deathclient.mods.impl.HUD());
    }

    // Add this method anywhere inside your ModManager class
    public Mod getModByName(String name) {
        for (Mod mod : mods) {
            if (mod.getName().equalsIgnoreCase(name)) {
                return mod;
            }
        }
        return null; // Return null if the mod doesn't exist
    }

    public void onEvent(Event e) {
        for (Mod mod : mods) {
            if (mod.isToggled()) {
                if (e instanceof EventUpdate) {
                    mod.onUpdate((EventUpdate) e);
                }
                // You can add more event types here later (like EventRender, EventPacket, etc.)
            }
        }
    }
}