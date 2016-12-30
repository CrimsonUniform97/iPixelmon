package com.ipixelmon.mcstats.client;

/**
 * Created by colbymchenry on 12/29/16.
 */
public class EXPAnimation {

    public double posX, posY, speedX = 200f, speedY = 1f;
    public boolean done = false;

    private double timeOfLastFrame = System.nanoTime() / 1e9;

    public void update(double neededX, double neededY) {
        double time = System.nanoTime() / 1e9;
        double timePassed = time - timeOfLastFrame;
        timeOfLastFrame = time;
        update(timePassed, neededX, neededY);

        if(posX == neededX && posY == neededY) {
            done = true;
        }
    }


    private void update(double time, double neededX, double neededY) {
        posX = posX < neededX ? posX + (time * speedX) > neededX ? neededX : posX + (time * speedX) : posX - (time * speedX) < neededX ? neededX : posX - (time * speedX);
        posY = posY < neededY ? posY + (time * speedY) > neededY ? neededY : posY + (time * speedY) : posY - (time * speedY) < neededY ? neededY : posY - (time * speedY);
    }

}
