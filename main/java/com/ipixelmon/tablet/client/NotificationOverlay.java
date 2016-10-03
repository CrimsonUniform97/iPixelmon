package com.ipixelmon.tablet.client;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by colbymchenry on 10/3/16.
 */
public class NotificationOverlay {

    public static final NotificationOverlay instance = new NotificationOverlay();
    public final int maxNotificationWidth = 100;

    private NotificationOverlay() {
    }

    private static final LinkedList<NotificationProperties> notifications = new LinkedList<NotificationProperties>();

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent event) {
        if (event.type != RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            return;
        }

        Iterator<NotificationProperties> iterator = notifications.descendingIterator();

        int posX = event.resolution.getScaledWidth() - maxNotificationWidth;
        int posY = 0;

        NotificationProperties n;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        while(iterator.hasNext()) {
            n = iterator.next();

            if(System.currentTimeMillis() - n.notification.startTime > n.notification.getDuration()) {
                iterator.remove();
            } else {
                GlStateManager.pushMatrix();
                {
                    n.animationX = (n.notification.startTime + 10) - System.currentTimeMillis() < 0 ? 0 : (n.notification.startTime + 10) - System.currentTimeMillis();
//                    n.animationX = n.animationX - 0.2f < 0 ? 0 : n.animationX - 0.2f;
                    GlStateManager.translate(posX + n.animationX, posY, 400f);
                    GL11.glScissor(posX, posY, maxNotificationWidth, n.notification.getHeight());
                    n.notification.draw();
                    posY += n.notification.getHeight();
                }
                GlStateManager.popMatrix();
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public void addNotification(Notification notification) {
        notifications.addLast(new NotificationProperties(notification, maxNotificationWidth, 0));
    }

    private class NotificationProperties {

        private Notification notification;
        private float animationX, animationY;

        public NotificationProperties(Notification notification, float animationX, float animationY) {
            this.notification = notification;
            this.animationX = animationX;
            this.animationY = animationY;
        }
    }

}
