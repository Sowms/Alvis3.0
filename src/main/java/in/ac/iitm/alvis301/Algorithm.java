
package in.ac.iitm.alvis301;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


/**
 *
 * @author SavithaSam 
 */

//Do not modify this file
//Your algorithm must extend this class

public abstract class Algorithm extends Thread {
    
    public Graph g; 
    HashMap <Integer, Node> nodes;
    HashMap <Integer, Edge> edges;
    int type;
    //Do not call this function
    public void setGraph() {
        this.g = Graph.getInstance();
        nodes = g.getNodes();
        edges = g.getEdges();
    }
    //call update edge whenever an edge is modified in your code
    public void updateEdge(Edge e) {
        edges.put(e.getEdgeID(),e);
        g.setEdges(edges);
        Graph.setInstance(g);
    }
    //similarly call this function whenevr a node is modified.
    public void updateNode(Node n) {
        nodes.put(n.getNodeID(),n);
        g.setNodes(nodes);
        Graph.setInstance(g);
    }
    
    public Algorithm(int t) {
        type = t;
    }
    //call this function to display on the UI
    public void display() {
        
        final GraphWindow instance1 = GraphWindow.getInstance();
        final TreeWindow instance2 = TreeWindow.getInstance();
        final TSPWindow instance3 = TSPWindow.getInstance();
        //final String s = d.toString();
        try {
            if(type==1) {
                Thread.sleep(GraphWindow.time);
            }
            else if(type==2) {
                Thread.sleep(TreeWindow.time);
            }
            else if(type==3) {
                Thread.sleep(TSPWindow.time);
            }
            Runnable showPanelRun = new Runnable() {
                @Override
                public void run() {
                    try {
                        if(type==1)
                            instance1.showGraph();
                        else if(type==2)
                            instance2.showGraph();
                        else if(type==3)
                        {
                            instance3.showGraph();
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Algorithm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            SwingUtilities.invokeAndWait(showPanelRun);
            } catch ( InterruptedException ix ) {
                System.out.println("main interrupted while waiting on invokeAndWait()");
            } catch ( InvocationTargetException x ) {
                System.out.println("main exception thrown from run()");
            }
    }
    
    
    //call this function to display TSP SCREEN(INPUT PARAMETERS:current tour_cost,current temprature
    public void displayTSP(Double tour_cost,Double temp) {
        
        final TSPWindow instance3 = TSPWindow.getInstance();
        final String s = tour_cost.toString();
        final String t = temp.toString();
        try {
            Thread.sleep(TSPWindow.time);
            Runnable showPanelRun = new Runnable() {
                @Override
                public void run() {
                    try {
                            instance3.showGraph();
                            instance3.setDistance(s);
                            instance3.setTemprature(t);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Algorithm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            };
            SwingUtilities.invokeAndWait(showPanelRun);
            } catch ( InterruptedException ix ) {
                System.out.println("main interrupted while waiting on invokeAndWait()");
            } catch ( InvocationTargetException x ) {
                System.out.println("main exception thrown from run()");
            }
    }
    static String ans = "";
        
    public abstract boolean goalTest(Node goalNode);
    public abstract Object moveGen(Node parentNode);
    // this method will get a message for the user as a string
    public String getInput(final String message) {
        try {
            Runnable showPanelRun = new Runnable() {
                @Override
                public void run() {
                    ans = JOptionPane.showInputDialog(null,message,"",1);
                }
            };
            SwingUtilities.invokeAndWait(showPanelRun);
            } catch ( InterruptedException ix ) {
                System.out.println("main interrupted while waiting on invokeAndWait()");
            } catch ( InvocationTargetException x ) {
                System.out.println("main exception thrown from run()");
            }
        return ans;
    }
    // this method will display a message on the UI
    public void displayMessage(final String message) {
        try {
            Runnable showPanelRun = new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(null,message,"",1);
                }
            };
            SwingUtilities.invokeAndWait(showPanelRun);
            } catch ( InterruptedException ix ) {
                System.out.println("main interrupted while waiting on invokeAndWait()");
            } catch ( InvocationTargetException x ) {
                System.out.println("main exception thrown from run()");
            }
    }
    // this method will display a component on the UI
    public void displayComponent(final JComponent UIcomponent) {
        try {
            Runnable showPanelRun = new Runnable() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(null,UIcomponent,"",1);
                }
            };
            SwingUtilities.invokeAndWait(showPanelRun);
            } catch ( InterruptedException ix ) {
                System.out.println("main interrupted while waiting on invokeAndWait()");
            } catch ( InvocationTargetException x ) {
                System.out.println("main exception thrown from run()");
            }
    }
    @Override
    //write your code in this method in the extended class
    public abstract void run();
}
