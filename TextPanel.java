import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;


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
	
	private JComboBox _openFont = new JComboBox(_fonts);
	private JComboBox _openSize = new JComboBox(_fontSizes);
	private JComboBox _openColour = new JComboBox(_fontColours);
	
	private JLabel _openScene = new JLabel("Create Opening Screen?");
	private JCheckBox _openSceneCheck = new JCheckBox();
	
	private JLabel _creditLabel = new JLabel("Closing Text");
	private JTextArea _creditText = new JTextArea("Enter Text Here");
	
	private JComboBox<String> _creditFont = new JComboBox<String>(_fonts);
	private JComboBox<Integer> _creditSize = new JComboBox<Integer>(_fontSizes);
	private JComboBox<String> _creditColour = new JComboBox<String>(_fontColours);
	
	private JLabel _creditScene = new JLabel("Create Closing Screen?");
	private JCheckBox _creditSceneCheck = new JCheckBox();
	
	private boolean _openClicked = false;
	private boolean _creditClicked = false;

	public TextPanel(VAMIX main){
		//Set main
		_main = main;
		
		//Adding opening text options
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
		add(_openFont);
		add(_openSize);
		add(_openColour);
		add(_openScene);
		add(_openSceneCheck);
		
		//Adding closing text options
		add(_creditLabel);
		add(_creditText);
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
		add(_creditFont);
		add(_creditSize);
		add(_creditColour);
		add(_creditScene);
		add(_creditSceneCheck);
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
		switch((String)_openFont.getSelectedItem()){
		
		}
		
		return "Nonsense";
	}

}
