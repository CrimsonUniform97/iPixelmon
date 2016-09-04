package ipixelmon.pixelbay.gui.search;

import net.minecraft.client.Minecraft;

public abstract class ISearchList extends BasicScrollList
{

    public ISearchList(final Minecraft client, final int width, final int height, final int top, final int bottom, final int left, final int entryHeight, final int screenWidth, final int screenHeight)
    {
        super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
    }

    public abstract void search(String str);

}
