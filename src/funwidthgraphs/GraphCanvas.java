package funwidthgraphs;

import Integrals.function;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
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

    ArrayList<Drawable> drawobjects;

    double mouseScrollSpeed;

    int mousePosX = -1;
    int mousePosY = -1;
    
    JLabel XY;

    public GraphCanvas() {
        initComponents();

        backgroundcolor = Color.white;
        mouseScrollSpeed = 1.1;
        drawobjects = new ArrayList<>();

        transform = new AffineTransform();
        xScale = 20; //How many pixels there is between each number on the number line
        yScale = 20;

        
        transform.translate(getWidth() / 2, getHeight() / 2);
        transform.scale(xScale, -yScale);

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
//                if(XY != null)
                mousePosX = e.getX();
                mousePosY = e.getY();
                setXYText(mousePosX, mousePosY);
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double scalechange = Math.pow(mouseScrollSpeed, -e.getWheelRotation());
                zoom(scalechange);
                directionalZoom(scalechange);
                validate();
                repaint();
            }

        };

        this.addMouseListener(mouse);
        this.addMouseMotionListener(mouse);
        this.addMouseWheelListener(mouse);

    }

    private void zoom(double scalechange) {
        xScale *= scalechange;
        yScale *= scalechange;
    }

    private void directionalZoom(double scalechange) {
        xOrigoOffset = (int) Math.round((xOrigoPos - mousePosX) * scalechange + mousePosX - getWidth() / 2);
        yOrigoOffset = (int) Math.round((yOrigoPos - mousePosY) * scalechange + mousePosY - getHeight() / 2);
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
        
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(0, 255, 0,80));
        g2d.fill(new Rectangle(0, 0, 100,100));
//        AffineTransform transform = new AffineTransform();
//        transform.translate(getxOrigoPos(), getyOrigoPos());
//        transform.scale(getxScale(), -getyScale());
//                
//        g2d.transform(transform);
//        g2d.setColor(new Color(0, 255, 0,80));
//        g2d.fill(new Rectangle(0, 0, 10,10));
//        g2d.fill(new Rectangle(10, 10, 10,10));
//        g2d.fill(new Rectangle(-10, -10, 10,10));
//        try { 
//            g2d.transform(transform.createInverse());
//        } catch (NoninvertibleTransformException ex) {
//            Logger.getLogger(GraphCanvas.class.getName()).log(Level.SEVERE, null, ex);
//        }


        xOrigoPos = this.getWidth() / 2 + xOrigoOffset; //Pixel position of origo, can be negative
        yOrigoPos = this.getHeight() / 2 + yOrigoOffset;

        drawBackground(g);
        drawGraph(g);

        drawobjects.forEach((drawobject) -> drawobject.draw(g, this));
    }

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

    public ArrayList<Drawable> getDrawobjects() {
        return drawobjects;
    }

    public void setDrawobjects(ArrayList<Drawable> drawobjects) {
        this.drawobjects = drawobjects;
    }

    public Drawable getDrawobject(int i) {
        return drawobjects.get(i);
    }

    public void addDrawobject(Drawable drawobject) {
        drawobjects.add(drawobject);
    }

    public void setDrawobject(int i, Drawable drawobject) {
        drawobjects.set(i, drawobject);
    }

    public void removeDrawobject(int i) {
        drawobjects.remove(i);
    }

    public int getDrawobjectsSize() {
        return drawobjects.size();
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
