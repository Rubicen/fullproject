package main;

import java.io.File;
import java.text.ParseException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import panels.AudioPanel;
import panels.DownloadPanel;
import panels.OpenPanel;
import panels.PlayerPanel;
import panels.SubtitlePanel;
import panels.TextPanel;
import panels.VideoFilterPanel;


import java.awt.Color;


@SuppressWarnings("serial")
public class VAMIX extends JFrame{
	
	AudioPanel _audioPanel = new AudioPanel(this);
	DownloadPanel _downloadPanel = new DownloadPanel();
	PlayerPanel _playerPanel = new PlayerPanel();
	
	TextPanel _textPanel = new TextPanel(this);
	JTextField _outName = new JTextField();
	JLabel _outNameLabel = new JLabel("Outname here: ");
	JTabbedPane _tabs = new JTabbedPane();
	VideoFilterPanel _filters = new VideoFilterPanel(_playerPanel);
	SubtitlePanel _subtitles;
	OpenPanel _openPanel = new OpenPanel(this);
	
	public VAMIX(){
		super("VAMIX");
		_tabs.setForeground(Color.GRAY);
		getContentPane().setBackground(Color.DARK_GRAY);
		getContentPane().setForeground(Color.BLACK);
		setBackground(Color.DARK_GRAY);
		try {
			_subtitles = new SubtitlePanel(_playerPanel);
			_subtitles.setBackground(Color.GRAY);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		setSize(800,660);
		getContentPane().setLayout(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.setResizable(false);
		_outName.setBounds(458, 374, 336, 19);
		getContentPane().add(_outName);
		_outName.setColumns(10);
		
		JLabel lblOutnameHere = new JLabel("Outname here: ");
		lblOutnameHere.setBounds(458, 359, 133, 15);
		getContentPane().add(lblOutnameHere);
		_audioPanel.setBackground(Color.GRAY);
		_audioPanel.setBounds(444, 229, 350, 175);
		getContentPane().add(_audioPanel);
		_downloadPanel.setBackground(Color.GRAY);
		_downloadPanel.setBounds(444, 85, 350, 141);
		getContentPane().add(_downloadPanel);
		_playerPanel.setBackground(Color.GRAY);
		_playerPanel.setBounds(5, 0, 435, 404);
		getContentPane().add(_playerPanel);
		_openPanel.setBackground(Color.GRAY);
		_openPanel.setBounds(444, 0, 350, 84);
		getContentPane().add(_openPanel);
		_tabs.setBorder(null);
		_tabs.setBackground(Color.GRAY);
		_tabs.setBounds(5, 408, 788, 230);
		getContentPane().add(_tabs);
		_textPanel.setBackground(Color.GRAY);
		_textPanel.setBorder(null);
		_tabs.add(_textPanel,"Text");
		_filters.setBackground(Color.GRAY);
		_tabs.add(_filters,"Filters");
		_tabs.add(_subtitles,"Subtitles");
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
		
		_audioPanel.newInput(selectedFile,boo);
		_playerPanel.newInput(selectedFile,boo);
		_textPanel.newInput(selectedFile,boo);
		_subtitles.newInput(selectedFile, boo);
		_filters.newInput(selectedFile,  boo);
		
		
	}
	
	/**
	 * Get the length of the current video
	 */
	public long getLength(){
		return _playerPanel.getLength();
	}
	
	/**
	 * gets the outname of the text for the other boxes that require it for outputting files
	 */
	public String getOutName(){
		return _outName.getText();
	}
	
}
