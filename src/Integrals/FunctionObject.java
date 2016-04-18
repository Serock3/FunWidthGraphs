package Integrals;

import funwidthgraphs.GraphCanvas;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import Drawables.DrawableBeforeTransform;

public class FunctionObject implements DrawableBeforeTransform {

    Color color = Color.black;
    BasicStroke stroke = new BasicStroke(2);
    function func;
    String functext;

    public FunctionObject(String functext, function func) {
        this.func = func;
        this.functext = functext;
    }

    @Override
    public void draw(Graphics2D g2d, GraphCanvas graphcanvas) {
        g2d.setColor(color);
        g2d.setStroke(stroke);
        int amountOfPoints = graphcanvas.getWidth();
        int[] x = new int[amountOfPoints];
        int[] y = new int[amountOfPoints];
        for (int i = 0; i < amountOfPoints; i++) {
            x[i] = i;
            y[i] = function.convertPixelPos(i, func, graphcanvas);
        }
        g2d.drawPolyline(x, y, amountOfPoints);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public function getFunc() {
        return func;
    }

    public void setFunc(function func) {
        this.func = func;
    }

    public String getFunctext() {
        return functext;
    }

    public void setFunctext(String functext) {
        this.functext = functext;
    }

}
