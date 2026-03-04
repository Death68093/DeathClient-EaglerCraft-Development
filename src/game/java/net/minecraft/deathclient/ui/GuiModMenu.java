package net.minecraft.deathclient.ui;

import java.io.IOException;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.deathclient.DeathClient;
import net.minecraft.deathclient.mods.Mod;

public class GuiModMenu extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Darkens the background behind the menu
        this.drawDefaultBackground();
        
        // Title
        this.fontRendererObj.drawStringWithShadow("DeathClient Mod Menu", this.width / 2 - 50, 20, 0xFFFFFF);
        
        int yOffset = 50;
        
        // Loop through all registered mods and draw them
        for (Mod mod : DeathClient.getInstance().getModManager().mods) {
            // Green (0x00FF00) if toggled on, Red (0xFF0000) if off
            int color = mod.isToggled() ? 0x00FF00 : 0xFF0000; 
            
            this.fontRendererObj.drawStringWithShadow(mod.getName(), this.width / 2 - 40, yOffset, color);
            yOffset += 15;
        }
        
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        int yOffset = 50;
        
        for (Mod mod : net.minecraft.deathclient.DeathClient.getInstance().getModManager().mods) {
            // Check if the mouse is hovering over this specific mod's text
            if (mouseX >= this.width / 2 - 40 && mouseX <= this.width / 2 + 40 && mouseY >= yOffset && mouseY <= yOffset + 10) {
                
                if (mouseButton == 0) {
                    // Left Click: Toggle the mod
                    mod.toggle(); 
                } 
                else if (mouseButton == 1) {
                    // Right Click: Open the Settings GUI, passing the current screen and the selected mod
                    this.mc.displayGuiScreen(new GuiModSettings(this, mod));
                }
            }
            yOffset += 15;
        }
        
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}