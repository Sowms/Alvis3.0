
package alvis301;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SavithaSam
 */
public class Minimax extends Algorithm{

    public Minimax(int t) {
        super(t);
    }

    @Override
    public boolean goalTest(Node goalNode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Node> moveGen(Node parentNode) {
        ArrayList<Node> adjlist;
        GameNodeData gdp=(GameNodeData)parentNode.getData();
        ArrayList<Node> children=new ArrayList<Node>();
        adjlist = parentNode.getAdjList();
        for (Node node1 : adjlist) {
            GameNodeData gd=(GameNodeData)node1.getData();
            if(gd.level>gdp.level){
                children.add(node1);
            }
        }    
        return children;
    }
        
    public int getMinimax(Node j)
    {
        GameNodeData gd = (GameNodeData) j.getData();
        int cv = 0;
        if(testTerminal(j)){
            return gd.value;
        }
        else{    
            ArrayList<Node> children=moveGen(j);
            for (Node child:children){
                int v = getMinimax(child);
                GameNodeData gd1=(GameNodeData) child.getData();
                gd1.value=v;
                child.setData(gd1);
                updateNode(child); 
                if(child.equals(children.get(0)))
                    cv=v;
                else if(j.getState()==alvis301.State.max)
                    cv=(cv>v)?cv:v;
                else
                    cv=(cv<v)?cv:v;
            }
            return cv;
        }
    }
    @Override
    public void run() {
           Node root=g.getNode(g.getStartID());
           GameNodeData gd=(GameNodeData)root.getData();
           gd.value=getMinimax(root);
           System.out.println("a"+gd.value);
           root.setData(gd);
           updateNode(root);
           
           printPath(root);
        try {
            display();
        } catch (InterruptedException ex) {
            Logger.getLogger(Minimax.class.getName()).log(Level.SEVERE, null, ex);
        }
           
    }
    public void printPath(Node root){
        Node cur=root;
        GameNodeData gd=(GameNodeData) cur.getData();
        while(!gd.terminal){
            ArrayList<Node> children=moveGen(cur);
            if(children.isEmpty())
                return;
            Node next = children.get(0);
            gd = (GameNodeData) next.getData();
            if (cur.getState() == alvis301.State.min) {
                        ArrayList<Edge> adjList = cur.getAdjEdgeList();
                        for (Edge e : adjList) {
                            if (children.contains(g.getNode(e.getNodeID1())) || children.contains(g.getNode(e.getNodeID2()))) {
                                e.setState(alvis301.State.path);
                                updateEdge(e);
                            }
                        }    
                    }
            int max = gd.value; Node maxNode = next; int min = gd.value; Node minNode=next;
            for(int i=1;i<children.size();i++){
                //if (cur.getState() == alvis301.State.max) {
                    gd = (GameNodeData) children.get(i).getData();
                    if (gd.value > max) {
                        max = gd.value;
                        maxNode = children.get(i);
                    }
                    if (gd.value < min) {
                        min = gd.value;
                        minNode = children.get(i);
                    }
                //} 
                    
            }
            if (cur.getState() == alvis301.State.max) {
                next = maxNode;
                ArrayList<Edge> adjList = cur.getAdjEdgeList();
                for (Edge e : adjList) {
                    if (e.getNodeID1() == next.getNodeID() || e.getNodeID2() == next.getNodeID()) {
                        e.setState(alvis301.State.path);
                        updateEdge(e);
                        break;
                    }
                }
            }
            else {
                next = minNode;
            }
            cur = next;
            gd=(GameNodeData) cur.getData();
        
        }
    }
    public boolean testTerminal(Node n)
    {
       GameNodeData gd=(GameNodeData)n.getData();
       return gd.terminal;
       
    }
    
    
        
}
