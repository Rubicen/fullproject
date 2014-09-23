
import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;


public abstract class VamixPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Abstract method that will be used to tell panel what to do when a
	 * file is opened in the OpenPanel
	 */
	abstract void newInput(File file);
	
	/**
	 * Helper method that starts the given command in bash
	 * @param cmd the command to be executed
	 */
	protected int bashCommand(String cmd){
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		
		try {
			Process process = builder.start();
			return process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return 1;
	}
}
