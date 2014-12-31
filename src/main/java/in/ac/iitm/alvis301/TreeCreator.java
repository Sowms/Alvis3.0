
package in.ac.iitm.alvis301;

import java.awt.Dimension;
import java.awt.Toolkit;
import static java.lang.Math.floor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author SavithaSam
 */
public class TreeCreator {
    
    Graph g;
    public void create() {
        g = new Graph();
        Dimension panelSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxX = panelSize.width - 100;
        int maxY = panelSize.height - 200;
        Random rand1=new Random();
        int no_of_levels;
        no_of_levels = (int) floor((maxY-40)/(maxY/6));
        //root
        int rootx = maxX/2;
        int rooty = 40;
        int id = 1;
        int level, prevlevelnodes=0, curlevelnodes=0;
        ArrayList<Node> prevLevel = new ArrayList<Node>();
        ArrayList<Node> curLevel = new ArrayList<Node>();
        Node root = new Node(rootx,rooty,id);
        root.setState(State.max);
        GameNodeData gd = new GameNodeData();
        root.setData(gd);
        g.setStartId(id);
        g.setNode(root);
        id++;
        //leaf nodes
        for (int curX = 40; curX<=maxX; curX+=20, id++, prevlevelnodes++) {
            Node n = new Node(curX, maxY, id);
            if(no_of_levels%2==1) 
                n.setState(State.max);
            else
                n.setState(State.min);
            gd=new GameNodeData();
            gd.terminal=true;
            gd.value=rand1.nextInt(201) - 100;
            
            gd.level=no_of_levels+1;
            System.out.println(gd.value);
            n.setData(gd);
            g.setNode(n);
            prevLevel.add(n);
        }
        //random parents
        int eid = 1;
        for (level = no_of_levels - 1; level >= 0; level--) {
            int counter = 0;
            while (counter < prevlevelnodes) {
                    Random rand = new Random();
                    int randomBranch = rand.nextInt(4) + 1;
                    while (randomBranch > prevlevelnodes) {
                        randomBranch = rand.nextInt(4) + 1;
                    }
                    Node min = prevLevel.get(counter), max;
                    if ((counter+randomBranch-1)< prevlevelnodes)
                        max = prevLevel.get(counter+randomBranch-1);
                    else
                        max = prevLevel.get(prevlevelnodes-1);
                    int curX = (int) ((min.getX() + max.getX())/2);
                    int curY = (int) (min.getY() - maxY/6);
                    Node cur = new Node(curX,curY,id++);
                    curLevel.add(cur);
                    gd=new GameNodeData();
                    if (level%2==1)
                        cur.setState(State.max);
                    else
                        cur.setState(State.min);
                    gd.level=level+1;
                    cur.setData(gd);
                    g.setNode(cur);
                    int m = ((counter+randomBranch-1) < prevlevelnodes) ? (counter+randomBranch-1) : prevlevelnodes - 1; 
                    for (int k=counter; k<=m; k++) {
                        Node n1 = g.getNode(cur.getNodeID());
                        Node n2 = g.getNode(prevLevel.get(k).getNodeID());
                        Edge e = new Edge(n1.getNodeID(),n2.getNodeID(),eid++);
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
                    counter += randomBranch;
                    curlevelnodes++;
            }
            prevlevelnodes = curlevelnodes;
            prevLevel = curLevel;
            curLevel = new ArrayList<Node>();
            curlevelnodes = 0;
        }
        for (Node k1 : prevLevel) {
            Node n1 = k1;
            Node n2 = root;
            Edge e = new Edge (k1.getNodeID(), root.getNodeID(),eid);
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
            eid++;
        }
        Graph.setInstance(g);
    }
}
