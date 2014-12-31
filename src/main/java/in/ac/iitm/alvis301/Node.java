
package in.ac.iitm.alvis301;

import static in.ac.iitm.alvis301.State.*;
import java.util.ArrayList;

/**
 *
 * @author SavithaSam
 */

//Do not modify code
public class Node {

    private final double x, y; // co-ordinate position in terms of x and y
    private int nodeID;
    private State nodeState;
    private NodeData data;
    private ArrayList <Node> adjList; //ArrayList of Nodes connected to given node
    private ArrayList <Edge> adjEdgeList; //ArrayList of Edges connected to given node
    
    public Node(double x, double y, int ID) {
        this.x = x;
        this.y = y;
        nodeID = ID;
        nodeState = unvisited;
        adjList = new ArrayList<Node>();
        adjEdgeList = new ArrayList<Edge>();
    }
    public int getNodeID() {
        return nodeID;
    }
    public void setNodeID(int ID) {
        nodeID = ID;
    }
    
    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public State getState() {
        return nodeState;
    }
    public ArrayList<Node> getAdjList() {
        return adjList;
    }
    public ArrayList<Edge> getAdjEdgeList() {
        return adjEdgeList;
    }
    public void setState(State currentState) {
        nodeState = currentState;
    }
    public void setAdjList(ArrayList<Node> adjList) {
        this.adjList = adjList;
    }
    public void setAdjEdgeList(ArrayList<Edge> adjEdgeList) {
        this.adjEdgeList = adjEdgeList;
    }
    public NodeData getData() {
        return data;
    }
    public void setData(NodeData data) {
        this.data = data;
    }
}
