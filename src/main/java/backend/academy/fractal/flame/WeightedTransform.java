package backend.academy.fractal.flame;

import backend.academy.fractal.flame.transformation.AffineTransformation;
import backend.academy.fractal.flame.transformation.Transformation;
import java.awt.Color;
import lombok.Getter;

public class WeightedTransform {
    private final AffineTransformation affineTransformation;
    private final Transformation variation;

    @Getter
    private final Color color;

    public WeightedTransform(AffineTransformation affineTransformation, Transformation variation, Color color) {
        this.affineTransformation = affineTransformation;
        this.variation = variation;
        this.color = color;
    }

    public Point apply(Point p) {
        Point afterAffine = affineTransformation.apply(p);
        return variation.apply(afterAffine);
    }

}

