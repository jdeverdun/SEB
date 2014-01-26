package com.display;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

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
	
	public TubePanel( ElasticTube tube, TubeClass type) {
		setTube(tube);
        setLineLinks(new ArrayList<LineLink>());
        setTubeType(type);
        
		setPreferredSize(new Dimension(235, 80));
		setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][grow][grow]"));
		Image img = IconLibrary.LINKICON;
		img = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); 
		
		btnEditinit = new JButton();
		add(btnEditinit, "cell 2 1,grow");
		img = IconLibrary.EDITVARIABLESICON;
		img = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); 
		btnEditinit.setIcon(new ImageIcon(img));
		
		btnAddLink = new JButton();
		img = IconLibrary.LINKICON;
		img = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); 
		btnAddLink.setIcon(new ImageIcon(img));
		
		add(btnAddLink, "cell 0 2,grow");

		
		btnDisplayCharts = new JButton();
		img = IconLibrary.CHARTICON;
		img = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); 
		btnDisplayCharts.setIcon(new ImageIcon(img));
		add(btnDisplayCharts, "cell 1 2,grow");
		
		btnRemovebloc = new JButton();
		img = IconLibrary.RMBLOCKICON;
		img = img.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH); 
		btnRemovebloc.setIcon(new ImageIcon(img));
		add(btnRemovebloc, "cell 2 2,grow");
		
		
		
		// Listeners
		btnAddLink.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(linkModeActivated && linkModeActivatedSource!=null){
					if(linkModeActivatedSource != parentInternalFrame){
						addLineLinkAsChild(new LineLink(linkModeActivatedSource, parentInternalFrame));
					}
					linkModeActivated = false;
					linkModeActivatedSource = null;
					for(JScrollInternalFrame jsf : parentInternalFrame.getJsDesktopPane().getInternalFrames()){
						jsf.getTubePanel().resetBtnAddLink();
					}
				}else{
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
									}
								}
							}
						}
					}
				}
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
				if(line.getParent() != getParentInternalFrame() && line.getParent().getTubePanel().getTube().getChildren().contains(getTube()))
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
	}
	public void deactivateLinkMode(){
		btnAddLink.setBackground(null);
		btnAddLink.setEnabled(false);
	}
	public void resetBtnAddLink(){
		btnAddLink.setBackground(null);
		if(getTubeType() == TubeClass.VenousSinus)
			btnAddLink.setEnabled(false);
		else
			btnAddLink.setEnabled(true);
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
