package com.ipixelmon.gyms.client.gui;

import com.ipixelmon.PixelmonUtility;
import com.ipixelmon.gyms.PacketStorePokemon;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.pixelbay.gui.ColorPicker;
import com.pixelmonmod.pixelmon.achievement.PixelmonAchievements;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.pokedex.GuiPokedex;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayerMP;
import org.lwjgl.util.Rectangle;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by colby on 10/10/2016.
 */
public class GuiClaimGym extends GuiScreen {

    private int posX, posY, bgWidth = 250, bgHeight = 200;
    private ArrayList<EntityPixelmon> pixelmonList = new ArrayList<EntityPixelmon>();
    private int page = 0;

    public GuiClaimGym() {
        for (PixelmonData pixelmonData : ServerStorageDisplay.pokemon) {
            EntityPixelmon pixelmon = (EntityPixelmon) PixelmonEntityList.createEntityByName(pixelmonData.name, Minecraft.getMinecraft().thePlayer.worldObj);
            pixelmon.setHealth(pixelmonData.health);
            pixelmon.setIsShiny(pixelmonData.isShiny);
            pixelmon.setForm(pixelmonData.form);
            pixelmon.getLvl().setLevel(pixelmonData.lvl);
            pixelmon.setGrowth(EnumGrowth.Enormous);
            pixelmon.caughtBall = EnumPokeballs.PokeBall;
            pixelmon.friendship.initFromCapture();
            pixelmonList.add(pixelmon);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        drawRect(new Rectangle(posX, posY, bgWidth, bgHeight), ColorPicker.color(249f, 39f, 39f, 255f), ColorPicker.color(255f, 66f, 66f, 255f));

        mc.fontRendererObj.setUnicodeFlag(true);
        EntityPixelmon pixelmon = pixelmonList.get(page);
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.translate(this.width / 2f, (this.height / 2f) + 50, 0f);
        GuiPokedex.drawEntityToScreen(0, 0, 71 * 2, 77 * 2, pixelmon, partialTicks, true);
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();

        mc.fontRendererObj.drawString("Name: " + pixelmon.getName(), posX + 5, posY + 5, 0xFFFFFF, true);
        mc.fontRendererObj.drawString("Shiny: " + pixelmon.getIsShiny(), posX + 5, posY + 12, 0xFFFFFF, true);
        mc.fontRendererObj.drawString("BP: " + PixelmonUtility.getBP(pixelmon), posX + 5, posY + 19, 0xFFFFFF, true);
        mc.fontRendererObj.drawString("Level: " + pixelmon.getLvl().getLevel(), posX + 5, posY + 26, 0xFFFFFF, true);

        mc.fontRendererObj.setUnicodeFlag(false);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        page = button.id == 0 ? page - 1 <= 0 ? 0 : page - 1 : button.id == 1 ? page + 1 < pixelmonList.size() ? page + 1 : page : page;

        if (button.id == 2) Minecraft.getMinecraft().thePlayer.closeScreen();

        if (button.id == 3) {
            iPixelmon.network.sendToServer(new PacketStorePokemon(ServerStorageDisplay.pokemon[page]));
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        posX = (this.width - bgWidth) / 2;
        posY = (this.height - bgHeight) / 2;

        buttonList.clear();

        buttonList.add(new GuiButton(0, posX - 25, posY + ((bgHeight / 2) - (20 / 2)), 20, 20, "<"));
        buttonList.add(new GuiButton(1, posX + bgWidth + 5, posY + ((bgHeight / 2) - (20 / 2)), 20, 20, ">"));
        buttonList.add(new GuiButton(2, posX, posY + bgHeight, 40, 20, "Exit"));
        buttonList.add(new GuiButton(3, posX + bgWidth - 100, posY + bgHeight, 100, 20, "Store Pixelmon"));
    }

    public void drawRect(Rectangle rect, Color bgColor, Color trimColor) {
        int x = rect.getX(), y = rect.getY(), w = rect.getWidth(), h = rect.getHeight();
        x += 4;
        y += 4;
        w -= 8;
        h -= 8;
        int l = bgColor.getRGB();
        this.drawGradientRect(x - 3, y - 4, x + w + 3, y - 3, l, l);
        this.drawGradientRect(x - 3, y + h + 3, x + w + 3, y + h + 4, l, l);
        this.drawGradientRect(x - 3, y - 3, x + w + 3, y + h + 3, l, l);
        this.drawGradientRect(x - 4, y - 3, x - 3, y + h + 3, l, l);
        this.drawGradientRect(x + w + 3, y - 3, x + w + 4, y + h + 3, l, l);
        int i1 = trimColor.getRGB();
        int j1 = (i1 & 16711422 /* white */) >> 1 | i1 & -16777216 /* black */;
        this.drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + h + 3 - 1, i1, j1);
        this.drawGradientRect(x + w + 2, y - 3 + 1, x + w + 3, y + h + 3 - 1, i1, j1);
        this.drawGradientRect(x - 3, y - 3, x + w + 3, y - 3 + 1, i1, i1);
        this.drawGradientRect(x - 3, y + h + 2, x + w + 3, y + h + 3, j1, j1);
    }
}
