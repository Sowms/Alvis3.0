/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alvis301;

/**
 *
 * @author sowmya
 */
public class SMGSNodeData extends NodeData {
    private double gvalue = 0.0;
	private int rhovalue = -1;
	private double hvalue = 20000.0;
	private double fvalue = 20000.0;
	private Integer ancestor;
	private final int nodeID;
	
	public SMGSNodeData(int nodeID){
		this.nodeID = nodeID;
	}
	public double getFvalue() {
		return this.gvalue+this.hvalue;
	}
	public void setFvalue(double fvalue) {
		this.fvalue = fvalue;
	}
	public Integer getAncestor() {
		return ancestor;
	}
	public void setAncestor(Integer ancestor) {
		this.ancestor = ancestor;
	}
	public double getGvalue() {
		return gvalue;
	}
	public void setGvalue(double gvalue) {
		this.gvalue = gvalue;
	}
	public int getRhovalue() {
		return rhovalue;
	}
	public void setRhovalue(int rhovalue) {
		this.rhovalue = rhovalue;
	}
	public double getHvalue() {
		return this.hvalue;
	}
	public void setHvalue(double hvalue) {
		this.hvalue = hvalue;
	}
    
}
