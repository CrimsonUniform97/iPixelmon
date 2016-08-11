package ipixelmon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public abstract class GuiList extends Gui {

    private int SCREEN_X, SCREEN_Y, WIDTH, HEIGHT;
    private GuiButton BTN_LEFT, BTN_RIGHT;

    private Page CURRENT_PAGE;

    private List<GuiObject> OBJECTS;

    private Page[] PAGES;

    private GuiObject SELECTED;

    public GuiList(final int screenX, final int screenY, final int width, final int height, final List<GuiObject> objects) {
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
        this.drawList();

        this.BTN_RIGHT.drawButton(mc, x, y);
        this.BTN_LEFT.drawButton(mc, x, y);
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
        this.drawTexturedModalRect(this.SCREEN_X - 1, this.SCREEN_Y + this.SELECTED.getY() - 1, 0, 0, this.WIDTH - 2, this.SELECTED.HEIGHT + 1);
        glEnable(GL_TEXTURE_2D);
    }

    public final void drawList() {
        this.CURRENT_PAGE.objects.forEach(object -> object.draw(this.SCREEN_X, this.SCREEN_Y + object.getY()));
    }

    public final void mouseClicked(final Minecraft mc, final int x, final int y) {
        this.SELECTED = this.CURRENT_PAGE.objects.stream().filter(object -> y - this.SCREEN_Y >= object.getY() && y - this.SCREEN_Y <= object.getY() + object.HEIGHT && x - this.SCREEN_X > 0 && x - this.SCREEN_X < this.WIDTH).findFirst().orElse(this.SELECTED != null ? this.SELECTED : null);

        if (this.BTN_LEFT.mousePressed(mc, x, y))
            this.CURRENT_PAGE = this.CURRENT_PAGE.getPageNumber() < 1 ? this.PAGES[0] : this.PAGES[this.CURRENT_PAGE.getPageNumber() - 1];
        if (this.BTN_RIGHT.mousePressed(mc, x, y))
            this.CURRENT_PAGE = this.CURRENT_PAGE.getPageNumber() > this.PAGES.length - 2 ? this.PAGES[this.PAGES.length - 1] : this.PAGES[this.CURRENT_PAGE.getPageNumber() + 1];
    }

    public void keyTyped(char c, int i){}

    public final int getPages() {
        int totalHeight = 0;
        for (GuiObject object : this.OBJECTS) totalHeight += object.HEIGHT;
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
                totalHeight += this.OBJECTS.get(i).HEIGHT;
                if (totalHeight < this.HEIGHT) {
                    this.OBJECTS.get(i).Y = totalHeight - this.OBJECTS.get(i).HEIGHT;
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

    public final GuiObject getSelectedObject() {
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
        private List<GuiObject> objects;
        private int number;

        private Page(int number) {
            this.objects = new ArrayList<>();
            this.number = number;
        }

        public final int getPageNumber() {
            return this.number;
        }

        public final List<GuiObject> getObjects() {
            return objects;
        }
    }

    public static abstract class GuiObject extends Gui {

        private int WIDTH, HEIGHT, Y;
        protected final Minecraft mc = Minecraft.getMinecraft();

        public GuiObject(final int width, final int height) {
            this.WIDTH = width;
            this.HEIGHT = height;
        }

        public abstract void draw(int x, int y);

        public final int getY() {
            return this.Y;
        }

        public final int getWidth() {
            return this.WIDTH;
        }

        public final int getHeight() {
            return this.HEIGHT;
        }
    }
}
