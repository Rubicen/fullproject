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
	
	JTextArea _textArea = new JTextArea();
	JScrollPane _scrollPane = new JScrollPane(_textArea);
	JButton _save = new JButton("Save");
	String _name;
	String _fileName;
	MaskFormatter mask = new MaskFormatter("##:##:##,###");
	File f;
	private final JFormattedTextField txtStart;
	private final JFormattedTextField txtEnd;
	private final String[] colours = {"Black","Blue","Green","Yellow","Orange","Grey","White"};
	private final JLabel lblStartTime = new JLabel("Start time");
	private final JLabel lblEndTime = new JLabel("End time");
	private final JLabel lblColour = new JLabel("Colour: ");
	private JTextField subtitleText;
	JButton _generate;
	JComboBox<?> _colourCombo;
	JButton _update;
	private final JButton questionmarkbutton = new JButton("?");
	private final JSpinner numbervalue = new JSpinner();
	PlayerPanel _player = new PlayerPanel();
	private JTextField txtLong;
	
	public SubtitlePanel(PlayerPanel player) throws ParseException{
		_player = player;
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
		_scrollPane.setBackground(UIManager.getColor("Button.background"));
		_scrollPane.setBounds(12, 14, 400, 154);
		_scrollPane.setPreferredSize(new Dimension(600,180));
		add(_scrollPane);
				
		_save.setBounds(12, 175, 117, 25);
		_save.addActionListener(this);
		add(_save);
		
		SpinnerModel model = new SpinnerNumberModel(0,0,99999,1);
		numbervalue.setModel(model);
		add(numbervalue);
		
		JLabel lblNumber = new JLabel("Number:");
		lblNumber.setBounds(448, 15, 70, 15);
		add(lblNumber);
		
		add(txtStart);
		
		add(txtEnd);
		
		_generate = new JButton("Generate");
		_generate.setBounds(628, 175, 117, 25);
		add(_generate);
		
		_update = new JButton("Update");
		_update.setBounds(141, 175, 117, 25);
		add(_update);
		lblStartTime.setBounds(448, 42, 90, 15);
		
		add(lblStartTime);
		lblEndTime.setBounds(631, 42, 70, 15);
		
		add(lblEndTime);
		
		_colourCombo = new JComboBox<Object>(colours);
		_colourCombo.setBounds(518, 87, 90, 24);
		add(_colourCombo);
		lblColour.setBounds(448, 92, 70, 15);
		
		add(lblColour);
		
		subtitleText = new JTextField();
		subtitleText.setBounds(448, 132, 297, 19);
		add(subtitleText);
		subtitleText.setColumns(10);
		
		JLabel lblSubtitle = new JLabel("Subtitle");
		lblSubtitle.setBounds(448, 119, 70, 15);
		add(lblSubtitle);
		
		_generate.addActionListener(this);
		questionmarkbutton.setBounds(700, 10, 48, 25);
		
		add(questionmarkbutton);
		questionmarkbutton.addActionListener(this);
		numbervalue.setBounds(521, 13, 64, 20);
		
		add(numbervalue);
		
		txtLong = new JTextField();
		txtLong.setBounds(298, 175, 114, 19);
		add(txtLong);
		txtLong.setColumns(10);
		txtLong.setFocusable(false);
		
	
		_save.setEnabled(false);
		_textArea.setEnabled(false);
		_generate.setEnabled(false);
		_update.setEnabled(false);
	}
	
	
	
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(_save)){
			try {
				
				if(!f.equals(null)){
					FileWriter write = new FileWriter(f);
					write.append(_textArea.getText());
					write.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else if(arg0.getSource().equals(_generate)){
			if(subtitleText.getText().equals(null)||subtitleText.getText().equals("")){
				//do nothing
			}else{
				_textArea.append(numbervalue.getValue()+"\n");
				_textArea.append(txtStart.getText()+" --> "+txtEnd.getText()+"\n");
				_textArea.append("<font color=\""+_colourCombo.getSelectedItem()+"\">"+"\n");
				_textArea.append(subtitleText.getText()+"\n");
				_textArea.append("<\\font>\n");
				_textArea.append("\n");
			}
		}else if(arg0.getSource().equals(questionmarkbutton)){
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
		}
		
	}



	@Override
	public void newInput(File file, Boolean boo) {
		
		
		if(boo){
			setAllEnabled();
			txtLong.setText(""+_player.getLength()+"s long");
			String tempname = file.getPath();
			_fileName = file.getPath().split("\\.(?=[^\\.]+$)")[0];
			if(tempname.contains(".")){
				String[] nametemp = tempname.split("\\.(?=[^\\.]+$)");
				_name = nametemp[0];
				f = new File(_name+".srt");
				String text;
				if(f.exists()){
					try{
						FileReader read = new FileReader(f);
						
						char[] chars = new char[(int) f.length()];
						read.read(chars);
						text = new String(chars);
						read.close();				
						_textArea.setText(text);
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
		_save.setEnabled(true);
		_textArea.setEnabled(true);
		_generate.setEnabled(true);
		_update.setEnabled(true);
	}
	
	public void setAllDisabled(){
		_save.setEnabled(false);
		_textArea.setEnabled(false);
		_generate.setEnabled(false);
		_update.setEnabled(false);
	}
}
