package main;

import java.io.File;
import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import panels.audio.AudioPanel;
import panels.basic.DownloadPanel;
import panels.basic.OpenPanel;
import panels.player.PlayerPanel;
import panels.text.SubtitlePanel;
import panels.text.TextPanel;
import panels.video.VideoFilterPanel;


import java.awt.Color;


@SuppressWarnings("serial")
public class VAMIX extends JFrame{
	
	
	DownloadPanel downloadPanel = new DownloadPanel();
	PlayerPanel playerPanel = new PlayerPanel();
	AudioPanel audioPanel = new AudioPanel(this,playerPanel);
	
	TextPanel textPanel = new TextPanel(this);
	JTextField outnameTextField = new JTextField();
	JLabel outNameLabel = new JLabel("Outname here: ");
	JTabbedPane tabs = new JTabbedPane();
	VideoFilterPanel filtersPanel = new VideoFilterPanel(playerPanel);
	SubtitlePanel subtitlePanel;
	OpenPanel openPanel = new OpenPanel(this);
	
	public VAMIX(){
		super("VAMIX");
		tabs.setForeground(Color.BLACK);
		getContentPane().setBackground(Color.DARK_GRAY);
		getContentPane().setForeground(Color.BLACK);
		setBackground(Color.DARK_GRAY);
		try {
			subtitlePanel = new SubtitlePanel(playerPanel);
			subtitlePanel.setBackground(Color.GRAY);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		setSize(800,660);
		getContentPane().setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setResizable(false);
		outnameTextField.setBounds(458, 374, 310, 19);
		getContentPane().add(outnameTextField);
		outnameTextField.setColumns(10);
		
		JLabel lblOutnameHere = new JLabel("Outname here: ");
		lblOutnameHere.setBounds(458, 359, 133, 15);
		getContentPane().add(lblOutnameHere);
		audioPanel.setBackground(Color.GRAY);
		audioPanel.setBounds(444, 229, 350, 175);
		getContentPane().add(audioPanel);
		downloadPanel.setBackground(Color.GRAY);
		downloadPanel.setBounds(444, 85, 350, 141);
		getContentPane().add(downloadPanel);
		playerPanel.setBackground(Color.GRAY);
		playerPanel.setBounds(5, 0, 435, 404);
		getContentPane().add(playerPanel);
		openPanel.setBackground(Color.GRAY);
		openPanel.setBounds(444, 0, 350, 84);
		getContentPane().add(openPanel);
		tabs.setBorder(null);
		tabs.setBackground(Color.GRAY);
		tabs.setBounds(5, 408, 788, 230);
		getContentPane().add(tabs);
		textPanel.setBackground(Color.GRAY);
		textPanel.setBorder(null);
		tabs.add(textPanel,"Text");
		filtersPanel.setBackground(Color.GRAY);
		tabs.add(filtersPanel,"Filters");
		tabs.add(subtitlePanel,"Subtitles");
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
	public void updateFile(File selectedFile,Boolean boo) {
		
		audioPanel.newInput(selectedFile,boo);
		playerPanel.newInput(selectedFile,boo);
		textPanel.newInput(selectedFile,boo);
		subtitlePanel.newInput(selectedFile, boo);
		filtersPanel.newInput(selectedFile,  boo);
		
		
	}
	
	/**
	 * Get the length of the current video
	 */
	public long getLength(){
		return playerPanel.getLength();
	}
	
	/**
	 * gets the outname of the text for the other boxes that require it for outputting files
	 */
	public String getOutName(){
		return outnameTextField.getText();
	}
	
}
