package ipixelmon.landcontrol.client;

import ipixelmon.landcontrol.Region;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GuiMemberScrollList extends GuiScrollingList
{

    private Region region;
    private Map<UUID, String> playerNames;

    public GuiMemberScrollList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight, Region parRegion)
    {
        super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
        region = parRegion;
        // TODO: Test this out, new method getNames().
        playerNames = UUIDManager.getNames(region.getMembers());
    }

    @Override
    protected int getSize()
    {
        return playerNames.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick)
    {

    }

    @Override
    protected boolean isSelected(int index)
    {
        return index == selectedIndex;
    }

    @Override
    protected void drawBackground()
    {

    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
    {
        Minecraft.getMinecraft().fontRendererObj.drawString(playerNames.get(slotIdx), left, slotTop, 0xFFFFFF);
    }
}
