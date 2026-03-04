package net.minecraft.deathclient.ui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.deathclient.DeathClient;
import net.minecraft.deathclient.mods.HudMod;
import net.minecraft.deathclient.mods.Mod;

public class GuiHudEditor extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        
        // Draw some instructions
        this.drawCenteredString(this.fontRendererObj, "Click and drag elements to move them!", this.width / 2, 20, 0xFFFFFF);

        if (DeathClient.getInstance().getModManager() != null) {
            for (Mod mod : DeathClient.getInstance().getModManager().mods) {
                if (mod.isToggled() && mod instanceof HudMod) {
                    HudMod hudMod = (HudMod) mod;
                    
                    // Draw the element and handle its dragging logic
                    hudMod.renderDummy(mouseX, mouseY);
                    
                    // Draw a subtle highlighted box around the element so we know it's movable
                    drawRect(hudMod.x - 2, hudMod.y - 2, hudMod.x + hudMod.getWidth() + 2, hudMod.y + hudMod.getHeight() + 2, 0x44FFFFFF);
                }
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (DeathClient.getInstance().getModManager() != null) {
            for (Mod mod : DeathClient.getInstance().getModManager().mods) {
                if (mod.isToggled() && mod instanceof HudMod) {
                    ((HudMod) mod).mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (DeathClient.getInstance().getModManager() != null) {
            for (Mod mod : DeathClient.getInstance().getModManager().mods) {
                if (mod.isToggled() && mod instanceof HudMod) {
                    ((HudMod) mod).mouseReleased(mouseX, mouseY, state);
                }
            }
        }
    }
}