package net.minecraft.deathclient.api;

import org.teavm.jso.JSBody;

public class JSBridge {

    // This connects the Game folder to the JS folder
    public static void init() {
        BridgeHelper.tickHandler = new BridgeHelper.TickListener() {
            @Override
            public void onTick() {
                onClientTick();
            }
        };
    }

    @JSBody(params = { "eventName", "data" }, script = "if(window.DeathClient) { window.DeathClient.trigger(eventName, data); }")
    public static native void triggerEvent(String eventName, String data);

    public static void onClientTick() {
        triggerEvent("onTick", "");
    }
}