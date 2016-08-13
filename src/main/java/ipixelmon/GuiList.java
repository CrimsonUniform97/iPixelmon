package ipixelmon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public abstract class GuiList extends Gui {

    private int SCREEN_X, SCREEN_Y, WIDTH, HEIGHT;
    private GuiButton BTN_LEFT, BTN_RIGHT;

    private Page CURRENT_PAGE;

    private List<ListObject> OBJECTS;

    private Page[] PAGES;

    private ListObject SELECTED;

    public GuiList(final int screenX, final int screenY, final int width, final int height, final List<ListObject> objects) {
        this.SCREEN_X = screenX;
        this.SCREEN_Y = screenY;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.BTN_LEFT = getLeftBtn();
        this.BTN_RIGHT = getRightBtn();
        this.OBJECTS = objects;

        try {
            if (this.BTN_LEFT == null || this.BTN_RIGHT == null) throw new Exception("Button cannot be null.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        BTN_LEFT.enabled = this.getPages() != 1;
        BTN_RIGHT.enabled = this.getPages() != 1;

        this.setupPages();
    }

    public final void draw(final Minecraft mc, final int x, final int y) {
        this.drawBackground();
        if (this.drawSelectionBox()) this.renderSelectionBox();
        this.drawList(x, y);

        this.BTN_RIGHT.drawButton(mc, x, y);
        this.BTN_LEFT.drawButton(mc, x, y);
    }

    public final void updateScreen() {
        for(ListObject object : this.CURRENT_PAGE.objects) {
            object.updateScreen();
        }
    }

    public final void drawBackground() {
        glColor4f(1f, 1f, 1f, 1f);

        glDisable(GL_TEXTURE_2D);
        glColor4f(128f / 255f, 128f / 255f, 128f / 255f, 1f);
        this.drawTexturedModalRect(this.SCREEN_X - 2, this.SCREEN_Y - 2, 0, 0, this.WIDTH, this.HEIGHT + 2);
        glColor4f(0f, 0f, 0f, 1f);
        this.drawTexturedModalRect(this.SCREEN_X - 1, this.SCREEN_Y - 1, 0, 0, this.WIDTH - 2, this.HEIGHT + 2 - 2);
        glEnable(GL_TEXTURE_2D);
    }

    public void renderSelectionBox() {
        if (this.SELECTED == null) return;

        if (!this.CURRENT_PAGE.objects.contains(this.SELECTED)) return;

        glDisable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glColor4f(1, 1, 1, 50f / 255f);
        this.drawTexturedModalRect(this.SCREEN_X - 1, this.SCREEN_Y + this.SELECTED.getY(), 0, 0, this.WIDTH - 2, this.SELECTED.height);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public final void drawList(final int mouseX, final int mouseY) {
        for(ListObject listObject : this.CURRENT_PAGE.objects) {
            listObject.xPos = this.SCREEN_X;
            listObject.yPos = this.SCREEN_Y + listObject.getY();
            listObject.draw(mouseX, mouseY);
        }
    }

    public final void mouseClicked(final Minecraft mc, final int x, final int y) {
        if(this.SELECTED != null) this.SELECTED.isSelected = false;

        this.SELECTED = this.CURRENT_PAGE.objects.stream().filter(object -> y - this.SCREEN_Y >= object.getY() && y - this.SCREEN_Y <= object.getY() + object.height && x - this.SCREEN_X > 0 && x - this.SCREEN_X < this.WIDTH).findFirst().orElse(this.SELECTED != null ? this.SELECTED : null);

        if(this.SELECTED != null) {
            this.SELECTED.isSelected = true;
        }

        for(ListObject object : this.CURRENT_PAGE.objects) {
            object.mouseClicked(x, y);
        }

        if (this.BTN_LEFT.mousePressed(mc, x, y))
            this.CURRENT_PAGE = this.CURRENT_PAGE.getPageNumber() < 1 ? this.PAGES[0] : this.PAGES[this.CURRENT_PAGE.getPageNumber() - 1];
        if (this.BTN_RIGHT.mousePressed(mc, x, y))
            this.CURRENT_PAGE = this.CURRENT_PAGE.getPageNumber() > this.PAGES.length - 2 ? this.PAGES[this.PAGES.length - 1] : this.PAGES[this.CURRENT_PAGE.getPageNumber() + 1];
    }

    public final void keyTyped(final char c, final int i){
        if(this.SELECTED != null) this.SELECTED.keyTyped(c, i);
    }

    public final int getPages() {
        int totalHeight = 0;
        for (ListObject object : this.OBJECTS) totalHeight += object.height;
        int pages = totalHeight / this.HEIGHT;
        if (totalHeight % this.HEIGHT != 0) pages++;

        return pages == 0 ? 1 : pages;
    }

    public final void setupPages() {
        this.PAGES = new Page[this.getPages()];
        int index = 0, totalHeight;
        for (int page = 0; page < this.PAGES.length; page++) {
            this.PAGES[page] = new Page(page);

            totalHeight = 0;
            for (int i = index; i < this.OBJECTS.size(); i++) {
                totalHeight += this.OBJECTS.get(i).height;
                if (totalHeight < this.HEIGHT) {
                    this.OBJECTS.get(i).yInList = totalHeight - this.OBJECTS.get(i).height;
                    this.OBJECTS.get(i).listX = this.SCREEN_X;
                    this.OBJECTS.get(i).listY = this.SCREEN_Y;
                    this.OBJECTS.get(i).listWidth = this.WIDTH;
                    this.OBJECTS.get(i).listHeight = this.HEIGHT;
                    this.PAGES[page].objects.add(this.OBJECTS.get(i));
                } else {
                    break;
                }
                index++;
            }
        }

        this.CURRENT_PAGE = this.CURRENT_PAGE == null && this.PAGES.length > 0 ? this.PAGES[0] : this.CURRENT_PAGE;

        if (this.CURRENT_PAGE == null) this.CURRENT_PAGE = new Page(0);
    }

    public final Page getCurrentPage() {
        return this.CURRENT_PAGE;
    }

    public abstract GuiButton getLeftBtn();

    public abstract GuiButton getRightBtn();

    public final ListObject getSelectedObject() {
        return this.SELECTED;
    }

    public final int getScreenY() {
        return this.SCREEN_Y;
    }

    public final int getScreenX() {
        return this.SCREEN_X;
    }

    public final int getHeight() {
        return this.HEIGHT;
    }

    public final int getWidth() {
        return this.WIDTH;
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
