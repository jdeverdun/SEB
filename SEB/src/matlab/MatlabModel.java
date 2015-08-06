package matlab;

import java.nio.file.Path;

/**
 * Permet de gerer les fichiers associes au modele
 * @author DEVERDUN Jeremy
 *
 */
public class MatlabModel {
	private Path mainScript;
	private Path equationsInitiales;
	private Path equationsTime;
	
	public MatlabModel(Path main, Path eqInitiales, Path eqTime){
		setMainScript(main);
		setEquationsInitiales(eqInitiales);
		setEquationsTime(eqTime);
	}
	
	public Path getMainScript() {
		return mainScript;
	}
	public void setMainScript(Path mainScript) {
		this.mainScript = mainScript;
	}
	public Path getEquationsInitiales() {
		return equationsInitiales;
	}
	public void setEquationsInitiales(Path equationsInitiales) {
		this.equationsInitiales = equationsInitiales;
	}
	public Path getEquationsTime() {
		return equationsTime;
	}
	public void setEquationsTime(Path equationsTime) {
		this.equationsTime = equationsTime;
	}
}
