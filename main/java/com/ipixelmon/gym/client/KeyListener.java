package com.ipixelmon.gym.client;

import com.ipixelmon.gym.packet.PacketOpenGymGuiToServer;
import com.ipixelmon.iPixelmon;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class KeyListener {

    private static KeyBinding gymKey = new KeyBinding("Open Gym Info", Keyboard.KEY_G, "key.ipixelmon");

    public static void registerKeyBindings() {
        ClientRegistry.registerKeyBinding(gymKey);
        MinecraftForge.EVENT_BUS.register(new KeyListener());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEvent(InputEvent.KeyInputEvent event) {
        if (gymKey.isPressed()) {
            iPixelmon.network.sendToServer(new PacketOpenGymGuiToServer());
        }
    }

}
