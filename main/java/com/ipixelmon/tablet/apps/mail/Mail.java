package com.ipixelmon.tablet.apps.mail;

import com.ipixelmon.GuiUtil;
import com.ipixelmon.pixelbay.gui.ColorPicker;
import com.ipixelmon.tablet.client.App;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.util.Rectangle;

import java.awt.*;
import java.io.IOException;

/**
 * Created by colby on 12/14/2016.
 */
public class Mail extends App {

    public Mail(String name, boolean register) {
        super(name, register);
    }

    private GuiButton composeBtn;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawBackground(getScreenBounds());
        composeBtn.drawButton(mc, mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.equals(composeBtn)) setActiveApp(new ComposeMail());
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        composeBtn.mousePressed(mc, mouseX, mouseY);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        int stringWidth = mc.fontRendererObj.getStringWidth("Compose Message");
        this.buttonList.add(composeBtn = new GuiButton(0, getScreenBounds().getX() + getScreenBounds().getWidth() - stringWidth, getScreenBounds().getY(), stringWidth, 20, "Compose Message"));
    }

    public static void drawBackground(Rectangle bounds) {
        GuiUtil.drawRectFillBorder(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight(),
                Color.black, ColorPicker.color(255f, 252f, 211f, 255f), 1);
    }

}
