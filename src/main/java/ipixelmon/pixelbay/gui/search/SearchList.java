package ipixelmon.pixelbay.gui.search;

import ipixelmon.guiList.IGuiList;
import ipixelmon.guiList.IListObject;
import ipixelmon.guiList.QueryType;
import net.minecraft.client.Minecraft;
import org.lwjgl.util.Rectangle;

import java.sql.SQLException;
import java.util.*;

public abstract class SearchList extends IGuiList {

    public SearchList(final SearchGui parentScreen) {
        super(parentScreen);
    }

    private int maxPages = 1;
    private Map</* rowIndex */ Integer, /* row to start from Ex. 0,250,500,750 */ Integer> rows = new HashMap<>();
    private Map</* rowIndex */ Integer, /* amount of entries */ Integer> entries = new HashMap<>();
    private Map<Integer, Integer> pages = new HashMap<>();
    private int rowIndex = 0;
    private int row = 0;
    private boolean showPlus = true;

    @Override
    public void drawList(final int mouseX, final int mouseY, final Minecraft mc) {
        String pageString = "Page (" + getDisplayPage() + "/" + maxPages + (showPlus ? "+" : "") + ")";
        int pageStringWidth = mc.fontRendererObj.getStringWidth(pageString);
        mc.fontRendererObj.drawString(pageString, this.getBounds().getX() + (this.getBounds().getWidth() - pageStringWidth), this.getBounds().getY() - 10, 0xFFFFFF);

        super.drawList(mouseX, mouseY, mc);
    }

    @Override
    public Rectangle getBounds() {
        int listWidth = this.getParentScreen().width - 50, listHeight = this.getParentScreen().height - 50;
        return new Rectangle((this.getParentScreen().width - listWidth) / 2, ((this.getParentScreen().height - listHeight) / 2) + 40, listWidth, listHeight - 40);
    }

    @Override
    public SearchGui getParentScreen() {
        return (SearchGui) super.getParentScreen();
    }

    public final void search(String str, QueryType queryType) {
        if (queryType == QueryType.NEW_SEARCH) {
            this.maxPages = 1;
            this.rows.clear();
            this.entries.clear();
            this.rowIndex = 0;
            this.row = 0;
            this.showPlus = true;
            queryType = QueryType.UP;
        }

        if (queryType == QueryType.UP) {

            // this fixes the rowIndex from going up to infinity.
            if (this.getMaxPages() == 0 && this.getPage() == 0 && this.rowIndex > 0) {
                return;
            }

            this.setObjects(new IListObject[this.getQueryLimit()]);

            if (rows.containsKey(rowIndex)) {
                row = rows.get(rowIndex);
            }

            try {
                doSearch(str);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            this.initGui();

            entries.put(rowIndex, entries());

            boolean prevShowPlus = showPlus;

            showPlus = removeEntriesOnLastPage();

            if (!prevShowPlus) showPlus = false;

            entries.put(rowIndex, entries() - this.getObjectsOnPage(this.getMaxPages()).size());

            if (!rows.containsKey(rowIndex))
                maxPages += this.getMaxPages();

            rows.put(rowIndex, row);
            pages.put(rowIndex, this.getMaxPages());
            row += entries() - this.getObjectsOnPage(this.getMaxPages()).size();
            this.setPage(0);
            rowIndex += 1;
        } else if (queryType == QueryType.DOWN) {
            List<Integer> toRemove = new ArrayList<>();
            for (int i = 0; i < this.entries.size(); i++) {
                if (this.entries.get(i) != null && this.entries.get(i) == 0) toRemove.add(i);
            }
            for (int i : toRemove) {
                this.entries.remove(i);
            }

            if (getDisplayPage() == 1 && this.getPage() == 0) {
                return;
            }

            rowIndex = rowIndex - 1 > 0 ? rowIndex - 1 : 0;
            if (rowIndex > entries.size()) rowIndex = entries.size();
            row = rows.get(rowIndex - 1 > 0 ? rowIndex - 1 : 0);
            this.setObjects(new IListObject[this.getQueryLimit()]);

            try {
                doSearch(str);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            this.initGui();

            entries.put(rowIndex - 1, entries());
            removeEntriesOnLastPage();
            entries.put(rowIndex - 1, entries() - this.getObjectsOnPage(this.getMaxPages()).size());

            this.setPage(this.getMaxPages());
        }
    }

    public int entries() {
        List<IListObject> list = new ArrayList<>(Arrays.asList(this.getObjects()));
        list.removeAll(Collections.singleton(null));
        return list.size();
    }

    public int totalEntries() {
        int count = 0;
        for (int i = 0; i < this.entries.size(); i++) {
            if (this.entries.containsKey(i)) {
                count += this.entries.get(i);
            }
        }
        return count;
    }

    public int getRow() {
        return row;
    }

    public int getDisplayPage() {
        int page = this.getPage() + 1;
        for (int i = 0; i < rowIndex - 1; i++) {
            if (pages.containsKey(i)) {
                page += pages.get(i);
            }
        }
        return page;
    }

    private boolean removeEntriesOnLastPage() {
//        if (totalEntries() != getMaxTotalEntries()) {
        if (totalEntries() >= getQueryLimit()) {
            this.getObjectsOnPage(this.getMaxPages()).forEach(this::removeObject);
            return true;
        }

        return false;
    }

    public abstract void doSearch(String str) throws SQLException;

    public abstract int getQueryLimit();

    public abstract int getMaxTotalEntries();
}
