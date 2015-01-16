
package in.ac.iitm.alvis301;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author SavithaSam
 */
public class GraphPanel extends javax.swing.JPanel {

    /**
     * Creates new form GraphPanel
     */
    public HashMap <State,Color> colorMap;
    Graph gr;
    GraphWindow gw; 
    TSPWindow tw;
    TreeWindow trw;
    int x1,x2,y1,y2;
    public GraphPanel() {
        gr = Graph.getInstance();
        gw = GraphWindow.getInstance();
        colorMap = ColorMap.getInstance().getMap();
    }
   
    @Override
     public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        setBackground(new java.awt.Color(204, 204, 255));
        gr = Graph.getInstance();
        gw = GraphWindow.getInstance();
        tw = TSPWindow.getInstance();
        trw = TreeWindow.getInstance();
        if (gr == null) {
            System.out.println("Hello");
            return;
        }
        HashMap edges = gr.getEdges();
        Iterator it = edges.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Edge e = (Edge) pairs.getValue();
            int nodeID1 = e.getNodeID1();
            int nodeID2 = e.getNodeID2();
   //         System.out.println(e.getEdgeID()+"|"+e.getNodeID1()+"|"+e.getNodeID2());
            Node n1 = gr.getNode(nodeID1);
            Node n2 = gr.getNode(nodeID2);
            if(n1==null||n2==null)
                continue;
            double x11 = n1.getX(), y11 = n1.getY();
            double x21 = n2.getX(), y22 = n2.getY();
            g2d.setColor(colorMap.get(e.getState()));
            Line2D line = new Line2D.Double(x11, y11, x21, y22);
            if(e.getState()==State.pipepath || (gw!=null && gw.path.contains(e) && e.getState()!=State.path) || (trw!=null && trw.path.contains(e))) {
                g2d.setColor(colorMap.get(State.pipepath));
                g2d.setStroke(new BasicStroke(3));
                if (tw!=null) { 
                    g2d.setColor(Color.gray);
                    g2d.setStroke(new BasicStroke(1));
                }
                g2d.draw(line);
            }
            else {
                if (e.getState() != State.tsp) {
                    g2d.setStroke(new BasicStroke(1));
                    if(e.getState() == State.path)
                        g2d.setStroke(new BasicStroke(2));
                    if(e.getState() == State.pop_path)
                        g2d.setStroke(new BasicStroke(4));
                    g2d.draw(line);
                }
            }
        }
        //render nodes
        HashMap nodes = gr.getNodes();
        it = nodes.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Node n = (Node) pairs.getValue();
            double x = n.getX(), y = n.getY();
            g2d.setColor(colorMap.get(n.getState()));
            Ellipse2D ellipse = new Ellipse2D.Double(x-4, y-4, 8, 8);
            if (n.getState()==State.start || n.getState()==State.goal || n.getState()==State.pipe) {
                ellipse = new Ellipse2D.Double(x-4, y-4, 10, 10);
            }
            if (n.getState()!=State.max) {
                g2d.draw(ellipse);
                g2d.fill(ellipse);
            }
            else {
                Rectangle2D rect = new Rectangle2D.Double(x-4,y-4,8,8);
                g2d.draw(rect);
                g2d.fill(rect);
            }
            if (n.getState()==State.max || n.getState()==State.min) {
                GameNodeData gd = (GameNodeData) n.getData();
                if (gd.value != 0) {
                    int posx = (int) (n.getX() - 15);
                    int posy  = (int) (n.getY() - 15);
                    String s = "" + gd.value;
                    g2d.drawString(s, (float)posx, (float)posy);
                }
            }
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(204, 204, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 977, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 618, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
