package com.company;

import java.awt.geom.Rectangle2D;

public class Tricorn extends FractalGenerator {

    public static final int MAX_ITERATIONS = 2000;

    @Override
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2;
        range.y = -2;
        range.width = 4;
        range.height = 4;
    }

    public int numIterations(double x, double y) {
        int iteration = 0;
        Complex cxnum = new Complex(x, y);
        while (iteration < MAX_ITERATIONS && cxnum.abs2() < 4) {
            cxnum.tricornIteration();
            iteration++;
        }

        if (iteration == MAX_ITERATIONS)
            return -1;
        else
            return iteration;
    }
    public String toString(){
        return "Tricorn";
    }
}
