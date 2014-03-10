package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jscroll.widgets.JScrollInternalFrame;

import com.display.LineLink;
import com.display.TubePanel;

import models.Arteriole;
import models.Artery;
import models.Capillary;
import models.ElasticTube;
import models.FirstArtery;
import models.FourthVentricle;
import models.Hemisphere;
import models.SAS;
import models.SpinalCord;
import models.ThirdVentricle;
import models.Tube;
import models.Vein;
import models.Veinule;
import models.VenousSinus;
import models.Ventricle;

import params.SystemParams;
import params.WindowManager;

public class SEButils {
	public static final String SAVE_PARAM_SEPARATOR = "@@";
	public static final String SAVE_NAME_PARAM_SEPARATOR = "==";
	public static final String SAVE_LINK_SEPARATOR = "-->";
	public static final String SAVE_PARAM_VALUE_SEPARATOR = ":";
	public static final String SAVE_COMMENT_CHAR = "#";
	public static final String SAVE_TUBEDEF_TAG = "TUBES";
	public static final String SAVE_LINKDEF_TAG = "LINKS";
	public static final String FILE_EXTENSION = ".seb";
	public static final String SAVE_TAG = "$";

	public final static Charset ENCODING = StandardCharsets.UTF_8;
	private static String NEWLINE_CHAR = "\n";

	public static void saveModel(){
		if(WindowManager.MAINWINDOW.getGraphicalModelPanel().getFirstArteryFrame() == null){
			SystemParams.errordlg("No first artery ...");
			return;
		}
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode( JFileChooser.FILES_ONLY );

		File selectedFile = null;
		if( fc.showOpenDialog( WindowManager.MAINWINDOW ) == JFileChooser.APPROVE_OPTION )
		{
			selectedFile = fc.getSelectedFile();
		}else{
			return;
		}
		if(!selectedFile.toString().endsWith(FILE_EXTENSION))
			selectedFile = new File(selectedFile + FILE_EXTENSION);
		
		HashMap<String, String> paramsByTube = new HashMap<String,String>();
		HashSet<String> links = new HashSet<String>();
		// on recupere les infos de facon recursive a partir de first artery
		WindowManager.MAINWINDOW.getGraphicalModelPanel().getFirstArteryFrame().getTubePanel().saveInfoTo(paramsByTube,links);
		// on integre le LCR
		WindowManager.MAINWINDOW.getGraphicalModelPanel().getVentricleleftFrame().getTubePanel().saveInfoTo(paramsByTube,links);
		WindowManager.MAINWINDOW.getGraphicalModelPanel().getVentriclerightFrame().getTubePanel().saveInfoTo(paramsByTube,links);

		// on ecrit dans le fichier
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		ArrayList<String> content = new ArrayList<String>();
		content.add(SAVE_COMMENT_CHAR +" File generated by "+SystemParams.PROGRAM_NAME+" Version : "+SystemParams.VERSION+"") ;
		content.add(SAVE_COMMENT_CHAR+" Date : "+dateFormat.format(date)+"");
		// on ajoute la valeur du compteur des tubes
		content.add(Tube.ID_LABEL+SAVE_NAME_PARAM_SEPARATOR+Tube.ID);
		// on commence les tubes
		content.add(SAVE_TAG+SAVE_TUBEDEF_TAG);
		for(String key:paramsByTube.keySet())
			content.add(key+SAVE_NAME_PARAM_SEPARATOR+paramsByTube.get(key));
		content.add(SAVE_COMMENT_CHAR+" Now the links");
		// on gere les liens
		content.add(SAVE_TAG+SAVE_LINKDEF_TAG);
		for(String link:links)
			content.add(link);
		try {
			writeLargerTextFile(selectedFile,content);
		} catch (IOException e) {
			SystemParams.errordlg("Error while saving file ["+e.toString()+"]");
			return;
		}
	}

	public static void writeLargerTextFile(File file, List<String> aLines) throws IOException {
		Path path = file.toPath();
		try (BufferedWriter writer = Files.newBufferedWriter(path, ENCODING)){
			for(String line : aLines){
				writer.write(line);
				writer.newLine();
			}
		}
	}
	
	public static void loadModel(){
		JFileChooser fc = new JFileChooser();
		FileFilter sebFilter = new FileNameExtensionFilter("SEB models", FILE_EXTENSION.substring(1), "txt");
		fc.setFileSelectionMode( JFileChooser.FILES_ONLY );
		fc.addChoosableFileFilter(sebFilter);
		fc.setFileFilter(sebFilter);
		fc.setAcceptAllFileFilterUsed(false);
		File selectedFile = null;
		if( fc.showOpenDialog( WindowManager.MAINWINDOW ) == JFileChooser.APPROVE_OPTION ){
			selectedFile = fc.getSelectedFile();
			if(WindowManager.MAINWINDOW != null)
				WindowManager.MAINWINDOW.getGraphicalModelPanel().initNew();
	        BufferedReader reader;
			try {
				reader = Files.newBufferedReader(selectedFile.toPath(), ENCODING);
		        String line = "";
		        String currentTag = "";
		        int maxid = 0;
		        ArrayList<TubePanel> tubeList = new ArrayList<TubePanel>();
		        // Lecture du fichier
		        while ((line = reader.readLine()) != null) {
		        	if(line.startsWith(SAVE_COMMENT_CHAR))
		            	continue;
		            if(line.startsWith(Tube.ID_LABEL))
		            	maxid = Integer.parseInt(line.split(SAVE_NAME_PARAM_SEPARATOR)[1]);
		            if(line.startsWith(SAVE_TAG)){
		            	currentTag = line.substring(SAVE_TAG.length());
		            	continue;
		            }
		            switch(currentTag){
		            case SAVE_TUBEDEF_TAG:
		            	// on ajoute les tubes
		            	tubeList.add(parseAndAddTube(line));
		            	break;
		            case SAVE_LINKDEF_TAG:
		        		Tube.ID = maxid;
		        		// on ajoute les liens
		        		addLink(line,tubeList);
		            	break;
		            }
		        }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			return;
		}
	}

	/**
	 * Add link from line
	 * @param line
	 * @param tubeList 
	 */
	private static void addLink(String line, ArrayList<TubePanel> tubeList) {
		String[] parts = line.split(SAVE_LINK_SEPARATOR);
		TubePanel parent = null;
		for(TubePanel pan : tubeList){
			if(pan.getTube().getName().equals(parts[0])){
				parent = pan;
				for(TubePanel pan2:tubeList){
					if(pan2.getTube().getName().equals(parts[1])){
						pan2.addLineLinkAsChild(new LineLink(parent.getParentInternalFrame(), pan2.getParentInternalFrame()));
						return;
					}
				}
			}
		}
	}

	/**
	 * A partir d'une ligne, recree un tube et l'ajoute dans le model
	 * @param line
	 */
	private static TubePanel parseAndAddTube(String line) {
		String[] base = line.split(SAVE_NAME_PARAM_SEPARATOR);
		String name = base[0];
		
		String[] cont = name.split("_");
		Hemisphere hemi = null;
		String tubenum = "";
		int tubeid;
		// on parse le nom
		if(cont.length==3){
			tubenum = cont[1].substring(1);
			switch(cont[0].charAt(cont[0].length()-1)){
			case 'R':
				hemi = Hemisphere.RIGHT;break;
			case 'L':
				hemi = Hemisphere.LEFT;break;
			}
			tubeid = Integer.parseInt(cont[2].substring(0, cont[2].length()-1));
		}else{
			tubenum = cont[0].substring(3);
			tubeid = Integer.parseInt(cont[1].substring(0, cont[1].length()-1));
		}
		// on recupere les params
		String[] params = base[1].split(SAVE_PARAM_SEPARATOR);
		// on cree des tubes
		TubePanel tubepan = null;
		switch(tubenum){
		case FirstArtery.TUBE_NUM:
			tubepan = WindowManager.MAINWINDOW.getGraphicalModelPanel().getFirstArteryFrame().getTubePanel();
			break;
		case VenousSinus.TUBE_NUM:
			tubepan = WindowManager.MAINWINDOW.getGraphicalModelPanel().getVenousSinousFrame().getTubePanel();
			break;
		case Ventricle.TUBE_NUM:
			if(hemi == Hemisphere.LEFT)
				tubepan = WindowManager.MAINWINDOW.getGraphicalModelPanel().getVentricleleftFrame().getTubePanel();
			else
				tubepan = WindowManager.MAINWINDOW.getGraphicalModelPanel().getVentriclerightFrame().getTubePanel();
			break;
		case ThirdVentricle.TUBE_NUM:
			tubepan = WindowManager.MAINWINDOW.getGraphicalModelPanel().getThirdVentFrame().getTubePanel();
			break;
		case FourthVentricle.TUBE_NUM:
			tubepan = WindowManager.MAINWINDOW.getGraphicalModelPanel().getFourthVentFrame().getTubePanel();
			break;
		case SAS.TUBE_NUM:
			tubepan = WindowManager.MAINWINDOW.getGraphicalModelPanel().getSasFrame().getTubePanel();
			break;
		case SpinalCord.TUBE_NUM:
			tubepan = WindowManager.MAINWINDOW.getGraphicalModelPanel().getSpinalFrame().getTubePanel();
			break;
		case Artery.TUBE_NUM:
			tubepan = ((JScrollInternalFrame)WindowManager.MAINWINDOW.getGraphicalModelPanel().addArtery(hemi)).getTubePanel();
			break;
		case Arteriole.TUBE_NUM:
			tubepan = ((JScrollInternalFrame)WindowManager.MAINWINDOW.getGraphicalModelPanel().addArteriole(hemi)).getTubePanel();
			break;
		case Capillary.TUBE_NUM:
			tubepan = ((JScrollInternalFrame)WindowManager.MAINWINDOW.getGraphicalModelPanel().addCapillary(hemi)).getTubePanel();
			break;
		case Veinule.TUBE_NUM:
			tubepan = ((JScrollInternalFrame)WindowManager.MAINWINDOW.getGraphicalModelPanel().addVeinule(hemi)).getTubePanel();
			break;
		case Vein.TUBE_NUM:
			tubepan = ((JScrollInternalFrame)WindowManager.MAINWINDOW.getGraphicalModelPanel().addVein(hemi)).getTubePanel();
			break;
		default:
			System.err.println("Unknown tube ...");
			break;
		}
		tubepan.getTube().setName(name);
		tubepan.getParentInternalFrame().setTitle(name);
		tubepan.getTube().setMyID(tubeid);
		fillTubeInfo(tubepan.getTube(),params);
		tubepan.refreshDisplayFromTube();
		return tubepan;
		
	}

	/**
	 * Remplit les parametres des tubes  a partir d'un tableau
	 * @param tube
	 * @param params
	 */
	private static void fillTubeInfo(ElasticTube tube, String[] params) {
		for(String param:params){
			String[] spl = param.split(SAVE_PARAM_VALUE_SEPARATOR);
			String name = spl[0];
			float value = Float.parseFloat(spl[1]);
			String[] parts = name.split("_");
			String nameparam;
			if(parts.length==3)
				nameparam = parts[1];
			else
				nameparam = parts[2];
			if(nameparam.equals(ElasticTube.LENGTH_LABEL))
				tube.setLength(value);
			if(nameparam.equals(ElasticTube.ALPHA_LABEL))
				tube.setAlpha(value);
			if(nameparam.equals(ElasticTube.ELASTANCE_LABEL))
				tube.setElastance(value);
			if(nameparam.equals(ElasticTube.AREA_LABEL))
				tube.setArea(value);
			if(nameparam.equals(ElasticTube.INITIAL_AREA_LABEL))
				tube.setInitialArea(value);
			if(nameparam.equals(ElasticTube.FLOWIN_LABEL))
				tube.setFlowin(value);	
			if(nameparam.equals(ElasticTube.FLOWOUT_LABEL))
				tube.setFlowout(value);
			if(nameparam.equals(ElasticTube.PRESSURE_LABEL))
				tube.setPressure(value);
		}
	}
}
