package ipixelmon.landcontrol.client;

import ipixelmon.landcontrol.Region;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.*;

public class GuiMemberScrollList extends GuiScrollingList
{

    private Region region;
    private List<String> playerNames;
    private String ownerName;

    public GuiMemberScrollList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight, Region parRegion)
    {
        super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
        region = parRegion;
        playerNames = new ArrayList<>();
        playerNames.addAll(UUIDManager.getNames(parRegion.getMembers()).values());
        ownerName = UUIDManager.getPlayerName(region.getOwner());
    }

    @Override
    protected int getSize()
    {
        return playerNames.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick)
    {
        if(doubleClick)
        {
            // TODO: Open popup
        }
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
        Minecraft.getMinecraft().fontRendererObj.drawString(playerNames.get(slotIdx), left + 4, slotTop, ownerName.equals(playerNames.get(slotIdx)) ? 0x9900cc : 0xFFFFFF);
    }
}
