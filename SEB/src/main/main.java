package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import models.Hemisphere;
import models.Variable;
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
		System.out.println("Hello");
		StandardModel.run();
	}
}
