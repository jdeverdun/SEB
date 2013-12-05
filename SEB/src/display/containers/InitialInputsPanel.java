package display.containers;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.geom.Ellipse2D;

import net.miginfocom.swing.MigLayout;
import javax.swing.JSplitPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import params.ModelSpecification;

public class InitialInputsPanel extends JPanel {

	private JSplitPane splitPane;
	
	public InitialInputsPanel() {
		setLayout(new MigLayout("", "[grow]", "[grow]"));

		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.5);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add(splitPane, "cell 0 0,grow");
		
		initPlot();
	}

	public void initPlot(){
		try {
			plot(0,new float[0],new float[0],"P_INIT","time", "First Artery pressure");
			plot(1,new float[0],new float[0],"P_OUT","time", "Output pressure");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void refreshPlot(){
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					plot(0,ModelSpecification.time,ModelSpecification.P_INIT,"P_INIT","time", "First Artery pressure");
					plot(1,ModelSpecification.time,ModelSpecification.P_OUT,"P_OUT","time", "Output pressure");
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
			splitPane.setLeftComponent(chartPanel);break;
		case 1:
			splitPane.setRightComponent(chartPanel);break;
		default:
			throw new Exception("Position must be 0 or 1");
		}
	}

	
}
