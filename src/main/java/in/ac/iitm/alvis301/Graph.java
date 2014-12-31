
package in.ac.iitm.alvis301;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author SavithaSam
 */
//Do not modify the code
//This class gives you a handle on all the nodes and edges of the graph

public class Graph {
    private HashMap <Integer,Node> nodeList ;
    private HashMap <ArrayList<Double>,Integer> posID; 
    private HashMap <Integer,Edge> edgeList ;
    private int startID;
    private int goalID;
    private static Graph graph = null;
    
    
    public Graph() {
        nodeList = new HashMap();
        posID = new HashMap();
        edgeList = new HashMap();
        startID = -1;
        goalID = -1;
    }

    //Do not use the following four methods
    public static Graph getInstance(){
        return graph;
    }
    public static void setInstance(Graph g){
        graph = g;
    }
    public void choose_start(double x, double y) {
        ArrayList<Double> position = new ArrayList();
        position.add(x);
        position.add(y);
        startID = posID.get(position);
        Node startNode = nodeList.get(startID);
        startNode.setState(State.start);    
    }    
    public void choose_goal(double x, double y) {
        ArrayList<Double> position = new ArrayList();
        position.add(x);
        position.add(y);
        goalID = posID.get(position);
        Node goalNode = nodeList.get(goalID);
        goalNode.setState(State.goal);
    }
    //Get total no. of nodes
    public int getNoNodes(){
         return nodeList.size();
     }
    //Get ID of start node
    public int getStartID() {
        return startID;
    }
    //Get ID of goal node
    public int getGoalID() {
        return goalID;
    }
    //Get Node given its ID
    public Node getNode(int id) {
        return nodeList.get(id);
    }
    //Get an Edge given its ID
    public Edge getEdge(int id) {
        return edgeList.get(id);
    }
    //The remaining functions are not to be used.
    //If you feel it is necessary to do so, please contact the TAs
    public HashMap getNodes () {
        return nodeList;
    }
    public HashMap getPosIDs(){
        return posID;
    }
    public HashMap getEdges() {
        return edgeList;
    }
    public Integer getNodeID(double x, double y) {
        ArrayList<Double> position = new ArrayList();
        position.add(x);
        position.add(y);
        return posID.get(position);
    }
    public void setNode(Node n) {
        nodeList.put(n.getNodeID(), n);
        ArrayList <Double> position = new ArrayList();
        position.add(n.getX());
        position.add(n.getY());
        posID.put(position, n.getNodeID());
    }
    public void createEdge(Edge e) {
        edgeList.put(e.getEdgeID(), e);
    } 
    public void setPosIDs(HashMap posID)
    {
        this.posID=posID;
    }
    public void setNodes(HashMap <Integer,Node> Nodes) {
        nodeList = Nodes;
    }
    public void setEdges(HashMap <Integer,Edge> Edges) {
        edgeList = Edges;
    }
    public void setStartId(int id) {
        startID = id;
    }
}
