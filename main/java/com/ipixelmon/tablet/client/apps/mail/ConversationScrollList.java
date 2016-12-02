package com.ipixelmon.tablet.client.apps.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by colby on 11/21/2016.
 */
public class ConversationScrollList extends  CustomSizeScrollList{

    private List<String> list = new ArrayList<>();

    public ConversationScrollList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
        list.add("Test 1");
        list.add("Test 2");
        list.add("Test 3");
        list.add("Test 4");
        list.add("Test 5");
        list.add("Test 6");
        list.add("Test 7");
        list.add("Test 8");
        list.add("Test 9");
        list.add("Test 10");
        list.add("Test 11");
        list.add("Test 12");
        list.add("Test 13");
        list.add("Test 14");
        list.add("Test 15");
        list.add("Test 16");
        list.add("Test 17");
    }

    @Override
    public int getObjectHeight(int index) {
        return 10;
    }

    @Override
    public void drawObject(int index, int x, int y) {
        mc.fontRendererObj.drawString(list.get(index), x, y, 0xFFFFFF);
    }

    @Override
    public int getSize() {
        return list.size();
    }
}