package backend.academy.fractal.flame;

import java.awt.Color;

public class Pixel {
    public static final double MAX_RGB_VALUE = 255.0;

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
        if (hitCount == 0) {
            return Color.BLACK;
        }
        float scale = (float) (1.0 / hitCount);
        return new Color(
            (float) (red * scale),
            (float) (green * scale),
            (float) (blue * scale)
        );
    }

    public void applyGammaCorrection(double gamma) {
        this.red = applyGamma(red, gamma);
        this.green = applyGamma(green, gamma);
        this.blue = applyGamma(blue, gamma);
    }

    private double applyGamma(double colorValue, double gamma) {
        double normalized = colorValue / MAX_RGB_VALUE;
        double corrected = Math.pow(normalized, gamma);
        return Math.min(MAX_RGB_VALUE, Math.max(0.0, corrected * MAX_RGB_VALUE));
    }
}
