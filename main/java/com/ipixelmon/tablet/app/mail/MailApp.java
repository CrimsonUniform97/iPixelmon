package com.ipixelmon.tablet.app.mail;

import com.ipixelmon.tablet.AppBase;
import com.ipixelmon.tablet.AppProxy;
import com.ipixelmon.tablet.app.mail.client.ClientProxy;
import com.ipixelmon.tablet.app.mail.server.ServerProxy;

public class MailApp implements AppBase {

    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public String getName() {
        return "mail";
    }

    @Override
    public Class<? extends AppProxy> clientProxyClass() {
        return ClientProxy.class;
    }

    @Override
    public Class<? extends AppProxy> serverProxyClass() {
        return ServerProxy.class;
    }
}
