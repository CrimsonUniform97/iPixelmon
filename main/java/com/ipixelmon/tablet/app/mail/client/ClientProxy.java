package com.ipixelmon.tablet.app.mail.client;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.AppProxy;
import com.ipixelmon.tablet.app.mail.Mail;
import net.minecraft.util.ResourceLocation;

import java.util.Set;
import java.util.TreeSet;

public class ClientProxy extends AppProxy {

    private static final ResourceLocation icon = new ResourceLocation(iPixelmon.id, "textures/apps/mail/icon.png");
    private static final ResourceLocation icon_new = new ResourceLocation(iPixelmon.id, "textures/apps/mail/icon_new.png");

    public static final Set<Mail> mail = new TreeSet<>();

    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public Object getIcon() {
        return icon;
    }

    @Override
    public Object getGuiForApp(Object... parameters) {
        return new MailAppGui(parameters);
    }
}
