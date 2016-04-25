package Integrals;

import funwidthgraphs.GraphCanvas;

/**
 *
 * @author sebastian
 */
public interface function {

    double F(double x);

    public static int convertPixelPos(int i, function func, GraphCanvas graphcanvas) {
        return graphcanvas.getPixelY(func.F(graphcanvas.getNumericX(i)));
    }
}
