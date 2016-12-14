package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.App;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Created by colby on 10/28/2016.
 */
public class Mail extends App {

    private static ResourceLocation icon, icon_new;
    public static Set<Conversation> messages = new TreeSet<>();
    private GuiMessages guiMessages;

    public Mail(String name) {
        super(name, true);
        icon = new ResourceLocation(iPixelmon.id, "textures/apps/" + name + "/icon.png");
        icon_new = new ResourceLocation(iPixelmon.id, "textures/apps/" + name + "/icon_new.png");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        guiMessages.draw(mouseX, mouseY, Mouse.getDWheel());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if(button.id == 0) {
            setActiveApp(new AppComposeMessage());
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        guiMessages.keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        messages.clear();
//        if(Mail.messages.isEmpty()) {
            try {
                ResultSet result = iPixelmon.clientDb.query("SELECT * FROM tabletMessages");

                while(result.next()) {
                    messages.add(new Conversation(UUID.fromString(result.getString("messageID"))));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
//        }

        int listWidth = getScreenBounds().getWidth() - 50;
        int listHeight = getScreenBounds().getHeight() - 10;

        guiMessages = new GuiMessages((width - listWidth) / 2, (height - listHeight) / 2, listWidth, listHeight);

        buttonList.add(new GuiButton(0, guiMessages.xPosition + listWidth - 80, guiMessages.yPosition + listHeight - 21, 75, 20, "New Message"));
    }

    @Override
    public void updateScreen() {
    }

    // TODO: Show new message icon
    @Override
    public ResourceLocation getIcon() {
        return icon;
    }


}
