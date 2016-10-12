package com.ipixelmon.gyms.client.gui;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.pokedex.GuiPokedex;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokeballs;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colby McHenry on 10/1/2016.
 */
// TODO
public class GuiAddPixelmon extends GuiScreen{

    private GuiButton left, right, leavePixelmon;
    private int posX, posY, bgWidth, bgHeight;

    private List<EntityPixelmon> pixelmon;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        super.drawDefaultBackground();

//        GlStateManager.pushMatrix();
//        GlStateManager.translate((this.width - pixelmon.width) / 2f, (this.height - pixelmon.height) / 2f, 0f);
//        GuiPokedex.drawEntityToScreen(0, 0, 51 * 2, 55 * 2, pixelmon, partialTicks, true);
//        GlStateManager.popMatrix();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        pixelmon = getPixelmon();

        bgWidth = this.width - 80;
        bgHeight = this.height - 80;
        posX = (this.width - bgWidth) / 2;
        posY = (this.height - bgHeight) / 2;

        this.buttonList.add(left = new GuiButton(0, posX, posY, 20, 20, "<"));
        this.buttonList.add(right = new GuiButton(1, posX + bgWidth, posY, 20, 20, ">"));
        this.buttonList.add(leavePixelmon = new GuiButton(2, posX + (bgWidth / 2), posY + bgHeight, 150, 20, "Leave Pixelmon Here"));
    }

    public List<EntityPixelmon> getPixelmon()
    {
        List<EntityPixelmon> pDataList = new ArrayList<EntityPixelmon>();

        final PixelmonData[] pixelList = ServerStorageDisplay.pokemon;

        for(PixelmonData pData : pixelList) {
            if(pData != null){
                EntityPixelmon pixelmon = (EntityPixelmon) PixelmonEntityList.createEntityByName(EnumPokemon.Rayquaza.name, MinecraftServer.getServer().getEntityWorld());
                pixelmon.setHealth(pData.health);
                pixelmon.setIsShiny(pData.isShiny);
                pixelmon.setForm(pData.form);
                pixelmon.getLvl().setLevel(pData.lvl);
                pixelmon.caughtBall = EnumPokeballs.PokeBall;
                pixelmon.friendship.initFromCapture();
                pDataList.add(pixelmon);
            }
        }

        return pDataList;
    }

}
