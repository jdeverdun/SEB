package com.display.images;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.display.ArchitectureToolbar;

public class IconLibrary {
	public static Image EDITVARIABLESICON = null;
	public static Image ARTERIOLE = null;
	public static Image ARTERY = null;
	public static Image FIRSTARTERY = null;
	public static Image CAPILLARY = null;
	public static Image VEINULE = null;
	public static Image VEIN = null;
	public static Image VENOUSSENOUS = null;
	public static Image LINKICON = null;
	public static Image CHARTICON = null;
	public static Image RMBLOCKICON = null;
	public static Image RESTOREICON = null;
	public static Image NEWICON = null;
	public static Image ORGANIZEICON = null;
	public static Image RUNICON = null;
	public static Image VENTRICLE = null;
	public static Image THIRDVENTRICLE = null;
	public static Image FOURTHVENTRICLE = null;
	public static Image SAS = null;
	public static Image SPINAL = null;
	/**
	 * Load each icon
	 */
	public static void load(){
		try {
			FIRSTARTERY = ImageIO.read(ArchitectureToolbar.class.getResource("images/artery1.png"));
			ARTERY = ImageIO.read(ArchitectureToolbar.class.getResource("images/artery1.png"));
			ARTERIOLE = ImageIO.read(ArchitectureToolbar.class.getResource("images/arteriole1.png"));
			CAPILLARY = ImageIO.read(ArchitectureToolbar.class.getResource("images/capillary1.png"));
			VEINULE = ImageIO.read(ArchitectureToolbar.class.getResource("images/veinule1.png"));
			VEIN = ImageIO.read(ArchitectureToolbar.class.getResource("images/vein1.png"));
			VENOUSSENOUS = ImageIO.read(ArchitectureToolbar.class.getResource("images/vein1.png"));
			LINKICON = ImageIO.read(ArchitectureToolbar.class.getResource("images/Link-icon.png"));
			CHARTICON = ImageIO.read(ArchitectureToolbar.class.getResource("images/Charts-Line-icon.png"));
			RMBLOCKICON = ImageIO.read(ArchitectureToolbar.class.getResource("images/removeblock.png"));
			EDITVARIABLESICON = ImageIO.read(ArchitectureToolbar.class.getResource("images/Editing-Edit-icon.png"));
			RESTOREICON = ImageIO.read(ArchitectureToolbar.class.getResource("images/restore-icon.png"));
			NEWICON = ImageIO.read(ArchitectureToolbar.class.getResource("images/new-icon.png"));
			ORGANIZEICON = ImageIO.read(ArchitectureToolbar.class.getResource("images/organize-icon.png"));
			RUNICON = ImageIO.read(ArchitectureToolbar.class.getResource("images/run-icon.png"));
			VENTRICLE = ImageIO.read(ArchitectureToolbar.class.getResource("images/run-icon.png"));
			THIRDVENTRICLE = ImageIO.read(ArchitectureToolbar.class.getResource("images/run-icon.png"));
			FOURTHVENTRICLE = ImageIO.read(ArchitectureToolbar.class.getResource("images/run-icon.png"));
			SAS = ImageIO.read(ArchitectureToolbar.class.getResource("images/run-icon.png"));
			SPINAL = ImageIO.read(ArchitectureToolbar.class.getResource("images/run-icon.png"));
					
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
