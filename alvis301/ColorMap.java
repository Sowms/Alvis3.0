/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alvis301;

import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author sowmya
 */
public class ColorMap {
    
    private HashMap <State,Color> colorMap;
    public ColorMap() {
        colorMap = new HashMap();
        colorMap.put(State.unvisited,Color.lightGray);
        colorMap.put(State.boundary,new Color(0,153,0));
        colorMap.put(State.rollback,new Color(255,102,0));
        colorMap.put(State.old,Color.darkGray);
        colorMap.put(State.deleted, Color.white);
        colorMap.put(State.closed,new Color(153,153,255));
        colorMap.put(State.goal,Color.blue);
        colorMap.put(State.open,new Color(255,102,102));
        colorMap.put(State.start,Color.red);
        colorMap.put(State.relay,Color.MAGENTA);
        colorMap.put(State.path,new Color(153,0,153));
        colorMap.put(State.pipe,Color.black);
        colorMap.put(State.pipepath,Color.black);
        colorMap.put(State.max,Color.red);
        colorMap.put(State.min,Color.blue);
    }
    public static ColorMap cm;
    public static ColorMap getInstance(){
        return cm;
    }
    public static void setInstance(ColorMap cm){
        ColorMap.cm = cm;
    }
    
    public Color getColor(State s) {
        return colorMap.get(s);
    }
    public void setColor(State s, Color c) {
        colorMap.put(s,c);
    }
    public HashMap getMap() {
        return colorMap;
    }
}
