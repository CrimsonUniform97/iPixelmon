package com.ipixelmon.landcontrol.client.gui;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.server.regions.PacketModifyRegion;
import com.ipixelmon.util.GuiUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.util.Map;
import java.util.UUID;

/**
 * Created by colby on 1/8/2017.
 */
public class PlayerListX extends PlayerList {

    private UUID regionID;
    private static final ResourceLocation xTexture = new ResourceLocation(iPixelmon.id, "textures/apps/mail/x.png");

    public PlayerListX(int xPosition, int yPosition, int width, int height, UUID regionID, Map<UUID, String> players) {
        super(xPosition, yPosition, width, height, players);
        this.regionID =regionID;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        super.drawObject(index, mouseX, mouseY, isHovering);

        if (isHovering) {
            int x = width - 20;
            int y = 2;
            boolean hoveringX = mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16;

            mc.getTextureManager().bindTexture(xTexture);
            GlStateManager.color(1f, 1f, 1f, hoveringX ? 1f : 0.5f);
            GlStateManager.enableBlend();
            GuiUtil.drawImage(x, y, 16, 16);

            if (Mouse.isButtonDown(0) && hoveringX) {
                iPixelmon.network.sendToServer(new PacketModifyRegion(regionID, "removePlayer", ((UUID) players.keySet().toArray()[index]).toString()));
                players.remove(players.keySet().toArray()[index]);
            }
        }
    }
}
