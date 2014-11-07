
package alvis301;

import java.util.ArrayList;

public class BFSAlgorithm extends Algorithm {

    public ArrayList<ArrayList<Node>> open;
    public ArrayList<ArrayList<Node>> closed;
    
    public BFSAlgorithm(int type) {
        super(type);
    }
    @Override
    public boolean goalTest(Node goalNode) {
        
        int nodeID = goalNode.getNodeID();
        return (nodeID == g.getGoalID());
    }

    @Override
    public ArrayList<ArrayList<Node>> moveGen(Node parentNode) {
        
        ArrayList<Node> adjList = parentNode.getAdjList();
        ArrayList<ArrayList<Node>> children = new ArrayList<ArrayList<Node>>();
        for (Node n : adjList) {
            ArrayList<Node> nodePair  = new ArrayList<Node>();
            nodePair.add(n);
            nodePair.add(parentNode);
            children.add(nodePair);
        }
        return children;
    }

    @Override
    public void run() {
        ArrayList<Node> first;
        first = new ArrayList<Node>();
        first.add(g.getNode(g.getStartID()));
        first.add(null);
        open = new ArrayList<ArrayList<Node>>();
        closed = new ArrayList<ArrayList<Node>>();
        open.add(first);
        while (!open.isEmpty()) {
            ArrayList <Node> nodePair = open.remove(0);
            Node node = nodePair.get(0);
            if (goalTest(node)) {
             
                ReconstructPath(nodePair);
                display();
                return;
            }
            closed.add(nodePair);
            node.setState(alvis301.State.closed);
            Node aNode = nodePair.get(1);
            if (aNode!=null) {
            ArrayList<Edge> adjEdgeList = node.getAdjEdgeList();
            for (Edge e : adjEdgeList) {
                Integer nodeID1 = e.getNodeID1();
                Integer nodeID2 = e.getNodeID2();
                if((nodeID1.equals(aNode.getNodeID())) || (nodeID2.equals(aNode.getNodeID()))) {
                    e.setState(alvis301.State.closed);
                    updateEdge(e);
                    break;
                }
            }
            }
            updateNode(node);
            display();
            ArrayList<ArrayList<Node>> children = moveGen(node);
            ArrayList<ArrayList<Node>> noLoops = removeSeen(children);
            open.addAll(noLoops);
            for (ArrayList<Node> open1 : open) {
                Node nParent = open1.get(1);
                if (nParent!=null) {
                    ArrayList<Edge> adjEdgeList = nParent.getAdjEdgeList();
                    for (Edge e : adjEdgeList) {
                        int nodeID1 = e.getNodeID1();
                        int nodeID2 = e.getNodeID2();
                        if ((nodeID1 == open1.get(0).getNodeID()) || nodeID2 == open1.get(0).getNodeID()) {
                            e.setState(alvis301.State.open);
                            updateEdge(e);
                            Node n = open1.get(0);
                            n.setState(alvis301.State.open);
                            updateNode(n);
                            break;
                        }
                    }
                }
            }
        }            
    }

    private ArrayList<ArrayList<Node>> removeSeen(ArrayList<ArrayList<Node>> nodes) {
        
        if(nodes.isEmpty())
            return nodes;
        Node n = nodes.get(0).get(0);
        if (OccursIn(n,open) || OccursIn(n,closed)) {
            ArrayList<ArrayList<Node>> newList = new ArrayList<ArrayList<Node>>();
            newList.addAll(nodes);
            newList.remove(nodes.get(0));
            return removeSeen(newList);
        }
        else {
            ArrayList<ArrayList<Node>> newList = new ArrayList<ArrayList<Node>>();
            ArrayList<ArrayList<Node>> tailList = new ArrayList<ArrayList<Node>>();
            tailList.addAll(nodes);
            tailList.remove(nodes.get(0));
            newList.add(nodes.get(0));
            newList.addAll(removeSeen(tailList));
            return newList;
        }
    }

    private boolean OccursIn(Node n, ArrayList<ArrayList<Node>> nodeList) {
    
        if(nodeList.isEmpty())
            return false;
        if (n.equals(nodeList.get(0).get(0)))
            return true;
        ArrayList<ArrayList<Node>> newList;
        newList = new ArrayList<ArrayList<Node>>();
        newList.addAll(nodeList);
        newList.remove(nodeList.get(0));
        return OccursIn(n,newList);
    }

    private void ReconstructPath(ArrayList<Node> nodePair) {
       
        Node node = nodePair.get(0);
        node.setState(alvis301.State.path);
        updateNode(node);
        Node parent = nodePair.get(1);
        Node child = node;
        while(parent!=null) {
            parent.setState(alvis301.State.path);
            ArrayList<Edge> adjEdgeList = parent.getAdjEdgeList();
            for (Edge e : adjEdgeList) {
                int node1 = e.getNodeID1();
                int node2 = e.getNodeID2();
                if (node1==child.getNodeID() || node2==child.getNodeID()) {
                    e.setState(alvis301.State.path);
                    updateEdge(e);
                    break;
                }
            }
            updateNode(parent);
            ArrayList<Node> nodePair1=findLink(parent,closed);
            child = parent; 
            parent=nodePair1.get(1); 
        }
    }
    private ArrayList<Node> findLink(Node parent, ArrayList<ArrayList<Node>> nodes)
    {
         if(parent.equals(nodes.get(0).get(0)))
            return nodes.get(0);
         else  
         { 
             
            ArrayList<ArrayList<Node>> temp=new ArrayList<ArrayList<Node>>();
            temp.addAll(nodes);
            temp.remove(nodes.get(0));
            return findLink(parent,temp);
         }  
    }
}
