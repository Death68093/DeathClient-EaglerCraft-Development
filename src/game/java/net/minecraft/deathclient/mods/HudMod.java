package net.minecraft.deathclient.mods;

import net.minecraft.client.Minecraft;

public class HudMod extends Mod {
    public int x = 2;
    public int y = 2;
    
    // Dragging math variables
    private boolean dragging;
    private int dragX, dragY;

    public HudMod(String name, String description, Category category) {
        // Hud mods don't usually need a default keybind, so we pass 0 (NONE) if your constructor supports it, 
        // or just rely on your existing constructor.
        super(name, description, category, false);
    }

    // What the mod actually draws on the screen (Override this in your specific mods)
    public void draw() { }

    // How wide and tall the text/element is (Needed for the dragging hitbox!)
    public int getWidth() { return 50; }
    public int getHeight() { return Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT; }

    // This runs inside the HUD Editor to handle the dragging math
    public void renderDummy(int mouseX, int mouseY) {
        if (dragging) {
            this.x = mouseX - dragX;
            this.y = mouseY - dragY;
        }
        this.draw();
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0 && mouseX >= x && mouseX <= x + getWidth() && mouseY >= y && mouseY <= y + getHeight()) {
            dragging = true;
            dragX = mouseX - x;
            dragY = mouseY - y;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        dragging = false;
    }
}