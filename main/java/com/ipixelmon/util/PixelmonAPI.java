package com.ipixelmon.util;

import com.google.common.collect.Lists;
import com.ipixelmon.iPixelmon;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.render.RenderPixelmon;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.Add;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.Remove;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.UpdateCurrency;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity3HasStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.enums.*;
import com.pixelmonmod.pixelmon.listener.EntityPlayerExtension;
import com.pixelmonmod.pixelmon.storage.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import scala.actors.threadpool.Arrays;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by colby on 12/31/2016.
 */
public class PixelmonAPI {

    // TODO: Update everything to the new AI

    public static PixelmonData entityToData(EntityPixelmon pixelmon) {
        NBTTagCompound n = new NBTTagCompound();
        pixelmon.writeToNBT(n);
        n.setString("id", "Pixelmon");
        n.setBoolean("IsInBall", true);
        n.setBoolean("IsShiny", pixelmon.getIsShiny());
        if (pixelmon.getHeldItem() != null) {
            n.setTag("HeldItem", pixelmon.getHeldItem().getTagCompound());
        }

//        PlayerStorage.addToParty()

//        n.func_74768_a("PixelmonOrder", this.getNextOpen());
//        this.partyPokemon[this.getNextOpen()] = n;
        if (pixelmon.getHealth() > 0.0F) {
            n.setBoolean("IsFainted", false);
        }

        return new PixelmonData(n);
    }

    public static String pixelmonToString(EntityPixelmon p) {
        NBTTagCompound n = new NBTTagCompound();
        p.writeToNBT(n);
        n.setString("id", p.getPokemonName());
        n.setBoolean("IsInBall", true);
        n.setBoolean("IsShiny", p.getIsShiny());
        return n.toString();
    }

    public static EntityPixelmon pixelmonFromString(String s, World worldObj) {
        NBTTagCompound n = NBTUtil.fromString(s);
        if (n != null) {
            n.setFloat("FallDistance", 0.0F);
            n.setBoolean("IsInBall", false);
            EntityPixelmon e = (EntityPixelmon) PixelmonEntityList.createEntityFromNBT(n, worldObj);
            e.playerOwned = true;
            return e;
        }

        return null;
    }

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

    public static double getCP(EntityPixelmon pixelmon) {
        double TotalBpMultiplier = 0.095 * Math.sqrt(pixelmon.getLvl().getLevel());
        double Stamina = (pixelmon.baseStats.speed + pixelmon.stats.Speed) * TotalBpMultiplier;
        double Attack = (pixelmon.baseStats.attack + pixelmon.stats.Attack) * TotalBpMultiplier;
        double Defense = (pixelmon.baseStats.defence + pixelmon.stats.Defence) * TotalBpMultiplier;
        return Math.max(10, Math.floor(Math.pow(Stamina, 0.5) * Attack * Math.pow(Defense, 0.5) / 10));
    }

    public static int getPixelmonMaxHealth(PixelmonData pixelmonData) {
        return pixelmonData.HP;
    }

    public static int getPixelmonHealth(PixelmonData pixelmonData) {
        return pixelmonData.health;
    }

    @SideOnly(Side.CLIENT)
    public static class Client {

        public static final int getBalance() {
            return UpdateCurrency.playerMoney;
        }

        public static List<EntityPixelmon> getPixelmon(boolean fromPokeBalls) {
            List<PixelmonData> pixelmon = new ArrayList<>(Arrays.asList(ServerStorageDisplay.pokemon));
            pixelmon.removeAll(Collections.singleton(null));

            List<EntityPixelmon> entityPixelmons = Lists.newArrayList();

            for (PixelmonData pixelmonData : pixelmon) {
                EntityPixelmon entityPixelmon = (EntityPixelmon) PixelmonEntityList.createEntityByName(pixelmonData.name, iPixelmon.proxy.getDefaultWorld());
                NBTTagCompound tagCompound = new NBTTagCompound();
                entityPixelmon.writeToNBT(tagCompound);
                pixelmonData.updatePokemon(tagCompound);
                entityPixelmon.readFromNBT(tagCompound);
                entityPixelmon.setPokemonId(pixelmonData.pokemonID);
                entityPixelmons.add(entityPixelmon);
            }

            return entityPixelmons;
        }

        public static final void renderPokeDollar(Minecraft mc, int x, int y, int scale, int color) {
            glPushAttrib(GL_ALL_ATTRIB_BITS);
            {
                float red = (float) (color >> 16 & 255) / 255.0F;
                float blue = (float) (color >> 8 & 255) / 255.0F;
                float green = (float) (color & 255) / 255.0F;
                float alpha = (float) (color >> 24 & 255) / 255.0F;
                glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_ADD);
                GlStateManager.color(red, blue, green, 1);
                mc.getTextureManager().bindTexture(new ResourceLocation(iPixelmon.id, "textures/gui/pokedollar.png"));
                int width = 6;
                int height = 10;
                GuiHelper.drawImageQuad(x, y, width * scale, height * scale, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);
            }
            glPopAttrib();
        }

        public static void renderPixelmon2D(EntityPixelmon pixelmon, int x, int y, int width, int height) {
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.enableBlend();
            GuiHelper.bindPokemonSprite(PixelmonAPI.entityToData(pixelmon), Minecraft.getMinecraft());
            GuiHelper.drawImageQuad(x, y, width, height, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);
        }

        public static PixelmonRenderer renderPixelmon3D(EntityPixelmon pixelmon, boolean spin, GuiScreen screen) {
            return new PixelmonRenderer(pixelmon, spin, screen);
        }

        public static int[] renderPixelmonTip(EntityPixelmon pixelmon, int x, int y, int width, int height) {
            PixelmonData pixelmonData = PixelmonAPI.entityToData(pixelmon);

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            List<String> pixelmonInfo = new ArrayList<>();
            pixelmonInfo.add(pixelmonData.name);
            pixelmonInfo.add("");
            pixelmonInfo.add(EnumChatFormatting.YELLOW + "Lvl: " + pixelmonData.lvl);
            pixelmonInfo.add(EnumChatFormatting.LIGHT_PURPLE + "XP: " + pixelmonData.xp + "/" + pixelmonData.nextLvlXP);
            float half = pixelmonData.HP / 2;
            float third = pixelmonData.HP / 3;
            float health = pixelmonData.HP;
            EnumChatFormatting healthColor = health <= half ? EnumChatFormatting.RED : health <= third ?
                    EnumChatFormatting.GOLD : EnumChatFormatting.GREEN;
            pixelmonInfo.add(healthColor + "HP: " + pixelmonData.health + "/" + pixelmonData.HP);
            pixelmonInfo.add(EnumChatFormatting.BLUE + "CP: " + PixelmonAPI.getCP(pixelmon));
            return GuiUtil.drawHoveringText(pixelmonInfo, x, y, width, height);
        }

        public static class PixelmonRenderer implements Runnable {
            private EntityPixelmon pixelmon;
            private boolean spin, countDown;
            private float partialTicks, spinCount;
            private RenderPixelmon renderPixelmon;
            private GuiScreen screen;

            public PixelmonRenderer(EntityPixelmon pixelmon, boolean spin, GuiScreen screen) {
                this.pixelmon = pixelmon;
                renderPixelmon = new RenderPixelmon(Minecraft.getMinecraft().getRenderManager());
                this.spin = spin;
                this.screen = screen;
            }


            @Override
            public void run() {
                while (Minecraft.getMinecraft().currentScreen == screen) {
                    if (countDown) {
                        partialTicks -= 0.08F;
                    } else {
                        partialTicks += 0.08F;
                    }

                    if (partialTicks >= 1.0F) {
                        partialTicks = 1.0F;
                        countDown = true;
                    } else if (partialTicks <= 0.0F) {
                        partialTicks = 0.0F;
                        countDown = false;
                    }

                    spinCount += 0.66F;

                    try {
                        Thread.sleep(20L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            // TODO: Fix spin
            public void render(int x, int y, float scale) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(x, y, 400.0F);
                float width = pixelmon.widthDiff / 2;
                float height = pixelmon.heightDiff / 2;
                GlStateManager.translate(width, height, 0.0F);
                GlStateManager.scale(scale, scale, scale);
                GlStateManager.rotate(180, 1, 0, 0);
                GlStateManager.rotate(spinCount, 0, 1, 0);

                if (pixelmon.baseStats.canSurf) {
                    GlStateManager.translate(0.0F, 1.0F, 0.0F);
                }

                GlStateManager.translate(-width, -height, 0.0F);
                GuiUtil.setBrightness(0.5F, 0.8F, 0.8F);
                renderPixelmon.renderPixelmon(pixelmon, 0, 0, 0, 0, partialTicks, false);
                GlStateManager.popMatrix();
                RenderHelper.disableStandardItemLighting();
            }
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

        public static void removePixelmon(EntityPixelmon pixelmon, EntityPlayerMP player) {
            MinecraftServer.getServer().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    PixelmonData pixelmonData = new PixelmonData(pixelmon);
                    PCServer.deletePokemon(player, -1, pixelmonData.order);
                    Pixelmon.network.sendTo(new Remove(pixelmonData.pokemonID), player);
                }
            });
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
