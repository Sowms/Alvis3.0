/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alvis301;

import java.util.ArrayList;

/**
 *
 * @author SavithaSam
 */
public class DCNodeData extends NodeData {

    private double g_n, h_n, f_n;
    public boolean relay = false;
    private int childCount;
    private int relayID;
    public ArrayList<Node> tabuListNirav=null;

    private int level,fullLevelflag,relayptr;
	
 	private ArrayList <Node> tabuList; //ArrayList of Nodes connected to given node
	 
    public DCNodeData() {
        
    	tabuList = new ArrayList<Node>();
    	relayptr=Integer.MAX_VALUE;
    	
   }
    
    public void setinFullLevel(int flag){
    	fullLevelflag=flag;
    }
    public int getinFullLevel(){
    	return fullLevelflag;
    }
    
    public void setRelay(int a) {
        relayID = a;
    }

    public int getRelay() {
        return relayID;
    }

    public void setGValue(double a) {
        g_n = a;
    }

    public void setHValue(double a) {
        h_n = a;
    }

    public void setFValue(double a) {
        f_n = a;
    }

    public double getGValue() {
        return g_n;
    }

    public double getHValue() {
        return h_n;
    }

    public double getFVAlue() {
        return f_n;
    }

    public void setChildCount(int a) {
        childCount = a;
    }

    public int getChildCount() {
        return childCount;
    }

    public void decrementChildCount() {
        childCount--;
    }
    
     public int getLevel(){
        return level;
    }
    public void setLevel(int level){
        this.level=level;
    }
    
    public int getRelayPtr(){
        return relayptr;
    }
    public void setRelayPtr(int relayptr){
        this.relayptr=relayptr;
    }
    public ArrayList<Node> gettabuList() {
        return tabuList;
    }
    
    public void settabuList(ArrayList<Node> tabuList) {
        this.tabuList = tabuList;
    }
}
