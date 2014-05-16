package display.containers;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import models.SimpleVariable;
import net.miginfocom.swing.MigLayout;
import javax.swing.JSplitPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.statistics.MeanAndStandardDeviation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import params.ModelSpecification;

public class GraphsPanel extends JPanel {

	private ArrayList<SimpleVariable> varToDisplay;
	private JSplitPane splitPane;
	private JSplitPane splitPaneBottom;
	private JSplitPane splitPaneTop;
	public GraphsPanel() {		
		
		setLayout(new MigLayout("", "[grow]", "[grow]"));

		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane, "cell 0 0,grow");
		
		splitPaneTop = new JSplitPane();
		splitPaneTop.setResizeWeight(0.5);
		splitPane.setLeftComponent(splitPaneTop);
		
		splitPaneBottom = new JSplitPane();
		splitPaneBottom.setResizeWeight(0.5);
		splitPane.setRightComponent(splitPaneBottom);
		
	}
	
	private void refreshPlot(){
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					for(int i = 0;i<varToDisplay.size();i++){
						plot(i,ModelSpecification.time.getValue(),varToDisplay.get(i).getValueInTime(),varToDisplay.get(i).getName(),ModelSpecification.time.getName(), varToDisplay.get(i).getName());
						System.out.println("Mean "+varToDisplay.get(i).getName()+" : "+mean(varToDisplay.get(i).getValueInTime()));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
	private void plot(int position, float[] x, float[] y, String seriename, String xlabel, String ylabel) throws Exception{
		if(x.length != y.length){
			throw new Exception("x and y array must have the same size");
		}
		StandardChartTheme theme = (StandardChartTheme)org.jfree.chart.StandardChartTheme.createDarknessTheme();

		XYSeries series = new XYSeries(seriename);
		for (int i = 0; i < x.length; i++) {
			series.add(x[i], y[i]);
		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		NumberAxis domain = new NumberAxis(xlabel);
		NumberAxis range = new NumberAxis(ylabel);
		XYSplineRenderer r = new XYSplineRenderer(3);
		r.setSeriesShape(0, new Ellipse2D.Double(-3, -3, 6, 6));
		r.setShapesVisible(false);
		XYPlot xyplot = new XYPlot(dataset, domain, range, r);
		JFreeChart chart = new JFreeChart(xyplot);
		theme.apply( chart );
		ChartPanel chartPanel = new ChartPanel(chart);/*{

			@Override
			public Dimension getPreferredSize() {
				return new Dimension(640, 480);
			}
		};*/
		switch(position){
		case 0:
			splitPaneTop.setLeftComponent(chartPanel);break;
		case 1:
			splitPaneTop.setRightComponent(chartPanel);break;
		case 2:
			splitPaneBottom.setLeftComponent(chartPanel);break;
		case 3:
			splitPaneBottom.setRightComponent(chartPanel);break;
		default:
			throw new Exception("Position must be 0 to 3");
		}
	}

	public ArrayList<SimpleVariable> getVarToDisplay() {
		return varToDisplay;
	}

	public void setVarToDisplay(ArrayList<SimpleVariable> varToDisplay) {
		if(varToDisplay.size()>4)
			System.err.println("");
		this.varToDisplay = varToDisplay;
		refreshPlot();
	}
	public static double mean(float[] m) {
	    double sum = 0;
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    return sum / m.length;
	}
	
}
