
package alvis301;

import static alvis301.State.*;

/**
 *
 * @author SavithaSam
 */


public class Edge {

    private int edgeID;
    private double cost;
    private State edgeState;
    private final int nodeID1, nodeID2;
    private EdgeData data;
    
    public Edge(int x, int y, int ID) {
        nodeID1 = x;
        nodeID2 = y;
        edgeID = ID;
        edgeState = unvisited;
    }
    public int getEdgeID() {
        return edgeID;
    }
    public double getCost() {
        return cost;
    }
    public void setCost(double value) {
        cost = value;
    }
    public void setEdgeID(int ID) {
        edgeID = ID;
    }
    
    public int getNodeID1() {
        return nodeID1;
    }
    public int getNodeID2() {
        return nodeID2;
    }
    public State getState() {
        return edgeState;
    }
    public void setState(State currentState) {
        edgeState = currentState;
    }
    public EdgeData getData() {
        return data;
    }
    public void setData(EdgeData data) {
        this.data = data;
    }
}
