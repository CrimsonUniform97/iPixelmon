package ipixelmon.pixelbay.gui;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoMessage
{

    private static InfoMessage ourInstance = new InfoMessage();

    public static InfoMessage getInstance()
    {
        return ourInstance;
    }

    private static Map<Info, Integer> messages = new HashMap<>();
    // TODO: Buying and Selling are implemented into the new list. Just make the sell list look like the buy list, and we should be good?
    private InfoMessage()
    {
        new Thread()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    try
                    {
                        Thread.sleep(500L);
                        List<Info> toRemove = new ArrayList<Info>();
                        for (Info info : messages.keySet())
                        {
                            info.duration--;
                            if (info.duration <= 0)
                            {
                                toRemove.add(info);
                            }
                        }

                        for (Info info : toRemove)
                        {
                            messages.remove(info);
                        }
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public static Info newMessage(String text, int duration)
    {
        Info info;
        messages.put(info = new Info(text, duration), 1);
        return info;
    }

    public static class Info
    {
        private volatile String text;
        private volatile int duration;

        public Info(String text, int duration)
        {
            this.text = text;
            this.duration = duration;
        }


        public String getText()
        {
            return text;
        }

        public void draw(int x, int y, int color)
        {
            boolean found = false;

            for (Info i : messages.keySet())
            {
                if (i == this)
                {
                    found = true;
                    break;
                }
            }

            if (!found) return;

            Minecraft.getMinecraft().fontRendererObj.drawString(text, x, y, color);
        }
    }
}
