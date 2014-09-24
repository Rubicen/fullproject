import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.NativeLibrary;

import java.awt.Color;


@SuppressWarnings("serial")
public class VAMIX extends JFrame{
	
	AudioPanel audioPanel = new AudioPanel();
	DownloadPanel downloadPanel = new DownloadPanel();
	PlayerPanel playerPanel = new PlayerPanel();
	OpenPanel openPanel = new OpenPanel(this);
	TextPanel _textPanel = new TextPanel(this);
	
	public VAMIX(){
		super("VAMIX");
		setSize(800,600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		audioPanel.setBackground(Color.LIGHT_GRAY);
		audioPanel.setBounds(444, 229, 350, 175);
		getContentPane().add(audioPanel);
		downloadPanel.setBackground(Color.LIGHT_GRAY);
		downloadPanel.setBounds(444, 85, 350, 141);
		getContentPane().add(downloadPanel);
		playerPanel.setBackground(Color.LIGHT_GRAY);
		playerPanel.setBounds(0, 0, 441, 404);
		getContentPane().add(playerPanel);
		openPanel.setBackground(Color.LIGHT_GRAY);
		openPanel.setBounds(444, 0, 350, 84);
		getContentPane().add(openPanel);
		_textPanel.setBounds(5, 408, 784, 160);
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
}
