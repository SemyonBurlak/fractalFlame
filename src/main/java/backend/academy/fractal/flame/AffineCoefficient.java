package backend.academy.fractal.flame;

import java.awt.Color;
import java.util.Random;

public record AffineCoefficient(double a, double b, double c, double d, double e, double f, Color dummyColor) {
    private static final int MAX_COLOR_RANGE = 255;

    public static AffineCoefficient generateRandom(Random random) {
        double a = randomValue(random);
        double b = randomValue(random);
        double c = randomValue(random);
        double d = randomValue(random);
        double e = randomValue(random);
        double f = randomValue(random);

        while (!isAffine(a, b, c, d, e, f)) {
            a = randomValue(random);
            b = randomValue(random);
            c = randomValue(random);
            d = randomValue(random);
            e = randomValue(random);
            f = randomValue(random);
        }

        return new AffineCoefficient(a, b, c, d, e, f,
            new Color(random.nextInt(MAX_COLOR_RANGE), random.nextInt(MAX_COLOR_RANGE),
                random.nextInt(MAX_COLOR_RANGE)));
    }

    private static double randomValue(Random r) {
        return -1 + 2 * r.nextDouble();
    }

    private static boolean isAffine(double a, double b, double c, double d, double e, double f) {
        return ((a * a + d * d) < 1) && ((b * b + e * e) < 1)
            && ((a * a + b * b + d * d + e * e) < (1 + (a * e - b * d) * (a * e - b * d)));
    }
}


