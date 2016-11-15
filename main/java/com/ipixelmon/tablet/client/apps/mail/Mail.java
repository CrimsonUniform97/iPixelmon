package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.App;
import com.ipixelmon.tablet.client.apps.friends.GuiFriends;
import net.minecraft.util.ResourceLocation;

import java.io.*;

/**
 * Created by colby on 10/28/2016.
 */
public class Mail extends App {

    private static ResourceLocation icon, icon_new;
    private GuiFriends friendsList;

    public Mail(String name) {
        super(name);
        icon = getIcon(false);
        icon_new = getIcon(true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        friendsList.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        friendsList = new GuiFriends(mc, 0, 0, 100, 100, 10, this);
    }

    @Override
    public ResourceLocation getIcon() {
        return icon;
    }

    private ResourceLocation getIcon(boolean isNew) {
        return new ResourceLocation(iPixelmon.id, "textures/apps/" + name + "/" + (isNew ? "icon_new.png" : "icon.png"));
    }
}
