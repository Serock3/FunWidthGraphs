package Integrals;

import funwidthgraphs.Drawable;
import funwidthgraphs.GraphCanvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IntegralObject implements Drawable{

    FunctionObject funcobject;
    Color positivecolor = new Color(0, 255, 0,80);
    Color negativecolor = Color.red;
    
    ArrayList<Rectangle> rectangles;

    public IntegralObject(FunctionObject funcobject) {
        this.funcobject = funcobject;
        rectangles =  new ArrayList<>();
        rectangles.add(new Rectangle(0, 0, 10,10));
    }
    
    @Override
    public void draw(Graphics g, GraphCanvas GraphCanvas) {
//        Graphics2D g2d = (Graphics2D)g;
//        g2d.setColor(positivecolor);
//        
//        AffineTransform transform = new AffineTransform();
//        transform.translate(GraphCanvas.getxOrigoPos(), GraphCanvas.getyOrigoPos());
//        transform.scale(GraphCanvas.getxScale(), -GraphCanvas.getyScale());
//                
//        g2d.transform(transform);
//        g2d.fill(new Rectangle(0, 0, 10,10));
//        g2d.fill(new Rectangle(10, 10, 10,10));
//        g2d.fill(new Rectangle(-10, -10, 10,10));
//        
//        try {
//            g2d.transform(transform.createInverse()); 
//        } catch (NoninvertibleTransformException ex) {
//            Logger.getLogger(IntegralObject.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
