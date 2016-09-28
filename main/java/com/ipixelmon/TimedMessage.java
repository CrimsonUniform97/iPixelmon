package com.ipixelmon;

public class TimedMessage implements Runnable
{

    private String message;
    private int duration;

    public TimedMessage(String parMessage, int parDuration)
    {
        message = parMessage;
        duration = parDuration;
    }

    @Override
    public void run()
    {
        while(duration > 0)
        {
            try
            {
                Thread.sleep(1000L);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            duration--;
        }

        message = "";
    }

    public String getMessage()
    {
        return message;
    }
}
