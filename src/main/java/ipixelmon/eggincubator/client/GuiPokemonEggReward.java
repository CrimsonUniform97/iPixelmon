package ipixelmon.eggincubator.client;

import com.pixelmonmod.pixelmon.client.gui.pokedex.GuiPokedex;
import com.pixelmonmod.pixelmon.client.models.animations.EnumRotation;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import ipixelmon.eggincubator.egg.EggBlock;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GuiPokemonEggReward extends GuiScreen
{

    private static final ResourceLocation lens_flares = new ResourceLocation(iPixelmon.id, "textures/gui/lens_flares.png");
    private static final Rectangle circleFlare = new Rectangle(0, 0, 30, 26);
    private static final Rectangle sunFlare = new Rectangle(46, 0, 58, 46);
    private static final Rectangle orangeFlare = new Rectangle(0, 44, 52, 40);

    EntityPixelmon pixelmon;
    private Animation animation;

    private List<Animation> circleFlares = new ArrayList<>();

    @Override
    public void initGui()
    {
        pixelmon = (EntityPixelmon) PixelmonEntityList.createEntityByName(EnumPokemon.Totodile.name, Minecraft.getMinecraft().theWorld);
        pixelmon.getLvl().setLevel(50);
        pixelmon.setGrowth(EnumGrowth.Ordinary);

        int centerX = (this.width - 16) / 2;
        int centerY = (this.height - 16) / 2;

        animation = Animation.newInstance().translate(centerX, centerY, 100f).scale(7f)
                .rotateTo(EnumRotation.x, -40f, 3.2f).rotateTo(EnumRotation.x, 0f, 3.0f).wait(500)
                .rotateTo(EnumRotation.z, -40f, 3.2f).rotateTo(EnumRotation.z, 0f, 2.0f).wait(200)
                .rotateTo(EnumRotation.z, 40f, 3.2f).rotateTo(EnumRotation.z, 0f, 2.5f).wait(100)
        .translateTo(EnumRotation.y, centerY - 80f, 8.0f)
        .scaleTo(0.5f, 0.4f);

        Random r = new Random();

        for(int i = 0; i < 20; i++)
        {
            circleFlares.add(Animation.newInstance().translate(r.nextInt(this.width), this.height, 0).translateTo(EnumRotation.y, -10, r.nextFloat() * (0.5f - 0.2f) + 0.2f));
        }
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GuiPokedex.drawEntityToScreen(100, 50, 51, 55, pixelmon, partialTicks, true);
        drawEgg();

        // TODO: Lens flare mechanism works! Now just need to work on background and work on all the other lens flares!
        mc.getTextureManager().bindTexture(lens_flares);
        for(Animation a : circleFlares)
        {
            a.animate();
            GlStateManager.pushMatrix();
            GlStateManager.translate(a.posX(), a.posY(), 0);
            this.drawTexturedModalRect(0, 0, circleFlare.getX(), circleFlare.getY(), circleFlare.getWidth(), circleFlare.getHeight());
            GlStateManager.popMatrix();
        }
}

    private void drawEgg()
    {

        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        GuiUtil.instance.setBrightness(0.6f, 1.5f, 0f);

        GlStateManager.pushMatrix();

        float width = 16f, height = 16f;
        animation.animate();
        //move to position
        GlStateManager.translate(animation.posX(), animation.posY(), animation.posZ());
        // translate to center of egg
        // We use 100f because that is what Minecraft is on by default.
        float z = 100f;
        GlStateManager.translate(width / 2f, height / 2f, z);
        Minecraft.getMinecraft().fontRendererObj.drawString("|", 0, 0, 0xFFFFFF);

        // TODO: Rotating works! Now lets work on the gui and then give the player a pokemon after walking and send the packets etc.

        // scale
        GlStateManager.scale(animation.scalar(), animation.scalar(), animation.scalar());

        // rotate
        GlStateManager.rotate(animation.rotX(), 1f, 0f, 0f);
        GlStateManager.rotate(animation.rotY(), 0f, 1f, 0f);
        GlStateManager.rotate(animation.rotZ(), 0f, 0f, 1f);

        // translate back
        GlStateManager.translate(-(width / 2f), -(height / 2f), -z);

        renderItem.renderItemIntoGUI(new ItemStack(Item.getItemFromBlock(EggBlock.instance)), 0, 0);
        GlStateManager.popMatrix();
    }

}
