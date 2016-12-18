package com.ipixelmon.tablet.apps.mail;

import com.ipixelmon.GuiUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.pixelbay.gui.ColorPicker;
import com.ipixelmon.tablet.apps.App;
import com.ipixelmon.tablet.apps.GuiTablet;
import com.ipixelmon.tablet.apps.mail.packet.PacketSendMail;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Rectangle;

import java.awt.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by colby on 12/14/2016.
 */
public class Mail extends App {

    public Mail(String name, boolean register) {
        super(name, register);
    }

    private IconBtn composeBtn;

    public static List<MailObject> mail = new ArrayList<>();

    private ListMail listMail;

    // TODO: Draw mail
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawBackground(getScreenBounds());
        listMail.draw(mouseX, mouseY, Mouse.getDWheel());
        GlStateManager.color(1, 1, 1, 1);
        composeBtn.drawButton(mc, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if(composeBtn.mousePressed(mc, mouseX, mouseY)) setActiveApp(new ComposeMail());
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        Mail.mail.clear();
        try {
            ResultSet result = iPixelmon.clientDb.query("SELECT * FROM tabletMail");

            while(result.next()) {
                Mail.mail.add(new MailObject(
                        PacketSendMail.dateFormat.parse(result.getString("sentDate")),
                        result.getString("sender"), result.getString("message"), result.getBoolean("read")));
            }

            Collections.sort(Mail.mail);
            Collections.reverse(Mail.mail);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.composeBtn = new IconBtn(0, getScreenBounds().getX() + getScreenBounds().getWidth() - 30,
                getScreenBounds().getY() + getScreenBounds().getHeight() - 25, "Compose",
                new ResourceLocation(iPixelmon.id, "textures/apps/mail/compose.png"));
        listMail = new ListMail(getScreenBounds().getX() + 4, getScreenBounds().getY() + 4,
                getScreenBounds().getWidth() - 8, getScreenBounds().getHeight() - 8);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        listMail.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    public static void drawBackground(Rectangle bounds) {
        GuiUtil.drawRectFillBorder(bounds.getX() + 2, bounds.getY() + 2, bounds.getWidth() - 4, bounds.getHeight() - 4,
                ColorPicker.color(255f, 252f, 211f, 255f), Color.black, 2);
    }

    public static void deleteMailFromClientSQL(MailObject mailObject) {
        try {
            String query = "DELETE FROM tabletMail WHERE sentDate = (SELECT sentDate FROM tabletMail WHERE " +
                    "sentDate='" + PacketSendMail.dateFormat.format(mailObject.getSentDate()) + "' AND " +
                    "sender='" + mailObject.getSender() + "' AND " +
                    "message='" + mailObject.getMessage() + "' LIMIT 1);";

            iPixelmon.clientDb.query(query);
            Mail.mail.remove(mailObject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
