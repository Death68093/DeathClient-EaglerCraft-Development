package net.minecraft.deathclient.ui;

import java.io.IOException;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.BooleanSetting;
import net.minecraft.deathclient.settings.NumberSetting;
import net.minecraft.deathclient.settings.Setting;

public class GuiModSettings extends GuiScreen {

    private GuiScreen parentScreen;
    private Mod mod;
    private Setting dragging = null; // Tracks if we are dragging a slider

    public GuiModSettings(GuiScreen parentScreen, Mod mod) {
        this.parentScreen = parentScreen;
        this.mod = mod;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 30, "Back to Modules"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, mod.getName() + " Settings", this.width / 2, 20, 0xFFFFFF);

        int y = 60;
        
        // Loop through all settings for this mod
        for (Setting setting : mod.settings) {
            
            if (setting instanceof BooleanSetting) {
                BooleanSetting bool = (BooleanSetting) setting;
                String text = bool.name + ": " + (bool.isEnabled() ? "ON" : "OFF");
                int color = bool.isEnabled() ? 0x00FF00 : 0xFF0000;
                this.drawCenteredString(this.fontRendererObj, text, this.width / 2, y, color);
            } 
            else if (setting instanceof NumberSetting) {
                NumberSetting num = (NumberSetting) setting;
                String text = num.name + ": " + num.getValue();
                this.drawCenteredString(this.fontRendererObj, text, this.width / 2, y - 12, 0xFFFFFF);
                
                int sliderWidth = 100;
                int sliderX = this.width / 2 - (sliderWidth / 2);
                
                // Draw Slider Background (Dark Gray)
                Gui.drawRect(sliderX, y, sliderX + sliderWidth, y + 6, 0xFF555555);
                
                // Calculate how full the slider should be
                double range = num.getMaximum() - num.getMinimum();
                double percentage = (num.getValue() - num.getMinimum()) / range;
                
                // Draw Slider Fill (Blue)
                Gui.drawRect(sliderX, y, sliderX + (int)(sliderWidth * percentage), y + 6, 0xFF00AAFF);
            } else if (setting instanceof net.minecraft.deathclient.settings.ModeSetting) {
                    if (mouseY >= y && mouseY <= y + 10 && mouseX >= this.width / 2 - 50 && mouseX <= this.width / 2 + 50) {
                        ((net.minecraft.deathclient.settings.ModeSetting) setting).cycle();
                    }
                }
            
            y += 40; // Space between settings
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton == 0) { // Left Click
            int y = 60;
            for (Setting setting : mod.settings) {
                
                if (setting instanceof BooleanSetting) {
                    // Check if mouse clicked the boolean text area
                    if (mouseY >= y && mouseY <= y + 10 && mouseX >= this.width / 2 - 50 && mouseX <= this.width / 2 + 50) {
                        ((BooleanSetting) setting).toggle();
                    }
                } 
                else if (setting instanceof NumberSetting) {
                    int sliderWidth = 100;
                    int sliderX = this.width / 2 - (sliderWidth / 2);
                    
                    // Check if mouse clicked the slider bar
                    if (mouseY >= y && mouseY <= y + 6 && mouseX >= sliderX && mouseX <= sliderX + sliderWidth) {
                        this.dragging = setting;
                        updateSlider((NumberSetting) setting, mouseX); // Snap value to click
                    }
                } else if (setting instanceof net.minecraft.deathclient.settings.ModeSetting) {
                    if (mouseY >= y && mouseY <= y + 10 && mouseX >= this.width / 2 - 50 && mouseX <= this.width / 2 + 50) {
                        ((net.minecraft.deathclient.settings.ModeSetting) setting).cycle();
                    }
                }
                y += 40;
                
            }
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        // If we are holding down the mouse on a NumberSetting, update its value!
        if (this.dragging instanceof NumberSetting) {
            updateSlider((NumberSetting) this.dragging, mouseX);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.dragging = null; // Stop dragging when mouse is let go
    }

    // Math to convert mouse position into a NumberSetting value
    private void updateSlider(NumberSetting num, int mouseX) {
        int sliderWidth = 100;
        int sliderX = this.width / 2 - (sliderWidth / 2);
        
        double percentage = (double)(mouseX - sliderX) / (double)sliderWidth;
        percentage = Math.max(0.0D, Math.min(1.0D, percentage)); // Clamp between 0% and 100%
        
        double range = num.getMaximum() - num.getMinimum();
        num.setValue(num.getMinimum() + (range * percentage));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }
}