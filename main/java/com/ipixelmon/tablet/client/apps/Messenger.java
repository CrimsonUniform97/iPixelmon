package com.ipixelmon.tablet.client.apps;

import com.ipixelmon.tablet.client.App;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

/**
 * Created by colby on 10/28/2016.
 */
public class Messenger extends App {

    public Messenger(String name, ResourceLocation icon) {
        super(name, icon);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }


}
