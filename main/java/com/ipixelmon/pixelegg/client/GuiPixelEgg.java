package com.ipixelmon.pixelegg.client;

import com.pixelmonmod.pixelmon.client.gui.pokedex.GuiPokedex;
import com.pixelmonmod.pixelmon.client.models.animations.EnumRotation;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.ipixelmon.pixelegg.egg.PixelEggItem;
import com.ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GuiPixelEgg extends GuiScreen
{

    private static final ResourceLocation lens_flares = new ResourceLocation(iPixelmon.id, "textures/gui/lens_flares.png");
    private static final ResourceLocation super_flare = new ResourceLocation(iPixelmon.id, "textures/gui/super_flare.png");
    private static final Rectangle orb = new Rectangle(0, 0, 94, 82);
    private static final Rectangle underGlow = new Rectangle(0, 128, 256, 128);
    private static final Rectangle shadow = new Rectangle(116, 0, 140, 54);
    private static final float eggScale = 7f;

    EntityPixelmon pixelmon;
    private Animation eggAnimation, superFlareAnimation;
    private ItemStack toRender;
    private float brightness = 255f;

    private List<Animation> orbs = new ArrayList<Animation>();

    public GuiPixelEgg(EntityPixelmon pixelmon)
    {
        this.pixelmon = pixelmon;
        this.pixelmon.getLvl().setLevel(50);
        this.pixelmon.setGrowth(EnumGrowth.Ordinary);
    }

    @Override
    public void initGui()
    {
        toRender = new ItemStack(PixelEggItem.instance, 1, 0);

        int centerX = (this.width - 16) / 2;
        int centerY = (this.height - 16) / 2;
        eggAnimation = new Animation(centerX - 5, centerY + 5, 0f).scale(7f).rotate(20f, 0f, 0f)
                .rotateTo(EnumRotation.x, -40f, 3.2f).rotateTo(EnumRotation.x, 20f, 3.0f).wait(1000)
                .rotateTo(EnumRotation.z, -40f, 3.2f).rotateTo(EnumRotation.z, 0f, 2.0f).wait(800)
                .rotateTo(EnumRotation.z, 40f, 3.2f).rotateTo(EnumRotation.z, 0f, 2.5f).wait(1180)
                .translateTo(centerX, centerY - 80f, 2.5f)
                .scaleTo(0.5f, 0.4f);
        superFlareAnimation = new Animation((this.width - 256) / 2, 50 + (this.height - 256) / 2, 0f).scaleTo(1.3f, 0.3f).scaleTo(1.2f, 0.002f).wait(200);

        addOrbs();
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException
    {
        if(superFlareAnimation.stage() >= 2)
        {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

        drawBackground();

        mc.getTextureManager().bindTexture(lens_flares);
        this.drawTexturedModalRect((this.width - underGlow.getWidth()) / 2, this.height - underGlow.getHeight(), underGlow.getX(), underGlow.getY(), underGlow.getWidth(), underGlow.getHeight());

        if (superFlareAnimation.stage() >= 2)
        {
            drawShadow();
            GlStateManager.pushMatrix();
            GlStateManager.translate((this.width - pixelmon.width) / 2f, (this.height - pixelmon.height) / 2f, 0f);
            GuiPokedex.drawEntityToScreen(0, 0, 51 * 2, 55 * 2, pixelmon, partialTicks, true);
            GlStateManager.popMatrix();

        }

        drawEgg();

        if (eggAnimation.stage() < 13)
        {
            drawShadow();
        }

        if (eggAnimation.stage() >= 13)
        {
            drawSuperFlare();
        }

        drawOrbs();

        brightness -= 1.8f;
        GlStateManager.color(1f, 1f, 1f, brightness / 255f);
        GlStateManager.translate(0f, 0f, 300f);
        GlStateManager.disableTexture2D();
        this.drawTexturedModalRect(0, 0, 0, 0, this.width, this.height);
        GlStateManager.enableTexture2D();
    }

    private void drawSuperFlare()
    {
        if (superFlareAnimation.stage() >= 2)
        {
            return;
        }

        mc.getTextureManager().bindTexture(super_flare);
        GuiUtil.instance.setBrightness(1.6f, 1.8f, 0f);
        GlStateManager.enableBlend();
        GlStateManager.pushMatrix();

        float width = 256f, height = 256f;
        superFlareAnimation.animate();
        //move to position
        GlStateManager.translate(superFlareAnimation.posX(), superFlareAnimation.posY(), superFlareAnimation.posZ());
        // translate to center of egg
        // We use 100f because that is what Minecraft is on by default.
        float z = 300f;
        GlStateManager.translate(width / 2f, height / 2f, z);

        // scale
        float scale = superFlareAnimation.scalar();
        GlStateManager.scale(scale + (this.width / width), scale + (this.height / height) * 0.5f, 1f);

        // rotate
        GlStateManager.rotate(superFlareAnimation.rotX(), 1f, 0f, 0f);
        GlStateManager.rotate(superFlareAnimation.rotY(), 0f, 1f, 0f);
        GlStateManager.rotate(superFlareAnimation.rotZ(), 0f, 0f, 1f);

        // translate back
        GlStateManager.translate(-(width / 2f), -(height / 2f), -z);
        this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);

        GlStateManager.popMatrix();
    }

    private void drawEgg()
    {

        if (eggAnimation.stage() >= 13)
        {
            return;
        }

        GuiUtil.instance.setBrightness(0.6f, 1.5f, 0f);
        GlStateManager.enableBlend();

        GlStateManager.pushMatrix();

        float width = 16f, height = 16f;
        eggAnimation.animate();
        //move to position
        GlStateManager.translate(eggAnimation.posX(), eggAnimation.posY(), eggAnimation.posZ());
        // translate to center of egg
        // We use 100f because that is what Minecraft is on by default.
        float z = 100f;
        GlStateManager.translate(width / 2f, height / 2f, z);

        // scale
        GlStateManager.scale(eggAnimation.scalar(), eggAnimation.scalar(), 1f);

        // rotate
        GlStateManager.rotate(eggAnimation.rotX(), 1f, 0f, 0f);
        GlStateManager.rotate(eggAnimation.rotY(), 0f, 1f, 0f);
        GlStateManager.rotate(eggAnimation.rotZ(), 0f, 0f, 1f);

        // translate back
        GlStateManager.translate(-(width / 2f), -(height / 2f), -z);

        if (eggAnimation.stage() == 6)
        {
            toRender = new ItemStack(PixelEggItem.instance, 1, 1);
        } else if (eggAnimation.stage() == 9)
        {
            toRender = new ItemStack(PixelEggItem.instance, 1, 2);
        }
        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(toRender, 0, 0);
        GlStateManager.popMatrix();
    }

    private List<Float> setupYTranslations(float startY)
    {
        List<Float> list = new ArrayList<Float>();
        Random r = new Random();
        float y = startY;

        while (y > -100)
        {
            list.add(y = MathHelper.randomFloatClamp(r, y - 30, y - 10));
        }

        return list;
    }

    private List<Float> setupXTranslations(float startX, List<Float> yTranslations)
    {
        List<Float> list = new ArrayList<Float>();
        Random r = new Random();
        float x = startX;

        for (int i = 0; i < yTranslations.size(); i++)
        {
            list.add((float) MathHelper.getRandomIntegerInRange(r, (int) x - 50, (int) x + 50));
        }

        return list;
    }

    private void addOrbs()
    {
        Random r = new Random();

        for (int circle = 0; circle < 10; circle++)
        {
            Animation a = new Animation(r.nextInt(this.width), r.nextInt(this.height), 100f);
            List<Float> yTranslations = this.setupYTranslations(a.startX());
            List<Float> xTranslations = this.setupXTranslations(a.startY(), yTranslations);

            int translation = 0;
            for (Float f : yTranslations)
            {
                a.translateTo(xTranslations.get(translation), yTranslations.get(translation), 0.5f);
                translation++;
            }

            orbs.add(a);
        }
    }

    private void drawOrbs()
    {
        GuiUtil.instance.setBrightness(1.6f, 1.8f, 0f);
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(lens_flares);
        for (Animation a : orbs)
        {
            a.animate();
            GlStateManager.pushMatrix();
            GlStateManager.translate(a.posX(), a.posY(), 0);
            GlStateManager.translate(orb.getWidth() / 2, orb.getHeight() / 2, 200f);
            GlStateManager.scale(0.2f, 0.2f, 1f);
            this.drawTexturedModalRect(0, 0, orb.getX(), orb.getY(), orb.getWidth(), orb.getHeight());
            GlStateManager.translate(-(orb.getWidth() / 2), -(orb.getHeight() / 2), -200f);
            GlStateManager.popMatrix();
        }
    }

    private void drawShadow()
    {
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(lens_flares);
        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 175f / 255f);
        if (superFlareAnimation.stage() < 2)
        {
            GlStateManager.translate(eggAnimation.posX() - (shadow.getWidth() / 2) + 10f, eggAnimation.startY() + 45f, 100f);
            GlStateManager.translate(shadow.getWidth() / 2, shadow.getHeight() / 2, 100f);
            float scalar = eggAnimation.posY() / (eggAnimation.startY() + 5);
            scalar *= (eggAnimation.scalar() / eggScale);
            GlStateManager.scale(scalar, scalar, scalar);
            GlStateManager.translate(-(shadow.getWidth() / 2), -(shadow.getHeight() / 2), -100f);
            this.drawTexturedModalRect(0, 0, shadow.getX(), shadow.getY(), shadow.getWidth(), shadow.getHeight());
        } else
        {
            GlStateManager.translate(eggAnimation.posX() - (shadow.getWidth() / 2) + 6, eggAnimation.startY() + 45f, 100f);
            GlStateManager.translate(shadow.getWidth() / 2, shadow.getHeight() / 2, 100f);
            GlStateManager.scale(0.3f, 0.3f, 0f);
            GlStateManager.translate(-(shadow.getWidth() / 2), -(shadow.getHeight() / 2), -100f);
            this.drawTexturedModalRect(0, 0, shadow.getX(), shadow.getY(), shadow.getWidth(), shadow.getHeight());
        }
        GlStateManager.popMatrix();
    }

    private void drawBackground()
    {
        GlStateManager.disableTexture2D();
        GlStateManager.color(170f / 255f, 140f / 255f, 70f / 255f, 255f / 255f);
        this.drawTexturedModalRect(0, 0, 0, 0, this.width, this.height);
        GlStateManager.enableTexture2D();
        GlStateManager.color(1, 1, 1, 1);
    }
}
