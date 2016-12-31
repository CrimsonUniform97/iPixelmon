package com.ipixelmon.quest.client;

import com.ipixelmon.quest.Quest;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Mouse;

import java.io.IOException;

/**
 * Created by colby on 12/30/2016.
 */
public class GuiQuest extends GuiScreen {

    private Quest quest;
    private ListItem toObtain, toReward;


    public GuiQuest(Quest quest) {
        this.quest = quest;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int dWheel = Mouse.getDWheel();
        toObtain.draw(mouseX, mouseY, dWheel);
        toReward.draw(mouseX, mouseY, dWheel);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        toObtain.keyTyped(typedChar, keyCode);
        toReward.keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui() {
        super.initGui();
        int listWidth = 100;
        int listHeight = 100;
        int xPos = (this.width - (listWidth * 2)) / 2;
        int yPos = (this.height - listHeight) / 2;

        toObtain = new ListItem(xPos, yPos, listWidth, listHeight, quest.getItemsToObtain());
        toReward = new ListItem(xPos + listWidth, yPos, listWidth, listHeight, quest.getItemsToReward());
    }
}
