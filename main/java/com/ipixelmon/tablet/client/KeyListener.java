package com.ipixelmon.tablet.client;

import com.ipixelmon.tablet.notification.NotificationOverlay;
import net.minecraft.client.Minecraft;
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
    private KeyBinding key1 = new KeyBinding("key.notifications.test1", Keyboard.KEY_K, "key.ipixelmon.test1");

    private int count = 0;

    public KeyListener() {
        ClientRegistry.registerKeyBinding(key);
        ClientRegistry.registerKeyBinding(key1);
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if(key.isPressed()) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiTablet());
        }

    }

}
