
package in.ac.iitm.alvis301;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
    public double distance(Node n1, Node n2) {
        return Math.sqrt(Math.pow((n1.getX()-n2.getX()),2)+Math.pow((n1.getY()-n2.getY()),2));
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
        int node1 = e.getNodeID1();
        int node2 = e.getNodeID2();
        for (Map.Entry pairs : edgeList.entrySet()) {
            Edge curEdge = (Edge) pairs.getValue();
            if (curEdge.equals(e) || (curEdge.getNodeID1()==node1 && curEdge.getNodeID2()==node2) ||(curEdge.getNodeID1()==node2 && curEdge.getNodeID2()==node1))
                return;
        }
        Node n1 = getNode(node1);
        Node n2 = getNode(node2);
        double dist = distance(n1,n2);
        System.out.println(dist);
        Random generator = new Random();
        int delta = generator.nextInt((int)(0.1*dist));
        e.setCost(dist+delta);
        ArrayList <Node> adjList1 = n1.getAdjList();
        ArrayList <Node> adjList2 = n2.getAdjList();
        adjList1.add(n2);
        adjList2.add(n1);
        n1.setAdjList(adjList1);
        n2.setAdjList(adjList2);
        ArrayList <Edge> adjEdgeList1 = n1.getAdjEdgeList();
        ArrayList <Edge> adjEdgeList2 = n2.getAdjEdgeList();
        adjEdgeList1.add(e);
        adjEdgeList2.add(e);
        n1.setAdjEdgeList(adjEdgeList1);
        n2.setAdjEdgeList(adjEdgeList2);
        setNode(n1);
        setNode(n2);
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
