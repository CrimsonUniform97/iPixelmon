package com.ipixelmon.util;

import com.google.common.collect.Lists;
import com.ipixelmon.iPixelmon;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.UpdateCurrency;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity3HasStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.storage.ComputerBox;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by colby on 12/31/2016.
 */
public class PixelmonAPI {

    public static final ItemStack makePokemonItem(final EnumPokemon pokemon) {
        final ItemStack stack = new ItemStack(PixelmonItems.itemPixelmonSprite);
        final NBTTagCompound tagCompound = new NBTTagCompound();
        final Optional stats = Entity3HasStats.getBaseStats(pokemon.name);
        tagCompound.setString("SpriteName", "pixelmon:sprites/items/" + String.format("%03d", new Object[]{Integer.valueOf(((BaseStats) stats.get()).nationalPokedexNumber)}));
        final NBTTagCompound display = new NBTTagCompound();
        display.setString("Name", EntityPixelmon.getLocalizedName(pokemon.name) + " " + StatCollector.translateToLocal("item.PixelmonSprite.name"));
        tagCompound.setTag("display", display);
        stack.setTagCompound(tagCompound);
        return stack;
    }

    public static double getBP(EntityPixelmon pixelmon) {
        double TotalBpMultiplier = 0.095 * Math.sqrt(pixelmon.getLvl().getLevel());
        double Stamina = (pixelmon.baseStats.speed + pixelmon.stats.Speed) * TotalBpMultiplier;
        double Attack = (pixelmon.baseStats.attack + pixelmon.stats.Attack) * TotalBpMultiplier;
        double Defense = (pixelmon.baseStats.defence + pixelmon.stats.Defence) * TotalBpMultiplier;
        return Math.max(10, Math.floor(Math.pow(Stamina, 0.5) * Attack * Math.pow(Defense, 0.5) / 10));
    }

    @SideOnly(Side.CLIENT)
    public static class Client {

        public static final int getBalance() {
            return UpdateCurrency.playerMoney;
        }

        public static List<PixelmonData> getPixelmon(boolean fromPokeBalls) {
            List<PixelmonData> pixelmon = Arrays.asList(ServerStorageDisplay.pokemon);
            pixelmon.removeAll(Collections.singleton(null));

            return pixelmon;
        }

        public static final void drawPokeDollar(Minecraft mc, int x, int y, int w, int h, int color) {
            glPushAttrib(GL_ALL_ATTRIB_BITS);
            {
                float red = (float) (color >> 16 & 255) / 255.0F;
                float blue = (float) (color >> 8 & 255) / 255.0F;
                float green = (float) (color & 255) / 255.0F;
                float alpha = (float) (color >> 24 & 255) / 255.0F;
                glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_ADD);
                GlStateManager.color(red, blue, green, 1);
                mc.getTextureManager().bindTexture(new ResourceLocation(iPixelmon.id + ":pixelbay/textures/gui/pokedollar.png"));
                GuiHelper.drawImageQuad(x, y, w, h, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);
            }
            glPopAttrib();
        }

    }

    @SideOnly(Side.SERVER)
    public static class Server {

        public static List<EntityPixelmon> getPixelmon(EntityPlayerMP player, boolean fromPokeBalls) {
            List<EntityPixelmon> pixelmonList = Lists.newArrayList();
            try {
                if (fromPokeBalls) {
                    for (int[] pID : PixelmonStorage.PokeballManager.getPlayerStorage(player).getAllAblePokemonIDs()) {
                        pixelmonList.add(PixelmonStorage.PokeballManager.getPlayerStorage(player).getPokemon(pID, player.worldObj));
                    }
                } else {
                    for (ComputerBox box : PixelmonStorage.ComputerManager.getPlayerStorage(player).getBoxList()) {
                        if (box != null) {
                            for (NBTTagCompound tagCompound : box.getStoredPokemon()) {
                                if (tagCompound != null)
                                    pixelmonList.add((EntityPixelmon) PixelmonEntityList.createEntityFromNBT(tagCompound, player.worldObj));
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }

            return pixelmonList;
        }

        public static final void giveMoney(final UUID player, final int balance) {
            try {
                final PlayerStorage targetStorage = PixelmonStorage.PokeballManager.getPlayerStorageFromUUID(player);
                targetStorage.addCurrency(Math.abs(balance));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static final void takeMoney(final UUID player, final int balance) {
            try {
                final PlayerStorage targetStorage = PixelmonStorage.PokeballManager.getPlayerStorageFromUUID(player);
                targetStorage.addCurrency(-1 * Math.abs(balance));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static final int getBalance(final UUID player) {
            try {
                final PlayerStorage targetStorage = PixelmonStorage.PokeballManager.getPlayerStorageFromUUID(player);
                return targetStorage.getCurrency();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return 0;
        }
    }

}
