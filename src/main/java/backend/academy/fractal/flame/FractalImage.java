package backend.academy.fractal.flame;

import lombok.Getter;
import java.awt.image.BufferedImage;

@Getter
public class FractalImage {
    private final Pixel[][] data;
    private final int width;
    private final int height;

    private FractalImage(int width, int height) {
        this.width = width;
        this.height = height;
        data = new Pixel[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                data[y][x] = new Pixel();
            }
        }
    }

    public static FractalImage create(int width, int height) {
        return new FractalImage(width, height);
    }

    public boolean contains(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Pixel getPixel(int x, int y) {
        if (!contains(x, y)) return null;
        return data[y][x];
    }

    public BufferedImage toBufferedImage() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Pixel pixel = data[y][x];
                image.setRGB(x, y, pixel.getColor().getRGB());
            }
        }
        return image;
    }
}
