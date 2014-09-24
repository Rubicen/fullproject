import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class OpenPanel extends VamixPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private JLabel input = new JLabel("Input file:   ");
	private JTextField inputFile = new JTextField();
	private JButton selectFile = new JButton("Choose File");
	private JFileChooser fc = new JFileChooser();
	
	private VAMIX mainPanel;
	
	/**
	 * Constructor for OpenPanel
	 * @param main
	 */
	public OpenPanel(VAMIX main){
		mainPanel = main;
		
		add(input);
		inputFile.setPreferredSize(new Dimension(200,19));
		add(inputFile);
		inputFile.setFocusable(false);
		
		add(selectFile);
		selectFile.addActionListener(this);
	}
	
	//Select file button is pressed
	public void actionPerformed(ActionEvent a) {
		//Open FileChooser and put selected file in text field
		fc.showOpenDialog(this);
		
		//Check if the selected file is an audio or video file
		String chosenFile = fc.getSelectedFile().getPath();
		String cmd = "file -ib " + chosenFile + " | grep -i \"video\\|mpeg\\|octet-stream\\|audio\"";
		int isValid = bashCommand(cmd);
		
		if(isValid == 0){
			Boolean boo;
			inputFile.setText(chosenFile);
			//Have main frame update all panels with the new file
			if(bashCommand("file -ib " + chosenFile + " | grep -i \"video\\|octet-stream\"")==0){
				boo = true;
			}else{
				boo=false;
			}
			mainPanel.updateFile(fc.getSelectedFile(),boo);
		}else{
			JOptionPane.showMessageDialog(this, chosenFile + " is not a valid file");
		}
	}

	void newInput(File file,Boolean boo) {
		//Not required by this class
	}

}
