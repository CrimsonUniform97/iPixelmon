package ipixelmon.guiList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.util.Rectangle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public abstract class IGuiList extends Gui {

    private GuiButton pageUpBtn, pageDownBtn;
    private Rectangle bounds;
    private final GuiScreen parentScreen;
    private IListObject[] objects;
    private IListObject selected;
    private int page = 0;

    public IGuiList(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
        this.bounds = this.getBounds();
        this.objects = new IListObject[1000];
        this.pageUpBtn = new GuiButton(1, this.bounds.getX() + this.bounds.getWidth(), this.bounds.getY(), 20, 20, ">");
        this.pageDownBtn = new GuiButton(0, this.bounds.getX() + this.bounds.getWidth(), this.bounds.getY() + 20, 20, 20, "<");
    }

    public void drawList(final int mouseX, final int mouseY, final Minecraft mc) {
        this.drawRect(this.bounds, ColorPicker.color(16, 0, 16, 250), ColorPicker.color(29, 0, 102, 250));

        Iterator<IListObject> iterator = this.getIterator();

        int combinedHeight = 0;
        IListObject listObject;
        while (iterator.hasNext()) {
            listObject = iterator.next();
            if (listObject.page == this.page) {
                glPushMatrix();
                glTranslatef(this.getBounds().getX(), this.getBounds().getY() + combinedHeight, 0f);
                if(listObject.isSelected()) this.drawRect(new Rectangle(-2, 0, this.bounds.getWidth() + 4, listObject.getHeight()), ColorPicker.color(70, 0, 80, 250), ColorPicker.color(90, 0, 100, 250));
                listObject.drawObject(mouseX - this.bounds.getX(), mouseY - (this.bounds.getY() + combinedHeight), mc);
                combinedHeight += listObject.getHeight();
                glPopMatrix();
            }
        }

        this.pageDownBtn.xPosition = this.bounds.getX() + this.bounds.getWidth();
        this.pageUpBtn.xPosition = this.bounds.getX() + this.bounds.getWidth();
        this.pageDownBtn.yPosition = this.bounds.getY() + 20;
        this.pageUpBtn.yPosition = this.bounds.getY();
        this.pageDownBtn.drawButton(mc, mouseX, mouseY);
        this.pageUpBtn.drawButton(mc, mouseX, mouseY);
    }

    public void mouseClicked(final int mouseX, final int mouseY, final int btn) {
        if(this.pageUpBtn.isMouseOver() && this.pageUpBtn.enabled) {
            this.page = this.page + 1 < this.getMaxPages() ? this.page + 1 : this.page;
            this.pageDownBtn.playPressSound(Minecraft.getMinecraft().getSoundHandler());
            return;
        } else if(this.pageDownBtn.isMouseOver() && this.pageDownBtn.enabled) {
            this.page = this.page -1 > -1 ? this.page - 1 : this.page;
            this.pageUpBtn.playPressSound(Minecraft.getMinecraft().getSoundHandler());
            return;
        }

        Iterator<IListObject> iterator = this.getIterator();

        int combinedHeight = 0;
        IListObject listObject;
        while (iterator.hasNext()) {
            listObject = iterator.next();

            if (listObject.page == this.page) {
                if (mouseY > this.bounds.getY() + combinedHeight &&
                        mouseY < this.bounds.getY() + combinedHeight + listObject.getHeight() &&
                        mouseX > this.bounds.getX() && mouseX < this.bounds.getX() + this.bounds.getWidth()) {
                    listObject.mouseClicked(mouseX - this.bounds.getX(), mouseY - (this.bounds.getY() + combinedHeight), btn);
                    this.selected = listObject;
                    return;
                }
                combinedHeight += listObject.getHeight();
            }
        }

        this.selected = null;
    }

    public void keyTyped(final char typedChar, final int keyCode) {
        Iterator<IListObject> iterator = this.getIterator();

        while(iterator.hasNext()) iterator.next().keyTyped(typedChar, keyCode);
    }

    public void update() {
        Iterator<IListObject> iterator = this.getIterator();

        while(iterator.hasNext()) iterator.next().update();
    }

    public final void addObject(IListObject object) {
        int pageNum = 0;
        int combinedHeight = 0;
        object.list = this;
        object.initGui();
        for (int i = 0; i < this.objects.length; i++) {
            if (this.objects[i] == null) {
                object.page = pageNum;
                this.objects[i] = object;
                break;
            } else {
                if ((combinedHeight += this.objects[i].getHeight()) > this.bounds.getHeight()) {
                    pageNum++;
                    combinedHeight = 0;
                }
                this.objects[i].page = pageNum;
            }
        }
    }

    public final void removeObject(IListObject object) {
        List<IListObject> list = new ArrayList<>(Arrays.asList(this.objects));
        list.remove(object);
        this.objects = (IListObject[]) list.toArray();
    }

    public void initGui() {
        int pageNum = 0;
        int combinedHeight = 0;

        this.bounds = this.getBounds();
        Iterator<IListObject> iterator = this.getIterator();
        IListObject listObject;
        while(iterator.hasNext()) {
            listObject = iterator.next();

            if ((combinedHeight += listObject.getHeight()) > this.bounds.getHeight()) {
                pageNum++;
                combinedHeight = 0;
            }
            listObject.page = pageNum;

            listObject.initGui();
        }
    }

    public void drawRect(Rectangle rect, Color bgColor, Color trimColor) {
        int x = rect.getX(), y = rect.getY(), w = rect.getWidth(), h = rect.getHeight();
        x += 4;
        y += 4;
        w -= 8;
        h -= 8;
        int l = bgColor.getRGB();
        this.drawGradientRect(x - 3, y - 4, x + w + 3, y - 3, l, l);
        this.drawGradientRect(x - 3, y + h + 3, x + w + 3, y + h + 4, l, l);
        this.drawGradientRect(x - 3, y - 3, x + w + 3, y + h + 3, l, l);
        this.drawGradientRect(x - 4, y - 3, x - 3, y + h + 3, l, l);
        this.drawGradientRect(x + w + 3, y - 3, x + w + 4, y + h + 3, l, l);
        int i1 = trimColor.getRGB();
        int j1 = (i1 & 16711422 /* white */) >> 1 | i1 & -16777216 /* black */;
        this.drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + h + 3 - 1, i1, j1);
        this.drawGradientRect(x + w + 2, y - 3 + 1, x + w + 3, y + h + 3 - 1, i1, j1);
        this.drawGradientRect(x - 3, y - 3, x + w + 3, y - 3 + 1, i1, i1);
        this.drawGradientRect(x - 3, y + h + 2, x + w + 3, y + h + 3, j1, j1);
    }

    /**
     * SETTERS
     */
    public final void setObjects(IListObject[] objects) {
        this.objects = objects;
    }

    public final void setSelected(IListObject listObject) {
        this.selected = listObject;
    }

    public final void setPage(int page) { this.page = page; }

    /**
     * GETTERS
     */
    public final IListObject[] getObjects() {
        return this.objects;
    }

    public final int getMaxPages() {
        Iterator<IListObject> iterator = this.getIterator();

        int pageNum = 0;
        while (iterator.hasNext()) {
            pageNum = iterator.next().page;
        }

        return pageNum;
    }

    public final IListObject getSelected() {
        return this.selected;
    }

    public final int getPage() { return this.page; }

    public final Iterator<IListObject> getIterator() {
        return new Iterator<IListObject>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return objects[i] != null;
            }

            @Override
            public IListObject next() {
                return objects[i++];
            }
        };
    }

    public GuiScreen getParentScreen() {
        return this.parentScreen;
    }

    public abstract Rectangle getBounds();
}
