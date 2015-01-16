/*
AUTHOR: CHARU CHAUHAN
DATE: 4.12.14
*/
package alvis301;

import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
/**
 *
 * @author CHARU CHAUHAN
 */

public class TSPGraphV extends ApplicationFrame{
    
    private XYSeries plot;
    
    //MAKE THE INSTANCE OF THE GRAPH.PARAMETRES:TITLE OF THE GRAPH AND THE TEMPRATURE
    public TSPGraphV(String title,String temp) {
    super(title);//THE TITLE FOR THE GRAPH
    plot = new XYSeries(temp);
    }
    
    // USE THIS METHOD TO ADD THE POINTS THAT ARE TO BE DISPLAYED
    public void addDataSet(Double temp_x,Double tour_y) {
        
        plot.add(temp_x,tour_y);
        System.out.println("ADDED");
    }
    
    //CREATE DATASET
     public XYDataset createDataset(){
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(plot);//MULTIPLE SERIES CAN BE ADDED TO ONE XYDATASET
        return dataset;
    }
    
    //DISPLAY THE GRAPH THAT IS CREATED
    public void displayGraph(){
        XYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1200, 1000));
        setContentPane(chartPanel);
        this.pack();
        RefineryUtilities.centerFrameOnScreen(this);
        this.setVisible(true);
     }
    
    
    //CREATE CHART ACCORDING TO THE DATASET CREATED
    private JFreeChart createChart(final XYDataset dataset) {
        
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "TEMP VS TOUR COST",      // TITLE
            "TEMPRATURE",             // X AXIS LABEL
            "TOUR COST",              // Y AXIS LABEL
            dataset,                  // DATASET
            PlotOrientation.VERTICAL, //KEEP THE ORIENTATION OF THE AXIS SAME AS THE ORDER IN WHICH WE USED ADD DATASET 
            true,                     // LEGEND
            true,                     // TOOLTIP
            false                     // URLS
        );

        //CUSTOMISE THE LOOK AND FEEL OF THE LINE GRAPH
        chart.setBackgroundPaint(Color.white);
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        //STYLE OF THE LINE GRAPH
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setBaseShapesVisible(true);
        renderer.setBaseShapesFilled(true);
        plot.setRenderer(renderer);
        
        //SET THE RANGE OF THE X AND Y AXIS
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
        return chart;
    }
}
