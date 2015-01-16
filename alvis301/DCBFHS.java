package alvis301;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import javax.swing.JOptionPane;

public class DCBFHS extends Algorithm {

    public ArrayList<ArrayList<Node>> open;
    public ArrayList<ArrayList<Node>> closedBeamSearch;
    public ArrayList<Node> boundary;
    public ArrayList<Node> relay;

    public double bound;
    private Node goal;

    public DCBFHS(int type) {
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
            ArrayList<Node> nodePair = new ArrayList<Node>();

            double current_gn = ((DCNodeData)parentNode.getData()).getGValue() + computeDistance(n, parentNode);
            double current_hn = computeDistance(n, goal);

            double current_fn = current_gn + current_hn;
            //  System.out.println("f:" + current_fn + " bound:" + bound + "\n");

            if (current_fn > bound) {
//                System.out.println("movegen_fn:"+current_fn);
                continue;
            }

            DCNodeData data = (DCNodeData) n.getData();
            if (data == null) {
                data = new DCNodeData();
                data.setGValue(current_gn);
                data.setHValue(current_hn);
                data.setFValue(current_fn);
                if (data.tabuListNirav == null) {
                    data.tabuListNirav = new ArrayList<Node>();
                }
                data.tabuListNirav.add(parentNode);
                n.setData(data);
            } else {
                data.setGValue(current_gn);
                data.setHValue(current_hn);
                data.setFValue(current_fn);
                if (data.tabuListNirav == null) {
                    data.tabuListNirav = new ArrayList<Node>();
                }
                data.tabuListNirav.add(parentNode);
            }

            nodePair.add(n);
            nodePair.add(parentNode);

            children.add(nodePair);
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
        double beamBoundNext = 0.0;
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
        closedBeamSearch = new ArrayList<ArrayList<Node>>();
        bigopen.add(first);
        while (!bigopen.isEmpty()) {
            open.addAll(bigopen);
            bigopen.clear();
            while (!open.isEmpty()) {
                ArrayList<Node> nodePair = open.remove(0);

                Node node = nodePair.get(0);
                beamBoundNext = ((DCNodeData) node.getData()).getGValue();
                //System.out.println("G:"+beamBound);
                if (goalTest(node)) {
                    beamBound = ((DCNodeData)node.getData()).getGValue();
                    beamBound+=200;
                    return beamBound;
                }
                closedBeamSearch.add(nodePair);
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
        return beamBoundNext*1000;
    }

    @Override
    public void run() {
        //finding bound using beamsearch
        bound = beamSearch();
        System.out.println("BOUND:" + bound);
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
        gdata.tabuListNirav = new ArrayList<Node>();
        start.setData(gdata);

        int currentRelayNode = start.getNodeID();

        open = new ArrayList<ArrayList<Node>>();
        boundary = new ArrayList<Node>();
        relay = new ArrayList<Node>();

        boolean relayDone = false;
        int flag = 0;
        open.add(first);
        while (!open.isEmpty()) {
//            System.out.println("------------------------------------------------");
            //for (int p = 0; p < 10; p++) {
//            if (flag == 0) {
//                int returnValue = JOptionPane.showConfirmDialog(null, "Select", "Select", JOptionPane.YES_NO_OPTION);
//                if (returnValue == JOptionPane.NO_OPTION) {
//                    flag = 1;
//                }
//            }
            ArrayList<Node> nodePair = open.remove(0);
            Node node = nodePair.get(0);
            DCNodeData data = (DCNodeData) node.getData();

            if (!relayDone) {
                int gap = (int) (data.getHValue() - data.getGValue());
                gap=Math.abs(gap);
                if (gap <= 50) {
                    for (Node boundaryNode : boundary) {
                        relay.add(boundaryNode);
                    }

                    for (Node relayNode : relay) {
                        relayNode.setState(alvis301.State.relay);
//                        System.out.println("Relay:" + relayNode.getNodeID());
                        updateNode(relayNode);
                    }
                    relayDone = true;
//                    System.out.println("RELAY DONE...!");

                    display();
                }
            }

            if (!relayDone) {
                data.setRelay(currentRelayNode);
            } else if (nodePair.get(1) != null) {
                if (((DCNodeData)nodePair.get(1).getData()).getRelay() == start.getNodeID()) {
                    data.setRelay(nodePair.get(1).getNodeID());
                } else {
                    data.setRelay(((DCNodeData)nodePair.get(1).getData()).getRelay());
                }
            }

//            System.out.println("Node Extracted:" + node.getNodeID());
            if (goalTest(node)) {
                System.out.println("FINAL DISTANCE FOUND:" + ((DCNodeData)node.getData()).getGValue());
                int relayGoal = ((DCNodeData)node.getData()).getRelay();
                for (Node relayNode : relay) {
                    if(relayNode.getNodeID()!=relayGoal){
                        relayNode.setState(alvis301.State.old);
                    }
                }
                
                start.setState(alvis301.State.start);
                goal.setState(alvis301.State.goal);
                
                display();
                String stmt="Goal Found...!!";
                stmt+="\nSize of OPEN:"+open.size();
                stmt+="\nSize of Boundary:"+boundary.size();
                stmt+="\nSize of Relay Layer:"+relay.size();
                
                JOptionPane.showMessageDialog(null,stmt, "Information",JOptionPane.INFORMATION_MESSAGE);
                
                return;
            }

//            for (Node boundary1 : boundary) {
//                System.out.println("1Node:" + boundary1.getNodeID() + " Count:" + boundary1.getData().getChildCount());
//            }
            ArrayList<ArrayList<Node>> children = moveGen(node);
//            System.out.println("Children after moveGen:" + children.size());
//            for (int cs = 0; cs < children.size(); cs++) {
//                System.out.println(cs + " ID:" + children.get(cs).get(0).getNodeID());
//            }

            ArrayList<ArrayList<Node>> noLoops2 = removeSeenDCBFHS(children);
         

//            System.out.println("NOLOOP:" + noLoops2.size());
            ArrayList<ArrayList<Node>> noLoops = new ArrayList<ArrayList<Node>>();
            for (ArrayList<Node> noLoop : noLoops2) {
                Node child = noLoop.get(0);
                boolean occurInTabuList = occurInTabuList(node, child);
                if (!occurInTabuList) {
                    noLoops.add(noLoop);
                }
            }
            
               

//            System.out.println("Total Children after REMOVETABU:" + noLoops.size());
//            for (int cs = 0; cs < noLoops.size(); cs++) {
//                System.out.println(cs + " ID:" + noLoops.get(cs).get(0).getNodeID());
//            }
//            
            for (ArrayList<Node> noLoop : noLoops) {
                Node child = noLoop.get(0);
                for (ArrayList<Node> open1 : open) {
                    if (open1.get(0).getNodeID() == child.getNodeID()) {
                        ((DCNodeData)open1.get(0).getData()).tabuListNirav.add(node);
                    }
                }
//                System.out.println("Adding TABU " + node.getNodeID() + " in Child ID:" + child.getNodeID());
                ((DCNodeData)child.getData()).tabuListNirav.add(node);
            }

            int flagChild = 0;
            //System.out.println("CHILD_COUNT:"+noLoops.size());
            ((DCNodeData)node.getData()).setChildCount(noLoops.size());
            if (noLoops.isEmpty()) {
                flagChild = node.getNodeID();
            }
            //System.out.println("IN_BOUNDARY_CHILD_COUNT:"+boundary.get(0).getData().getChildCount());
            boundary.add(node);

            node.setState(alvis301.State.boundary);
            Node aNode = nodePair.get(1);
            if (aNode != null) {
                ArrayList<Edge> adjEdgeList = node.getAdjEdgeList();
                for (Edge e : adjEdgeList) {
                    Integer nodeID1 = e.getNodeID1();
                    Integer nodeID2 = e.getNodeID2();
                    if ((nodeID1.equals(aNode.getNodeID())) || (nodeID2.equals(aNode.getNodeID()))) {
                        e.setState(alvis301.State.boundary);
                        updateEdge(e);
                        break;
                    }
                }
            }
            updateNode(node);
            display();
            
//            if(open.size()==0 && node.getNodeID()!=start.getNodeID())
//            { 
//                open.addAll(children);
//                System.out.println("Size of open is zero."+children.size());
//                System.out.println("bound:"+bound);
//                System.out.println("f:"+node.getData().getFVAlue());
//                
//            }
//            else
//            {    
            open.addAll(noLoops);
//            }
            for (int tr = 0; tr < boundary.size(); tr++) {
//                System.out.println("Node:" + boundary.get(tr).getNodeID() + " Count:" + boundary.get(tr).getData().getChildCount());
                if (nodePair.get(1) == null) {
                    Node startNode = nodePair.get(0);
                    if(startNode.getState()!=alvis301.State.relay)
                        startNode.setState(alvis301.State.deleted);
                    ArrayList<Edge> adjEdgeList = startNode.getAdjEdgeList();
                    for (Edge e : adjEdgeList) {
                        Integer nodeID1 = e.getNodeID1();
                        Integer nodeID2 = e.getNodeID2();
                        if ((nodeID1.equals(startNode.getNodeID())) || (nodeID2.equals(startNode.getNodeID()))) {
                            e.setState(alvis301.State.deleted);
                            updateEdge(e);
                            break;
                        }
                    }
                    break;
                }
                if (boundary.get(tr).getNodeID() == nodePair.get(1).getNodeID()) {
                    ((DCNodeData)boundary.get(tr).getData()).decrementChildCount();
//                    System.out.println("Decrementing child count for node:" + boundary.get(tr).getNodeID() + " to " + boundary.get(tr).getData().getChildCount());
                    //break;
                }
                //System.out.println(i+": NID:"+boundary.get(i).getNodeID());
                //flagChild != boundary.get(tr).getNodeID() &&
                if ( ((DCNodeData)boundary.get(tr).getData()).getChildCount() <= 0) {

                    Node bnode = boundary.get(tr);
//                    System.out.println("Deleting node:" + bnode.getNodeID());
                    if(bnode.getState()!=alvis301.State.relay)
                        bnode.setState(alvis301.State.deleted);
                    if (bnode != null) {
                        ArrayList<Edge> adjEdgeList = bnode.getAdjEdgeList();
                        for (Edge e : adjEdgeList) {
                            Integer nodeID1 = e.getNodeID1();
                            Integer nodeID2 = e.getNodeID2();
                            if ((nodeID1.equals(bnode.getNodeID())) || (nodeID2.equals(bnode.getNodeID()))) {
                                e.setState(alvis301.State.deleted);
                                updateEdge(e);
                                break;
                            }
                        }
                    }
                    boundary.remove(tr);
                }
            }

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

    private boolean occurInTabuList(Node parentNode, Node child) {
        for (Node tabuListNode : ((DCNodeData)parentNode.getData()).tabuListNirav) {
            if (child.getNodeID() == tabuListNode.getNodeID()) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<ArrayList<Node>> removeSeenDCBFHS(ArrayList<ArrayList<Node>> nodes) {

        if (nodes.isEmpty()) {
            return nodes;
        }
        Node n = nodes.get(0).get(0);
        if (OccursIn(n, open) || OccursInBoundary(n, boundary)) {
            ArrayList<ArrayList<Node>> newList = new ArrayList<ArrayList<Node>>();
            newList.addAll(nodes);
            newList.remove(nodes.get(0));
            return removeSeenDCBFHS(newList);
        } else {
            ArrayList<ArrayList<Node>> newList = new ArrayList<ArrayList<Node>>();
            ArrayList<ArrayList<Node>> tailList = new ArrayList<ArrayList<Node>>();
            tailList.addAll(nodes);
            tailList.remove(nodes.get(0));
            newList.add(nodes.get(0));
            newList.addAll(removeSeenDCBFHS(tailList));
            return newList;
        }
    }

    private ArrayList<ArrayList<Node>> removeSeen(ArrayList<ArrayList<Node>> nodes) {

        if (nodes.isEmpty()) {
            return nodes;
        }
        Node n = nodes.get(0).get(0);
        if (OccursIn(n, open) || OccursIn(n, closedBeamSearch)) {
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
                if (i.get(0).getNodeID() == j.get(0).getNodeID()) {
                    flag = 1;
                }
            }
            if (flag == 0) {
                children2.add(i);
            }

        }

        return children2;
    }

    private boolean OccursInBoundary(Node n, ArrayList<Node> boundary) {
        for (Node boundary1 : boundary) {
            if (boundary1.getNodeID() == n.getNodeID()) {
                //System.out.println("RETURNING TRUE FROM OCCURINBOUNDARY");
                return true;
            }
        }
        return false;
    }

}

