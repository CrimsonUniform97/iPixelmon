package com.ipixelmon.gym.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

/**
 * Created by Colby McHenry on 9/29/2016.
 */
public class CustomSkinManager {

    public static final CustomSkinManager instance = new CustomSkinManager();

    private final TextureManager textureManager;
    private final File skinCacheDir;

    private CustomSkinManager() {
        textureManager = Minecraft.getMinecraft().getTextureManager();
        skinCacheDir = new File("skinCache");
    }

    public ResourceLocation loadSkin(UUID playerid) {
        final ResourceLocation resourcelocation = new ResourceLocation("skins/" + playerid.toString() + ".png");
        ITextureObject itextureobject = this.textureManager.getTexture(resourcelocation);

        if (itextureobject == null) {
            final IImageBuffer iimagebuffer = new ImageBufferDownload();
            File file1 = new File(this.skinCacheDir, playerid.toString() + ".png");
            ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(file1, "https://crafatar.com/skins/" + playerid.toString(), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer() {
                public BufferedImage parseUserSkin(BufferedImage image) {
                    if (iimagebuffer != null) {
                        image = iimagebuffer.parseUserSkin(image);
                    }

                    return image;
                }

                public void skinAvailable() {
                    if (iimagebuffer != null) {
                        iimagebuffer.skinAvailable();
                    }
                }
            });
            this.textureManager.loadTexture(resourcelocation, threaddownloadimagedata);
        }
        return resourcelocation;
    }


}
