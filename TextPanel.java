import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;


public class TextPanel extends VamixPanel{

	private static final long serialVersionUID = 1L;
	
	private File _file;
	
	private VAMIX _main;
	
	//Lists containing font options
	private String[] _fonts = {"Mono", "Sans-Serif", "Serif"};
	private String[] _fontColours = {"White", "Black", "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Violet"};
	private Integer[] _fontSizes = {16, 18, 20, 22, 24, 28, 32, 36, 40, 44, 48, 54, 60};
	
	private JLabel _openLabel = new JLabel("Opening Text");
	private JTextArea _openText = new JTextArea("Enter Text Here");
	
//	private JComboBox<String> _openFont = new JComboBox<String>(_fonts);
//	private JComboBox<Integer> _openSize = new JComboBox<Integer>(_fontSizes);
//	private JComboBox<String> _openColour = new JComboBox<String>(_fontColours);
	
	private JLabel _openScene = new JLabel("Create Opening Screen?");
	private JCheckBox _openSceneCheck = new JCheckBox();
	
	private JLabel _creditLabel = new JLabel("Closing Text");
	private JTextArea _creditText = new JTextArea("Enter Text Here");
	
//	private JComboBox<String> _creditFont = new JComboBox<String>(_fonts);
//	private JComboBox<Integer> _creditSize = new JComboBox<Integer>(_fontSizes);
//	private JComboBox<String> _creditColour = new JComboBox<String>(_fontColours);
	
	private boolean _openClicked = false;
	private boolean _creditClicked = false;
	private final JComboBox _openFont = new JComboBox(_fonts);
	private final JComboBox _openSize = new JComboBox(_fontSizes);
	private final JComboBox _openColour = new JComboBox(_fontColours);
	private final JComboBox _creditFont = new JComboBox(_fonts);
	private final JComboBox _creditSize = new JComboBox(_fontSizes);
	private final JComboBox _creditColour = new JComboBox(_fontColours);
	private final JButton btnLoadState = new JButton("Load State");
	private final JButton btnNewButton_1 = new JButton("Add text");

	public TextPanel(VAMIX main){
		setBackground(Color.LIGHT_GRAY);
		//Set main
		_main = main;
		_openText.setBounds(12, 26, 257, 124);
		
		//Adding opening text options
		_openText.setPreferredSize(new Dimension(600,19));
		_openText.setLineWrap(true);
		setLayout(null);
		_openLabel.setBounds(12, 5, 94, 15);
		add(_openLabel);
		add(_openText);
		//Logic that makes the enter text message vanish when clicked
		_openText.addMouseListener(new MouseAdapter(){
			@Override
            public void mouseClicked(MouseEvent e){
				if(!_openClicked){
					_openText.setText("");
					_openClicked = true;
				}
            }
		});
		
//		add(_openFont);
//		add(_openSize);
//		add(_openColour);
		_creditText.setBounds(387, 26, 268, 124);
		
		//Adding closing text options
		_creditText.setPreferredSize(new Dimension(600,19));
		_creditText.setLineWrap(true);
		_creditLabel.setBounds(388, 5, 86, 15);
		add(_creditLabel);
		add(_creditText);
		_openFont.setBounds(281, 26, 94, 24);
		add(_openFont);
		_openSize.setBounds(281, 64, 94, 24);
		
		add(_openSize);
		_openColour.setBounds(281, 101, 94, 24);
		
		add(_openColour);
		_creditFont.setBounds(667, 26, 94, 24);
		
		add(_creditFont);
		_creditSize.setBounds(667, 64, 94, 24);
		
		add(_creditSize);
		_creditColour.setBounds(667, 101, 94, 24);
		
		add(_creditColour);
		
		JButton btnNewButton = new JButton("Save State");
		btnNewButton.setBounds(12, 154, 117, 25);
		add(btnNewButton);
		btnLoadState.setBounds(152, 154, 117, 25);
		
		add(btnLoadState);
		btnNewButton_1.setBounds(644, 154, 117, 25);
		
		add(btnNewButton_1);
		
		JButton btnPreview = new JButton("Preview");
		btnPreview.setBounds(515, 154, 117, 25);
		add(btnPreview);
		//Logic that makes the enter text message vanish when clicked
		_creditText.addMouseListener(new MouseAdapter(){
			@Override
            public void mouseClicked(MouseEvent e){
				if(!_creditClicked){
					_creditText.setText("");
					_creditClicked = true;
				}
            }
		});
		
//		add(_creditFont);
//		add(_creditSize);
//		add(_creditColour);
		
	}
	
	public void newInput(File file) {
		//Set the current file to the _file field
		_file = file;
	}
	
	/**
	 * Adds input required to add text to the video (as specified) to the given command string
	 * @param input Current command string
	 * @return Command string with text part added
	 */
	public String generate(String input){
		//An example avconv that'll append text to the beginning and end
		//avconv -i themagician.rmvb -vf "drawtext=
		//fontfile='/usr/share/fonts/truetype/freefont/FreeMono.ttf':text='test text':
		//fontsize=24:fontcolor=white:draw='lt(t,10)', drawtext=
		//fontfile='/usr/share/fonts/truetype/freefont/FreeMono.ttf':text='test text':fontsize=24:
		//fontcolor=white:draw='gt(t,41)'" output3.avi
		
		//Find the length of the video
		long length = _main.getLength();
		
		//Find the input font
//		switch((String)_openFont.getSelectedItem()){
		
//		}
		
		return "Nonsense";
	}
}
