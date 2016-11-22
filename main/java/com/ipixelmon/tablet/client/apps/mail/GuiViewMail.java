package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.GuiMultiLineTextField;
import com.ipixelmon.tablet.client.App;
import com.ipixelmon.tablet.client.TextBtn;
import com.ipixelmon.tablet.client.apps.mail.packet.PacketReceiveMail;
import net.minecraft.client.gui.GuiButton;

import java.io.IOException;

/**
 * Created by colby on 11/20/2016.
 */
public class GuiViewMail extends App {

    private MailObject mailObject;
    private GuiMultiLineTextField messageField, responseField;

    public GuiViewMail(MailObject mailObject) {
        super("mailView", false);
        this.mailObject = mailObject;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        messageField.drawTextField(mouseX, mouseY);
        responseField.drawTextField(mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        responseField.mouseClicked(mouseX, mouseY);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        responseField.keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        messageField = new GuiMultiLineTextField(screenBounds.getX(), screenBounds.getY(), screenBounds.getWidth() / 2, screenBounds.getHeight() - 30);
        responseField = new GuiMultiLineTextField(messageField.getBounds().getX() + messageField.getBounds().getWidth(),
                messageField.getBounds().getY(), screenBounds.getWidth() / 2, messageField.getBounds().getHeight());
        messageField.setText(PacketReceiveMail.df.format(mailObject.date) + "\n" + "From: " + mailObject.playerName + "\n\n" + mailObject.message);

        this.buttonList.add(new GuiButton(0, responseField.getBounds().getX() + responseField.getBounds().getWidth() - 51,
                responseField.getBounds().getY() + responseField.getBounds().getHeight() + 5, 50, 20, "Send"));
    }
}
