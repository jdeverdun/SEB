package com.display;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.FlowLayout;

import models.ElasticTube;
import net.miginfocom.swing.MigLayout;
import javax.swing.JButton;

import org.jscroll.widgets.JScrollInternalFrame;

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
	
	public TubePanel( ElasticTube tube, TubeClass type) {
		setTube(tube);
        setLineLinks(new ArrayList<LineLink>());
        setTubeType(type);
        
		setPreferredSize(new Dimension(80, 80));
		setLayout(new MigLayout("", "[grow][grow][grow]", "[grow][grow][grow]"));
		
		btnAddLink = new JButton("Add Link");
		add(btnAddLink, "cell 1 2,grow");
		
		
		btnAddLink.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(linkModeActivated && linkModeActivatedSource!=null){
					if(linkModeActivatedSource != parentInternalFrame){
						lineLinks.add(new LineLink(linkModeActivatedSource, parentInternalFrame));
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
						if(jsf != parentInternalFrame){
							TubePanel ltubep = jsf.getTubePanel();
							if(tubeType == ltubep.getTubeType() && (tubeType == TubeClass.Artery || tubeType == TubeClass.Vein)){
								ltubep.activateLinkMode();
							}else{
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
		});
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
		btnAddLink.setEnabled(true);
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
	
	public void addLineLinkAsParent(LineLink line){
		this.lineLinks.add(line);
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
