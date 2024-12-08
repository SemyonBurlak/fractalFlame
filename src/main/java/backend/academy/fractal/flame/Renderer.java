package backend.academy.fractal.flame;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Renderer {
    private final FractalImage canvas;
    private final Rect world;
    private final List<Transformation> transformations;
    private final int samples;
    private final int iterationsPerSample;
    private final int symmetry;
    private final long seed;

    public Renderer(FractalImage canvas, Rect world, List<Transformation> transformations,
        int samples, int iterationsPerSample, int symmetry, long seed) {
        this.canvas = canvas;
        this.world = world;
        this.transformations = transformations;
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
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        int samplesPerThread = samples / numThreads;

        for (int i = 0; i < numThreads; i++) {
            final int start = i * samplesPerThread;
            final int end = (i == numThreads - 1) ? samples : start + samplesPerThread;
            executor.submit(() -> render(start, end));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // Wait for all threads to finish
        }
    }

    private void render(int startSample, int endSample) {
        Random random = new Random(seed + startSample);
        for (int num = startSample; num < endSample; num++) {
            Point pw = randomPointInWorld(random);

            for (int step = 0; step < iterationsPerSample; step++) {
                Transformation transformation = transformations.get(random.nextInt(transformations.size()));
                pw = transformation.apply(pw);

                double thetaIncrement = (2 * Math.PI) / symmetry;
                for (int s = 0; s < symmetry; s++) {
                    double theta = s * thetaIncrement;
                    Point rotatedPoint = rotate(pw, theta);
                    if (!world.contains(rotatedPoint)) continue;

                    int x = (int) ((rotatedPoint.x - world.x) / world.width * canvas.width());
                    int y = (int) ((rotatedPoint.y - world.y) / world.height * canvas.height());

                    Pixel pixel = canvas.getPixel(x, y);
                    if (pixel == null) continue;

                    // Accumulate color
                    double colorScale = 1.0 / iterationsPerSample;
                    double red = Math.abs(Math.sin(pw.x)) * colorScale;
                    double green = Math.abs(Math.sin(pw.y)) * colorScale;
                    double blue = Math.abs(Math.sin(pw.x + pw.y)) * colorScale;

                    pixel.accumulateColor(red, green, blue);
                }
            }
        }
    }

    private Point randomPointInWorld(Random random) {
        double x = world.x + random.nextDouble() * world.width;
        double y = world.y + random.nextDouble() * world.height;
        return new Point(x, y);
    }

    private Point rotate(Point p, double theta) {
        double cosTheta = Math.cos(theta);
        double sinTheta = Math.sin(theta);
        double x = p.x * cosTheta - p.y * sinTheta;
        double y = p.x * sinTheta + p.y * cosTheta;
        return new Point(x, y);
    }
}
