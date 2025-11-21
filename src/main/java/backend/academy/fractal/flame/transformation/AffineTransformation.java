package backend.academy.fractal.flame.transformation;

import backend.academy.fractal.flame.AffineCoefficient;
import backend.academy.fractal.flame.Point;

public class AffineTransformation implements Transformation {
    private final AffineCoefficient affineCoefficient;

    public AffineTransformation(AffineCoefficient affineCoefficient) {
        this.affineCoefficient = affineCoefficient;
    }

    @Override
    public Point apply(Point point) {
        double x = affineCoefficient.a() + point.x() * affineCoefficient.b() + point.y() * affineCoefficient.c();
        double y = affineCoefficient.d() + point.x() * affineCoefficient.e() + point.y() * affineCoefficient.f();
        return new Point(x, y);
    }
}
