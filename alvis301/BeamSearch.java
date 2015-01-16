package alvis301;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JOptionPane;

public class BeamSearch extends Algorithm {

	public ArrayList<ArrayList<Node>> open;
	public ArrayList<ArrayList<Node>> closed;

	public BeamSearch(int type) {
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
			nodePair.add(n);
			nodePair.add(parentNode);
			System.out.println("Adding in children child: "
					+ nodePair.get(0).getNodeID() + " Parent:"
					+ nodePair.get(1).getNodeID());
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

				System.out.println("Corrected Adding in children2 child: "
						+ i.get(0).getNodeID() + " Parent:"
						+ i.get(1).getNodeID());
			}

		}

		return children2;

	}

	@Override
	public void run() {

		int bw = Integer.parseInt(JOptionPane.showInputDialog( "Enter the beam Width", null));
		ArrayList<ArrayList<Node>> bigopen = new ArrayList<ArrayList<Node>>();

		ArrayList<Node> first;

		// Set g value of root to 0
		DCNodeData nd = new DCNodeData();
		nd.setLevel(1);
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
				if (goalTest(node)) {

					ReconstructPath(nodePair);
					
					System.out.println("No. of nodes in Open:"+open.size());
					System.out.println("No. of nodes in Closed:"+closed.size());
					
					
					
					display();
					JOptionPane.showMessageDialog(null,"No of nodes in closed: " +closed.size()+"\nNo. of nodes in open: "+open.size());
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
						if ((nodeID1.equals(aNode.getNodeID()))
								|| (nodeID2.equals(aNode.getNodeID()))) {
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

				// For all children in noloops set the g f and h values

				for (ArrayList<Node> open1 : noLoops) {

					double hvalue, gvalue, fvalue; // Euclidean Distance
													// heuristic

					Node currNode = open1.get(0);
					Node parentNode = open1.get(1);
					Node goal = g.getNode(g.getGoalID());

					gvalue = ((DCNodeData)parentNode.getData()).getGValue()
							+ computeDistance(currNode, parentNode);
					hvalue = computeDistance(currNode, goal);
					fvalue = gvalue + hvalue;

					DCNodeData nd1 = new DCNodeData(); // Creating Node Data
					nd1.setHValue(hvalue);
					nd1.setGValue(gvalue);
					nd1.setFValue(fvalue);
					currNode.setData(nd1); // Setting newly created data to the
											// node

					System.out.println("In noloops: " + fvalue);
				}
				// /////////////////////////////////////

				for (ArrayList<Node> open1 : children) {
					System.out.println("In children: "
							+ ((DCNodeData)open1.get(0).getData()).getFVAlue());
				}

				
				
				//If two nodes on same levl generate same child on next level
				for (ArrayList<Node> i : noLoops) {
					int flag=0;
					for (ArrayList<Node> j : oneLevel) {
						
						if(i.get(0).getNodeID()==j.get(0).getNodeID())
							flag=1;
							
					}
					if(flag==0)
						oneLevel.add(i);
						
				}
				
				System.out.println("");
				for (ArrayList<Node> z : oneLevel) {
					System.out.print(z.get(0).getNodeID()+"--");
				}
				System.out.println("");
				
				for (ArrayList<Node> open1 : oneLevel) {
					Node nParent = open1.get(1);
					if (nParent != null) {
						ArrayList<Edge> adjEdgeList = nParent.getAdjEdgeList();
						for (Edge e : adjEdgeList) {
							int nodeID1 = e.getNodeID1();
							int nodeID2 = e.getNodeID2();
							if ((nodeID1 == open1.get(0).getNodeID())
									|| nodeID2 == open1.get(0).getNodeID()) {
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

			// Selecting Best from oneLevel

			// Sorting
			System.out.println("Before Sorting");
			for (ArrayList<Node> x : oneLevel) {

				System.out.print(((DCNodeData)x.get(0).getData()).getFVAlue() + " -- ");
			}
			Collections.sort(oneLevel, new MyComparator());

			System.out.println("");
			System.out.println("After Sorting");
			for (ArrayList<Node> x : oneLevel) {

				System.out.print(((DCNodeData)x.get(0).getData()).getFVAlue() + " -- ");
			}

			System.out.println("");
			// Selecting best nodes
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
								e.setState(alvis301.State.unvisited);
								updateEdge(e);
								Node n = x.get(0);
								n.setState(alvis301.State.unvisited);
								updateNode(n);
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
	}

	public double computeDistance(Node n1, Node n2) {
		double n1_x = n1.getX();
		double n1_y = n1.getY();

		double n2_x = n2.getX();
		double n2_y = n2.getY();

		double x_diff = Math.abs(n2_x - n1_x);
		double y_diff = Math.abs(n2_y - n1_y);
		return Math.sqrt(x_diff * x_diff + y_diff * y_diff);

	}

	private ArrayList<ArrayList<Node>> removeSeen(
			ArrayList<ArrayList<Node>> nodes) {

		if (nodes.isEmpty())
			return nodes;
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

		if (nodeList.isEmpty())
			return false;
		if (n.equals(nodeList.get(0).get(0)))
			return true;
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

	private ArrayList<Node> findLink(Node parent,
			ArrayList<ArrayList<Node>> nodes) {
		if (parent.equals(nodes.get(0).get(0)))
			return nodes.get(0);
		else {

			ArrayList<ArrayList<Node>> temp = new ArrayList<ArrayList<Node>>();
			temp.addAll(nodes);
			temp.remove(nodes.get(0));
			return findLink(parent, temp);
		}
	}
}

class MyComparator implements Comparator<ArrayList<Node>> {

	@Override
	public int compare(ArrayList<Node> arg0, ArrayList<Node> arg1) {
		// System.out.println(arg0.get(0).getData().getFValue()+"--"+
		// arg1.get(0).getData().getFValue());
		if (((DCNodeData)arg0.get(0).getData()).getFVAlue() > ((DCNodeData)arg1.get(0).getData())
				.getFVAlue()) {
			return 1;
		}

		return -1;

	}
}