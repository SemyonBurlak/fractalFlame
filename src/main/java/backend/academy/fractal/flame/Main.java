package backend.academy.fractal.flame;

import backend.academy.fractal.flame.transformation.AffineTransformation;
import backend.academy.fractal.flame.transformation.HeartTransformation;
import backend.academy.fractal.flame.transformation.SphericalTransformation;
import backend.academy.fractal.flame.transformation.Transformation;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("MagicNumber")
@UtilityClass
public class Main {

    public static void main(String[] args) {
        int width = 1920;
        int height = 1080;
        int samples = 10_000_000;
        int iterationsPerSample = 10;
        int symmetry = 1;
        long seed = System.currentTimeMillis();
        boolean multiThreaded = false;

        Rect world = new Rect(-4.0, -3.0, 8.0, 6.0);
        FractalImage canvas = FractalImage.create(width, height);

        List<WeightedTransform> transformations = createTransformations(seed, 10);

        Renderer renderer = new Renderer(canvas, world, transformations, samples, iterationsPerSample, symmetry, seed);

        long startTime = System.currentTimeMillis();

        if (multiThreaded) {
            renderer.renderMultiThreaded();
        } else {
            renderer.renderSingleThreaded();
        }

        long endTime = System.currentTimeMillis();

        log.info("Rendering completed in {} ms", endTime - startTime);

        ImageProcessor gammaCorrection = new GammaCorrectionProcessor(2.2);
        gammaCorrection.process(canvas);

        try {
            ImageUtils.save(canvas, "fractal_flame.png", ImageFormat.PNG);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private static List<WeightedTransform> createTransformations(long seed, int count) {
        Random rand = new Random(seed);
        List<Transformation> variations = List.of(
            new HeartTransformation(),
            new SphericalTransformation()
        );
        List<WeightedTransform> transforms = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            AffineCoefficient aff = AffineCoefficient.generateRandom(rand);
            Transformation transform = variations.get(rand.nextInt(variations.size()));
            Color col = new Color(rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
            transforms.add(new WeightedTransform(new AffineTransformation(aff), transform, col));
        }
        return transforms;
    }
}


