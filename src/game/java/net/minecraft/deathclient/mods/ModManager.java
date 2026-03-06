package net.minecraft.deathclient.mods;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.deathclient.events.Event;
import net.minecraft.deathclient.events.EventUpdate;

public class ModManager {
    public List<Mod> mods = new ArrayList<>();

    public ModManager() {
        // ---- MOVEMENT ----
        mods.add(new net.minecraft.deathclient.mods.impl.Fly());

        // ---- PLAYER ----
        mods.add(new net.minecraft.deathclient.mods.impl.Eagle());
        mods.add(new net.minecraft.deathclient.mods.impl.ToggleSprint());
        mods.add(new net.minecraft.deathclient.mods.impl.ToggleSneak());
        mods.add(new net.minecraft.deathclient.mods.impl.NoFall());
        mods.add(new net.minecraft.deathclient.mods.impl.BlockReach());

        // ---- COMBAT ----
        mods.add(new net.minecraft.deathclient.mods.impl.KillAura());
        mods.add(new net.minecraft.deathclient.mods.impl.Aimbot());
        mods.add(new net.minecraft.deathclient.mods.impl.CombatReach());
        mods.add(new net.minecraft.deathclient.mods.impl.PlayerESP());
        mods.add(new net.minecraft.deathclient.mods.impl.ESP());

        // ---- RENDER ----
        mods.add(new net.minecraft.deathclient.mods.impl.Fullbright());
        mods.add(new net.minecraft.deathclient.mods.impl.XRay());
        mods.add(new net.minecraft.deathclient.mods.impl.BlockESP());
        mods.add(new net.minecraft.deathclient.mods.impl.Trajectories());
        mods.add(new net.minecraft.deathclient.mods.impl.TNTTimer());
        mods.add(new net.minecraft.deathclient.mods.impl.WAILA());
        mods.add(new net.minecraft.deathclient.mods.impl.NoDynamicFOV());
        mods.add(new net.minecraft.deathclient.mods.impl.FPSBoost());
        mods.add(new net.minecraft.deathclient.mods.impl.ScoreboardToggle());

        // ---- WORLD ----
        mods.add(new net.minecraft.deathclient.mods.impl.Scaffold());

        // ---- HUD ----
        mods.add(new net.minecraft.deathclient.mods.impl.HUD());
        mods.add(new net.minecraft.deathclient.mods.impl.ArmorHUD());
        mods.add(new net.minecraft.deathclient.mods.impl.ReachDisplay());
        mods.add(new net.minecraft.deathclient.mods.impl.Compass());
    }

    public Mod getModByName(String name) {
        for (Mod mod : mods) {
            if (mod.getName().equalsIgnoreCase(name)) return mod;
        }
        return null;
    }

    public void onEvent(Event e) {
        for (Mod mod : mods) {
            if (mod.isToggled() && e instanceof EventUpdate) {
                mod.onUpdate((EventUpdate) e);
            }
        }
    }
}
