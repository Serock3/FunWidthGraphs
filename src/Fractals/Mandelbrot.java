package Fractals;

import Drawables.DrawableBeforeTransform;
import funwidthgraphs.GraphCanvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

public class Mandelbrot implements DrawableBeforeTransform {

    int resolution = 1;

    int bastiterations = 60;
    int maxiterations = bastiterations;
    int maxscale = 5;

    Gradient redtoblue = (value, max) -> {
        if (value == max) {
            return Color.BLACK;
        }

        return new Color(value / max, 0, 1-value / max, value / max);

    };

    Gradient greentoblue = (value, max) -> {
        if (value == max) {
            return Color.BLACK;
        }

        return new Color(0, value / max, 1-value / max, value / max);

    };
    
    Gradient bluetored = (value, max) -> {
        if (value == max) {
            return Color.BLACK;
        }

        return new Color(1-value/max, 0, value / max, value / max);

    };
    Gradient blackandwhite = (value, max) -> new Color(0, 0, 0, value / max);

    @Override
    public void draw(Graphics2D g2d, GraphCanvas graphcanvas) {
        maxiterations = (int) (bastiterations + Math.log(graphcanvas.getxScale()) * maxscale);
//        System.out.println(maxiterations);
        for (int x = 0; x < graphcanvas.getWidth(); x += resolution) {
            for (int y = 0; y < graphcanvas.getHeight(); y += resolution) {
                Point2D.Double point = graphcanvas.pixelToNumeric(new Point2D.Double(x, y));
                Complex z = new Complex(point.x, point.y);
                float value = testNumber(z, maxiterations);
                if (value == -1) {
                    g2d.setColor(Color.red);
                } else {
                    g2d.setColor(redtoblue.getColor(value, maxiterations));
                }
                g2d.fillRect(x, y, resolution, resolution);
//                g2d.fillOval(x, y, accuracy, accuracy);
            }
        }
    }

    public int testNumber(Complex z0, int max) {
        Complex z = z0;
        if (quicktest(z)) {
            return max;
        }
        for (int t = 0; t < max; t++) {
            if (z.re() * z.re() + z.im() * z.im() >= 4) {
                return t;
            }
            z = z.times(z).plus(z0);
        }
        return max;
    }

    private boolean quicktest(Complex z) {
        if (z.re() < 0 && (z.re() + 0.1) * (z.re() + 0.1) + z.im() * z.im() < 0.42) {
            return true;
        }
        if (z.re() * z.re() + z.im() * z.im() < 0.0625) {
            return true;
        }
        if ((z.re() + 1) * (z.re() + 1) + z.im() * z.im() < 0.0625) {
            return true;
        }
        if ((z.re()) * (z.re()) + (z.im() - 0.25) * (z.im() - 0.25) < 0.125) {
            return true;
        }
        if ((z.re()) * (z.re()) + (z.im() + 0.25) * (z.im() + 0.25) < 0.125) {
            return true;
        }
        if ((z.re() + 1.31) * (z.re() + 1.31) + z.im() * z.im() < 0.00390625) {
            return true;
        }
        if ((z.re() + 0.125) * (z.re() + 0.125) + (z.im() + 0.7425) * (z.im() + 0.7425) < 0.009) {
            return true;
        }
        return (z.re() + 0.125) * (z.re() + 0.125) + (z.im() - 0.7425) * (z.im() - 0.7425) < 0.009;
    }

    public void changeResolution(int i) {
        resolution = 6 - i / 20;
    }

    public void changeIterations(int i) {
        bastiterations = i + 1;
        maxscale = i / 10;
    }
}
