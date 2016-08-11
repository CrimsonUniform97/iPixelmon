package ipixelmon.minebay;

import ipixelmon.iPixelmon;
import ipixelmon.minebay.gui.SearchGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public final class BreakListener {

    public static KeyBinding pong;

    public BreakListener() {
        pong = new KeyBinding("key.pong", Keyboard.KEY_P, "key.categories.mymod");
        ClientRegistry.registerKeyBinding(pong);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(pong.isPressed()) {
            System.out.println("CALLED");
            Minecraft.getMinecraft().thePlayer.openGui(iPixelmon.instance, SearchGui.ID, null, 0, 0, 0);
        }
    }
}
