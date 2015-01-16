/**
TSP SIMULATED ANNEALING ALGORITHM
AUTHOR:CHARU CHAUHAN
**/

package alvis301;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JOptionPane;
import java.util.Arrays;
import org.jfree.ui.RefineryUtilities;

//FINDS AN OPTIMAL SOLUTION TO TRAVELLING SALESMAN PROBLEM(TSP) USING SIMULATED ANNEALING
public class TSP_sm extends Algorithm{
    TSPGraphV lg;//to draw the line graph
    double temprature = 100;
    double distance_diff = 0;
    double coolingRate = 0.9999;
    double freezingTemperature = 0.00001;
    double min_distance = 0;
    ArrayList<ArrayList<Double>> dist;
    Random random = new Random();
    ArrayList<Node> curr_tour;
    ArrayList<Node> next_tour;
    
    
    
    public TSP_sm(int t) {
        super(t);
    }

    @Override
    public boolean goalTest(Node goalNode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object moveGen(Node parentNode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        startAnnealing();
    }
    
    //prints the solution to the console
    public void printSolution (ArrayList<Node> Tour) {
        System.out.println("inside with size" + Tour.size());
        for (Node n : Tour) {
            System.out.print(n.getNodeID()+" ");
        }
        
    }
    
    //returns the cost of the tour
    public double costTour(ArrayList<Node> tour) {
        Node first = tour.get(0);
        Node prev = first;
        double cost = 0;
        for (Node n : tour) {
            if (n.equals(prev))
                continue;
            cost = cost + distance(prev,n);
            prev = n;
        }
        cost = cost + distance(prev,first);
        return cost;
    }
    
    //calculates the distance between two nodes.
    public double distance(Node n1, Node n2) {
        return Math.sqrt(Math.pow((n1.getX()-n2.getX()),2)+Math.pow((n1.getY()-n2.getY()),2));
    }
    
    //set tour to the calculated tour
    public void setTour (ArrayList <Node> tour) {
        
        Node prev = tour.get(0);
        Node first = prev;
        for (Map.Entry pairs : edges.entrySet()) {
            Edge e = (Edge) pairs.getValue();
            e.setState(alvis301.State.tsp);
            updateEdge(e);
        }
        for (Node n:tour) {
            if (n.equals(prev))
                continue;
            ArrayList<Edge> adjEdgeList = prev.getAdjEdgeList();
            for (Edge e : adjEdgeList) {
                int nodeID1 = e.getNodeID1();
                int nodeID2 = e.getNodeID2();
                if (nodeID1 == n.getNodeID() || nodeID2 == n.getNodeID()) {
                    e.setState(alvis301.State.path);
                    updateEdge(e);
                    break;
                }
            }
            prev = n;
        }
        
        ArrayList<Edge> adjEdgeList = prev.getAdjEdgeList();
            for (Edge e : adjEdgeList) {
                int nodeID1 = e.getNodeID1();
                int nodeID2 = e.getNodeID2();
                if (nodeID1 == first.getNodeID() || nodeID2 == first.getNodeID()) {
                    e.setState(alvis301.State.path);
                    updateEdge(e);
                    break;
                }
            }
    }
    
    //RETURNS RANDOM TOUR
    public ArrayList<Node> getRandomSolution(){
        
        ArrayList<Node> solution = new ArrayList<Node>();
        int counter = 0;
        int size = g.getNoNodes();    
        Random rand = new Random();
        Node prev = null;
        while (counter < size) {
            int nid = rand.nextInt(size) + 1;
            Node n = g.getNode(nid);
            if (!solution.contains(n)) {
                solution.add(n);
                counter++;
            }
        }
        return solution;
    }
    
    public ArrayList<Node> getSimulatedSolution(){
        
        ArrayList<Node> solution = new ArrayList<Node>();
        
        return solution;
    }
    
   
    //MAIN FUNCTIONAL COMPONENT (USED FOR SIMULATED ANNEALING)
    public void startAnnealing()
    {
    	double curr_distance;
        int flag=0;
        int update = 0;
        //ask the user for the starting temprature
        String temp=getInput("Temparature");
        temprature=Double.parseDouble(temp);
        lg = new TSPGraphV("SIMULATED ANNEALING-FIRST RUN",temp);
        //ask the user for the cooling rate
        String cr=getInput("Cooling Rate");
        coolingRate=Double.parseDouble(cr);
        //ask user how often to update the screen while displaying the modified tour
        String screen_update = getInput("Update screen after how many tour updates??");
        Integer update_number = Integer.parseInt(screen_update);
        System.out.println("the starting temprature and cooling rate are"+temp+" " +coolingRate);
        //ask the user for the type of perturbation (city/edge(2 edge,3edge))
        JList list = new JList(new String[] {"2-city exchange","edge exchange"});
        JOptionPane.showMessageDialog(null, list, "Choose Operator", JOptionPane.PLAIN_MESSAGE);
        String choice = Arrays.toString(list.getSelectedIndices());
        if(choice.equals("[1]"))
        {
            JList list1 = new JList(new String[] {"2-edge exchange","3-edge exchange"});
            JOptionPane.showMessageDialog(null, list1, "Edge Exchange Operator", JOptionPane.PLAIN_MESSAGE);
            String choice1 = Arrays.toString(list.getSelectedIndices());
            if(choice1.equals("[0]"))
                flag=0;
            else if(choice.equals("[1]"))
                flag=1;
        }
    	curr_tour = getRandomSolution();//initial tour is chosen randomly 
        setTour(curr_tour);
    	curr_distance = costTour(curr_tour);
        lg.addDataSet(temprature, curr_distance);
        //start annealing
    	while(temprature > freezingTemperature)
    	{
    		for(int m=0;m<10;m++)
    		{
    	            if(choice.equals("[0]"))
                    {
                        next_tour = getNextTour2CityExchange(curr_tour);
                    }
                    else if(choice.equals("[1]"))
                    {
                        if(flag==0)
                            next_tour=getNextTour2EdgeExchange(curr_tour);
                        else if(flag==1)
                        {
                            next_tour=getNextTour3EdgeExchange(curr_tour);
                        }
                    }
    		distance_diff =  costTour(next_tour) - curr_distance;
                //update the tour probablistically
    		if ((distance_diff < 0) || (distance_diff > 0 &&  Math.exp((-1*distance_diff) / temprature) > random.nextDouble()))
    		{
    			update++;
                        curr_tour.clear();
    			for (int i = 0; i < next_tour.size(); i++)
    			{
    				curr_tour.add(next_tour.get(i));
    			}

                curr_distance = distance_diff + curr_distance;
                setTour(next_tour);
                if(update == update_number)//display new tour after update_time modifications
                {
                    displayTSP(curr_distance , temprature);
                    update = 0;
                }
                lg.addDataSet(temprature,curr_distance);
                System.out.println("\nTEMPRATURE: "+temprature);
                
    		}
    		}
    		temprature = temprature * coolingRate;//Linear cooling function
        }
    	
    	min_distance = curr_distance;
        String outPut=Double.toString(min_distance);
        displayMessage("Minimum distance is:"+outPut);
        lg.displayGraph();
        restartAnnealing(next_tour);
    }
    
    //METHOD FOR RESTART ANNEANLING PROCESS(THE INPUT IS THE OUTPUT TOUR FROM STARTANNEALING)
    public void restartAnnealing(ArrayList<Node> annealedTour)
    {
    	double curr_distance;
        int flag=0;
        Double temp = 100.0;//initial temprature for annealing restart 
        TSPGraphV lg1 = new TSPGraphV("SIMULATED ANNEALING-RESTART",temp.toString());
        Double coolingR=0.999;//cooling rate
    	curr_tour = annealedTour;//the output of annealing process is fed here
        setTour(curr_tour);
    	curr_distance = costTour(curr_tour);
        lg1.addDataSet(temp, curr_distance);
        
        //restart annealing
    	while(temp > freezingTemperature)
    	{
    		for(int m=0;m<10;m++)
    		{
    	            next_tour=getNextTour2EdgeExchange(curr_tour);
                    distance_diff =  costTour(next_tour) - curr_distance;
                    if ((distance_diff < 0) || (distance_diff > 0 &&  Math.exp((-1*distance_diff) / temp) > random.nextDouble()))
                    {
    			curr_tour.clear();
    			for (int i = 0; i < next_tour.size(); i++)
    			{
    				curr_tour.add(next_tour.get(i));
    			}

                    curr_distance = distance_diff + curr_distance;
                    setTour(next_tour);
                    displayTSP(curr_distance , temp);
                    lg1.addDataSet(temp,curr_distance);
                    System.out.println("\nTEMPRATURE: "+temp);
                }
    		}
    		temp = temp * coolingRate;
            }
            min_distance = curr_distance;
            String outPut=Double.toString(min_distance);
            displayMessage("Minimum distance is:"+outPut);
            lg1.displayGraph();
    }

    //PERTURBATION TO FIND NEIGHBOUR BY 3 EDGE EXCHANGE
    public ArrayList<Node> getNextTour3EdgeExchange(ArrayList<Node> oldTour)
    {
        ArrayList<Node> newTour = new ArrayList<Node>();
        Random rand = new Random();
    	int i,j,k,l,index;
        i = 0;//initialise variables
        j = 0;
        k = 0;
        l = 0;
    	Integer rand1,rand2,rand3;
    	rand1 = rand.nextInt((oldTour.size()-1));
    	rand2 = rand.nextInt((oldTour.size()-1));
    	rand3 = rand.nextInt((oldTour.size()-1));
        
        //keep the perturbation process on till the we find all the three edges that do not have any vertex in common..
        while((Math.abs(rand1-rand2) < 1) || (Math.abs(rand2-rand3) < 1) || (Math.abs(rand3-rand1) < 1))//difference between i and j should be more than 2
    	{
    		rand1 = rand.nextInt(oldTour.size());
        	rand2 = rand.nextInt(oldTour.size());
                rand3 = rand.nextInt(oldTour.size());
    	}
        if((rand1<rand2) && (rand1<rand3))
        {
            i = rand1;//smallest number is assigned to i
            if(rand2<rand3)
            {
                j = rand2;
                k = rand3;
            }
            else
            {
                j = rand3;
                k = rand2;   
            }
        }
        else if((rand2<rand1) && (rand2<rand3))
        {
            i = rand2;//smallest number is assigned to i
            if(rand1<rand3)
            {
                j = rand1;
                k = rand3;
            }
            else
            {
                j = rand3;
                k = rand1;   
            }
        }
        else if((rand3<rand1) && (rand3<rand2))
        {
            i = rand3;//smallest number is assigned to i
            if(rand1<rand2)
            {
                j = rand1;
                k = rand2;
            }
            else
            {
                j = rand2;
                k = rand1;   
            }
        }
        for(l=0;l<=i;l++)
        {
            newTour.add(oldTour.get(l));
          //  System.out.println(l);
        }
        for(l=k;l>=j+1;l--)
        {
            newTour.add(oldTour.get(l));
           // System.out.println(l);
        }
        for(l=i+1;l<=j;l++)
        {
            newTour.add(oldTour.get(l));
         //   System.out.println(l);
        }
        for(l=k+1;l<oldTour.size();l++)
        {
            newTour.add(oldTour.get(l));
        }
       return newTour;
    }
    
    //PERTURBATION TO FIND NEIGHBOUR BY 2 EDGE EXCHANGE
    public ArrayList<Node> getNextTour2EdgeExchange(ArrayList<Node> oldTour)
    {
    	ArrayList<Node> newTour = new ArrayList<Node>();
        Random rand = new Random();
    	int i,j,k,index;
    	Integer rand1,rand2;
    	rand1 = rand.nextInt((oldTour.size()-1));
    	rand2 = rand.nextInt((oldTour.size()-1));
    	while(Math.abs(rand1-rand2) < 1)//difference between i and j should be more than 2
    	{
    		rand1 = rand.nextInt(oldTour.size());
        	rand2 = rand.nextInt(oldTour.size());
    	}
    	if(rand1 < rand2)
    	{
    		i = rand1;
    		j = rand2;
    	}
    	else
    	{
    		i = rand2;
    		j = rand1;
    	}
    	for(k=0;k<=i;k++)
    	{
    		newTour.add(oldTour.get(k));
    	}
    	for(k=j;k>=(i+1);k--)
    	{
    		newTour.add(oldTour.get(k));
    	}
    	for(k=j+1;k<oldTour.size();k++)
    	{
    		newTour.add(oldTour.get(k));
    	}
    	return newTour;
    }
    
    //PERTURBATION TO FIND NEIGHBOUR BY 2 CITY EXCHANGE
    public ArrayList<Node> getNextTour2CityExchange(ArrayList<Node> oldTour)
    {
        ArrayList<Node> newTour = new ArrayList<Node>();
        Random rand = new Random();
    	int i,j,k;
    	Integer rand1,rand2;
    	rand1 = rand.nextInt((oldTour.size()-1));
    	rand2 = rand.nextInt((oldTour.size()-1));
    	while(Math.abs(rand1-rand2) == 0)//the cities should not be same otherwise no changes will be there
    	{
    		rand1 = rand.nextInt(oldTour.size());
        	rand2 = rand.nextInt(oldTour.size());
    	}
        if(rand1 < rand2)
        {
            i = rand1;
            j = rand2;
        }
        else
        {
            i = rand2;
            j = rand1;
        }
        for(k=0 ; k<i ; k++)
        {
            newTour.add(oldTour.get(k));
        }
        newTour.add(oldTour.get(j));
        for(k = i+1; k<j ;k++)
        {
            newTour.add(oldTour.get(k));
        }
        newTour.add(oldTour.get(i));
        for(k = j+1; k<oldTour.size() ;k++)
        {
            newTour.add(oldTour.get(k));
        }
        
        return newTour;
    }
    //get the initial tour as greedy solution..to improve the solution
    /*THIS METHOD WILL FILL THE TOUR FROM THE MEDIAN TO ALL THE NODES IN A GREEDY MANNER*/
/*ArrayList<Node> make_greedy_path(ArrayList<Node> randomTour)
{
	ArrayList<Boolean> visited = new ArrayList<Boolean>();
        ArrayList<Node> greedy_tour = new ArrayList<Node>();
	double minimum_distance= -1.0;
	int curr_index = 0;
	ArrayList<Double> curr_city;
	for(int j=0;j<randomTour.size();j++)
	{
		visited.add(false);
	}
	//Collections.fill(visited, new Boolean(true));//initially all the unvisited hence fill it to false
	greedy_tour.add(randomTour.get(index));//start from the median point
	visited.set(curr_index, true);
	while(greedy_tour.size() < randomTour.size())//for every city find the nearest neighbour
	{
		curr_city = distance.get(curr_index);
		minimum_distance = -1;
		int i;
		for(i=0;i<curr_city.size();i++)
		{
			if(((curr_city.get(i) < minimum_distance) || (minimum_distance == -1)) && (visited.get(i) == false))
			{
				minimum_distance = curr_city.get(i);
				curr_index = i;
			}
		}
		greedy_tour.add(curr_index);
		visited.set(curr_index, true);
	}
	greedy_tour.add(index);
	return greedy_tour;
}
*/
    
    
}
