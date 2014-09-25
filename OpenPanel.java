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
	
	private JLabel _input = new JLabel("Input file:   ");
	private JTextField _inputFile = new JTextField();
	private JButton _selectFile = new JButton("Choose File");
	private JFileChooser _fc = new JFileChooser();
	
	private VAMIX _mainPanel;
	
	/**
	 * Constructor for OpenPanel
	 * @param main
	 */
	public OpenPanel(VAMIX main){
		_mainPanel = main;
		
		add(_input);
		_inputFile.setPreferredSize(new Dimension(200,19));
		add(_inputFile);
		_inputFile.setFocusable(false);
		
		add(_selectFile);
		_selectFile.addActionListener(this);
	}
	
	//Select file button is pressed
	public void actionPerformed(ActionEvent a) {
		//Open FileChooser and put selected file in text field
		_fc.showOpenDialog(this);
		
		//Check if the selected file is an audio or video file
		String chosenFile = _fc.getSelectedFile().getPath();
		String cmd = "file -ib " + chosenFile + " | grep -i \"video\\|mpeg\\|octet-stream\\|audio\"";
		int isValid = bashCommand(cmd);
		
		if(isValid == 0){
			Boolean boo;
			_inputFile.setText(chosenFile);
			//Have main frame update all panels with the new file
			if(bashCommand("file -ib " + chosenFile + " | grep -i \"video\\|octet-stream\"")==0){
				boo = true;
			}else{
				boo=false;
			}
			_mainPanel.updateFile(_fc.getSelectedFile(),boo);
		}else{
			JOptionPane.showMessageDialog(this, chosenFile + " is not a valid file");
		}
	}

	void newInput(File file,Boolean boo) {
		//Not required by this class
	}

}
