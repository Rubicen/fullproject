package panels.basic;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import main.VAMIX;

/**
 * 
 * @author logan and sam
 *
 */
public class OpenPanel extends VamixPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private JLabel inputLabel = new JLabel("Input file:   ");
	private JTextField inputFileTextField = new JTextField();
	private JButton selectFileButton = new JButton("Choose File");
	private JFileChooser fileChooser = new JFileChooser();
	
	private VAMIX mainPanel;
	
	/**
	 * Constructor for OpenPanel
	 * @param main
	 */
	public OpenPanel(VAMIX main){
		mainPanel = main;
		
		add(inputLabel);
		inputFileTextField.setPreferredSize(new Dimension(200,19));
		add(inputFileTextField);
		inputFileTextField.setFocusable(false);
		
		add(selectFileButton);
		selectFileButton.addActionListener(this);
	}
	
	//Select file button is pressed
	public void actionPerformed(ActionEvent a) {
		//Open FileChooser and put selected file in text field
		int check = fileChooser.showOpenDialog(this);
		
		if(check==JFileChooser.APPROVE_OPTION){
			//Check if the selected file is an audio or video file
			String chosenFile = fileChooser.getSelectedFile().getPath();
			String cmd = "file -ib " + chosenFile + " | grep -i \"video\\|mpeg\\|octet-stream\\|audio\"";
			int isValid = bashCommand(cmd);
			
			if(isValid == 0){
				Boolean boo;
				inputFileTextField.setText(chosenFile);
				//Have main frame update all panels with the new file
				if(bashCommand("file -ib " + chosenFile + " | grep -i \"video\\|octet-stream\"")==0){
					boo = true;
				}else{
					boo=false;
				}
				mainPanel.updateFile(fileChooser.getSelectedFile(),boo);
				
			}else{
				JOptionPane.showMessageDialog(this, chosenFile + " is not a valid file");
			}
		}
	}

	public void newInput(File file,Boolean boo) {
		//Not required by this class
	}

}
