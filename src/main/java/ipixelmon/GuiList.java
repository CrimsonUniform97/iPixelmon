package ipixelmon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public abstract class GuiList extends Gui {

    private int xPos, yPos, width, height;
    private GuiButton pageLeftBtn, pageRightBtn;
    private Page currentPage;
    private List<ListObject> objectList;
    private Page[] pages;
    private ListObject selectedObject;

    public GuiList(final int screenX, final int screenY, final int width, final int height, final List<ListObject> objects) {
        this.xPos = screenX;
        this.yPos = screenY;
        this.width = width;
        this.height = height;
        this.pageLeftBtn = getLeftBtn();
        this.pageRightBtn = getRightBtn();
        this.objectList = objects;

        try {
            if (this.pageLeftBtn == null || this.pageRightBtn == null) throw new Exception("Button cannot be null.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        pageLeftBtn.enabled = this.getPages() != 1;
        pageRightBtn.enabled = this.getPages() != 1;

        this.setupPages();
    }

    public final void draw(final Minecraft mc, final int x, final int y) {
        this.drawBackground();
        if (this.drawSelectionBox()) this.renderSelectionBox();
        this.drawList(x, y);

        this.pageRightBtn.drawButton(mc, x, y);
        this.pageLeftBtn.drawButton(mc, x, y);
    }

    public final void updateScreen() {
        for(ListObject object : this.currentPage.objects) {
            object.updateScreen();
        }
    }

    public final void drawBackground() {
        glColor4f(1f, 1f, 1f, 1f);

        glDisable(GL_TEXTURE_2D);
        glColor4f(128f / 255f, 128f / 255f, 128f / 255f, 1f);
        this.drawTexturedModalRect(this.xPos - 2, this.yPos - 2, 0, 0, this.width, this.height + 2);
        glColor4f(0f, 0f, 0f, 1f);
        this.drawTexturedModalRect(this.xPos - 1, this.yPos - 1, 0, 0, this.width - 2, this.height + 2 - 2);
        glEnable(GL_TEXTURE_2D);
    }

    public void renderSelectionBox() {
        if (this.selectedObject == null) return;

        if (!this.currentPage.objects.contains(this.selectedObject)) return;

        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glColor4f(1, 1, 1, 50f / 255f);
        this.drawTexturedModalRect(this.xPos - 1, this.yPos + this.selectedObject.getY(), 0, 0, this.width - 2, this.selectedObject.height);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public final void drawList(final int mouseX, final int mouseY) {
        for(ListObject listObject : this.currentPage.objects) {
            listObject.xPos = this.xPos;
            listObject.yPos = this.yPos + listObject.getY();
            listObject.draw(mouseX, mouseY);
        }
    }

    public final void mouseClicked(final Minecraft mc, final int x, final int y) {
        if(this.selectedObject != null) this.selectedObject.isSelected = false;

        this.selectedObject = this.currentPage.objects.stream().filter(object -> y - this.yPos >= object.getY() && y - this.yPos <= object.getY() + object.height && x - this.xPos > 0 && x - this.xPos < this.width).findFirst().orElse(this.selectedObject != null ? this.selectedObject : null);

        if(this.selectedObject != null) {
            this.selectedObject.isSelected = true;
        }

        for(ListObject object : this.currentPage.objects) {
            object.mouseClicked(x, y);
        }

        if (this.pageLeftBtn.mousePressed(mc, x, y))
            this.currentPage = this.currentPage.getPageNumber() < 1 ? this.pages[0] : this.pages[this.currentPage.getPageNumber() - 1];
        if (this.pageRightBtn.mousePressed(mc, x, y))
            this.currentPage = this.currentPage.getPageNumber() > this.pages.length - 2 ? this.pages[this.pages.length - 1] : this.pages[this.currentPage.getPageNumber() + 1];
    }

    public final void keyTyped(final char c, final int i){
        if(this.selectedObject != null) this.selectedObject.keyTyped(c, i);
    }

    public final int getPages() {
        int totalHeight = 0;
        for (ListObject object : this.objectList) totalHeight += object.height;
        int pages = totalHeight / this.height;
        if (totalHeight % this.height != 0) pages++;

        return pages == 0 ? 1 : pages;
    }

    public final void setupPages() {
        this.pages = new Page[this.getPages()];
        int index = 0, totalHeight;
        for (int page = 0; page < this.pages.length; page++) {
            this.pages[page] = new Page(page);

            totalHeight = 0;
            for (int i = index; i < this.objectList.size(); i++) {
                totalHeight += this.objectList.get(i).height;
                if (totalHeight < this.height) {
                    this.objectList.get(i).yInList = totalHeight - this.objectList.get(i).height;
                    this.objectList.get(i).listX = this.xPos;
                    this.objectList.get(i).listY = this.yPos;
                    this.objectList.get(i).listWidth = this.width;
                    this.objectList.get(i).listHeight = this.height;
                    this.pages[page].objects.add(this.objectList.get(i));
                } else {
                    break;
                }
                index++;
            }
        }

        this.currentPage = this.pages[0];

        if (this.currentPage == null) this.currentPage = new Page(0);
    }

    public final Page getCurrentPage() {
        return this.currentPage;
    }

    public abstract GuiButton getLeftBtn();

    public abstract GuiButton getRightBtn();

    public final ListObject getSelectedObject() {
        return this.selectedObject;
    }

    public final int getScreenY() {
        return this.yPos;
    }

    public final int getScreenX() {
        return this.xPos;
    }

    public final int getHeight() {
        return this.height;
    }

    public final int getWidth() {
        return this.width;
    }

    public abstract boolean drawSelectionBox();

    public final class Page {
        private List<ListObject> objects;
        private int number;

        private Page(int number) {
            this.objects = new ArrayList<>();
            this.number = number;
        }

        public final int getPageNumber() {
            return this.number;
        }

        public final List<ListObject> getObjects() {
            return objects;
        }
    }

    public static abstract class ListObject extends Gui {

        protected int width, height, xPos, yPos, yInList;
        protected final Minecraft mc = Minecraft.getMinecraft();
        protected int listX, listY, listWidth, listHeight;
        protected boolean isSelected = false;

        public ListObject(final int width, final int height) {
            this.width = width;
            this.height = height;
        }

        public abstract void draw(final int mouseX, final int mouseY);

        public void mouseClicked(final int mouseX, final int mouseY) {}

        public void keyTyped(final char typedChar, final int keyCode) {}

        public void updateScreen() {}

        public final int getY() { return this.yInList; }

        public final int getYPos() {
            return this.yPos;
        }

        public final int getXPos() { return this.xPos; }

        public final int getWidth() {
            return this.width;
        }

        public final int getHeight() {
            return this.height;
        }

    }
}
