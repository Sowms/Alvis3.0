package alvis301;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JOptionPane;

public class BFHS extends Algorithm {

    public ArrayList<ArrayList<Node>> open;
    public ArrayList<ArrayList<Node>> closed;

    public double bound;
    private Node goal;

    public BFHS(int type) {
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

        int addedChildren = 0;
        for (Node n : adjList) {
            ArrayList<Node> nodePair = new ArrayList<Node>();

            double current_gn = ((DCNodeData)parentNode.getData()).getGValue() + computeDistance(n, parentNode);
            double current_hn = computeDistance(n, goal);

            double current_fn = current_gn + current_hn;
            //  System.out.println("f:" + current_fn + " bound:" + bound + "\n");

            DCNodeData data = new DCNodeData();
            data.setGValue(current_gn);
            data.setHValue(current_hn);
            data.setFValue(current_fn);

            n.setData(data);

            nodePair.add(n);
            nodePair.add(parentNode);
            if (current_fn <= bound) {
                children.add(nodePair);
                //   addedChildren++;
            }
        }

        ArrayList<ArrayList<Node>> children2 = new ArrayList<ArrayList<Node>>();
        for (ArrayList<Node> i : children) {
            int flag = 0;
            for (ArrayList<Node> j : children2) {
                if (i.get(0).getNodeID() == j.get(0).getNodeID()) {
                    flag = 1;
                }
            }
            if (flag == 0) {
                children2.add(i);

                //System.out.println("Corrected Adding in children2 child: "
                //              + i.get(0).getNodeID() + " Parent:"
                //            + i.get(1).getNodeID());
            }

        }
        return children2;
    }

    double computeDistance(Node a, Node b) {
        double s_x = a.getX();
        double s_y = a.getY();

        double g_x = b.getX();
        double g_y = b.getY();

        double x_diff = Math.abs(g_x - s_x);
        double y_diff = Math.abs(g_y - s_y);
        double dist = Math.sqrt(x_diff * x_diff + y_diff * y_diff);
        //double dist = x_diff + y_diff;
        return dist;
    }

    double beamSearch() {
        double beamBound = 0.0;
        double beamBoundNext=0.0;
        int bw = 4;
        ArrayList<ArrayList<Node>> bigopen = new ArrayList<ArrayList<Node>>();
        ArrayList<Node> first;
        // Set g value of root to 0
        DCNodeData nd = new DCNodeData();
        nd.setGValue(0);
        g.getNode(g.getStartID()).setData(nd);

        first = new ArrayList<Node>();
        first.add(g.getNode(g.getStartID()));
        first.add(null);
        open = new ArrayList<ArrayList<Node>>();
        ArrayList<ArrayList<Node>> oneLevel = new ArrayList<ArrayList<Node>>();
        closed = new ArrayList<ArrayList<Node>>();
        bigopen.add(first);
        while (!bigopen.isEmpty()) {
            open.addAll(bigopen);
            bigopen.clear();
            while (!open.isEmpty()) {
                ArrayList<Node> nodePair = open.remove(0);

                Node node = nodePair.get(0);
                beamBoundNext= ((DCNodeData)node.getData()).getGValue();
                //System.out.println("G:"+beamBound);
                if (goalTest(node)) {
                    beamBound = ((DCNodeData)node.getData()).getGValue();
                    beamBound+=200;
                    return beamBound;
                }
                closed.add(nodePair);
                //node.setState(alvis301.State.closed);
                Node aNode = nodePair.get(1);
                if (aNode != null) {
                    ArrayList<Edge> adjEdgeList = node.getAdjEdgeList();
                    for (Edge e : adjEdgeList) {
                        Integer nodeID1 = e.getNodeID1();
                        Integer nodeID2 = e.getNodeID2();
                        if ((nodeID1.equals(aNode.getNodeID()))
                                || (nodeID2.equals(aNode.getNodeID()))) {
                            // e.setState(alvis301.State.closed);
                            //updateEdge(e);
                            break;
                        }
                    }
                }
                //updateNode(node);
                display();
                ArrayList<ArrayList<Node>> children = moveGenBeamSearch(node);
                ArrayList<ArrayList<Node>> noLoops = removeSeen(children);

                // For all children in noloops set the g f and h values
                for (ArrayList<Node> open1 : noLoops) {

                    double hvalue, gvalue, fvalue; // Euclidean Distance
                    // heuristic

                    Node currNode = open1.get(0);
                    Node parentNode = open1.get(1);
                    Node LocalGoal = g.getNode(g.getGoalID());

                    gvalue = ((DCNodeData)parentNode.getData()).getGValue()
                            + computeDistance(currNode, parentNode);
                    hvalue = computeDistance(currNode, LocalGoal);
                    fvalue = gvalue + hvalue;

                    DCNodeData nd1 = new DCNodeData(); // Creating Node Data
                    nd1.setHValue(hvalue);
                    nd1.setGValue(gvalue);
                    nd1.setFValue(fvalue);
                    currNode.setData(nd1); // Setting newly created data to the
                    // node

                    // System.out.println("In noloops: " + fvalue);
                }
                // /////////////////////////////////////

                for (ArrayList<Node> open1 : children) {
                    //System.out.println("In children: "+ open1.get(0).getData().getFVAlue());
                }

                //If two nodes on same levl generate same child on next level
                for (ArrayList<Node> i : noLoops) {
                    int flag = 0;
                    for (ArrayList<Node> j : oneLevel) {

                        if (i.get(0).getNodeID() == j.get(0).getNodeID()) {
                            flag = 1;
                        }

                    }
                    if (flag == 0) {
                        oneLevel.add(i);
                    }

                }

                /* System.out.println("");
                 for (ArrayList<Node> z : oneLevel) {
                 System.out.print(z.get(0).getNodeID() + "--");
                 }
                 System.out.println("");
                 */
                for (ArrayList<Node> open1 : oneLevel) {
                    Node nParent = open1.get(1);
                    if (nParent != null) {
                        ArrayList<Edge> adjEdgeList = nParent.getAdjEdgeList();
                        for (Edge e : adjEdgeList) {
                            int nodeID1 = e.getNodeID1();
                            int nodeID2 = e.getNodeID2();
                            if ((nodeID1 == open1.get(0).getNodeID())
                                    || nodeID2 == open1.get(0).getNodeID()) {
                               // e.setState(alvis301.State.open);
                                // updateEdge(e);
                                Node n = open1.get(0);
                               // n.setState(alvis301.State.open);
                                // updateNode(n);
                                break;
                            }
                        }
                    }
                }

            }

            // Selecting Best from oneLevel
            // Sorting
           /* System.out.println("Before Sorting");
             for (ArrayList<Node> x : oneLevel) {

             System.out.print(x.get(0).getData().getFVAlue() + " -- ");
             }*/
            Collections.sort(oneLevel, new MyComparator());
            /*
             System.out.println("");
             System.out.println("After Sorting");
             for (ArrayList<Node> x : oneLevel) {

             System.out.print(x.get(0).getData().getFVAlue() + " -- ");
             }

             System.out.println("");
             // Selecting best nodes
             */
            int k = 0;
            ArrayList<ArrayList<Node>> best = new ArrayList<ArrayList<Node>>();
            for (ArrayList<Node> x : oneLevel) {
                k++;
                if (k <= bw) {
                    best.add(x);

                } else {

                    // Remove from open the excess nodes
                    Node nParent = x.get(1);
                    if (nParent != null) {
                        ArrayList<Edge> adjEdgeList = nParent.getAdjEdgeList();
                        for (Edge e : adjEdgeList) {
                            int nodeID1 = e.getNodeID1();
                            int nodeID2 = e.getNodeID2();
                            if ((nodeID1 == x.get(0).getNodeID())
                                    || nodeID2 == x.get(0).getNodeID()) {
                                //e.setState(alvis301.State.unvisited);
                                //updateEdge(e);
                                Node n = x.get(0);
                                //n.setState(alvis301.State.unvisited);
                                //updateNode(n);
                                display();
                                break;
                            }
                        }
                    }

                }

            }

            oneLevel.clear();
            open.clear();
            bigopen.addAll(best);
            best.clear();
        }
        beamBoundNext*=1000;
        return beamBoundNext;
    }

    @Override
    public void run() {
        //finding bound using beamsearch
        bound = beamSearch();
        System.out.println("BOUND:"+bound);
        goal = g.getNode(g.getGoalID());

        ArrayList<Node> first;
        first = new ArrayList<Node>();
        Node start = g.getNode(g.getStartID());

        first.add(start);
        first.add(null);

        //putting bound using heuristic of euclidean distance
        double s_x = start.getX();
        double s_y = start.getY();

        double g_x = goal.getX();
        double g_y = goal.getY();

       // System.out.println("\ns x:"+s_x+" s y:"+s_y+"\n");
        // System.out.println("g x:"+g_x+" g y:"+g_y+"\n");
        double x_diff = Math.abs(g_x - s_x);
        double y_diff = Math.abs(g_y - s_y);
        double dist = Math.sqrt(x_diff * x_diff + y_diff * y_diff);
        //double dist=x_diff+y_diff;
        //bound = x_diff + y_diff;
        //bound *= 2.5;
        //System.out.println("h(goal):"+bound+"\n");
        DCNodeData gdata = new DCNodeData();
        gdata.setGValue(0);
        gdata.setHValue(dist);
        gdata.setFValue(dist);

        start.setData(gdata);

        open = new ArrayList<ArrayList<Node>>();
        closed = new ArrayList<ArrayList<Node>>();
        open.add(first);
        while (!open.isEmpty()) {
            ArrayList<Node> nodePair = open.remove(0);
            Node node = nodePair.get(0);
            if (goalTest(node)) {
                System.out.println("FINAL DISTANCE FOUND:"+((DCNodeData)node.getData()).getGValue());
                ReconstructPath(nodePair);
                 start.setState(alvis301.State.start);
                goal.setState(alvis301.State.goal);
                
                display();
                String stmt="Goal Found...!!";
                stmt+="\nSize of OPEN:"+open.size();
                stmt+="\nSize of CLOSED:"+closed.size();
               
                
                
                JOptionPane.showMessageDialog(null,stmt, "Information",JOptionPane.INFORMATION_MESSAGE);
                
                return;
            }
            closed.add(nodePair);
            node.setState(alvis301.State.closed);
            Node aNode = nodePair.get(1);
            if (aNode != null) {
                ArrayList<Edge> adjEdgeList = node.getAdjEdgeList();
                for (Edge e : adjEdgeList) {
                    Integer nodeID1 = e.getNodeID1();
                    Integer nodeID2 = e.getNodeID2();
                    if ((nodeID1.equals(aNode.getNodeID())) || (nodeID2.equals(aNode.getNodeID()))) {
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

            Collections.sort(open, new MyComparator());

            for (ArrayList<Node> open1 : open) {
                Node nParent = open1.get(1);
                if (nParent != null) {
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

        if (nodes.isEmpty()) {
            return nodes;
        }
        Node n = nodes.get(0).get(0);
        if (OccursIn(n, open) || OccursIn(n, closed)) {
            ArrayList<ArrayList<Node>> newList = new ArrayList<ArrayList<Node>>();
            newList.addAll(nodes);
            newList.remove(nodes.get(0));
            return removeSeen(newList);
        } else {
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

        if (nodeList.isEmpty()) {
            return false;
        }
        if (n.equals(nodeList.get(0).get(0))) {
            return true;
        }
        ArrayList<ArrayList<Node>> newList;
        newList = new ArrayList<ArrayList<Node>>();
        newList.addAll(nodeList);
        newList.remove(nodeList.get(0));
        return OccursIn(n, newList);
    }

    private void ReconstructPath(ArrayList<Node> nodePair) {

        Node node = nodePair.get(0);
        node.setState(alvis301.State.path);
        updateNode(node);
        Node parent = nodePair.get(1);
        Node child = node;
        while (parent != null) {
            parent.setState(alvis301.State.path);
            ArrayList<Edge> adjEdgeList = parent.getAdjEdgeList();
            for (Edge e : adjEdgeList) {
                int node1 = e.getNodeID1();
                int node2 = e.getNodeID2();
                if (node1 == child.getNodeID() || node2 == child.getNodeID()) {
                    e.setState(alvis301.State.path);
                    updateEdge(e);
                    break;
                }
            }
            updateNode(parent);
            ArrayList<Node> nodePair1 = findLink(parent, closed);
            child = parent;
            parent = nodePair1.get(1);
        }
    }

    private ArrayList<Node> findLink(Node parent, ArrayList<ArrayList<Node>> nodes) {
        if (parent.equals(nodes.get(0).get(0))) {
            return nodes.get(0);
        } else {

            ArrayList<ArrayList<Node>> temp = new ArrayList<ArrayList<Node>>();
            temp.addAll(nodes);
            temp.remove(nodes.get(0));
            return findLink(parent, temp);
        }
    }

    private ArrayList<ArrayList<Node>> moveGenBeamSearch(Node parentNode) {
       	ArrayList<Node> adjList = parentNode.getAdjList();
		ArrayList<ArrayList<Node>> children = new ArrayList<ArrayList<Node>>();
		for (Node n : adjList) {
			ArrayList<Node> nodePair = new ArrayList<Node>();
			nodePair.add(n);
			nodePair.add(parentNode);
			
			children.add(nodePair);
		}
		// Error correction

		ArrayList<ArrayList<Node>> children2 = new ArrayList<ArrayList<Node>>();
		for (ArrayList<Node> i : children) {
			int flag = 0;
			for (ArrayList<Node> j : children2) {
				if (i.get(0).getNodeID() == j.get(0).getNodeID())
					flag = 1;
			}
			if (flag == 0) {
				children2.add(i);
			}

		}

		return children2;
    }

}


