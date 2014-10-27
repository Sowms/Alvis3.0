/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package alvis301;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author SavithaSam
 */
public class TSPLayoutCreator {
    Graph g;
    public void create(int number) {
        g = new Graph();
        HashMap <Integer, Node> nodes = g.getNodes();
        Random rand = new Random();
        Dimension panelSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxX = panelSize.width - 100;
        int maxY = panelSize.height - 200;
        for (int i=0 ; i<number; i++) {
            int randX = rand.nextInt(maxX) + 1;
            int randY = rand.nextInt(maxY) + 1;
            Node n = new Node(randX, randY, i+1);
            n.setState(State.open);
            g.setNode(n);
        }
        int eid = 1;
        for (int i=0 ; i<number; i++) {
            Node n = g.getNode(i+1);
            ArrayList<Node> adjList = n.getAdjList();
            ArrayList<Edge> adjEdgeList = n.getAdjEdgeList();
            for (int j=0; j<number; j++) {
                if (i != j) 
                    adjList.add(g.getNode(j+1));
                if (i < j) {
                    Node n1 = g.getNode(j+1);
                    Edge e = new Edge(n.getNodeID(),n1.getNodeID(),eid);
                    e.setState(State.tsp);
                    g.createEdge(e);
                    ArrayList<Edge> otherAdjEdgeList = n1.getAdjEdgeList();
                    otherAdjEdgeList.add(e);
                    adjEdgeList.add(e);
                    n.setAdjEdgeList(adjEdgeList);
                    n1.setAdjEdgeList(otherAdjEdgeList);
                    nodes.put(n.getNodeID(), n);
                    nodes.put(j+1, n1);
                    g.setNodes(nodes);
                    eid++;
                } 
            }
        }
        Graph.setInstance(g);
    }    
}
