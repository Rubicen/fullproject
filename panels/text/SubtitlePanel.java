package panels.text;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;
import javax.swing.JComboBox;
import javax.swing.JSpinner;

import panels.basic.VamixPanel;
import panels.player.PlayerPanel;


@SuppressWarnings("serial")
public class SubtitlePanel extends VamixPanel implements ActionListener{
	
	JTextArea textArea = new JTextArea();
	JScrollPane scrollPane = new JScrollPane(textArea);
	JButton saveButton = new JButton("Save");
	String name;
	String fileName;
	MaskFormatter mask = new MaskFormatter("##:##:##,###");
	File f;
	private final JFormattedTextField txtStart;
	private final JFormattedTextField txtEnd;
	private final String[] colours = {"Black","Blue","Green","Yellow","Orange","Grey","White"};
	private final JLabel lblStartTime = new JLabel("Start time");
	private final JLabel lblEndTime = new JLabel("End time");
	private final JLabel lblColour = new JLabel("Colour: ");
	private JTextField subtitleText;
	JButton generateButton;
	JComboBox<?> colourCombo;
	JButton updateButton;
	private final JButton questionMarkButton = new JButton("?");
	private final JSpinner numberValueSpinner = new JSpinner();
	PlayerPanel player = new PlayerPanel();
	private JTextField txtLong;
	
	public SubtitlePanel(PlayerPanel p) throws ParseException{
		player = p;
		mask.setPlaceholderCharacter('0');
		txtStart = new JFormattedTextField(mask);
		txtEnd = new JFormattedTextField(mask);
		txtEnd.setBounds(631, 61, 114, 19);
		txtEnd.setColumns(10);
		txtStart.setBounds(448, 61, 114, 19);
		txtStart.setColumns(10);
		setBackground(Color.LIGHT_GRAY);
		setPreferredSize(new Dimension(794, 200));
		setLayout(null);
		scrollPane.setBackground(UIManager.getColor("Button.background"));
		scrollPane.setBounds(12, 14, 400, 154);
		scrollPane.setPreferredSize(new Dimension(600,180));
		add(scrollPane);
				
		saveButton.setBounds(12, 175, 117, 25);
		saveButton.addActionListener(this);
		add(saveButton);
		
		SpinnerModel model = new SpinnerNumberModel(0,0,99999,1);
		numberValueSpinner.setModel(model);
		add(numberValueSpinner);
		
		JLabel lblNumber = new JLabel("Number:");
		lblNumber.setBounds(448, 15, 70, 15);
		add(lblNumber);
		
		add(txtStart);
		
		add(txtEnd);
		
		generateButton = new JButton("Generate");
		generateButton.setBounds(628, 175, 117, 25);
		add(generateButton);
		
		updateButton = new JButton("Update");
		updateButton.setBounds(141, 175, 117, 25);
		updateButton.addActionListener(this);
		add(updateButton);
		lblStartTime.setBounds(448, 42, 90, 15);
		
		add(lblStartTime);
		lblEndTime.setBounds(631, 42, 70, 15);
		
		add(lblEndTime);
		
		colourCombo = new JComboBox<Object>(colours);
		colourCombo.setBounds(518, 87, 90, 24);
		add(colourCombo);
		lblColour.setBounds(448, 92, 70, 15);
		
		add(lblColour);
		
		subtitleText = new JTextField();
		subtitleText.setBounds(448, 132, 297, 19);
		add(subtitleText);
		subtitleText.setColumns(10);
		
		JLabel lblSubtitle = new JLabel("Subtitle");
		lblSubtitle.setBounds(448, 119, 70, 15);
		add(lblSubtitle);
		
		generateButton.addActionListener(this);
		questionMarkButton.setBounds(700, 10, 48, 25);
		
		add(questionMarkButton);
		questionMarkButton.addActionListener(this);
		numberValueSpinner.setBounds(521, 13, 64, 20);
		
		add(numberValueSpinner);
		
		txtLong = new JTextField();
		txtLong.setBounds(298, 175, 114, 19);
		add(txtLong);
		txtLong.setColumns(10);
		txtLong.setFocusable(false);
		
	
		saveButton.setEnabled(false);
		textArea.setEnabled(false);
		generateButton.setEnabled(false);
		updateButton.setEnabled(false);
	}
	
	
	
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(saveButton)){
			try {
				
				if(!f.equals(null)){
					FileWriter write = new FileWriter(f);
					write.append(textArea.getText());
					write.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(arg0.getSource().equals(generateButton)){
			if(subtitleText.getText().equals(null)||subtitleText.getText().equals("")){
				//do nothing
			}else{
				textArea.append(numberValueSpinner.getValue()+"\n");
				textArea.append(txtStart.getText()+" --> "+txtEnd.getText()+"\n");
				textArea.append("<font color=\""+colourCombo.getSelectedItem()+"\">"+"\n");
				textArea.append(subtitleText.getText()+"\n");
				textArea.append("\n");
			}
		}else if(arg0.getSource().equals(questionMarkButton)){
			Object[] options = {"Ok"};
			@SuppressWarnings("unused")
			int optionPicked = JOptionPane.showOptionDialog(null,
		    "The template for the subtitles is as follows -\n\n" +
		    "#\n" +
		    "startTime --> endTime\n" +
		    "subtitle/colour able to be on multiple lines\n" +
		    "blank line\n\n" +
		    "Filling in the text boxes on the panel and entering\n" +
		    "the correct values will correctly format the subtiltes\n" +
		    "for you, making the process easier. You can also edit the\n" +
		    "subtitles in the text area where the subtitles are displayed.\n" +
		    "You can also save the subtitles written to the correct file,\n" +
		    "which will overwrite the one you have if you have one for the\n" +
		    "loaded video file. If you have issues, refer to the manual for\n" +
		    "more information.","Instructions and Help",JOptionPane.YES_OPTION,
		    JOptionPane.QUESTION_MESSAGE,null,options,null);
		}else if(arg0.getSource().equals(updateButton)){
			player.resetPlayer();
		}
		
	}



	@Override
	public void newInput(File file, Boolean boo) {
		
		
		if(boo){
			setAllEnabled();
			txtLong.setText(""+player.getLength()+"s long");
			String tempname = file.getPath();
			fileName = file.getPath().split("\\.(?=[^\\.]+$)")[0];
			if(tempname.contains(".")){
				String[] nametemp = tempname.split("\\.(?=[^\\.]+$)");
				name = nametemp[0];
				f = new File(name+".srt");
				String text;
				if(f.exists()){
					try{
						FileReader read = new FileReader(f);
						
						char[] chars = new char[(int) f.length()];
						read.read(chars);
						text = new String(chars);
						read.close();				
						textArea.setText(text);
					}catch(IOException e){
						e.printStackTrace();
					}
				}
				
			}
		}else{
			setAllDisabled();
		}
		
	}
	public void setAllEnabled(){
		saveButton.setEnabled(true);
		textArea.setEnabled(true);
		generateButton.setEnabled(true);
		updateButton.setEnabled(true);
	}
	
	public void setAllDisabled(){
		saveButton.setEnabled(false);
		textArea.setEnabled(false);
		generateButton.setEnabled(false);
		updateButton.setEnabled(false);
	}
}
