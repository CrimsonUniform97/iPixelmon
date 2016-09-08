package ipixelmon.landcontrol.client;

import ipixelmon.landcontrol.Region;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.io.IOException;

public class GuiRegionInfo extends GuiScreen
{

    private Region region;
    private int bgWidth = 150, bgHeight = 250, posX, posY;
    private GuiMemberScrollList scrollList;
    private GuiTextField textField;

    public GuiRegionInfo(Region parRegion)
    {
        region = parRegion;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        scrollList.drawScreen(mouseX, mouseY, partialTicks);
        textField.drawTextBox();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);
        scrollList.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        textField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        posX = (this.width - bgWidth) / 2;
        posY = (this.height - bgHeight) / 2;

        scrollList = new GuiMemberScrollList(mc, bgWidth, bgHeight, posY, posY + bgHeight, posX, 10, width, height, region);
        textField = new GuiTextField(0, mc.fontRendererObj, posX + (bgWidth - 75) / 2, posY + bgHeight + 2, 75, 20);
    }

}
