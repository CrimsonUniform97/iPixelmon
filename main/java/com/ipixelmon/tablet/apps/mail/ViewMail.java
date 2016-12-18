package com.ipixelmon.tablet.apps.mail;

import com.ipixelmon.GuiScrollingTextField;
import com.ipixelmon.TimedMessage;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.apps.App;
import com.ipixelmon.tablet.apps.mail.packet.PacketSendMail;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

/**
 * Created by colby on 12/17/2016.
 */
public class ViewMail extends App {

    private MailObject mailObject;
    private GuiScrollingTextField message, response;
    private IconBtn sendBtn;
    private TimedMessage timedMessage;

    public ViewMail(MailObject mailObject) {
        super("viewMail", false);
        this.mailObject = mailObject;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        Mail.drawBackground(getScreenBounds());
        int dWheel = Mouse.getDWheel();
        message.draw(mouseX, mouseY, dWheel);
        response.draw(mouseX, mouseY, dWheel);
        sendBtn.drawButton(mc, mouseX, mouseY);

        if(timedMessage.hasMessage()) {
            int stringWidth = mc.fontRendererObj.getStringWidth(timedMessage.getMessage());
            mc.fontRendererObj.drawString(timedMessage.getMessage(),
                    getScreenBounds().getX() + ((getScreenBounds().getWidth() - stringWidth) / 2),
                    getScreenBounds().getY() - 15, 0xFFFFFF);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE)
            setActiveApp(App.getApp(Mail.class));
        response.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        response.mouseClicked(mouseX, mouseY);

        if(sendBtn.mousePressed(mc, mouseX, mouseY)) {
            setActiveApp(App.getApp(Mail.class));
            iPixelmon.network.sendToServer(new PacketSendMail(message.getTextField().getText(), mailObject.getSender()));
            Mail.deleteMailFromClientSQL(mailObject);
        }
    }


    @Override
    public void initGui() {
        super.initGui();
        message = new GuiScrollingTextField(getScreenBounds().getX() + 8, getScreenBounds().getY() + 8,
                getScreenBounds().getWidth() - 16, (getScreenBounds().getHeight() / 2) + 20);
        message.getTextField().setEnabled(false);
        message.getTextField().setText(PacketSendMail.dateFormat.format(mailObject.getSentDate()) + "\n" +
                mailObject.getSender() + "\n\n" + mailObject.getMessage());
        response = new GuiScrollingTextField(message.xPosition, message.yPosition + message.height + 22,
                message.width, getScreenBounds().getHeight() - (message.height + 38));
        sendBtn = new IconBtn(0, response.xPosition + response.width - 24, response.yPosition - 19,
                "Send", new ResourceLocation(iPixelmon.id, "textures/apps/mail/send.png"));
        timedMessage = new TimedMessage("", 0);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        sendBtn.enabled = !response.getTextField().getText().isEmpty();
    }

    public void handleResponse(boolean success, String message) {
        if(!success) {
            timedMessage.setMessage(message, 10);
        } else {
            setActiveApp(App.getApp(Mail.class));
        }
    }
}
