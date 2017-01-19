package com.ipixelmon.tablet.client;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.GuiTablet;
import com.ipixelmon.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * Created by colby on 10/6/2016.
 */
public class KeyAndRenderListener {

    private KeyBinding tabletKey = new KeyBinding("Open Tablet", Keyboard.KEY_N, "key.ipixelmon");

    private static final ResourceLocation tablet_icon = new ResourceLocation(iPixelmon.id, "textures/gui/tablet/tablet_icon.png");

    public KeyAndRenderListener() {
        ClientRegistry.registerKeyBinding(tabletKey);
    }

    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        if (tabletKey.isPressed()) Minecraft.getMinecraft().displayGuiScreen(new GuiTablet(null));
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            Minecraft mc = Minecraft.getMinecraft();

            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.enableBlend();
            mc.getTextureManager().bindTexture(tablet_icon);
            int x = event.getResolution().getScaledWidth() - 74;
            int y = event.getResolution().getScaledHeight() - 36;
            GuiUtil.drawImage(x, y, 32, 32);
            mc.fontRendererObj.setUnicodeFlag(true);
            mc.fontRendererObj.drawString(Keyboard.getKeyName(tabletKey.getKeyCode()).replaceAll("KEY_", ""), x + 34,
                    y + 23, 0xFFFFFF);
            mc.fontRendererObj.setUnicodeFlag(false);
        }
    }

}
