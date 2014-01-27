package com.display;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;

import models.ElasticTube;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;

import org.jscroll.widgets.JScrollInternalFrame;

import com.display.images.IconLibrary;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TubePanel extends JPanel {
	public enum TubeClass{FirstArtery,Artery,Arteriole,Capillary,Veinule,Vein,VenousSinus, Ventricle,FourthVentricle,ThirdVentricle,SAS,SpinalCord};
	public static boolean linkModeActivated = false;
	public static JScrollInternalFrame linkModeActivatedSource = null;
	private static final Color activatedColor = Color.orange;
	private JScrollInternalFrame parentInternalFrame;
	private ElasticTube tube;
	private ArrayList<LineLink> lineLinks;
	private TubeClass tubeType;
	
	private JButton btnAddLink;
	private JButton btnDisplayCharts;
	private JButton btnRemovebloc;
	private JButton btnEditinit;
	private JPanel panel;
	private JLabel lblLength;
	private JTextField txtVallength;
	private JLabel lblAlpha;
	private JTextField textValAlpha;
	private JLabel lblElastance;
	private JTextField txtValelastance;
	private JLabel lblArea;
	private JTextField txtValarea;
	private JLabel lblFin;
	private JTextField txtValfin;
	private JLabel lblFout;
	private JTextField txtValfout;
	private JLabel lblPressure;
	private JTextField txtValpressure;
	
	public TubePanel( ElasticTube tube, TubeClass type) {
		setTube(tube);
        setLineLinks(new ArrayList<LineLink>());
        setTubeType(type);
        
		setPreferredSize(new Dimension(235, 111));
		setLayout(new MigLayout("", "[grow][grow][grow][grow]", "[62.00,grow][]"));
		Image img = IconLibrary.LINKICON;
		img = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); 
		img = IconLibrary.EDITVARIABLESICON;
		img = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); 
		
		btnAddLink = new JButton();
		img = IconLibrary.LINKICON;
		img = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); 
		
		panel = new JPanel();
		add(panel, "cell 0 0 4 1,grow");
		panel.setLayout(new MigLayout("", "[grow,left][68.00,left][grow,left][grow,left][grow,left][grow,left][grow][]", "[center][center]"));
		
		lblLength = new JLabel(ElasticTube.LENGTH_LABEL);
		panel.add(lblLength, "cell 0 0,alignx trailing");
		
		txtVallength = new JTextField();
		txtVallength.setEditable(false);
		txtVallength.setText(""+tube.getLength().getValue());
		panel.add(txtVallength, "cell 1 0,alignx center");
		txtVallength.setColumns(3);
		
		lblAlpha = new JLabel(ElasticTube.ALPHA_LABEL);
		panel.add(lblAlpha, "cell 2 0");
		
		textValAlpha = new JTextField(""+tube.getAlpha().getValue());
		textValAlpha.setEditable(false);
		panel.add(textValAlpha, "cell 3 0,alignx center");
		textValAlpha.setColumns(3);
		
		lblElastance = new JLabel(ElasticTube.ELASTANCE_LABEL);
		panel.add(lblElastance, "cell 4 0");
		
		txtValelastance = new JTextField(""+tube.getElastance().getValue());
		txtValelastance.setEditable(false);
		panel.add(txtValelastance, "cell 5 0,alignx center");
		txtValelastance.setColumns(3);
		
		lblPressure = new JLabel(ElasticTube.PRESSURE_LABEL);
		panel.add(lblPressure, "cell 6 0,alignx trailing");
		
		txtValpressure = new JTextField(""+tube.getPressure().getValue());
		txtValpressure.setEditable(false);
		panel.add(txtValpressure, "cell 7 0,alignx center");
		txtValpressure.setColumns(3);
		
		lblArea = new JLabel(ElasticTube.AREA_LABEL);
		panel.add(lblArea, "cell 0 1,alignx trailing");
		
		txtValarea = new JTextField(""+tube.getArea().getValue());
		txtValarea.setEditable(false);
		panel.add(txtValarea, "flowx,cell 1 1,alignx center");
		txtValarea.setColumns(3);
		
		lblFin = new JLabel(ElasticTube.FLOWIN_LABEL);
		panel.add(lblFin, "cell 2 1");
		
		txtValfin = new JTextField(""+tube.getFlowin().getValue());
		txtValfin.setEditable(false);
		panel.add(txtValfin, "cell 3 1,alignx center");
		txtValfin.setColumns(3);
		
		lblFout = new JLabel(ElasticTube.FLOWOUT_LABEL);
		panel.add(lblFout, "cell 4 1");
		
		txtValfout = new JTextField(""+tube.getFlowout().getValue());
		txtValfout.setEditable(false);
		panel.add(txtValfout, "cell 5 1,alignx center");
		txtValfout.setColumns(3);
		btnAddLink.setIcon(new ImageIcon(img));
		
		add(btnAddLink, "cell 0 1,grow");

		
		btnDisplayCharts = new JButton();
		img = IconLibrary.CHARTICON;
		img = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); 
		btnDisplayCharts.setIcon(new ImageIcon(img));
		add(btnDisplayCharts, "cell 1 1,grow");
		img = IconLibrary.RMBLOCKICON;
		img = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); 
		
		btnRemovebloc = new JButton();
		btnRemovebloc.setIcon(new ImageIcon(img));
		add(btnRemovebloc, "cell 2 1,grow");
		
		btnEditinit = new JButton();
		add(btnEditinit, "cell 3 1,grow");
		img = IconLibrary.EDITVARIABLESICON;
		img = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); 
		btnEditinit.setIcon(new ImageIcon(img));
		
		
		
		// Listeners
		btnAddLink.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(linkModeActivated && linkModeActivatedSource!=null){
					if(linkModeActivatedSource != parentInternalFrame){
						addLineLinkAsChild(new LineLink(linkModeActivatedSource, parentInternalFrame));
					}
					resetBtnAddLink();
					linkModeActivated = false;
					linkModeActivatedSource = null;
					for(JScrollInternalFrame jsf : parentInternalFrame.getJsDesktopPane().getInternalFrames()){
						jsf.getTubePanel().resetBtnAddLink();
					}
				}else{
					btnRemovebloc.setEnabled(false);
					btnDisplayCharts.setEnabled(false);
					btnEditinit.setEnabled(false);
					linkModeActivatedSource = parentInternalFrame;
					linkModeActivated = true;
					btnAddLink.setBackground(Color.cyan);
					for(JScrollInternalFrame jsf : parentInternalFrame.getJsDesktopPane().getInternalFrames()){
						TubePanel ltubep = jsf.getTubePanel();
						if(jsf != parentInternalFrame){
							if(isLinkedWith(jsf)){
								ltubep.deactivateLinkMode();
								continue;
							}
							
							if(tubeType == ltubep.getTubeType() && (tubeType == TubeClass.Artery || tubeType == TubeClass.Vein)){
								ltubep.activateLinkMode();
							}else{
								if(ltubep.getTube().getHemisphere() == getTube().getHemisphere()){
									switch(tubeType){
									case FirstArtery:
										if(ltubep.getTubeType() == TubeClass.Artery)
											ltubep.activateLinkMode();
										else
											ltubep.deactivateLinkMode();
										break;
									case Artery:
										if(ltubep.getTubeType() == TubeClass.Arteriole)
											ltubep.activateLinkMode();
										else
											ltubep.deactivateLinkMode();
										break;
									case Arteriole:
										if(ltubep.getTubeType() == TubeClass.Capillary)
											ltubep.activateLinkMode();
										else
											ltubep.deactivateLinkMode();
										break;
									case Capillary:
										if(ltubep.getTubeType() == TubeClass.Veinule)
											ltubep.activateLinkMode();
										else
											ltubep.deactivateLinkMode();
										break;
									case Veinule:
										if(ltubep.getTubeType() == TubeClass.Vein)
											ltubep.activateLinkMode();
										else
											ltubep.deactivateLinkMode();
										break;
									case Vein:
										if(ltubep.getTubeType() == TubeClass.VenousSinus)
											ltubep.activateLinkMode();
										else
											ltubep.deactivateLinkMode();
										break;
									default:
										break;
									}
								}
							}
						}
					}
				}
			}




		});
		btnRemovebloc.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						for(LineLink line : getLineLinks()){
							line.delete();
						}
						getParentInternalFrame().dispose();
					}
				});
				
			}
		});
		// display constraint
		if(getTubeType() == TubeClass.VenousSinus)
			btnAddLink.setEnabled(false);
	}
	
	private boolean isLinkedWith(JScrollInternalFrame jsf) {
		if(lineLinks.isEmpty())
			return false;
		for(LineLink line:lineLinks){
			if(line.getParent() == jsf || line.getChild() == jsf){
				return true;
			}else{
				if(line.getParent() != jsf && line.getChild() == getParentInternalFrame() && line.getParent().getTubePanel().getTube().getChildren().contains(jsf.getTubePanel().getTube())){
					return true;
				}else{
					if(line.getParent() == getParentInternalFrame()){
						if(iterativeCheckChildLink(jsf,line))
							return true;
					}
				}
			}
		}
		return false;
	}
	
	private boolean iterativeCheckChildLink(JScrollInternalFrame jsf, LineLink currentLine){
		for(LineLink li2 : currentLine.getChild().getLineLinks()){
			if(currentLine==li2)
				continue;
			if(li2.getChild() == jsf){
				return true;
			}else{
				if(iterativeCheckChildLink(jsf, li2))
					return true;
			}
		}
		return false;
	}
	/**
	 * Permet de mettre les boutons d'une couleur special en mode active
	 */
	public void activateLinkMode() {
		btnAddLink.setBackground(activatedColor);
		btnAddLink.setEnabled(true);
		btnRemovebloc.setEnabled(false);
		btnDisplayCharts.setEnabled(false);
		btnEditinit.setEnabled(false);
	}
	public void deactivateLinkMode(){
		btnAddLink.setBackground(null);
		btnAddLink.setEnabled(false);
		btnRemovebloc.setEnabled(false);
		btnDisplayCharts.setEnabled(false);
		btnEditinit.setEnabled(false);
	}
	public void resetBtnAddLink(){
		btnAddLink.setBackground(null);
		if(getTubeType() == TubeClass.VenousSinus)
			btnAddLink.setEnabled(false);
		else
			btnAddLink.setEnabled(true);
		btnRemovebloc.setEnabled(true);
		btnDisplayCharts.setEnabled(true);
		btnEditinit.setEnabled(true);
		getParentInternalFrame().getJsDesktopPane().repaint();
	}
	
	/**
	 * @return the lineLinks
	 */
	public ArrayList<LineLink> getLineLinks() {
		return lineLinks;
	}

	/**
	 * @param lineLinks the lineLinks to set
	 */
	public void setLineLinks(ArrayList<LineLink> lineLinks) {
		this.lineLinks = lineLinks;
	}
	public void addLineLinkAsChild(LineLink line) {
		this.lineLinks.add(line);
		tube.addParent(line.getParent().getTubePanel().getTube());
	}
	
	public void addLineLinkAsParent(LineLink line){
		this.lineLinks.add(line);
	}
	
	public void removeLineLinkAsParent(LineLink line){
		if(!tube.removeChild(line.getChild().getTubePanel().getTube())){
			System.err.println("Unable to remove child tube from "+tube.getName());
		}
		this.lineLinks.remove(line);
	}
	
	public void removeLineLinkAsChild(LineLink line){
		if(!tube.removeParent(line.getParent().getTubePanel().getTube())){
			System.err.println("Unable to remove parent tube from "+tube.getName());
		}
		this.lineLinks.remove(line);
	}
	
	/**
	 * @return the tubeType
	 */
	public TubeClass getTubeType() {
		return tubeType;
	}
	/**
	 * @param tubeType the tubeType to set
	 */
	public void setTubeType(TubeClass tubeType) {
		this.tubeType = tubeType;
	}
	/**
	 * @return the tube
	 */
	public ElasticTube getTube() {
		return tube;
	}
	/**
	 * @param tube the tube to set
	 */
	public void setTube(ElasticTube tube) {
		this.tube = tube;
	}
	/**
	 * @return the parentInternalFrame
	 */
	public JScrollInternalFrame getParentInternalFrame() {
		return parentInternalFrame;
	}
	/**
	 * @param parentInternalFrame the parentInternalFrame to set
	 */
	public void setParentInternalFrame(JScrollInternalFrame parentInternalFrame) {
		this.parentInternalFrame = parentInternalFrame;
	}

}
