package com.ipixelmon.util;

import com.google.common.collect.Lists;
import com.ipixelmon.iPixelmon;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.render.RenderPixelmon;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.Remove;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.UpdateCurrency;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity3HasStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.enums.*;
import com.pixelmonmod.pixelmon.storage.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
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

    public static PixelmonData entityToData(EntityPixelmon pixelmon) {
        NBTTagCompound n = new NBTTagCompound();
        pixelmon.writeToNBT(n);
        n.setString("id", "Pixelmon");
        n.setBoolean("IsInBall", true);
        n.setBoolean("IsShiny", pixelmon.getIsShiny());
        if (pixelmon.getHeldItemMainhand() != null) {
            n.setTag("HeldItem", pixelmon.getHeldItemMainhand().getTagCompound());
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
        display.setString("Name", EntityPixelmon.getLocalizedName(pokemon.name) + " " + I18n.format("item.PixelmonSprite.name"));
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

                entityPixelmon.setHealth(pixelmonData.health);
                entityPixelmon.getLvl().setExp(pixelmonData.xp);
                entityPixelmon.stats.HP = pixelmonData.HP;
                entityPixelmon.stats.Defence = pixelmonData.Defence;
                entityPixelmon.stats.Attack = pixelmonData.Attack;
                entityPixelmon.stats.Speed = pixelmonData.Speed;
                entityPixelmon.stats.SpecialAttack = pixelmonData.SpecialAttack;
                entityPixelmon.stats.SpecialDefence = pixelmonData.SpecialDefence;

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

        public static void renderPixelmon3D(EntityPixelmon pixelmon, int x, int y, int z, float scale, float spin) {
            RenderPixelmon renderPixelmon = new RenderPixelmon(Minecraft.getMinecraft().getRenderManager());

            pixelmon.deathTime = 0;

            GlStateManager.pushAttrib();
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            float width = pixelmon.widthDiff / 2;
            float height = pixelmon.heightDiff / 2;
            GlStateManager.translate(width, height, 0.0F);
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.rotate(180, 1, 0, 0);
            GlStateManager.rotate(spin, 0, 1, 0);

            if (pixelmon.baseStats.canSurf) {
                GlStateManager.translate(0.0F, 1.0F, 0.0F);
            }

            GlStateManager.translate(-width, -height, 0.0F);
            GuiUtil.setBrightness(0.5F, 0.8F, 0.8F);
            renderPixelmon.renderPixelmon(pixelmon, 0, 0, 0, 0, 0, false);
            GlStateManager.popMatrix();
            GlStateManager.popAttrib();
        }

        public static int[] renderPixelmonTip(EntityPixelmon pixelmon, int x, int y, int width, int height) {
            PixelmonData pixelmonData = PixelmonAPI.entityToData(pixelmon);

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            List<String> pixelmonInfo = new ArrayList<>();
            pixelmonInfo.add(pixelmonData.name);
            pixelmonInfo.add("");
            pixelmonInfo.add(TextFormatting.YELLOW + "Lvl: " + pixelmonData.lvl);
            pixelmonInfo.add(TextFormatting.LIGHT_PURPLE + "XP: " + pixelmonData.xp + "/" + pixelmonData.nextLvlXP);
            float halfHealth = pixelmonData.HP / 2;
            float thirdHealth = pixelmonData.HP / 3;
            TextFormatting healthColor = pixelmonData.health <= halfHealth ? TextFormatting.RED : pixelmonData.health <= thirdHealth ?
                    TextFormatting.GOLD : TextFormatting.GREEN;
            pixelmonInfo.add(healthColor + "HP: " + pixelmonData.health + "/" + pixelmonData.HP);
            pixelmonInfo.add(TextFormatting.BLUE + "CP: " + PixelmonAPI.getCP(pixelmon));
            return GuiUtil.drawHoveringText(pixelmonInfo, x, y, width, height);
        }

    }

    @SideOnly(Side.SERVER)
    public static class Server {

        public static List<EntityPixelmon> getPixelmon(EntityPlayerMP player, boolean fromPokeBalls) {
            List<EntityPixelmon> pixelmonList = Lists.newArrayList();
            try {
                if (fromPokeBalls) {
                    for (int[] pID : PixelmonStorage.pokeBallManager.getPlayerStorage(player).get().getAllAblePokemonIDs()) {
                        pixelmonList.add(PixelmonStorage.pokeBallManager.getPlayerStorage(player).get().getPokemon(pID, player.worldObj));
                    }
                } else {
                    for (ComputerBox box : PixelmonStorage.computerManager.getPlayerStorage(player).getBoxList()) {
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

        // TODO: Not working when claiming Gym
        public static void removePixelmon(EntityPixelmon pixelmon, EntityPlayerMP player) {
            FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    PCServer.deletePokemon(player, -1, Math.max(0, pixelmon.getPartyPosition()));
                }
            });

            Pixelmon.network.sendTo(new Remove(pixelmon.getPokemonId()), player);
        }

        public static final void giveMoney(final UUID player, final int balance) {
            try {
                final PlayerStorage targetStorage = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(FMLCommonHandler.instance().getMinecraftServerInstance(), player).get();
                targetStorage.addCurrency(Math.abs(balance));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static final void takeMoney(final UUID player, final int balance) {
            try {
                final PlayerStorage targetStorage = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(FMLCommonHandler.instance().getMinecraftServerInstance(), player).get();
                targetStorage.addCurrency(-1 * Math.abs(balance));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static final int getBalance(final UUID player) {
            try {
                final PlayerStorage targetStorage = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(FMLCommonHandler.instance().getMinecraftServerInstance(), player).get();
                return targetStorage.getCurrency();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return 0;
        }
    }

}
