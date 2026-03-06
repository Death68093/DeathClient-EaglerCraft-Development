package net.minecraft.deathclient.ui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.deathclient.DeathClient;
import net.minecraft.deathclient.mods.Mod;
import net.lax1dude.eaglercraft.v1_8.Keyboard;

public class GuiModMenu extends GuiScreen {

    private Mod.Category selectedCategory = Mod.Category.MOVEMENT;
    private static final int HEADER_H   = 38;
    private static final int FOOTER_H   = 20;
    private static final int CAT_W      = 85;
    private static final int MOD_H      = 20;
    private static final int MOD_MARGIN = 4;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // Full dark overlay
        drawRect(0, 0, this.width, this.height, 0xCC000000);

        // === HEADER ===
        drawRect(0, 0, this.width, HEADER_H, 0xDD0A0A0A);
        // Red accent line at bottom of header
        drawGradientRect(0, HEADER_H - 2, this.width, HEADER_H, 0xFFCC0000, 0xFF550000);
        this.drawCenteredString(this.fontRendererObj,
                "§c§l" + DeathClient.getInstance().CLIENT_NAME + " §r§7v"
                        + DeathClient.getInstance().CLIENT_VERSION,
                this.width / 2, 9, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj,
                "§7by §f" + DeathClient.getInstance().CREATOR,
                this.width / 2, 21, 0x888888);

        // === CATEGORY SIDEBAR ===
        drawRect(0, HEADER_H, CAT_W, this.height - FOOTER_H, 0xDD0A0A0A);

        Mod.Category[] cats = Mod.Category.values();
        for (int i = 0; i < cats.length; i++) {
            Mod.Category cat = cats[i];
            int cy = HEADER_H + 6 + i * 24;
            boolean sel = cat == selectedCategory;
            boolean hov = mouseX >= 4 && mouseX <= CAT_W - 4
                    && mouseY >= cy && mouseY <= cy + 19;

            int bg = sel ? 0xBB550000 : (hov ? 0x77222222 : 0x33111111);
            drawRect(4, cy, CAT_W - 4, cy + 19, bg);

            // Left accent strip
            if (sel) drawRect(4, cy, 6, cy + 19, 0xFFCC0000);

            // Mod count badge
            int count = getModsForCategory(cat).size();
            String catLabel = cat.name().substring(0, 1) + cat.name().substring(1).toLowerCase();
            int textColor = sel ? 0xFFFFFF : (hov ? 0xCCCCCC : 0x777777);
            this.fontRendererObj.drawStringWithShadow(catLabel, 12, cy + 6, textColor);

            String badge = "§8(" + count + ")";
            int badgeW = this.fontRendererObj.getStringWidth("(" + count + ")");
            this.fontRendererObj.drawStringWithShadow(badge, CAT_W - 8 - badgeW, cy + 6, 0xFFFFFF);
        }

        // === MOD LIST ===
        int contentX = CAT_W + 8;
        int contentW = this.width - CAT_W - 16;
        List<Mod> mods = getModsForCategory(selectedCategory);

        // Sub-header for selected category
        drawRect(CAT_W, HEADER_H, this.width, HEADER_H + 18, 0xCC111111);
        String catTitle = selectedCategory.name().substring(0, 1)
                + selectedCategory.name().substring(1).toLowerCase()
                + " §7(" + mods.size() + " mods)";
        this.fontRendererObj.drawStringWithShadow("§f" + catTitle, contentX, HEADER_H + 5, 0xFFFFFF);

        int startY = HEADER_H + 22;
        for (int i = 0; i < mods.size(); i++) {
            Mod mod = mods.get(i);
            int my = startY + i * (MOD_H + MOD_MARGIN);
            boolean on  = mod.isToggled();
            boolean hov = mouseX >= contentX && mouseX <= contentX + contentW
                    && mouseY >= my && mouseY <= my + MOD_H;

            // Background
            int bg = on
                    ? (hov ? 0xBB004400 : 0x88002800)
                    : (hov ? 0xBB330000 : 0x88200000);
            drawRect(contentX, my, contentX + contentW, my + MOD_H, bg);

            // Left accent strip
            drawRect(contentX, my, contentX + 2, my + MOD_H, on ? 0xFF00FF00 : 0xFFFF3333);

            // Mod name
            this.fontRendererObj.drawStringWithShadow(
                    mod.getName(), contentX + 7, my + 6, on ? 0x66FF66 : 0xFF6666);

            // Keybind (if set)
            if (mod.getKey() != 0) {
                String keyStr = "§8[" + Keyboard.getKeyName(mod.getKey()) + "]";
                int kw = this.fontRendererObj.getStringWidth("[" + Keyboard.getKeyName(mod.getKey()) + "]");
                this.fontRendererObj.drawStringWithShadow(keyStr, contentX + contentW - kw - 36, my + 6, 0xFFFFFF);
            }

            // ON/OFF indicator
            String ind = on ? "§a[ON]" : "§c[OFF]";
            int iw = this.fontRendererObj.getStringWidth(on ? "[ON]" : "[OFF]");
            this.fontRendererObj.drawStringWithShadow(ind, contentX + contentW - iw - 2, my + 6, 0xFFFFFF);
        }

        // === FOOTER ===
        drawRect(0, this.height - FOOTER_H, this.width, this.height, 0xDD0A0A0A);
        drawGradientRect(0, this.height - FOOTER_H, this.width, this.height - FOOTER_H + 2, 0xFF550000, 0xFFCC0000);
        this.drawCenteredString(this.fontRendererObj,
                "§7Left-click: §ftoggle  §7| Right-click: §fsettings  §7| ESC: §fclose",
                this.width / 2, this.height - 13, 0x999999);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        // Category sidebar clicks
        Mod.Category[] cats = Mod.Category.values();
        for (int i = 0; i < cats.length; i++) {
            int cy = HEADER_H + 6 + i * 24;
            if (mouseX >= 4 && mouseX <= CAT_W - 4 && mouseY >= cy && mouseY <= cy + 19) {
                selectedCategory = cats[i];
                return;
            }
        }

        // Mod list clicks
        int contentX = CAT_W + 8;
        int contentW = this.width - CAT_W - 16;
        List<Mod> mods = getModsForCategory(selectedCategory);
        int startY = HEADER_H + 22;

        for (int i = 0; i < mods.size(); i++) {
            Mod mod = mods.get(i);
            int my = startY + i * (MOD_H + MOD_MARGIN);
            if (mouseX >= contentX && mouseX <= contentX + contentW
                    && mouseY >= my && mouseY <= my + MOD_H) {
                if (mouseButton == 0) {
                    mod.toggle();
                } else if (mouseButton == 1) {
                    this.mc.displayGuiScreen(new GuiModSettings(this, mod));
                }
                return;
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private List<Mod> getModsForCategory(Mod.Category category) {
        List<Mod> result = new ArrayList<>();
        for (Mod mod : DeathClient.getInstance().getModManager().mods) {
            if (mod.getCategory() == category) result.add(mod);
        }
        return result;
    }
}
