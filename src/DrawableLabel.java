import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

public class DrawableLabel extends JLabel {
    Point pointStart = null;
    Point pointEnd   = null;
    public ArrayList<Feature> feats = new ArrayList<Feature>();
    public Stack<Feature> fStack = new Stack<Feature>();

    public DrawableLabel() {
    }

    {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                pointStart = e.getPoint();
            }

            public void mouseReleased(MouseEvent e) {
                Feature newF = new Feature(new Vector2d(pointStart.getX(), pointStart.getY()), new Vector2d(pointEnd.getX(), pointEnd.getY()));
                feats.add(newF);
                pointStart = null;
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                pointEnd = e.getPoint();
            }

            public void mouseDragged(MouseEvent e) {
                if(e.getPoint().getX() < getWidth() && e.getPoint().getX() > 0 && e.getPoint().getY() < getHeight() && e.getPoint().getY() > 0) {
                    pointEnd = e.getPoint();
                    repaint();
                }
            }
        });
    }
    public void paint(Graphics g) {
        super.paint(g);
        if (pointStart != null) {
            g.setColor(Color.RED);
            g.drawLine(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
        }
        if(!fStack.isEmpty()) {
            g.setColor(Color.GREEN);
            Feature fPeek = fStack.peek();
            g.drawLine((int) fPeek.getStart().getX(), (int) fPeek.getStart().getY(), (int) fPeek.getEnd().getX(), (int) fPeek.getEnd().getY());
            Iterator<Feature> stackIt = fStack.iterator();
            while(stackIt.hasNext()){
                Feature f = stackIt.next();
                if(f != fPeek) {
                    g.setColor(Color.CYAN);
                    g.drawLine((int) f.getStart().getX(), (int) f.getStart().getY(), (int) f.getEnd().getX(), (int) f.getEnd().getY());
                }
            }
        }
        for(Feature f : this.feats){
            g.setColor(Color.CYAN);
            g.drawLine((int)f.getStart().getX(), (int)f.getStart().getY(), (int)f.getEnd().getX(), (int)f.getEnd().getY());
        }

    }
}
