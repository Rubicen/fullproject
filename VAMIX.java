import java.io.File;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.NativeLibrary;

import java.awt.Color;
import java.awt.Dimension;


@SuppressWarnings("serial")
public class VAMIX extends JFrame{
	
	AudioPanel audioPanel = new AudioPanel(this);
	DownloadPanel downloadPanel = new DownloadPanel();
	PlayerPanel playerPanel = new PlayerPanel();
	OpenPanel openPanel = new OpenPanel(this);
	TextPanel _textPanel = new TextPanel(this);
	JTextField _outName = new JTextField();
	JLabel _outNameLabel = new JLabel("Outname here: ");
	
	public VAMIX(){
		super("VAMIX");
		setSize(800,620);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		
		_outName.setBounds(452, 385, 336, 19);
		getContentPane().add(_outName);
		_outName.setColumns(10);
		
		JLabel lblOutnameHere = new JLabel("Outname here: ");
		lblOutnameHere.setBounds(458, 359, 133, 15);
		getContentPane().add(lblOutnameHere);
		audioPanel.setBackground(Color.LIGHT_GRAY);
		audioPanel.setBounds(444, 229, 350, 180);
		getContentPane().add(audioPanel);
		downloadPanel.setBackground(Color.LIGHT_GRAY);
		downloadPanel.setBounds(444, 85, 350, 141);
		getContentPane().add(downloadPanel);
		playerPanel.setBackground(Color.LIGHT_GRAY);
		playerPanel.setBounds(5, 0, 435, 404);
		getContentPane().add(playerPanel);
		openPanel.setBackground(Color.LIGHT_GRAY);
		openPanel.setBounds(444, 0, 350, 84);
		getContentPane().add(openPanel);
		_textPanel.setBounds(5, 408, 788, 200);
		getContentPane().add(_textPanel);
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				VAMIX frame = new VAMIX();
				frame.setVisible(true);
			}
		});
	}

	/**
	 * Update all components with the currently selected file
	 * @param selectedFile the selected file
	 */
	public void updateFile(File selectedFile) {
		audioPanel.newInput(selectedFile);
		playerPanel.newInput(selectedFile);
		//text add here TODO
	}
	
	/**
	 * Get the length of the current video
	 * @return
	 */
	public long getLength(){
		return playerPanel.getLength();
	}
	
	public String getOutName(){
		return _outName.getText();
	}
}
