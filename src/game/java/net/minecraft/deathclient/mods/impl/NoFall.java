package net.minecraft.deathclient.mods.impl;

import net.minecraft.deathclient.events.EventUpdate;
import net.minecraft.deathclient.mods.Mod;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Mod {

    public NoFall() {
        super("NoFall", "Prevents you from taking fall damage.", Category.PLAYER, true);
    }

    @Override
    public void onUpdate(EventUpdate event) {
        // If we have fallen more than 2 blocks, start telling the server we are on the ground
        if (mc.thePlayer.fallDistance > 2.0F) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
    }
}