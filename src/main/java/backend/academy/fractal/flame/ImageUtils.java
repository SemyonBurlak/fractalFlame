package backend.academy.fractal.flame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageUtils {
    private ImageUtils() {
    }

    public static void save(FractalImage image, String filename, ImageFormat format) throws IOException {
        BufferedImage bufferedImage = image.toBufferedImage();
        ImageIO.write(bufferedImage, format.name(), new File(filename));
    }
}
