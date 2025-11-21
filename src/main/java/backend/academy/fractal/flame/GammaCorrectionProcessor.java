package backend.academy.fractal.flame;

public class GammaCorrectionProcessor implements ImageProcessor {
    private final double gamma;

    public GammaCorrectionProcessor(double gamma) {
        this.gamma = gamma;
    }

    @Override
    public void process(FractalImage image) {
        for (int y = 0; y < image.height(); y++) {
            for (int x = 0; x < image.width(); x++) {
                Pixel pixel = image.getPixel(x, y);
                synchronized (pixel) {
                    pixel.applyGammaCorrection(gamma);
                }
            }
        }
    }
}
