package com.ipixelmon.tablet.apps.mail;

import com.ipixelmon.GuiScrollList;
import com.ipixelmon.tablet.apps.mail.packet.PacketSendMail;

/**
 * Created by colby on 12/14/2016.
 */
public class ListMail extends GuiScrollList {

    public ListMail(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    // TODO:

    @Override
    public int getObjectHeight(int index) {
        return 30;
    }

    @Override
    public void drawObject(int index) {
        MailObject mailObject = Mail.mail.get(index);

        mc.fontRendererObj.drawString(PacketSendMail.dateFormat.format(mailObject.getSentDate()), 0, 0, 0xFFFFFF);
        mc.fontRendererObj.drawString(mailObject.getSender(), 0, 12, 0xFFFFFF);
        mc.fontRendererObj.drawString(mailObject.getMessage(), 0, 24, 0xFFFFFF);

    }

    @Override
    public int getSize() {
        return Mail.mail.size();
    }

    @Override
    public void elementClicked(int index, boolean doubleClick) {

    }

    @Override
    public void drawSelectionBox(int index, int width, int height) {
//        super.drawSelectionBox(index, width, height);
    }

    @Override
    public void drawBackground() {
//        super.drawBackground();
    }
}
