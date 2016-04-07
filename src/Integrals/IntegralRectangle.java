package Integrals;

import java.awt.geom.Rectangle2D;

public class IntegralRectangle {

    double posx;
    double posy;
    double width;
    double height;

    public IntegralRectangle(double posx, double posy, double width, double height) {
        this.posx = posx;
        this.posy = posy;
        this.width = width;
        this.height = height;
    }

    public double getArea() {
        return width * height;
    }

    public Rectangle2D.Double getRectangle() {
        double tmpx = posx;
        double tmpy = posy;
        double tmpwidth = width;
        double tmpheight = height;
        if (width < 0) {
            tmpx += width;
            tmpwidth = -width;
        }
        if (height < 0){
            tmpy += height;
            tmpheight = -height;
        }
        return new Rectangle2D.Double(tmpx,tmpy,tmpwidth,tmpheight); 
    }
}
