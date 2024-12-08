package backend.academy.fractal.flame;

import java.awt.Color;

public class Pixel {
    private double red;
    private double green;
    private double blue;
    private int hitCount;

    public synchronized void accumulateColor(double r, double g, double b) {
        this.red += r;
        this.green += g;
        this.blue += b;
        this.hitCount++;
    }

    public Color getColor() {
        if (hitCount == 0) return Color.BLACK;
        float scale = (float) (1.0 / hitCount);
        return new Color(
            (float) (red * scale),
            (float) (green * scale),
            (float) (blue * scale)
        );
    }

    public void applyGammaCorrection(double gamma) {
        this.red = Math.pow(this.red, 1.0 / gamma);
        this.green = Math.pow(this.green, 1.0 / gamma);
        this.blue = Math.pow(this.blue, 1.0 / gamma);
    }
}
