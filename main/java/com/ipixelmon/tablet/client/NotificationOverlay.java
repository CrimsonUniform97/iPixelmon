package com.ipixelmon.tablet.client;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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

        // TODO: Don't update animation or timer, just draw if we are typing in chat. Make it come in from the right as well

        Iterator<NotificationProperties> iterator = notifications.descendingIterator();

        int posY = 0;

        NotificationProperties n;

        while (iterator.hasNext()) {
            n = iterator.next();

            GlStateManager.pushMatrix();
            {
                int posX = n.notification.maxWidth * -1;

                n.update(System.currentTimeMillis() - n.notification.startTime > n.notification.getDuration() ? 0 : n.notification.maxWidth, posY);

                GlStateManager.translate(posX + n.posX, n.posY, 400f);
                n.notification.draw();


                if (n.posX <= 0) {
                    iterator.remove();
                }

                posY += n.notification.getHeight();
                GlStateManager.popMatrix();
            }
        }

    }

    public void addNotification(Notification notification) {
        notifications.addLast(new NotificationProperties(notification));
        System.out.println(notification.getDuration());
    }

    private class NotificationProperties {

        private Notification notification;
        private double posX, posY, speedX = 200f, speedY = 200f;

        private double timeOfLastFrame = System.nanoTime() / 1e9;

        public NotificationProperties(Notification notification) {
            this.notification = notification;
        }

        private void update(double neededX, double neededY) {
            double time = System.nanoTime() / 1e9;
            double timePassed = time - timeOfLastFrame;
            timeOfLastFrame = time;
            update(timePassed, neededX, neededY);
        }


        private void update(double time, double neededX, double neededY) {
            posX = posX < neededX ? posX + (time * speedX) > neededX ? neededX : posX + (time * speedX) : posX - (time * speedX) < neededX ? neededX : posX - (time * speedX);
            posY = posY < neededY ? posY + (time * speedY) > neededY ? neededY : posY + (time * speedY) : posY - (time * speedY) < neededY ? neededY : posY - (time * speedY);
        }

    }

}
