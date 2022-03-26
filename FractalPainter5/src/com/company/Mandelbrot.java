package com.company;

import java.awt.geom.Rectangle2D;

public class Mandelbrot extends FractalGenerator {

    public static final int MAX_ITERATIONS = 2000;
    public void getInitialRange(Rectangle2D.Double range) {
        range.x = -2 ;
        range.y = -1.5 ;
        range.width = 3;
        range.height = 3;
    }

    public int numIterations(double x, double y) {
        int iteration = 0;
        Complex cxnum = new Complex(x, y);

        while (iteration < MAX_ITERATIONS && cxnum.abs2() < 4) {
            cxnum.mandelbrotIteration();
            iteration++;
        }

        if (iteration == MAX_ITERATIONS)
            return -1;
        else {
            return iteration;
        }
    }
    public String toString(){
        return "Mandelbrot";
    }
}