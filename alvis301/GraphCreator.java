
package alvis301;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Sowmya
 */

//Do not modify this file

class component {
    public int minX;
    public int minY;
    public int maxX;
    public int maxY;
    ArrayList <Node> nodes;
    public component() {
        nodes = new ArrayList<Node>();
    }
    public void setComponent(int minX, int minY, int maxX, int maxY, ArrayList<Node> compNodes) {
        nodes = compNodes;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
    public static void sort (ArrayList<component> comp) {

        for (int i=0; i<comp.size(); i++)
            for (int j=0; j<comp.size() - i - 1; j++) {
                if ((comp.get(j).minX > comp.get(j+1).minX)) {
                   component temp = comp.get(j);
                   comp.set(j, comp.get(j+1));
                   comp.set(j+1,temp);
                }
            }
    }
  public ArrayList<Integer> findNearest(int x1,int y1,ArrayList <component>components,int i) {
        ArrayList<Integer> point=new ArrayList<Integer>();
        int MinX=9999;
        int MinY=9999;
        double distMin=99999;
        for(int j=i;j<components.size();j++) {
            int curMinX=components.get(j).minX;
            int curMinY=components.get(j).minY;
            double  dist=Math.sqrt((x1-curMinX)*(x1-curMinX)+(y1-curMinY)*(y1-curMinY));
            if(dist<distMin) {
               MinX=curMinX;
               MinY=curMinY;
               distMin=dist;
            }
        }
        point.add(MinX);
        point.add(MinY);
        return point;
    }
}

public class GraphCreator {

    Graph g;
    ArrayList <component> components = new ArrayList<component>();
    public void removeAdj(Node node1, Node node2, Edge edge) {
        ArrayList<Edge> adjEdgeList1 = node1.getAdjEdgeList();
        ArrayList<Edge> adjEdgeList2 = node2.getAdjEdgeList();
        adjEdgeList1.remove(edge);
        adjEdgeList2.remove(edge);
        ArrayList <Node> curNodes1 = node1.getAdjList();
        ArrayList <Node> curNodes2 = node2.getAdjList();
        curNodes1.remove(node2);
        curNodes2.remove(node1);
        node1.setAdjList(curNodes1);
        node2.setAdjList(curNodes2);
        node1.setAdjEdgeList(adjEdgeList1);
        node2.setAdjEdgeList(adjEdgeList2);
        HashMap <Integer,Node> nodes = g.getNodes();
        nodes.put(node1.getNodeID(), node1);
        nodes.put(node2.getNodeID(), node2);
        g.setNodes(nodes);
    }

    public void connect(int noEdges) {
        ArrayList <Node> allNodes = new ArrayList<Node>();
        HashMap <Integer,Node> nodes = g.getNodes();
        for (Map.Entry pairs : nodes.entrySet()) {
            Node n = (Node) pairs.getValue();
            allNodes.add(n);
        }
        Node source, first;
        while (!allNodes.isEmpty()) {
            source = allNodes.get(0);
            component c = new component();
            int minx, miny, maxx, maxy;
            LinkedList<Node> compNodes = new LinkedList<Node>();
            ArrayList<Node> cNodes = new ArrayList<Node>();
            cNodes.add(source);
            minx = (int) source.getX();
            miny = (int) source.getY();
            maxx = minx;
            maxy = miny;
            compNodes.add(source);
            while (!compNodes.isEmpty()) {
                first = compNodes.getFirst();
                compNodes.remove(first);
                allNodes.remove(first);
                c.setComponent(minx, miny, maxx, maxy, cNodes);
                components.add(c);
            }
        }
        component.sort(components);
        for (int i=1; i<components.size(); i++) {
            component c1 = components.get(i-1);
            int x1=c1.maxX;
            int y1=c1.maxY;
            ArrayList<Integer> point=c1.findNearest(x1, y1, components,i);
            int minx = point.get(0);
            int miny = point.get(1);
            int n1 = g.getNodeID(minx, miny);
            int n2 = g.getNodeID(x1, y1);
            Node N1 = nodes.get(n1);
            Node N2 = nodes.get(n2);
            ArrayList<Edge> adjEdgeList_N1 = N1.getAdjEdgeList();
            ArrayList<Node> adjList_N1 = N1.getAdjList();
            adjList_N1.add(N2);
            Edge e = new Edge(n1, n2, noEdges);
            adjEdgeList_N1.add(e);
            ArrayList<Edge> adjEdgeList_N2 = N2.getAdjEdgeList();
            ArrayList<Node> adjList_N2 = N2.getAdjList();
            adjList_N2.add(N1);
            adjEdgeList_N2.add(e);
            nodes.put(n2, N2);
            nodes.put(n1, N1);
            g.setNodes(nodes);
            noEdges++;
            if (!g.getEdges().containsValue(e))
                g.createEdge(e);
        }
    }
    public void create(String density) {
        g = new Graph();
        HashMap <ArrayList<Double>,Node> mapping = new HashMap <ArrayList<Double>,Node>();
        Dimension panelSize = Toolkit.getDefaultToolkit().getScreenSize();
        int maxX = panelSize.width - 100;
        int maxY = panelSize.height - 300;
        Random r = new Random();
        int gridSize = 20, id = 0;
        for (int i=40; i<maxX; i+=gridSize) {
            for (int j=40; j<maxY; j+=gridSize) {
                id++;
                ArrayList<Double> pos = new ArrayList<Double>();
                pos.add((double)i);
                pos.add((double)j);
                int rx = i;
                int ry = j;
                Node newNode = new Node(rx,ry,id);
                mapping.put(pos, newNode);            
                g.setNode(newNode);
            }
        }
        id = 0;
        for (int i=60; i<maxX; i+=gridSize) {
            for (int j=40; j<maxY; j+=gridSize) {
                id++;
                ArrayList<Double> pos = new ArrayList<Double>();
                pos.add((double)i-gridSize);
                pos.add((double)j);
                int nodeid1 = mapping.get(pos).getNodeID();
                pos = new ArrayList<Double>();
                pos.add((double)i);
                pos.add((double)j);
                int nodeid2 = mapping.get(pos).getNodeID();
                Edge newEdge = new Edge(nodeid1,nodeid2,id);
                Node n1 = g.getNode(nodeid1);
                Node n2 = g.getNode(nodeid2);
                ArrayList <Node> adjList1 = n1.getAdjList();
                ArrayList <Node> adjList2 = n2.getAdjList();
                adjList1.add(n2);
                adjList2.add(n1);
                n1.setAdjList(adjList1);
                n2.setAdjList(adjList2);
                ArrayList <Edge> adjEdgeList1 = n1.getAdjEdgeList();
                ArrayList <Edge> adjEdgeList2 = n2.getAdjEdgeList();
                adjEdgeList1.add(newEdge);
                adjEdgeList2.add(newEdge);
                n1.setAdjEdgeList(adjEdgeList1);
                n2.setAdjEdgeList(adjEdgeList2);
                g.setNode(n1);
                g.setNode(n2);
                if (!g.getEdges().containsValue(newEdge))
                    g.createEdge(newEdge);
            }
        }
        for (int i=40; i<maxX; i+=gridSize) {
            for (int j=60; j<maxY; j+=gridSize) {
                id++;
                ArrayList<Double> pos = new ArrayList<Double>();
                pos.add((double)i);
                pos.add((double)j-gridSize);
                
                int nodeid1 = mapping.get(pos).getNodeID();
                pos = new ArrayList<Double>();
                pos.add((double)i);
                pos.add((double)j);
                
                int nodeid2 = mapping.get(pos).getNodeID();
                Edge newEdge = new Edge(nodeid1,nodeid2,id);
                Node n1 = g.getNode(nodeid1);
                Node n2 = g.getNode(nodeid2);
                ArrayList <Node> adjList1 = n1.getAdjList();
                ArrayList <Node> adjList2 = n2.getAdjList();
                adjList1.add(n2);
                adjList2.add(n1);
                n1.setAdjList(adjList1);
                n2.setAdjList(adjList2);
                ArrayList <Edge> adjEdgeList1 = n1.getAdjEdgeList();
                ArrayList <Edge> adjEdgeList2 = n2.getAdjEdgeList();
                adjEdgeList1.add(newEdge);
                adjEdgeList2.add(newEdge);
                n1.setAdjEdgeList(adjEdgeList1);
                n2.setAdjEdgeList(adjEdgeList2);
                g.setNode(n1);
                g.setNode(n2);
                if (!g.getEdges().containsValue(newEdge))
                    g.createEdge(newEdge);
            }
        }
        for (int i=60; i<maxX; i+=gridSize) {
            for (int j=60; j<maxY; j+=gridSize) {
                id++;
                ArrayList<Double> pos = new ArrayList<Double>();
                pos.add((double)i-gridSize);
                pos.add((double)j-gridSize);
                
                int nodeid1 = mapping.get(pos).getNodeID();
                pos = new ArrayList<Double>();
                pos.add((double)i);
                pos.add((double)j);
                
                int nodeid2 = mapping.get(pos).getNodeID();
                Edge newEdge = new Edge(nodeid1,nodeid2,id);
                Node n1 = g.getNode(nodeid1);
                Node n2 = g.getNode(nodeid2);
                ArrayList <Node> adjList1 = n1.getAdjList();
                ArrayList <Node> adjList2 = n2.getAdjList();
                adjList1.add(n2);
                adjList2.add(n1);
                n1.setAdjList(adjList1);
                n2.setAdjList(adjList2);
                ArrayList <Edge> adjEdgeList1 = n1.getAdjEdgeList();
                ArrayList <Edge> adjEdgeList2 = n2.getAdjEdgeList();
                adjEdgeList1.add(newEdge);
                adjEdgeList2.add(newEdge);
                n1.setAdjEdgeList(adjEdgeList1);
                n2.setAdjEdgeList(adjEdgeList2);
                g.setNode(n1);
                g.setNode(n2);
                if (!g.getEdges().containsValue(newEdge))
                    g.createEdge(newEdge);
            }
        }
        int percent = 200;
        if (density.equals("Sparse")) {
            percent = 300;
        }
        int noEdges = id;
        int counter = (int) (percent * noEdges / 100.0);
        int i = 1;
        ArrayList <Integer> edgeIDs = new ArrayList<Integer>();
        HashMap <Integer, Node> nodes = g.getNodes();
        while (i <= counter) {
            Random rand = new Random();
            int randomEdge = rand.nextInt(noEdges) + 1;
            edgeIDs.add(randomEdge);
            i++;
        }
        i = 0;
        HashMap <Integer,Edge> edges = g.getEdges();
        while (i<counter) {
            int randomEdge = edgeIDs.get(i);
            Edge tempEdge = edges.get(randomEdge);
            if (tempEdge==null) {
                i++;
                continue;
            }
            Edge curEdge = edges.remove(randomEdge);
            if (curEdge == null) {
                i++;
                continue;
            }
            int nodeID1 = curEdge.getNodeID1();
            int nodeID2 = curEdge.getNodeID2();
            Node n1 = nodes.get(nodeID1);
            Node n2 = nodes.get(nodeID2);
            removeAdj(n1,n2,curEdge);
            i++;
        }
        nodes = g.getNodes();
        ArrayList <Integer> NodeIDs = new ArrayList<Integer>();
        for (Map.Entry pairs : nodes.entrySet()) {
            Node n = (Node) pairs.getValue();
            if (n.getAdjEdgeList().isEmpty() || n.getAdjList().isEmpty()) {
                NodeIDs.add(n.getNodeID());
            }
        }
        for (i=0 ; i<NodeIDs.size(); i++)
            nodes.remove(NodeIDs.get(i));
        g.setNodes(nodes);
        g.setEdges(edges);
        connect(noEdges);
        Graph.setInstance(g);
    }
}
