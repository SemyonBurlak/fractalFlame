package backend.academy.fractal.flame.transformation;

import backend.academy.fractal.flame.Point;

public class LinearTransformation implements Transformation {
    @Override
    public Point apply(Point p) {
        return p;
    }
}
