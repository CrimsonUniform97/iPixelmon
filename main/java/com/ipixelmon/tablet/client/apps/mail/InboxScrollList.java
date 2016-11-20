package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.tablet.client.App;
import com.ipixelmon.tablet.client.GuiTablet;
import com.ipixelmon.tablet.client.apps.friends.CustomScrollList;
import com.ipixelmon.tablet.client.apps.mail.packet.PacketReceiveMail;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.util.Rectangle;

/**
 * Created by colby on 11/18/2016.
 */
public class InboxScrollList extends CustomScrollList {

    public InboxScrollList(Minecraft mc, int x, int y, int width, int height, int entryHeight, GuiScreen screen) {
        super(mc, x, y, width, height, entryHeight, screen);
    }

    @Override
    protected int getSize() {
        return Mail.messages.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        selectedIndex = index;

        if(doubleClick){
            // TODO: Come up with a better system for setting the activeApp
            Rectangle screenBounds = App.activeApp.screenBounds;
            App.activeApp = new GuiViewMail((MailObject) Mail.messages.toArray()[selectedIndex]);
            App.activeApp.screenBounds = screenBounds;
            App.activeApp.setWorldAndResolution(mc, width, height);
            App.activeApp.initGui();
        }

    }

    @Override
    protected boolean isSelected(int index) {
        return index == selectedIndex;
    }

    @Override
    protected void drawBackground() {
        drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, -6250336);
        drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -16777216);
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        MailObject mailObject = (MailObject) Mail.messages.toArray()[slotIdx];
        int textColor = mailObject.read ? 0xBDBDBD : 0xFFFFFF;

//        mc.fontRendererObj.setUnicodeFlag(true);
        mc.fontRendererObj.drawString(PacketReceiveMail.df.format(mailObject.date), left + 3, slotTop, textColor);
        mc.fontRendererObj.drawString("From: " + mailObject.playerName, left + 3, slotTop + mc.fontRendererObj.FONT_HEIGHT, textColor);
        mc.fontRendererObj.drawString("\"" + mc.fontRendererObj.trimStringToWidth(mailObject.message, width - 12) + "...\"", left + 3, slotTop + mc.fontRendererObj.FONT_HEIGHT * 2, textColor);
//        mc.fontRendererObj.setUnicodeFlag(false);
    }
}
