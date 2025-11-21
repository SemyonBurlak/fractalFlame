package backend.academy.fractal.flame;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Renderer {
    private final static int BURN_IN_STEPS = 20;
    private final static long EXECUTOR_TIMEOUT_MINUTES = 10L;


    private final FractalImage canvas;
    private final Rect world;
    private final List<WeightedTransform> transforms;
    private final int samples;
    private final int iterationsPerSample;
    private final int symmetry;
    private final long seed;


    public Renderer(
        FractalImage canvas, Rect world, List<WeightedTransform> transforms,
        int samples, int iterationsPerSample, int symmetry, long seed
    ) {
        this.canvas = canvas;
        this.world = world;
        this.transforms = transforms;
        this.samples = samples;
        this.iterationsPerSample = iterationsPerSample;
        this.symmetry = symmetry;
        this.seed = seed;
    }

    public void renderSingleThreaded() {
        render(0, samples);
    }

    public void renderMultiThreaded() {
        int numThreads = Runtime.getRuntime().availableProcessors();
        try (ExecutorService executor = Executors.newFixedThreadPool(numThreads)) {

            int samplesPerThread = samples / numThreads;
            for (int i = 0; i < numThreads; i++) {
                final int start = i * samplesPerThread;
                final int end = (i == numThreads - 1) ? samples : start + samplesPerThread;
                executor.submit(() -> render(start, end));
            }

            executor.shutdown();
            if (!executor.awaitTermination(EXECUTOR_TIMEOUT_MINUTES, TimeUnit.MINUTES)) {
                log.error("Tasks did not finish in time!");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void render(int startSample, int endSample) {
        Random random = new Random(seed + startSample);
        for (int num = startSample; num < endSample; num++) {
            Point pw = randomPointInWorld(random);

            for (int step = -BURN_IN_STEPS; step < iterationsPerSample; step++) {
                WeightedTransform transform = transforms.get(random.nextInt(transforms.size()));
                pw = transform.apply(pw);

                if (step >= 0) {
                    double thetaIncrement = (2 * Math.PI) / symmetry;
                    for (int s = 0; s < symmetry; s++) {
                        double theta = s * thetaIncrement;
                        Point rotatedPoint = rotate(pw, theta);
                        if (!world.contains(rotatedPoint)) {
                            continue;
                        }

                        int x = (int) ((rotatedPoint.x() - world.x()) / world.width() * canvas.width());
                        int y = (int) ((rotatedPoint.y() - world.y()) / world.height() * canvas.height());

                        Pixel pixel = canvas.getPixel(x, y);
                        if (pixel == null) {
                            continue;
                        }

                        Color c = transform.color();
                        pixel.accumulateColor(
                            c.getRed() / Pixel.MAX_RGB_VALUE,
                            c.getGreen() / Pixel.MAX_RGB_VALUE,
                            c.getBlue() / Pixel.MAX_RGB_VALUE
                        );
                    }
                }
            }
        }
    }

    private Point randomPointInWorld(Random random) {
        double x = world.x() + random.nextDouble() * world.width();
        double y = world.y() + random.nextDouble() * world.height();
        return new Point(x, y);
    }

    private Point rotate(Point p, double theta) {
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        double x = p.x() * cosTheta - p.y() * sinTheta;
        double y = p.x() * sinTheta + p.y() * cosTheta;
        return new Point(x, y);
    }
}
