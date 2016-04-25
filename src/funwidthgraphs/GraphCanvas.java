package funwidthgraphs;

import Drawables.Drawable;
import Drawables.DrawableAfterTransform;
import Drawables.DrawableBeforeTransform;
import Integrals.function;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

public class GraphCanvas extends javax.swing.JPanel {

    Color backgroundcolor;

    AffineTransform transform;

    double xScale;
    double yScale;

    int xOrigoPos;
    int yOrigoPos;

    int xOrigoOffset = 0;
    int yOrigoOffset = 0;

    ArrayList<DrawableBeforeTransform> drawbeforetransform;
    ArrayList<DrawableAfterTransform> drawaftertransform;

    double mouseScrollSpeed;

    int mousePosX = -1;
    int mousePosY = -1;

    JLabel XY;
    
    public boolean directionalzoom = true;

    public GraphCanvas() {
        initComponents();

        backgroundcolor = Color.white;
        mouseScrollSpeed = 1.1;
        drawbeforetransform = new ArrayList<>();
        drawaftertransform = new ArrayList<>();

        transform = new AffineTransform();
        xScale = 20; //How many pixels there is between each number on the number line
        yScale = 20;

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePosX = e.getX();
                mousePosY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
//                mousePosX = -1;
//                mousePosY = -1;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (mousePosX == -1 || mousePosY == -1) {
                    mousePosX = e.getX();
                    mousePosY = e.getY();
                    return;
                }
                xOrigoOffset -= mousePosX - e.getX();
                yOrigoOffset -= mousePosY - e.getY();
                mousePosX = e.getX();
                mousePosY = e.getY();

                validate();
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mousePosX = e.getX();
                mousePosY = e.getY();
                if(XY != null)
                setXYText(mousePosX, mousePosY);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double scalechange = Math.pow(mouseScrollSpeed, -e.getWheelRotation());
                zoom(scalechange);
                if(directionalzoom)directionalZoom(scalechange);
                else zoomTowardsMiddle(scalechange);
                validate();
                repaint();
            }

        };

        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouse);
        this.addMouseWheelListener(mouse);
    }

    public void initiateOrigo() {
        transform.translate(getWidth() / 2, getHeight() / 2);
        transform.scale(xScale, -yScale);
    }

    private void zoom(double scalechange) {
        xScale *= scalechange;
        yScale *= scalechange;
    }

    private void directionalZoom(double scalechange) {
        xOrigoOffset = (int) Math.round((xOrigoPos - mousePosX) * scalechange + mousePosX - getWidth() / 2);
        yOrigoOffset = (int) Math.round((yOrigoPos - mousePosY) * scalechange + mousePosY - getHeight() / 2);
    }
    
    private void zoomTowardsMiddle(double scalechange){
        xOrigoOffset = (int) Math.round((xOrigoPos - getWidth() / 2) * scalechange);
        yOrigoOffset = (int) Math.round((yOrigoPos - getHeight() / 2) * scalechange);
    }
    
    public void setXYText(int mousePosX, int mousePosY) {
        XY.setText(getNumericX(mousePosX) + " " + getNumericY(mousePosY));
    }

    public void addXY(JLabel XY) {
        this.XY = XY;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        xOrigoPos = this.getWidth() / 2 + xOrigoOffset; //Pixel position of origo, can be negative
        yOrigoPos = this.getHeight() / 2 + yOrigoOffset;
        transform = getNewTransform();
        
        drawBackground(g);
        drawGraph(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        drawbeforetransform.forEach((drawobject) -> drawobject.draw(g2d, this));        
        g2d.transform(transform);
        drawaftertransform.forEach((drawobject) -> drawobject.draw(g2d, this));

        
//        drawSomeStuff(g2d);
//        drawAFunction(g2d);
        try {
            g2d.transform(getNewTransform().createInverse()); 
        } catch (NoninvertibleTransformException ex) {
            Logger.getLogger(GraphCanvas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // TMP
    private AffineTransform getNewTransform() {
        AffineTransform tmp = new AffineTransform();
        tmp.translate(xOrigoPos, yOrigoPos);
        tmp.scale(xScale, -yScale);
        return tmp;
    }

    private void drawSomeStuff(Graphics2D g2d) {
        g2d.setColor(new Color(0, 255, 0, 80));
        g2d.fill(new Rectangle(0, 0, 10, 10));
        g2d.fill(new Rectangle(10, 10, 10, 10));
        g2d.fill(new Rectangle(-10, -10, 10, 10));

    }
    // TMP
    
    private void drawBackground(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawGraph(Graphics g) {
        int alpha = 50;
        Color myColour = new Color(0, 0, 0, alpha);
        g.setColor(myColour);

        g.drawLine(0, yOrigoPos, getWidth(), yOrigoPos);
        g.drawLine(xOrigoPos, 0, xOrigoPos, getWidth());

        g.setColor(Color.BLACK);
        g.drawString("10", getPixelX(10), yOrigoPos + 10);
        g.drawString("-10", getPixelX(-10), yOrigoPos + 10);
        g.drawString("10", xOrigoPos + 5, getPixelY(10));
        g.drawString("-10", xOrigoPos + 5, getPixelY(-10));
    }

    private void drawFunction(Graphics g, function func) {
        g.setColor(Color.black);
        for (int i = 0; i < getWidth(); i++) {
            g.drawLine(i, getPixelY(func.F(getNumericX(i))), i + 1, getPixelY(func.F(getNumericX(i + 1))));
        }
    }
    
    public Point2D.Double numericToPixel(Point2D.Double point){
        Point2D.Double dest = new Point2D.Double(0,0);
        transform.transform(point,dest);
        return dest;
    }
    
    public Point2D.Double pixelToNumeric(Point2D.Double point){
        Point2D.Double dest = new Point2D.Double(0,0);
        try {
            transform.inverseTransform(point,dest);
        } catch (NoninvertibleTransformException ex) {
            Logger.getLogger(GraphCanvas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dest;
    }

    public double getxScale() {
        return xScale;
    }

    public void setxScale(double xScale) {
        this.xScale = xScale;
    }

    public double getyScale() {
        return yScale;
    }

    public void setyScale(double yScale) {
        this.yScale = yScale;
    }

    public int getxOrigoPos() {
        return xOrigoPos;
    }

    public void setxOrigoPos(int xOrigoPos) {
        this.xOrigoPos = xOrigoPos;
    }

    public int getyOrigoPos() {
        return yOrigoPos;
    }

    public void setyOrigoPos(int yOrigoPos) {
        this.yOrigoPos = yOrigoPos;
    }

    public double getNumericX(int pixelX) {
        return (pixelX - xOrigoPos) / xScale;
    }

    public double getNumericY(int pixelY) {
        return -1 * (pixelY - yOrigoPos) / yScale; //The y axis is counted the wrong direction in pixels
    }

    public int getPixelX(double numericX) {
        return (int) Math.round(numericX * xScale + xOrigoPos);
    }

    public int getPixelY(double numericY) {
        return -1 * (int) Math.round((numericY * yScale) - yOrigoPos);
    }

    public int getxOrigoOffset() {
        return xOrigoOffset;
    }

    public void setxOrigoOffset(int xOrigoOffset) {
        this.xOrigoOffset = xOrigoOffset;
    }

    public int getyOrigoOffset() {
        return yOrigoOffset;
    }

    public void setyOrigoOffset(int yOrigoOffset) {
        this.yOrigoOffset = yOrigoOffset;
    }

    public Color getBackgroundcolor() {
        return backgroundcolor;
    }

    public void setBackgroundcolor(Color backgroundcolor) {
        this.backgroundcolor = backgroundcolor;
    }

    public double getMouseScrollSpeed() {
        return mouseScrollSpeed;
    }

    public void setMouseScrollSpeed(double mouseScrollSpeed) {
        this.mouseScrollSpeed = mouseScrollSpeed;
    }

    public AffineTransform getTransform() {
        return transform;
    }

    public void setTransform(AffineTransform transform) {
        this.transform = transform;
    }

    public void addDrawobject(Drawable drawobject) {
        if (drawobject instanceof DrawableBeforeTransform) {
            drawbeforetransform.add((DrawableBeforeTransform) drawobject);
        } else {
            drawaftertransform.add((DrawableAfterTransform) drawobject);
        }
    }

    public int getDrawbeforetransformSize() {
        return drawbeforetransform.size();
    }

    public int getDrawaftertransformSize() {
        return drawaftertransform.size();
    }

    public void setDrawbeforetransformObject(int i, DrawableBeforeTransform drawobject) {
        drawbeforetransform.set(i, drawobject);
    }

    public void setDrawaftertransformObject(int i, DrawableAfterTransform drawobject) {
        drawaftertransform.set(i, drawobject);
    }

    public void addDrawbeforetransformObject(DrawableBeforeTransform drawobject) {
        drawbeforetransform.add(drawobject);
    }

    public void addDrawaftertransformObject(DrawableAfterTransform drawobject) {
        drawaftertransform.add(drawobject);
    }

    public ArrayList<DrawableBeforeTransform> getDrawbeforetransform() {
        return drawbeforetransform;
    }

    public void setDrawbeforetransform(ArrayList<DrawableBeforeTransform> drawbeforetransform) {
        this.drawbeforetransform = drawbeforetransform;
    }

    public ArrayList<DrawableAfterTransform> getDrawaftertransform() {
        return drawaftertransform;
    }

    public void setDrawaftertransform(ArrayList<DrawableAfterTransform> drawaftertransform) {
        this.drawaftertransform = drawaftertransform;
    }
    
    public void removeDrawbeforetransformObject(DrawableBeforeTransform drawobject){
        this.drawbeforetransform.remove(drawobject);
    }
    
    public void removeDrawaftertransformObject(DrawableAfterTransform drawobject){
        this.drawaftertransform.remove(drawobject);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
