package main;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;

import display.SEBWindow;

import models.Hemisphere;
import models.SimpleVariable;
import models.Vein;

import params.SystemParams;


import BaseModel.StandardModel;

public class main {

	public static void main(String[] args){
		// on charge le logger
		/*SystemParams.modelLogger = Logger.getLogger("flogger");
		try {
			FileHandler fh=new FileHandler(logfiledir.toString()+File.separator+WindowManager.PROGRAM_NAME+".log",1000000000,1);// taille max 1 Go
			fh.setFormatter(new SimpleFormatter());
			WindowManager.mwLogger.addHandler(fh);
			WindowManager.mwLogger.setLevel(Level.INFO);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		/*SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				JFrame.setDefaultLookAndFeelDecorated(true);
				try {
			          UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
		        } catch (Exception e) {
		          System.out.println("Substance Graphite failed to initialize");
		        }
				UIManager.put(SubstanceLookAndFeel.WINDOW_ROUNDED_CORNERS, Boolean.FALSE);
				display();
			}
		});*/
		
		System.out.println("Hello");
		//StandardModel.run();
		
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				JFrame.setDefaultLookAndFeelDecorated(true);
				try {
			          UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
		        } catch (Exception e) {
		          System.out.println("Substance Graphite failed to initialize");
		        }
				UIManager.put(SubstanceLookAndFeel.WINDOW_ROUNDED_CORNERS, Boolean.FALSE);
				SEBWindow s = new SEBWindow();
				s.createAndShowGUI();
			}
		});
		
		
	}
	private static void display() {
		 StandardChartTheme theme = (StandardChartTheme)org.jfree.chart.StandardChartTheme.createDarknessTheme();
		 
        XYSeries series = new XYSeries("test");
        for (int i = 0; i <= 10; i++) {
            series.add(i, Math.pow(2, i));
        }
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        NumberAxis domain = new NumberAxis("x");
        NumberAxis range = new NumberAxis("f(x)");
        XYSplineRenderer r = new XYSplineRenderer(3);
        XYPlot xyplot = new XYPlot(dataset, domain, range, r);
        JFreeChart chart = new JFreeChart(xyplot);
        theme.apply( chart );
        ChartPanel chartPanel = new ChartPanel(chart){

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(640, 480);
            }
        };
        JFrame frame = new JFrame("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(chartPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
