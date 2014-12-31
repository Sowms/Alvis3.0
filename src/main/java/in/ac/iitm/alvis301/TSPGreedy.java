/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package in.ac.iitm.alvis301;

import java.util.ArrayList;
//import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SavithaSam
 */
public class TSPGreedy extends Algorithm{

    public TSPGreedy(int t) {
        super(t);
    }

    @Override
    public boolean goalTest(Node goalNode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object moveGen(Node parentNode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        
        ArrayList<Node> cur = getRandomSolution();
        setTour(cur);
        int counter = 0;
        while (counter < 20) {
            ArrayList<Node> next = getRandomSolution();
            if ( costTour(next) < costTour(cur) ) {
                setTour(next);
                cur = next;
                display();
               // printSolution(cur);
            }
        }
    }
    public void printSolution (ArrayList<Node> Tour) {
        
        System.out.println("inside");
        for (Node n : Tour) {
            System.out.print(n.getNodeID()+" ");
        }
        
    }
    public double costTour(ArrayList<Node> tour) {
        
        Node first = tour.get(0);
        Node prev = first;
        //tour.remove(0);
        double cost = 0;
        for (Node n : tour) {
            if (n.equals(prev))
                continue;
            cost = cost + distance(prev,n);
            prev = n;
        }
        cost = cost + distance(prev,first);
        return cost;
    }
    public double distance(Node n1, Node n2) {
        return Math.sqrt(Math.pow((n1.getX()-n2.getX()),2)+Math.pow((n1.getY()-n2.getY()),2));
    }
    public void setTour (ArrayList <Node> tour) {
        
        Node prev = tour.get(0);
        Node first = prev;
        //tour.remove(0);
        for (Map.Entry pairs : edges.entrySet()) {
            Edge e = (Edge) pairs.getValue();
            e.setState(in.ac.iitm.alvis301.State.tsp);
            updateEdge(e);
        }
        for (Node n:tour) {
            if (n.equals(prev))
                continue;
            ArrayList<Edge> adjEdgeList = prev.getAdjEdgeList();
            for (Edge e : adjEdgeList) {
                int nodeID1 = e.getNodeID1();
                int nodeID2 = e.getNodeID2();
                if (nodeID1 == n.getNodeID() || nodeID2 == n.getNodeID()) {
                    e.setState(in.ac.iitm.alvis301.State.path);
                    //System.out.println(e.getNodeID1()+"|"+e.getNodeID2());
                    updateEdge(e);
                    break;
                }
            }
            prev = n;
        }
        
        ArrayList<Edge> adjEdgeList = prev.getAdjEdgeList();
            for (Edge e : adjEdgeList) {
                int nodeID1 = e.getNodeID1();
                int nodeID2 = e.getNodeID2();
                if (nodeID1 == first.getNodeID() || nodeID2 == first.getNodeID()) {
                    e.setState(in.ac.iitm.alvis301.State.path);
                   // System.out.println(e.getNodeID1()+"|"+e.getNodeID2());
                    updateEdge(e);
                    break;
                }
            }
    }
    public ArrayList<Node> getRandomSolution(){
       
        ArrayList<Node> solution = new ArrayList<Node>();
        int counter = 0;
        int size = g.getNoNodes();    
        Random rand = new Random();
        Node prev = null;
        while (counter < size) {
            int nid = rand.nextInt(size) + 1;
            Node n = g.getNode(nid);
            if (!solution.contains(n)) {
             //   System.out.print(nid+"-");
                solution.add(n);
                counter++;
            }
        }
        return solution;
    }
            
    
}
