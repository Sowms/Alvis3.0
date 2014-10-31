
package alvis301;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        
        try {
            String name = Thread.currentThread().getName();
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
                public void run() {
                    try {
                        if(type==1)
                            instance1.showGraph();
                        else if(type==2)
                            instance2.showGraph();
                        else if(type==3)
                            instance3.showGraph();
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
    public abstract boolean goalTest(Node goalNode);
    public abstract Object moveGen(Node parentNode);
    @Override
    //write your code in this method in the extended class
    public abstract void run();
}
