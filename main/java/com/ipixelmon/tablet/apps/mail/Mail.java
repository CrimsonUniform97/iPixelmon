package com.ipixelmon.tablet.apps.mail;

import com.ipixelmon.GuiUtil;
import com.ipixelmon.pixelbay.gui.ColorPicker;
import com.ipixelmon.tablet.apps.App;
import com.ipixelmon.tablet.apps.GuiTablet;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Rectangle;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by colby on 12/14/2016.
 */
public class Mail extends App {

    public Mail(String name, boolean register) {
        super(name, register);
    }

    private GuiButton composeBtn;

    public static List<MailObject> mail = new ArrayList<>();

    private ListMail listMail;

    // TODO: Draw mail
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawBackground(getScreenBounds());
        composeBtn.drawButton(mc, mouseX, mouseY);
        listMail.draw(mouseX, mouseY, Mouse.getDWheel());
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

        int stringWidth = mc.fontRendererObj.getStringWidth("Compose Message") + 10;
        this.buttonList.add(composeBtn = new GuiButton(0,
                getScreenBounds().getX() + getScreenBounds().getWidth() - stringWidth,
                getScreenBounds().getY(), stringWidth, 20, "Compose Message"));

        listMail = new ListMail(getScreenBounds().getX(), getScreenBounds().getY(), getScreenBounds().getWidth(), getScreenBounds().getHeight());
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_ESCAPE)
            setActiveApp(new GuiTablet());
        listMail.keyTyped(typedChar, keyCode);
    }

    public static void drawBackground(Rectangle bounds) {
        GuiUtil.drawRectFillBorder(bounds.getX() + 2, bounds.getY() + 2, bounds.getWidth() - 4, bounds.getHeight() - 4,
                ColorPicker.color(255f, 252f, 211f, 255f), Color.black, 2);
    }

}
