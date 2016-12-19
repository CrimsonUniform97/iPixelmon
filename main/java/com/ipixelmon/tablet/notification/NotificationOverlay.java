package com.ipixelmon.tablet.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.util.Iterator;

/**
 * Created by colbymchenry on 10/3/16.
 */
public class NotificationOverlay {

    public static final NotificationOverlay instance = new NotificationOverlay();
    public final int maxNotificationWidth = 100;

    private NotificationOverlay() {
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent event) {

        if (event.type != RenderGameOverlayEvent.ElementType.CROSSHAIRS) return;

        Iterator<NotificationProperties> iterator = Notification.notificationsForRendering.descendingIterator();

        int posY = 0;

        NotificationProperties n;

        Minecraft.getMinecraft().fontRendererObj.setUnicodeFlag(true);
        while (iterator.hasNext()) {
            n = iterator.next();

            GlStateManager.pushMatrix();
            {
                int posX = event.resolution.getScaledWidth();

                n.posX = n.posX == 0 ? posX : n.posX;

                boolean viewingChat = Minecraft.getMinecraft().currentScreen instanceof GuiChat;

                n.update(System.currentTimeMillis() - n.notification.startTime > n.notification.getDuration() ? viewingChat ? posX - n.notification.getWidth() : posX : posX - n.notification.getWidth(), posY);

                GlStateManager.translate(n.posX, n.posY, 100f);

                int mouseX = Mouse.getX() / event.resolution.getScaleFactor();
                int mouseY = event.resolution.getScaledHeight() - (Mouse.getY() / event.resolution.getScaleFactor());

                int mX = mouseX - (int) n.posX;
                int mY = mouseY - (int) n.posY;
                mY = mY < 0 ? 0 : mY;

                n.notification.draw(mX, mY);


                if (viewingChat) {
                    if (mouseX > n.posX && mouseX < n.posX + n.notification.getWidth() && mouseY > n.posY && mouseY < n.posY + n.notification.getHeight() && Mouse.isButtonDown(0)) {
                        Minecraft.getMinecraft().thePlayer.closeScreen();
                        n.notification.actionPerformed();
                    }
                }

                if (n.posX >= posX) iterator.remove();


                posY += n.notification.getHeight() + 1;
                GlStateManager.popMatrix();
            }
        }
        Minecraft.getMinecraft().fontRendererObj.setUnicodeFlag(false);

    }

    protected void addNotification(Notification notification) {
        Notification.notificationsForRendering.addLast(new NotificationProperties(notification));
        Notification.notifications.addLast(new NotificationProperties(notification));
    }

    public class NotificationProperties {

        protected Notification notification;
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

        public Notification getNotification() {
            return notification;
        }
    }

}
