package backend.academy;

import backend.academy.fractal.flame.FractalImage;
import backend.academy.fractal.flame.GammaCorrectionProcessor;
import backend.academy.fractal.flame.HeartTransformation;
import backend.academy.fractal.flame.ImageFormat;
import backend.academy.fractal.flame.ImageProcessor;
import backend.academy.fractal.flame.ImageUtils;
import backend.academy.fractal.flame.LinearTransformation;
import backend.academy.fractal.flame.Rect;
import backend.academy.fractal.flame.Renderer;
import backend.academy.fractal.flame.SinusoidalTransformation;
import backend.academy.fractal.flame.SphericalTransformation;
import backend.academy.fractal.flame.SwirlTransformation;
import backend.academy.fractal.flame.Transformation;
import java.io.IOException;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        // Configuration parameters
        int width = 1920;
        int height = 1080;
        int samples = 10_000;
        int iterationsPerSample = 100 ;
        int symmetry = 6;
        long seed = System.currentTimeMillis();
        boolean multiThreaded = true; // Set to false for single-threaded execution

        // Define the world coordinates
        Rect world = new Rect(-4.0, -3.0, 8.0, 6.0);

        // Initialize the canvas
        FractalImage canvas = FractalImage.create(width, height);

        // Define the transformations
        List<Transformation> transformations = List.of(
            new HeartTransformation(),
            new LinearTransformation()
        );

        // Render the fractal flame
        Renderer renderer = new Renderer(canvas, world, transformations, samples, iterationsPerSample, symmetry, seed);

        long startTime = System.currentTimeMillis();
        if (multiThreaded) {
            renderer.renderMultiThreaded();
        } else {
            renderer.renderSingleThreaded();
        }
        long endTime = System.currentTimeMillis();

        System.out.println("Rendering completed in " + (endTime - startTime) + " ms");

        // Apply gamma correction (optional)
        ImageProcessor gammaCorrection = new GammaCorrectionProcessor(2.2);
        gammaCorrection.process(canvas);

        // Save the image
        try {
            ImageUtils.save(canvas, "fractal_flame.png", ImageFormat.PNG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
