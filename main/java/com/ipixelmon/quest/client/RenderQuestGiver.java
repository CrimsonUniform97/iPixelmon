package com.ipixelmon.quest.client;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.quest.EntityQuestGiver;
import com.ipixelmon.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

/**
 * Created by colby on 12/30/2016.
 */
public class RenderQuestGiver extends RenderLiving<EntityQuestGiver>
{

    private ResourceLocation texture;

    public RenderQuestGiver()
    {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelPlayer(1f, false), 0.5f);
        texture = new ResourceLocation(iPixelmon.id, "textures/entity/questgivers/skin0.png");
    }

    @Override
    protected void preRenderCallback(EntityQuestGiver entity, float f)
    {
        super.preRenderCallback(entity, f);
    }

    @Override
    public void doRender(EntityQuestGiver entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        // TODO: If done with a quest render a exclamation mark
        GuiUtil.renderLabel(EnumChatFormatting.YELLOW.toString() + EnumChatFormatting.BOLD.toString() + "?", x, y + 3, z, 5.45f, false, this.getFontRendererFromRenderManager(),
                this.renderManager);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called
     * unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityQuestGiver par1Entity)
    {
        return texture;
    }
}