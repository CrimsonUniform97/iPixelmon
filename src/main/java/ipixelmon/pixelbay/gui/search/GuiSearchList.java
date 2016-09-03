package ipixelmon.pixelbay.gui.search;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import ipixelmon.guiList.IGuiList;
import ipixelmon.guiList.IListObject;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Rectangle;

public abstract class GuiSearchList extends IGuiList {

    protected int rowInTable = 0;
    protected int maxSearchSize = 250;

    public GuiSearchList(final SearchGui parentScreen) {
        super(parentScreen);
    }

    private static final ResourceLocation logo = new ResourceLocation(iPixelmon.id + ":pixelbay/textures/gui/PixelbayLogo.png");

    @Override
    public void drawList(final int mouseX, final int mouseY, final Minecraft mc) {
        mc.getTextureManager().bindTexture(logo);
        int logoWidth = 398 / 2;
        int logoHeight = 108 / 2;
        GuiHelper.drawImageQuad(this.getBounds().getX() + ((this.getBounds().getWidth() - logoWidth) / 2), this.getBounds().getY() - logoHeight, logoWidth, logoHeight, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);

        super.drawList(mouseX, mouseY, mc);
    }
    @Override
    public void pageUp() {
        if (this.getPage() == this.getLastPage())
            this.search(this.getParentScreen().popupWindow.textField.getText(), true, false);
        super.pageUp();
    }

    @Override
    public void pageDown() {
        if (this.getPage() == 0)
            this.search(this.getParentScreen().popupWindow.textField.getText(), false, false);
        super.pageDown();
    }

    public void search(String searchTxt, boolean searchUp, boolean newSearch) {

        if(!searchUp) {
            rowInTable -= maxSearchSize;
            if(rowInTable <= 0)  {
                rowInTable = 0;
                return;
            }
        } else {
            System.out.println(this.getTableEntries() - rowInTable);
            if(this.rowInTable > 0 && this.getObjects().size() < maxSearchSize) {
                return;
            }
        }

        System.out.println(rowInTable);

        this.setObjects(new IListObject[maxSearchSize]);
        this.search(searchTxt);

        this.initGui();

        if(searchUp) {
            rowInTable += maxSearchSize;
            if(rowInTable > this.getTableEntries()) rowInTable -= maxSearchSize;
            this.setPage(0);
        } else {
            this.setPage(this.getLastPage());
        }

    }

    public abstract void search(String searchTxt);

    @Override
    public Rectangle getBounds() {
        int listWidth = this.getParentScreen().width - 50, listHeight = this.getParentScreen().height - 50;
        return new Rectangle((this.getParentScreen().width - listWidth) / 2, ((this.getParentScreen().height - listHeight) / 2) + 40, listWidth, listHeight - 40);
    }

    @Override
    public SearchGui getParentScreen() {
        return (SearchGui) super.getParentScreen();
    }

    public abstract int getTableEntries();
}
