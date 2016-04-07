/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
