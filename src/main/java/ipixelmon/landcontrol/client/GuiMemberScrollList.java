package ipixelmon.landcontrol.client;

import ipixelmon.landcontrol.Region;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.*;

public class GuiMemberScrollList extends GuiScrollingList
{

    private Region region;
    public List<String> playerNames;
    private GuiRegionInfo parentScreen;

    public GuiMemberScrollList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight, Region parRegion, GuiRegionInfo parent)
    {
        super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
        region = parRegion;
        playerNames = new ArrayList<>();
        playerNames.add(UUIDManager.getPlayerName(region.getOwner()));
        playerNames.addAll(UUIDManager.getNames(parRegion.getMembers()).values());
        parentScreen = parent;
    }

    public int getSelectedIndex()
    {
        return selectedIndex;
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
            if(index == 0)
            {
                return;
            }

            Minecraft.getMinecraft().displayGuiScreen(new GuiYesNo(parentScreen, "", "Remove \'" + playerNames.get(index) + "\' from members list?", 3)
            {
                @Override
                protected void keyTyped(final char typedChar, final int keyCode) throws IOException
                {
                    if(keyCode == Keyboard.KEY_ESCAPE)
                    {
                        Minecraft.getMinecraft().displayGuiScreen((GuiScreen) parentScreen);
                        return;
                    }
                }
            });
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
        Minecraft.getMinecraft().fontRendererObj.drawString(playerNames.get(slotIdx), left + 4, slotTop, slotIdx == 0 ? 0x9900cc : 0xFFFFFF);
    }
}
