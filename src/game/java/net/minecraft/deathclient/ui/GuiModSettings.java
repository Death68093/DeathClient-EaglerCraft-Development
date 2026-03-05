package net.minecraft.deathclient.ui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.deathclient.settings.BooleanSetting;
import net.minecraft.deathclient.settings.ModeSetting;
import net.minecraft.deathclient.settings.NumberSetting;
import net.minecraft.deathclient.settings.Setting;

public class GuiModSettings extends GuiScreen {

    private GuiScreen parentScreen;
    private Mod mod;
    private Setting dragging = null;

    public GuiModSettings(GuiScreen parentScreen, Mod mod) {
        this.parentScreen = parentScreen;
        this.mod = mod;
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height - 30, "Back"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        // Header bar
        drawRect(0, 0, this.width, 35, 0xCC111111);
        drawRect(0, 33, this.width, 35, 0xFFFF0000);
        this.drawCenteredString(this.fontRendererObj, "§c" + mod.getName() + " §7Settings", this.width / 2, 10, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, "§7" + mod.getDescription(), this.width / 2, 22, 0xAAAAAA);

        int y = 55;

        for (Setting setting : mod.settings) {

            // ---- Boolean ----
            if (setting instanceof BooleanSetting) {
                BooleanSetting bool = (BooleanSetting) setting;
                boolean on = bool.isEnabled();
                boolean hovering = mouseX >= this.width / 2 - 100 && mouseX <= this.width / 2 + 100
                        && mouseY >= y - 2 && mouseY <= y + 12;

                drawRect(this.width / 2 - 100, y - 2, this.width / 2 + 100, y + 12,
                        hovering ? 0x88333333 : 0x44222222);
                drawRect(this.width / 2 - 100, y - 2, this.width / 2 - 98, y + 12,
                        on ? 0xFF00FF00 : 0xFFFF0000);

                this.fontRendererObj.drawStringWithShadow(bool.name, this.width / 2 - 90, y, 0xFFFFFF);
                String status = on ? "§aON" : "§cOFF";
                int statusW = this.fontRendererObj.getStringWidth(on ? "ON" : "OFF");
                this.fontRendererObj.drawStringWithShadow(status, this.width / 2 + 100 - statusW - 4, y, 0xFFFFFF);
            }

            // ---- Number (Slider) ----
            else if (setting instanceof NumberSetting) {
                NumberSetting num = (NumberSetting) setting;

                // Handle dragging BEFORE draw so the value text is up-to-date
                if (dragging == num) {
                    updateSlider(num, mouseX);
                }

                String valText = num.name + ": §e" + String.format("%.1f", num.getValue());
                this.drawCenteredString(this.fontRendererObj, valText, this.width / 2, y - 12, 0xFFFFFF);

                int sliderWidth = 150;
                int sliderX = this.width / 2 - sliderWidth / 2;

                double range = num.getMaximum() - num.getMinimum();
                double pct = (num.getValue() - num.getMinimum()) / range;

                // Track background
                Gui.drawRect(sliderX, y, sliderX + sliderWidth, y + 8, 0xFF333333);
                // Fill
                Gui.drawRect(sliderX, y, sliderX + (int) (sliderWidth * pct), y + 8, 0xFF0088FF);
                // Knob
                int knobX = sliderX + (int) (sliderWidth * pct);
                Gui.drawRect(knobX - 2, y - 2, knobX + 2, y + 10, 0xFFFFFFFF);
            }

            // ---- Mode (Cycle) ----
            else if (setting instanceof ModeSetting) {
                ModeSetting mode = (ModeSetting) setting;
                boolean hovering = mouseX >= this.width / 2 - 100 && mouseX <= this.width / 2 + 100
                        && mouseY >= y - 2 && mouseY <= y + 12;

                drawRect(this.width / 2 - 100, y - 2, this.width / 2 + 100, y + 12,
                        hovering ? 0x88333333 : 0x44222222);
                drawRect(this.width / 2 - 100, y - 2, this.width / 2 - 98, y + 12, 0xFF0088FF);

                this.fontRendererObj.drawStringWithShadow(mode.name, this.width / 2 - 90, y, 0xFFFFFF);
                String modeText = "§e< " + mode.getMode() + " >";
                int modeW = this.fontRendererObj.getStringWidth("< " + mode.getMode() + " >");
                this.fontRendererObj.drawStringWithShadow(modeText, this.width / 2 + 100 - modeW - 4, y, 0xFFFFFF);

                // NOTE: cycling happens in mouseClicked, NOT here
            }

            y += 40;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseButton != 0) return;

        int y = 55;
        for (Setting setting : mod.settings) {

            if (setting instanceof BooleanSetting) {
                if (mouseY >= y - 2 && mouseY <= y + 12
                        && mouseX >= this.width / 2 - 100 && mouseX <= this.width / 2 + 100) {
                    ((BooleanSetting) setting).toggle();
                }
            } else if (setting instanceof NumberSetting) {
                int sliderWidth = 150;
                int sliderX = this.width / 2 - sliderWidth / 2;
                if (mouseY >= y - 2 && mouseY <= y + 10
                        && mouseX >= sliderX && mouseX <= sliderX + sliderWidth) {
                    this.dragging = setting;
                    updateSlider((NumberSetting) setting, mouseX);
                }
            } else if (setting instanceof ModeSetting) {
                // BUG FIX: This was previously also in drawScreen(), causing it to fire every frame!
                if (mouseY >= y - 2 && mouseY <= y + 12
                        && mouseX >= this.width / 2 - 100 && mouseX <= this.width / 2 + 100) {
                    ((ModeSetting) setting).cycle();
                }
            }

            y += 40;
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (this.dragging instanceof NumberSetting) {
            updateSlider((NumberSetting) this.dragging, mouseX);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.dragging = null;
    }

    private void updateSlider(NumberSetting num, int mouseX) {
        int sliderWidth = 150;
        int sliderX = this.width / 2 - sliderWidth / 2;
        double pct = Math.max(0.0, Math.min(1.0, (double) (mouseX - sliderX) / sliderWidth));
        double range = num.getMaximum() - num.getMinimum();
        num.setValue(num.getMinimum() + range * pct);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }
}