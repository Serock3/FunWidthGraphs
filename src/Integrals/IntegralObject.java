package Integrals;

import Drawables.DrawableAfterTransform;
import funwidthgraphs.GraphCanvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IntegralObject implements DrawableAfterTransform {

    FunctionObject funcobject;
    Color positivecolor = new Color(0, 255, 0, 80);
    Color negativecolor = new Color(255, 0, 0, 80);

    ArrayList<IntegralRectangle> rectangles;

    private int accuracy;
    private double lowerlimit;
    private double upperlimit;

    private double value;

    public IntegralObject(FunctionObject funcobject) {
        this.funcobject = funcobject;
        rectangles = new ArrayList<>();
        rectangles.add(new IntegralRectangle(0, 0, 10, 10));
    }

    @Override
    public void draw(Graphics2D g2d, GraphCanvas GraphCanvas) {
        g2d.setColor(positivecolor);
        rectangles.stream().forEach((rectangle) -> {
            g2d.setColor(rectangle.getArea() < 0 ? negativecolor : positivecolor);
            g2d.fill(rectangle.getRectangle());
        });
    }

    public void update() {
        function func = funcobject.getFunc();
        
        value = 0;
        rectangles.clear();
        for (double i = lowerlimit; i < upperlimit; i += 0.1) {
            IntegralRectangle rectangle = new IntegralRectangle(i, 0, 0.1, func.F(i + 0.1));
            rectangles.add(rectangle);
            value += rectangle.getArea();
        }

//        rectangles.add(new Rectangle2D.Double(0, 0, 1,func.F(1)));
//        rectangles.add(new Rectangle2D.Double(10, 10, 10,10));
//        rectangles.add(new Rectangle2D.Double(-10, -10, 10,10));        
    }

    public double getInegralValue() {
        return value;
    }

    public FunctionObject getFuncobject() {
        return funcobject;
    }

    public void setFuncobject(FunctionObject funcobject) {
        this.funcobject = funcobject;
    }

    public Color getPositivecolor() {
        return positivecolor;
    }

    public void setPositivecolor(Color positivecolor) {
        this.positivecolor = positivecolor;
    }

    public Color getNegativecolor() {
        return negativecolor;
    }

    public void setNegativecolor(Color negativecolor) {
        this.negativecolor = negativecolor;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public double getLowerlimit() {
        return lowerlimit;
    }

    public void setLowerlimit(double lowerlimit) {
        this.lowerlimit = lowerlimit;
    }

    public double getUpperlimit() {
        return upperlimit;
    }

    public void setUpperlimit(double upperlimit) {
        this.upperlimit = upperlimit;
    }
}
