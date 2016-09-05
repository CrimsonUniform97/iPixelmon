package ipixelmon.pixelbay;

import ipixelmon.iPixelmon;
import ipixelmon.pixelbay.gui.buy.GuiSearch;
import ipixelmon.pixelbay.gui.sell.SellGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public final class BreakListener {

    // TODO: Players access Pixelbay to sell items through tellers in a building. NPC's

    public static KeyBinding pong, ping;

    public BreakListener() {
        pong = new KeyBinding("key.pong", Keyboard.KEY_P, "key.categories.mymod");
        ping = new KeyBinding("key.ping", Keyboard.KEY_O, "key.categories.mymod");
        ClientRegistry.registerKeyBinding(pong);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(pong.isPressed()) {
            Minecraft.getMinecraft().thePlayer.openGui(iPixelmon.instance, GuiSearch.ID, null, 0, 0, 0);
        }

        if(ping.isPressed()) {
            Minecraft.getMinecraft().thePlayer.openGui(iPixelmon.instance, SellGui.ID, null, 0, 0, 0);
        }

    }
}
