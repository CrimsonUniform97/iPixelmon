package com.ipixelmon.tablet.apps.mail;

import com.ipixelmon.GuiScrollingTextField;
import com.ipixelmon.GuiTextField;
import com.ipixelmon.tablet.apps.mail.packet.PacketSendMail;
import com.ipixelmon.tablet.client.App;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Mouse;

import java.io.IOException;

/**
 * Created by colby on 12/14/2016.
 */
public class ComposeMail extends App {

    public ComposeMail() {
        super("composeMail", false);
    }

    private GuiButton sendBtn;
    private GuiTextField players;
    private GuiScrollingTextField message;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        Mail.drawBackground(getScreenBounds());
        players.drawTextField();
        message.draw(mouseX, mouseY, Mouse.getDWheel());
        sendBtn.drawButton(mc, mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (PacketSendMail.checkChar(typedChar))
            players.keyTyped(typedChar, keyCode);

        message.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        players.mouseClicked(mouseX, mouseY);
        message.mouseClicked(mouseX, mouseY);
        sendBtn.mousePressed(mc, mouseX, mouseY);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        int stringWidth = mc.fontRendererObj.getStringWidth("Send");

        players = new GuiTextField(getScreenBounds().getX() + 2, getScreenBounds().getY() + 2,
                getScreenBounds().getWidth() - 2 - stringWidth, 10);
        message = new GuiScrollingTextField(players.getBounds().getX(),
                players.getBounds().getY() + players.getBounds().getHeight() + 2, getScreenBounds().getWidth(),
                getScreenBounds().getHeight() - players.getBounds().getHeight() - 2);
        this.buttonList.add(sendBtn = new GuiButton(0,
                getScreenBounds().getX() + getScreenBounds().getWidth() - stringWidth, players.getBounds().getY(),
                stringWidth, 20, "Send"));
    }
}
