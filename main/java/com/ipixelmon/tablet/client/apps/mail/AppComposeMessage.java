package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.GuiScrollingTextField;
import com.ipixelmon.GuiTextField;
import com.ipixelmon.GuiUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.App;
import com.ipixelmon.tablet.client.apps.mail.packets.PacketSendMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by colby on 12/9/2016.
 */
public class AppComposeMessage extends App {

    private GuiTextField players;
    private GuiScrollingTextField message;
    private static final ResourceLocation send_message_icon = new ResourceLocation(iPixelmon.id, "textures/gui/tablet/send_message.png");

    public AppComposeMessage() {
        super("composeMessage", false);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();

        int listWidth = screenBounds.getWidth() - 50;
        int listHeight = screenBounds.getHeight() - 10;

        message = new GuiScrollingTextField((width - listWidth) / 2, ((height - listHeight) / 2) + 10, listWidth, listHeight - 10);
        players = new GuiTextField((width - listWidth) / 2, message.yPosition - 12, listWidth, 10);

        buttonList.add(new GuiButton(0, message.xPosition + message.width + 2, message.yPosition, 20, 20, ""));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        mc.getTextureManager().bindTexture(send_message_icon);
        GuiUtil.instance.drawImage(this.buttonList.get(0).xPosition + ((20 - 16) / 2), this.buttonList.get(0).yPosition + ((20 - 16) / 2), 16, 16);

        players.drawTextField();
        message.draw(mouseX, mouseY, Mouse.getDWheel());
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        Pattern pattern = Pattern.compile("[^0-9 ^a-z A-Z ^,]");
        Matcher matcher = pattern.matcher("" + typedChar);
        if (!matcher.find() || keyCode == Keyboard.KEY_BACK)
            players.keyTyped(typedChar, keyCode);
        message.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        players.mouseClicked(mouseX, mouseY);
        message.mouseClicked(mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (!message.getTextField().getText().isEmpty() && !players.getText().isEmpty()) {
            iPixelmon.network.sendToServer(new PacketSendMessage(message.getTextField().getText(), players.getText().trim().split(",")));
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        players.update();
    }
}
