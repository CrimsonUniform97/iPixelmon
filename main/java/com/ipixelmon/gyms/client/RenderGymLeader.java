package com.ipixelmon.gyms.client;

import com.ipixelmon.gyms.EntityGymLeader;
import com.ipixelmon.uuidmanager.UUIDManager;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.pixelmonmod.pixelmon.client.render.RenderNPC;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Colby McHenry on 9/28/2016.
 */
public class RenderGymLeader extends RenderNPC {


    public RenderGymLeader(RenderManager manager) {
        super(manager);
    }

    // TODO: Get player skins to work
    @Override
    protected ResourceLocation getEntityTexture(EntityNPC entity) {
        EntityGymLeader gymLeader = (EntityGymLeader) entity;

        NetworkPlayerInfo networkplayerinfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(gymLeader.getPlayerUUID());

        ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
        GameProfile profile = new GameProfile(gymLeader.getPlayerUUID(), UUIDManager.getPlayerName(gymLeader.getPlayerUUID()));
        if (profile != null)
        {
            Minecraft minecraft = Minecraft.getMinecraft();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(profile);
            for(MinecraftProfileTexture.Type type : map.keySet())
            {
                System.out.println(type.name());
            }
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN))
            {
                System.out.println("CALLED");
                resourcelocation = minecraft.getSkinManager().loadSkin((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            }
            else
            {
                UUID uuid = EntityPlayer.getUUID(profile);
                resourcelocation = DefaultPlayerSkin.getDefaultSkin(uuid);
            }
        }

        return resourcelocation;
//        return networkplayerinfo == null ? DefaultPlayerSkin.getDefaultSkin(entity.getUniqueID()) : networkplayerinfo.getLocationSkin();
    }

}
