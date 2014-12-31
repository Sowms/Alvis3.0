/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package in.ac.iitm.alvis301;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author SavithaSam
 */
public class TSPCreator {
    Graph g;
    public void create(int number) {
        g = new Graph();
        int radius = number*5 ;
        Dimension panelSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxX = panelSize.width - 100;
        int maxY = panelSize.height - 200;
        int cenX = (40+maxX)/2;
        int cenY = (40+maxY)/2;
        //int counter = number/4;
        double theta = 2*3.14/number;
        int id = 1;
        int counter = 1;
        for (double i=0 ; i<2*3.14; i+=theta) {
            int x = (int) (radius*Math.cos(i));
            int y = (int) (radius*Math.sin(i));
            Node n = new Node(cenX+x,cenY+y,id++);
            n.setState(State.goal);
            g.setNode(n);
            if(counter==5)
                g.choose_goal(cenX+x, cenY+y);
            counter++;
         }
        
        int eid = 1;
        for (int nid = 1; nid <id; nid++) 
            for (int inid = 1; inid <id; inid++) {
                if (nid!=inid) {
                    Node n1 = g.getNode(nid);
                    Node n2 = g.getNode(inid);
                    Edge e = new Edge(nid,inid,eid);
                    eid++;
                    ArrayList<Edge> adjEdgeList1 = n1.getAdjEdgeList();
                    ArrayList<Edge> adjEdgeList2 = n2.getAdjEdgeList();
                    adjEdgeList1.add(e);
                    adjEdgeList2.add(e);
                        ArrayList<Node> adjList1 = n1.getAdjList();
                        ArrayList<Node> adjList2 = n2.getAdjList();
                        adjList1.add(n2);
                        adjList2.add(n1);
                        n1.setAdjEdgeList(adjEdgeList1);
                        n2.setAdjEdgeList(adjEdgeList2);
                        n1.setAdjList(adjList1);
                        n2.setAdjList(adjList2);
                        HashMap <Integer,Node> nodes = g.getNodes();
                        nodes.put(n1.getNodeID(),n1);
                        nodes.put(n2.getNodeID(),n2);
                        g.setNodes(nodes);
                        
                    g.createEdge(e);
                }
            }
        g.choose_start(cenX+radius, cenY);
        //g.choose_goal(cenX+radius, cenY-radius);
        Graph.setInstance(g);
    }    
}

