package ipixelmon.eggincubator.client;

import com.pixelmonmod.pixelmon.client.gui.pokedex.GuiPokedex;
import com.pixelmonmod.pixelmon.client.models.animations.EnumRotation;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import ipixelmon.eggincubator.egg.EggItem;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GuiPokemonEggReward extends GuiScreen
{

    private static final ResourceLocation lens_flares = new ResourceLocation(iPixelmon.id, "textures/gui/lens_flares.png");
    private static final Rectangle orb = new Rectangle(0, 0, 94, 82);
    private static final Rectangle underGlow = new Rectangle(0, 128, 256, 128);
    private static final Rectangle shadow = new Rectangle(116, 0, 140, 54);
    private static final float eggScale = 7f;

    EntityPixelmon pixelmon;
    private Animation eggAnimation;
    private ItemStack toRender;

    private List<Animation> orbs = new ArrayList<>();

    @Override
    public void initGui()
    {
        toRender = new ItemStack(EggItem.instance, 1, 0);
        pixelmon = (EntityPixelmon) PixelmonEntityList.createEntityByName(EnumPokemon.Totodile.name, Minecraft.getMinecraft().theWorld);
        pixelmon.getLvl().setLevel(50);
        pixelmon.setGrowth(EnumGrowth.Ordinary);

        int centerX = (this.width - 16) / 2;
        int centerY = (this.height - 16) / 2;
        eggAnimation = new Animation(centerX - 3, centerY + 5, 100f).scale(7f).rotate(20f, 0f, 0f)
                .rotateTo(EnumRotation.x, -40f, 3.2f).rotateTo(EnumRotation.x,20f, 3.0f).wait(500)
                .rotateTo(EnumRotation.z, -40f, 3.2f).rotateTo(EnumRotation.z, 0f, 2.0f).wait(200)
                .rotateTo(EnumRotation.z, 40f, 3.2f).rotateTo(EnumRotation.z, 0f, 2.5f).wait(100)
                .translateTo(centerX, centerY - 80f, 1.2f)
                .scaleTo(0.5f, 0.4f);

        addOrbs();
    }
    // TODO: add flash at the beginning
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawBackground();

        mc.getTextureManager().bindTexture(lens_flares);
        this.drawTexturedModalRect((this.width - underGlow.getWidth()) / 2, this.height - underGlow.getHeight(), underGlow.getX(), underGlow.getY(), underGlow.getWidth(), underGlow.getHeight());

        GuiPokedex.drawEntityToScreen(100, 50, 51, 55, pixelmon, partialTicks, true);
        drawEgg();

        mc.getTextureManager().bindTexture(lens_flares);
        drawShadow();
        drawOrbs();
    }

    private void drawEgg()
    {

        GuiUtil.instance.setBrightness(0.6f, 1.5f, 0f);

        GlStateManager.pushMatrix();

        float width = 16f, height = 16f;
        eggAnimation.animate();
        //move to position
        GlStateManager.translate(eggAnimation.posX(), eggAnimation.posY(), eggAnimation.posZ());
        // translate to center of egg
        // We use 100f because that is what Minecraft is on by default.
        float z = 100f;
        GlStateManager.translate(width / 2f, height / 2f, z);
        Minecraft.getMinecraft().fontRendererObj.drawString("|", 0, 0, 0xFFFFFF);

        // TODO: Rotating works! Now lets work on the gui and then give the player a pokemon after walking and send the packets etc.

        // scale
        GlStateManager.scale(eggAnimation.scalar(), eggAnimation.scalar(), eggAnimation.scalar());

        // rotate
        GlStateManager.rotate(eggAnimation.rotX(), 1f, 0f, 0f);
        GlStateManager.rotate(eggAnimation.rotY(), 0f, 1f, 0f);
        GlStateManager.rotate(eggAnimation.rotZ(), 0f, 0f, 1f);

        // translate back
        GlStateManager.translate(-(width / 2f), -(height / 2f), -z);

        // TODO: Get animation stage to update texture
        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(toRender, 0, 0);
        GlStateManager.popMatrix();
    }

    private List<Float> setupYTranslations(float startY)
    {
        List<Float> list = new ArrayList<>();
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
        List<Float> list = new ArrayList<>();
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

        for(int circle = 0; circle < 10; circle++)
        {
            Animation a = new Animation( r.nextInt(this.width), r.nextInt(this.height), 100f);
            List<Float> yTranslations = this.setupYTranslations(a.getStartX());
            List<Float> xTranslations = this.setupXTranslations(a.getStartY(), yTranslations);

            int translation = 0;
            for(Float f : yTranslations)
            {
                a.translateTo(xTranslations.get(translation), yTranslations.get(translation), 0.5f);
                translation++;
            }

            orbs.add(a);
        }
    }

    private void drawOrbs()
    {
        for (Animation a : orbs)
        {
            a.animate();
            GlStateManager.pushMatrix();
            GlStateManager.translate(a.posX(), a.posY(), 0);
            GlStateManager.translate(orb.getWidth() / 2, orb.getHeight() / 2, 100f);
            GlStateManager.scale(0.2f, 0.2f, 0.2f);
            this.drawTexturedModalRect(0, 0, orb.getX(), orb.getY(), orb.getWidth(), orb.getHeight());
            GlStateManager.translate(-(orb.getWidth() / 2), -(orb.getHeight() / 2), - 100f);
            GlStateManager.popMatrix();
        }
    }

    private void drawShadow()
    {
        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 175f/255f);
        GlStateManager.translate(eggAnimation.posX() - (shadow.getWidth() / 2) + 11, eggAnimation.getStartY() + 30f, 100f);
        GlStateManager.translate(shadow.getWidth() / 2, shadow.getHeight() / 2, 100f);
        float scalar = eggAnimation.posY()/ (eggAnimation.getStartY() + 5);
        scalar *= (eggAnimation.scalar() / eggScale);
        GlStateManager.scale(scalar, scalar, scalar);
        GlStateManager.translate(-(shadow.getWidth() / 2), -(shadow.getHeight() / 2), -100f);
        this.drawTexturedModalRect(0, 0, shadow.getX(), shadow.getY(), shadow.getWidth(), shadow.getHeight());
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
