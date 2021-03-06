package com.ipixelmon.tablet;

import org.lwjgl.util.Rectangle;

import java.util.Iterator;

/**
 * Created by colby on 11/1/2016.
 */
public class TileIterator implements Iterator {

    public final int columns, rows, tileWidth, tileHeight;
    private int index, column, row;
    private Rectangle bounds, tile;
    private Object[] objects;

    public TileIterator(Rectangle bounds, int columns, int rows, Object[] objects) {
        this.bounds = bounds;
        this.columns = columns;
        this.rows = rows;
        this.objects = objects;
        this.tileWidth = bounds.getWidth() / columns;
        this.tileHeight = bounds.getHeight() / rows;
    }

    @Override
    public boolean hasNext() {
        return index < objects.length;
    }

    @Override
    public Object next() {
        tile = new Rectangle(bounds.getX() + tileWidth * column, bounds.getY() + tileHeight * row, tileWidth, tileHeight);

        if ((index + 1) % columns == 0) {
            column = 0;
            row++;
            if(row >= rows) row = 0;
        } else {
            column++;
        }

        return objects[index++];
    }

    public Rectangle getTileBounds() {
        return tile;
    }

    public int getPage() {
        return (index - 1) / (rows * columns);
    }

    public int getMaxPages() {
        int max = objects.length / (columns * rows);
        max = objects.length % (columns * rows) == 0 ? max - 1 : max;
        return max;
    }
}
