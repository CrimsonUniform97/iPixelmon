package ipixelmon.list;

public abstract class ListObject {

    protected int x, y, width, height;

    public ListObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void draw(int mouseX, int mouseY);

    public void keyTyped(int keyCode){}

    public void mouseClicked(int x, int y) {}

    public void update() {}
}
