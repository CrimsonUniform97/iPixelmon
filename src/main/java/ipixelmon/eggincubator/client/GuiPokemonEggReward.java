package ipixelmon.eggincubator.client;

import com.pixelmonmod.pixelmon.client.gui.pokedex.GuiPokedex;
import com.pixelmonmod.pixelmon.client.render.RenderPixelmon;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import ipixelmon.eggincubator.egg.EggBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Matrix4f;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class GuiPokemonEggReward extends GuiScreen
{

    static float spinCount = 0.0F;
    EntityPixelmon pixelmon;

    @Override
    public void initGui()
    {
        pixelmon = (EntityPixelmon) PixelmonEntityList.createEntityByName(EnumPokemon.Totodile.name, Minecraft.getMinecraft().theWorld);
        pixelmon.getLvl().setLevel(50);
        pixelmon.setGrowth(EnumGrowth.Ordinary);
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GuiPokedex.drawEntityToScreen(100, 50,  51, 55, pixelmon, partialTicks, true);
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        GuiUtil.instance.setBrightness(0.6f, 1.5f, 0f);


        GlStateManager.pushMatrix();

        int width = 16, height = 16;
        int scale = 2;

        //move to position
        GlStateManager.translate(150, 50, 0);

        // translate to center of egg

        // We use 100f because that is what Minecraft is on by default.
        float z = 100f;
        GlStateManager.translate(width / 2,height / 2, z);
        Minecraft.getMinecraft().fontRendererObj.drawString("|", 0, 0, 0xFFFFFF);

        // TODO: Rotating works! Now lets work on the gui and then give the player a pokemon after walking and send the packets etc.

        // scale
        GlStateManager.scale(scale, scale, scale);

        // rotate
        GlStateManager.rotate(spinCount += 0.66F, 0, 1, 0);
        GlStateManager.rotate(30f, 1f, 0f, 0f);

        // translate back
        GlStateManager.translate(-(width / 2), -(height / 2), -z);

        renderItem.renderItemIntoGUI(new ItemStack(Item.getItemFromBlock(EggBlock.instance)), 0, 0);
        GlStateManager.popMatrix();
    }

}
