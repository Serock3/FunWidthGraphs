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
    }

    @Override
    public void draw(Graphics2D g2d, GraphCanvas GraphCanvas) {
        rectangles.stream().forEach((rectangle) -> {
            g2d.setColor(rectangle.getArea() < 0 ? negativecolor : positivecolor);
            g2d.fill(rectangle.getRectangle());
        });
    }

    public void update() {
        function func = funcobject.getFunc();

        int M = 100; //constant
        double diff = Math.abs(upperlimit - lowerlimit);
        
        int amtofboxes = (int) Math.round(Math.pow((diff * M), (accuracy / 100.0)));
        int positiveornegative = upperlimit - lowerlimit < 0 ? -1 : 1;

        value = 0;
        rectangles.clear();
        for (int i = 0; i < amtofboxes; i++) {
            IntegralRectangle rectangle = new IntegralRectangle(lowerlimit + i * positiveornegative * diff / amtofboxes, 0,
                    diff * positiveornegative / amtofboxes, func.F(lowerlimit + (i+0.5) * positiveornegative * diff / amtofboxes));
            rectangles.add(rectangle);
            value += rectangle.getArea();
        }       
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
