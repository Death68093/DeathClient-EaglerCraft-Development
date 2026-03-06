package net.minecraft.deathclient.mods.impl;

import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.deathclient.events.EventRender2D;
import net.minecraft.deathclient.mods.HudMod;
import net.minecraft.deathclient.settings.BooleanSetting;
import net.minecraft.item.ItemStack;

public class ArmorHUD extends HudMod {

    public BooleanSetting showDurability = new BooleanSetting("Show Durability", true);
    public BooleanSetting showMain       = new BooleanSetting("Show Main Hand",  true);

    public ArmorHUD() {
        super("Armor HUD", "Shows your equipped armor and durability.", Category.HUD);
        this.x = 2;
        this.y = 40;
        this.addSetting(showDurability);
        this.addSetting(showMain);
    }

    @Override
    public void draw() {
        if (mc.thePlayer == null) return;

        RenderHelper.enableGUIStandardItemLighting();

        ItemStack[] armor = mc.thePlayer.inventory.armorInventory;
        int drawX = this.x;

        // Draw armor slots 3 (helmet) → 0 (boots)
        for (int i = 3; i >= 0; i--) {
            ItemStack stack = armor[i];
            if (stack == null) {
                drawX += 18;
                continue;
            }
            mc.getRenderItem().renderItemIntoGUI(stack, drawX, this.y);

            if (showDurability.isEnabled() && stack.isItemStackDamageable()) {
                int max = stack.getMaxDamage();
                int cur = max - stack.getItemDamage();
                float ratio = (float) cur / max;
                int color = ratio > 0.5f ? 0x00FF00 : (ratio > 0.25f ? 0xFFAA00 : 0xFF0000);
                mc.fontRendererObj.drawStringWithShadow(
                        cur < 100 ? String.valueOf(cur) : (cur / max * 100) + "%",
                        drawX, this.y + 17, color);
            }

            drawX += 18;
        }

        // Main hand item
        if (showMain.isEnabled()) {
            ItemStack main = mc.thePlayer.getCurrentEquippedItem();
            if (main != null) {
                mc.getRenderItem().renderItemIntoGUI(main, drawX + 4, this.y);
                if (showDurability.isEnabled() && main.isItemStackDamageable()) {
                    int max = main.getMaxDamage();
                    int cur = max - main.getItemDamage();
                    float ratio = (float) cur / max;
                    int color = ratio > 0.5f ? 0x00FF00 : (ratio > 0.25f ? 0xFFAA00 : 0xFF0000);
                    mc.fontRendererObj.drawStringWithShadow(
                            String.valueOf(cur), drawX + 4, this.y + 17, color);
                }
            }
        }

        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public int getWidth()  { return 18 * 5; }
    @Override
    public int getHeight() { return 26; }
}
