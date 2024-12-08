package backend.academy.fractal.flame;

public class Rect {
    public final double x;
    public final double y;
    public final double width;
    public final double height;

    public Rect(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(Point p) {
        return p.x >= x && p.x <= x + width && p.y >= y && p.y <= y + height;
    }
}
