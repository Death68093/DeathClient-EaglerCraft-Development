package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.mods.Mod;

/**
 * Scoreboard Toggle - Hides the in-game scoreboard sidebar.
 *
 * To fully suppress the scoreboard rendering, add the following two lines at the
 * very top of GuiIngame.renderScoreboard() in your MCP/Eaglercraft source:
 *
 *   if (net.minecraft.deathclient.DeathClient.getInstance() != null
 *           && net.minecraft.deathclient.DeathClient.getInstance().getModManager() != null) {
 *       Mod scoreToggle = net.minecraft.deathclient.DeathClient.getInstance()
 *               .getModManager().getModByName("Scoreboard Toggle");
 *       if (scoreToggle != null && scoreToggle.isToggled()) return;
 *   }
 *
 * Without that hook the toggle state is still tracked, so other code
 * (e.g. a resource-pack or shader) can check it too.
 */
public class ScoreboardToggle extends Mod {

    public ScoreboardToggle() {
        super("Scoreboard Toggle", "Hides the scoreboard sidebar.", Category.RENDER, false);
    }

    // No onUpdate/onRender needed — rendering suppression is done via the GuiIngame hook above.
}
