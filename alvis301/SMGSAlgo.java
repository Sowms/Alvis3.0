package alvis301;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;


/**
 * @authors Jyothi,Ayesha
 *
 */

public class SMGSAlgo extends Algorithm{
	public final int INFINITY = 999999;
	public  int MEMORYFULL;

	public ArrayList<Integer> open;
	public  ArrayList<Integer> closed;

	int goalReached = 0;
	int goalID;
	int startID;


	public SMGSAlgo(int type){
		super(type);
	}

	@Override
	public boolean goalTest(Node goalNode) {
		// TODO Auto-generated method stub
		int nodeID = goalNode.getNodeID();
		return (nodeID == g.getGoalID());
	}

	@Override
	public Object moveGen(Node parentNode) {
		ArrayList<Node> adjList = parentNode.getAdjList();
		Map<Node, Node> children  = new HashMap<Node,Node>();
		for (Node n : adjList) {
			children.put(n, parentNode);
		}
		return children;
	}

	@Override
	public void run() {
		String message = "Number of nodes that can be kept in memory";
		message = getInput(message);
		MEMORYFULL = Integer.parseInt(message);	

		/*run SMGS algorithm and get the optimal path*/
                
		ArrayList<Integer> path = runSMGS(g.getNode(g.getStartID()), g.getNode(g.getGoalID()));
		g.getNode(g.getStartID()).setState(alvis301.State.path);
		updateNode(g.getNode(g.getStartID()));

		ArrayList<Edge> adjEdgeList1 =  g.getNode(g.getStartID()).getAdjEdgeList();
		for (Edge e : adjEdgeList1) {
			int node1 = e.getNodeID1();
			int node2 = e.getNodeID2();
			if (node1 == path.get(0) || node2 == path.get(0)) {
				e.setState(alvis301.State.path);
				updateEdge(e);
				display();
				break;
			}
		}


		displayPath(path);

	}

	/**
	 * update the nodes in the path list and edges with "path" state
	 * @param path list of nodes on the path
	 */
	public void displayPath(ArrayList<Integer> path){
		for(int i=0; i<path.size(); i++){
			g.getNode(path.get(i)).setState(alvis301.State.path);
			updateNode(g.getNode(path.get(i)));
			if(i != (path.size()-1)){
				ArrayList<Edge> adjEdgeList =  g.getNode(path.get(i)).getAdjEdgeList();
				for (Edge e : adjEdgeList) {
					int node1 = e.getNodeID1();
					int node2 = e.getNodeID2();
					if (node1== path.get(i+1) || node2== path.get(i+1)) {
						e.setState(alvis301.State.path);
						updateEdge(e);
						display();
					}
				}

			}
		}

		display();

	}

	/**
	 * runSMGS runs the SMGS algorithm on the subgraph with given start and goal nodes. 
	 * @param start start node of the subgraph.
	 * @param goal goal node of the subgraph.
	 * @return optimal path of the subgraph.
	 */
	public ArrayList<Integer> runSMGS(Node start, Node goal){
		if(goalReached != 0){
			for(int i=0; i<7;i++)
				display();
		}
                System.out.println(start.getData());
		ArrayList<Integer> SSP = new ArrayList<Integer>();  

		goalID = goal.getNodeID();
		startID = start.getNodeID();
		open = new ArrayList<Integer>();
		closed = new ArrayList<Integer>();

		SMGSNodeData sdata = (SMGSNodeData) start.getData();
                sdata.setAncestor(null);
		sdata.setGvalue(0);

		open.add(start.getNodeID());

		Node node = null, parent = null;
		while (!open.isEmpty()) {
			Integer nodeId = getBestFvalueNode(); 
			remove(open, nodeId);
			closed.add(nodeId);
			node = g.getNode(nodeId);
			node.setState(alvis301.State.closed);
			if(node.getNodeID() == start.getNodeID())
				parent  = null;
			else
				parent = g.getNode(((SMGSNodeData)node.getData()).getAncestor()); 

			if (parent!=null) {
				ArrayList<Edge> adjEdgeList = node.getAdjEdgeList();
				for (Edge e : adjEdgeList) {
					Integer nodeID1 = e.getNodeID1();
					Integer nodeID2 = e.getNodeID2();
					if((nodeID1.equals(parent.getNodeID())) || (nodeID2.equals(parent.getNodeID()))) {
						e.setState(alvis301.State.closed);
						updateEdge(e);
						display();
						break;
					}
				}
			}

			updateNode(node);
			display();

			/*when goal is reached*/
			if (node.getNodeID() == goal.getNodeID()) {
				goalReached++;

				/*change the state of already visited nodes to "deleted" for clarity except start, goal and relay nodes*/
				for(int i=0; i<closed.size(); i++){
					Integer id = closed.get(i);
					Node node1 = g.getNode(id);
					node1.setState(alvis301.State.deleted);
					updateNode(node1);
				}

				for(int i=0; i<open.size(); i++){
					Integer id = open.get(i);
					Node node1 = g.getNode(id);
					node1.setState(alvis301.State.deleted);
					updateNode(node1);
				}

				display();

				g.getNode(g.getStartID()).setState(alvis301.State.start);
				updateNode(g.getNode(g.getStartID()));

				g.getNode(g.getGoalID()).setState(alvis301.State.goal);
				updateNode(g.getNode(g.getGoalID()));

				display();

				/*construct the path by recursive calls to runSMGS*/
				/*get the sparse path*/
				SSP = extractSparseSolutionPath(node.getNodeID());
				/*construct the dense path*/

				for(int i=0; i<SSP.size(); i++){
					Integer id = SSP.get(i);
					Node node1 = g.getNode(id);
					node1.setState(alvis301.State.path);
					updateNode(node1);
				}
				for(int i=0; i<7; i++)
					display();

				ArrayList<Integer> finalDSP = new ArrayList<Integer>();  
				for(int i=1; i<SSP.size(); i++){
					ArrayList<Integer> DSP = new ArrayList<Integer>(); 
					if(g.getNode(SSP.get(i)).getAdjList().contains(g.getNode(SSP.get(i-1)))){
						DSP.add(SSP.get(i));
					}else{
						DSP.addAll(runSMGS(g.getNode(SSP.get(i-1)), g.getNode(SSP.get(i)))); //check the order
					}

					if(i==1){
						g.getNode(start.getNodeID()).setState(alvis301.State.path);
						updateNode(g.getNode(start.getNodeID()));
						display();

						ArrayList<Edge> adjEdgeList1 =  g.getNode(g.getStartID()).getAdjEdgeList();
						for (Edge e : adjEdgeList1) {
							int node1 = e.getNodeID1();
							int node2 = e.getNodeID2();
							if (node1 == DSP.get(0) || node2 == DSP.get(0)) {
								e.setState(alvis301.State.path);
								updateEdge(e);
								display();
								break;
							}
						}
					}
					displayPath(DSP);
					finalDSP.addAll(DSP);
				}

				return finalDSP;
			}else{
				/*expand the current node*/
				expandNode(node);
				display();
			}
		}

		return null;
	}

	/**
	 * @return the minimum f-value nodeID from all the nodes in the open list
	 */
	public int getBestFvalueNode() {
		double minF = 100000.0;
		Integer retValue = 0;
		for(int i = 0; i< open.size(); i++){
			Integer id = open.get(i);
			Node n = g.getNode(id);
			if(((SMGSNodeData)n.getData()).getFvalue() < minF ){
				minF = ((SMGSNodeData)n.getData()).getFvalue(); 
				retValue = id;
			}
		}
		return retValue;
	}

	/**
	 * construct the sparse path by taking back pointers from the given goal till the start node.
	 * @param goal goal node of the subgraph.
	 * @return sparse path: list of nodeIDs in the sparse path. 
	 */
	ArrayList<Integer> extractSparseSolutionPath(Integer goal){
		ArrayList<Integer> path = new ArrayList<Integer>();
		Stack<Integer> stack = new Stack<Integer>();
		Integer parent = goal;
		while(closed.contains(parent) && (g.getNode(parent) != null) ){
			stack.push(parent);
			parent = ((SMGSNodeData)g.getNode(parent).getData()).getAncestor();
		}

		while(!stack.isEmpty()){
			Integer n = stack.peek();
			path.add(n);
			stack.pop();

		}

		return path;
	}

	/**
	 * expands the current node and places its children in to open if not opened previously, update g-values to some children which are already opened,
	 * update rhovalues to children which are already closed. 
	 * @param node current node to expand.
	 */
	public void expandNode(Node node){
		Set<Node> nodeset = new HashSet<Node>(node.getAdjList());
		((SMGSNodeData)node.getData()).setRhovalue(nodeset.size());

		ArrayList<Integer> alreadyseen = new ArrayList<Integer>();
		boolean seen = false;
		ArrayList<Edge> edgelist = new ArrayList<Edge>();
		for(int i=0; i<node.getAdjEdgeList().size(); i++){
			seen = false;
			if(node.getAdjEdgeList().get(i).getNodeID1()== node.getNodeID()){
				if(!alreadyseen.contains(node.getAdjEdgeList().get(i).getNodeID2()))
					alreadyseen.add(node.getAdjEdgeList().get(i).getNodeID2());
				else{
					seen = true;
				}
			}else if(node.getAdjEdgeList().get(i).getNodeID2()== node.getNodeID()){
				if(!alreadyseen.contains(node.getAdjEdgeList().get(i).getNodeID1()))
					alreadyseen.add(node.getAdjEdgeList().get(i).getNodeID1());
				else{
					seen = true;
				}
			}
			if(seen == false){
				edgelist.add(node.getAdjEdgeList().get(i));
			}
		}


		for(int i=0; i<edgelist.size(); i++){
			Edge e = edgelist.get(i);
			Node othernode = null;

			if(e.getNodeID1() == node.getNodeID()){
				othernode = g.getNode(e.getNodeID2());
			}else if(e.getNodeID2() == node.getNodeID()){
				othernode = g.getNode(e.getNodeID1());
			}

			/*update g-values to some children which are already opened*/
			if(open.contains(othernode.getNodeID())){
				if( (((SMGSNodeData)node.getData()).getGvalue()+e.getCost() )< ((SMGSNodeData)othernode.getData()).getGvalue()){
					((SMGSNodeData)othernode.getData()).setGvalue(((SMGSNodeData)node.getData()).getGvalue()+e.getCost());
					((SMGSNodeData)othernode.getData()).setAncestor(node.getNodeID());
				}
			}/*update rho-values to children which are already closed*/
			else if(closed.contains(othernode.getNodeID())){
				((SMGSNodeData)node.getData()).setRhovalue(((SMGSNodeData)node.getData()).getRhovalue() - 1);
				if(!(othernode.getState().equals(alvis301.State.relay)))
					((SMGSNodeData)othernode.getData()).setRhovalue(((SMGSNodeData)othernode.getData()).getRhovalue() - 1); 
			}/*places its children in to open if not opened previously*/
			else{
				((SMGSNodeData)othernode.getData()).setGvalue(((SMGSNodeData)node.getData()).getGvalue()+e.getCost());
				((SMGSNodeData)othernode.getData()).setHvalue(getHvalue(othernode));
				((SMGSNodeData)othernode.getData()).setAncestor(node.getNodeID());
				open.add(othernode.getNodeID());

				if (node!=null) {
					ArrayList<Edge> adjEdgeList = node.getAdjEdgeList();
					for (Edge e1 : adjEdgeList) {
						int nodeID1 = e1.getNodeID1();
						int nodeID2 = e1.getNodeID2();
						if ((nodeID1 == othernode.getNodeID()) || nodeID2 == othernode.getNodeID()) {
							e1.setState(alvis301.State.open);
							updateEdge(e1);
							othernode.setState(alvis301.State.open);
							updateNode(othernode);
							display();
							break;
						}
					}
				}
			}

		}


		if((closed.size()+open.size()) >= MEMORYFULL)
			pruneClosedList();
	}

	/**
	 * prunes the closed list when the memory is full. It does that in two steps. 
	 * In the first step, it updates the ancestor pointer of any boundary node whose predecessor is about to be pruned.
	 * In the second step, it prunes the kernel nodes from closed list.
	 * 
	 */
	public void pruneClosedList(){
		Integer alpha = null;

		for(int i=0; i<closed.size(); i++){
			Integer id = closed.get(i);
			Node node = g.getNode(id);

			if((((SMGSNodeData)node.getData()).getRhovalue() > 0) && (((SMGSNodeData)node.getData()).getRhovalue() != INFINITY) ){
				node.setState(alvis301.State.boundary);
				updateNode(node);
			}else if((((SMGSNodeData)node.getData()).getRhovalue() == 0) && (((SMGSNodeData)node.getData()).getRhovalue() != INFINITY)){
				node.setState(alvis301.State.old);
				updateNode(node);
			}
			/*else if(node.getData().getRhovalue() == INFINITY){
				node.setState(alvis301.State.old);
				updateNode(node);
			}*/
		}

		//	for(int i=0; i<100; i++)
		display();

		for(int i=0; i<open.size(); i++){
			Integer id = open.get(i);
			Node node = g.getNode(id);
			if(node.getNodeID() == startID){
				((SMGSNodeData)g.getNode( startID).getData()).setRhovalue(INFINITY);
				g.getNode( startID).setState(alvis301.State.relay);
				updateNode(g.getNode( startID));
				display();
			}
			else if(node.getAdjList().contains(g.getNode(((SMGSNodeData)node.getData()).getAncestor()))){
				alpha = ((SMGSNodeData)node.getData()).getAncestor();
				while(closed.contains(alpha) && ((SMGSNodeData)g.getNode(alpha).getData()).getRhovalue() == 0  && (!alpha.equals(g.getStartID()))){
					alpha = ((SMGSNodeData)g.getNode(alpha).getData()).getAncestor();
				}
				if(!alpha.equals(((SMGSNodeData)node.getData()).getAncestor())){
					((SMGSNodeData)node.getData()).setAncestor(alpha);
					((SMGSNodeData)g.getNode(alpha).getData()).setRhovalue(INFINITY);
					g.getNode(alpha).setState(alvis301.State.relay);
					updateNode(g.getNode(alpha));
				}
			}

		}

		for(int i=0; i<closed.size(); i++){
			Integer id = closed.get(i);
			Node node = g.getNode(id);
			if(node.getNodeID() == startID){
				((SMGSNodeData)g.getNode( startID).getData()).setRhovalue(INFINITY);
				g.getNode( startID).setState(alvis301.State.relay);
				updateNode(g.getNode( startID));
				display();
			}
			else if(node.getAdjList().contains(g.getNode(((SMGSNodeData)node.getData()).getAncestor()))){
				alpha = ((SMGSNodeData)node.getData()).getAncestor();
				while(closed.contains(alpha) && ((SMGSNodeData)g.getNode(alpha).getData()).getRhovalue() == 0 && (!alpha.equals(g.getStartID()))){
					alpha = ((SMGSNodeData)g.getNode(alpha).getData()).getAncestor();
				}
				if(!alpha.equals(((SMGSNodeData)node.getData()).getAncestor())){
					((SMGSNodeData)node.getData()).setAncestor(alpha);
					((SMGSNodeData)g.getNode(alpha).getData()).setRhovalue(INFINITY);
					g.getNode(alpha).setState(alvis301.State.relay);
					updateNode(g.getNode(alpha));
				}
			}

		}

		ArrayList<Integer> tobeClosed = new ArrayList<Integer>(); 
		for(int i=0; i<closed.size(); i++){
			Integer id = closed.get(i);
			Node node = g.getNode(id);
			if((((SMGSNodeData)node.getData()).getRhovalue() == 0)){
				tobeClosed.add(id);
			}
		}

		for(int i=0; i<tobeClosed.size(); i++){
			Integer id = tobeClosed.get(i);
			Node node = g.getNode(id);

			remove(closed, node.getNodeID());
			node.setState(alvis301.State.deleted);
			updateNode(node);
		}


		//	for(int i=0; i<100; i++)
		display();

	}

	/**
	 * removes the given nodeID from the given list. 
	 * @param list from which the node has to removed.
	 * @param id to remove from the list. 
	 */
	public void remove(ArrayList<Integer> list, Integer id){
		for(int i=0; i< list.size(); i++){
			if(list.get(i).equals(id)){
				list.remove(i);
				break;
			}
		}
	}

	/**
	 * computes the euclidean distance from given node to the goal node as the heuristic value.
	 * @param node to get h-value.
	 * @return h-value of the given node.
	 */
	public double getHvalue(Node node) {
		Node n1 = node;
		Node n2 = g.getNode(goalID);

		return Math.sqrt(Math.pow((n1.getX()-n2.getX()),2)+Math.pow((n1.getY()-n2.getY()),2));
	}


}
