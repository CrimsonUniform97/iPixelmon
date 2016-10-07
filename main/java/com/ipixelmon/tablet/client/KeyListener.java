package com.ipixelmon.tablet.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Created by colby on 10/6/2016.
 */
public class KeyListener {

    private KeyBinding key = new KeyBinding("key.notifications.test", Keyboard.KEY_N, "key.ipixelmon.test");

    private int count = 0;

    public KeyListener() {
        ClientRegistry.registerKeyBinding(key);
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if(key.isPressed()) {
            NotificationOverlay.instance.addNotification(new TextNotification("Test Sample " + count++));
        }
    }

}
