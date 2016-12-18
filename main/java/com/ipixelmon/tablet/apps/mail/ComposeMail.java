package com.ipixelmon.tablet.apps.mail;

import com.ipixelmon.GuiScrollingTextField;
import com.ipixelmon.GuiTextField;
import com.ipixelmon.GuiUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.apps.mail.packet.PacketSendMail;
import com.ipixelmon.tablet.apps.App;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import scala.actors.threadpool.Arrays;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

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

        GuiUtil.drawRectFill(message.xPosition - 1, message.yPosition - 1, message.width + 2, message.height + 2, Color.gray);
        message.draw(mouseX, mouseY, Mouse.getDWheel());

        sendBtn.drawButton(mc, mouseX, mouseY);

        if(players.getBounds().contains(mouseX, mouseY)) {
            drawHoveringText(Arrays.asList(new String[]{"Separate players with commas ,"}), mouseX, mouseY);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        String pStr = players.getText() + ",";
        iPixelmon.network.sendToServer(new PacketSendMail(message.getTextField().getText(), pStr.split(",")));
        setActiveApp(App.getApp(Mail.class));
    }

    // TODO: Figure out max string length for network to send and limit it

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {

        if (!PacketSendMail.checkChar(typedChar) || keyCode == Keyboard.KEY_BACK)
            players.keyTyped(typedChar, keyCode);

        message.keyTyped(typedChar, keyCode);

        if(keyCode == Keyboard.KEY_ESCAPE) {
            setActiveApp(App.getApp(Mail.class));
            return;
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        players.mouseClicked(mouseX, mouseY);
        message.mouseClicked(mouseX, mouseY);
        if(sendBtn.mousePressed(mc, mouseX, mouseY)) actionPerformed(sendBtn);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        sendBtn.enabled = !message.getTextField().getText().isEmpty() && !players.getText().isEmpty();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        int stringWidth = mc.fontRendererObj.getStringWidth("Send") + 10;

        players = new GuiTextField(getScreenBounds().getX() + 6, getScreenBounds().getY() + 9,
                getScreenBounds().getWidth() - 2 - stringWidth - 12, 10);
        message = new GuiScrollingTextField(players.getBounds().getX(),
                players.getBounds().getY() + players.getBounds().getHeight() + 8, getScreenBounds().getWidth() - 12,
                getScreenBounds().getHeight() - players.getBounds().getHeight() - 23);
        sendBtn = new GuiButton(0,
                getScreenBounds().getX() + getScreenBounds().getWidth() - stringWidth - 5, getScreenBounds().getY() + 5,
                stringWidth, 20, "Send");
    }
}
