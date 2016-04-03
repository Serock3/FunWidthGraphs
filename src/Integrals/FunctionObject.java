package Integrals;

import funwidthgraphs.Drawable;
import funwidthgraphs.GraphCanvas;
import java.awt.Color;
import java.awt.Graphics;

public class FunctionObject implements Drawable {

    Color color = Color.black;
    function func;
    String functext;

    public FunctionObject(String functext, function func) {
        this.func = func;
        this.functext = functext;
    }

    @Override
    public void draw(Graphics g, GraphCanvas GraphCanvas) {
        g.setColor(color);
        for (int i = 0; i < GraphCanvas.getWidth(); i++) {
            g.drawLine(i, GraphCanvas.getPixelY(func.F(GraphCanvas.getNumericX(i))), i + 1, GraphCanvas.getPixelY(func.F(GraphCanvas.getNumericX(i + 1))));
        }
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
