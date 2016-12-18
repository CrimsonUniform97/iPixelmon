package com.ipixelmon.tablet.apps.mail;

import com.ipixelmon.GuiScrollList;
import com.ipixelmon.GuiUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.pixelbay.gui.ColorPicker;
import com.ipixelmon.tablet.apps.App;
import com.ipixelmon.tablet.apps.mail.packet.PacketSendMail;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.sql.SQLException;

/**
 * Created by colby on 12/14/2016.
 */
public class ListMail extends GuiScrollList {

    public ListMail(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    boolean mouseDown = false;

    @Override
    public int getObjectHeight(int index) {
        return 36;
    }

    @Override
    public void draw(int mouseX, int mouseY, int dWheel) {
        super.draw(mouseX, mouseY, dWheel);
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY) {
        MailObject mailObject = Mail.mail.get(index);
        int color = getSelected() == index ? 0xFFFFFF : 0x383838;

        if(mailObject.isRead()) {
            GuiUtil.drawRectFill(0, 0, width, height, ColorPicker.color(128f, 128f, 128f, 200f));
        }
            mc.fontRendererObj.drawString(PacketSendMail.dateFormat.format(mailObject.getSentDate()), 2, 2, color);
            mc.fontRendererObj.drawString(mailObject.getSender(), 2, 14, color);
            mc.fontRendererObj.drawString(mailObject.getMessage(), 2, 26, color);


        if (getSelected() == index) {
            // handle drawing the X
            IconBtn deleteBtn = new IconBtn(0, width - 24, 8, "Delete",
                    new ResourceLocation(iPixelmon.id, "textures/apps/mail/x.png"));
            deleteBtn.drawButton(mc, mouseX, mouseY);

            // handle deleting message
            if(Mouse.isButtonDown(0)) {
                if(!mouseDown) {
                    if(deleteBtn.mousePressed(mc, mouseX, mouseY)) {
                        Mail.deleteMailFromClientSQL(mailObject);
                    }
                }
                mouseDown = true;
            } else {
                mouseDown = false;
            }
        }
    }

    @Override
    public int getSize() {
        return Mail.mail.size();
    }

    @Override
    public void elementClicked(int index, boolean doubleClick) {
        if(doubleClick) {
            App.setActiveApp(new ViewMail(Mail.mail.get(index)));
        }
    }

    @Override
    public void drawSelectionBox(int index, int width, int height) {
        GuiUtil.drawRectFill(0, 0, width, height, ColorPicker.color(128f, 128f, 128f, 200f));
    }

    @Override
    public void drawBackground() {
//        super.drawBackground();
    }
}
