import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;


public class TextPanel extends VamixPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private File _file;
	
	private VAMIX _main;
	
	//Lists containing font options
	private String[] _fonts = {"Mono", "MonoBold", "Sans", "SansBold", "Serif", "SerifBold"};
	private String[] _fontColours = {"White", "Black", "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Violet"};
	private Integer[] _fontSizes = {16, 18, 20, 22, 24, 28, 32, 36, 40, 44, 48, 54, 60, 66, 72};
	
	private JLabel _openLabel = new JLabel("Opening Text - Max 80 characters");
	private JTextArea _openText = new JTextArea("Enter Text Here");
	
	
	private JLabel _creditLabel = new JLabel("Closing Text - Max 80 characters");
	private JTextArea _creditText = new JTextArea("Enter Text Here");
	
	
	private boolean _openClicked = false;
	private boolean _creditClicked = false;
	private final JComboBox<String> _openFont = new JComboBox<String>(_fonts);
	private final JComboBox<Integer> _openSize = new JComboBox<Integer>(_fontSizes);
	private final JComboBox<String> _openColour = new JComboBox<String>(_fontColours);
	private final JComboBox<String> _creditFont = new JComboBox<String>(_fonts);
	private final JComboBox<Integer> _creditSize = new JComboBox<Integer>(_fontSizes);
	private final JComboBox<String> _creditColour = new JComboBox<String>(_fontColours);
	private final JButton _btnLoadState = new JButton("Load State");
	private final JButton _btnAddText = new JButton("Add text");
	private final JButton _btnSaveButton = new JButton("Save State");
	private final JButton _btnPreview = new JButton("Preview");
	private final JProgressBar _progress = new JProgressBar();

	public TextPanel(VAMIX main){
		_progress.setBounds(300,158,200,19);
		_progress.setIndeterminate(true);
		_progress.setVisible(false);
		add(_progress);
		_btnPreview.setEnabled(false);
		_btnAddText.setEnabled(false);
		setBackground(Color.LIGHT_GRAY);
		//Set main
		_main = main;
		_openText.setBounds(12, 26, 257, 124);
		
		//Adding opening text options
		_openText.setPreferredSize(new Dimension(600,19));
		_openText.setLineWrap(true);
		setLayout(null);
		_openLabel.setBounds(12, 5, 250, 15);
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
		_openText.setDocument(new JTextFieldLimit(80));
		_creditText.setBounds(387, 26, 268, 124);
		
		//Adding closing text options
		_creditText.setPreferredSize(new Dimension(600,19));
		_creditText.setLineWrap(true);
		_creditText.setDocument(new JTextFieldLimit(80));
		_creditLabel.setBounds(388, 5, 250, 15);
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
		
		
		_btnSaveButton.setBounds(12, 154, 117, 25);
		add(_btnSaveButton);
		_btnSaveButton.addActionListener(this);
		_btnLoadState.setBounds(152, 154, 117, 25);
		
		add(_btnLoadState);
		_btnLoadState.addActionListener(this);
		_btnAddText.setBounds(644, 154, 117, 25);
		
		add(_btnAddText);
		_btnAddText.addActionListener(this);
		
		
		_btnPreview.setBounds(515, 154, 117, 25);
		add(_btnPreview);
		_btnPreview.addActionListener(this);
		
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
	}
	
	class TextWorker extends SwingWorker<Integer, Void>{

		//Fields required by the worker
		String cmd = "";
		
		public TextWorker(String inp){
			cmd = inp;
		}
		
		//returns the textpanels needed cmd information
		protected Integer doInBackground() throws Exception {
			int result = bashCommand(cmd);
			return result;
		}
		
		protected void done(){
			_progress.setVisible(false);
			_btnAddText.setEnabled(true);
			try {
				if(get() == 0){
					//Executed successfully
					JOptionPane.showMessageDialog(null, "Text addition successful.");
				}else{
					//Error occured
					JOptionPane.showMessageDialog(null, "Error occured in text addition.");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	class PreviewWorker extends SwingWorker<Integer, Void>{
		
		//Fields required by the worker
		String cmd = "";
				
		public PreviewWorker(String inp){
			cmd = inp;
		}
				
		//returns the textpanels needed cmd information
		protected Integer doInBackground() throws Exception {
			
			bashCommand("avconv -y -ss 00:00:00 -i " + _file + " -codec copy -t 00:00:06 .temp.mp4");
			int result = bashCommand(cmd);
			return result;
		}
		
		protected void done(){
			//Enable the button again
			_progress.setVisible(false);
			_btnPreview.setEnabled(true);
			
			//Display the preview video on a new frame
			JFrame previewFrame = new JFrame();
			previewFrame.setBounds(100,100,420,300);
			final EmbeddedMediaPlayerComponent mp = new EmbeddedMediaPlayerComponent();
			mp.setBounds(0,0,420,300);
			previewFrame.add(mp);
			previewFrame.addWindowListener(new WindowAdapter() {
	            @Override
	            public void windowClosed(WindowEvent e) {
	                mp.getMediaPlayer().stop();
	                mp.release();
	            }
			});
			previewFrame.setVisible(true);
			
			mp.getMediaPlayer().playMedia(".preview.mp4");
		}
	}
	
	public void newInput(File file,Boolean boo) {
		//Set the current file to the _file field
		if(boo){
			_btnPreview.setEnabled(true);
			_btnAddText.setEnabled(true);
		}else{
			_btnPreview.setEnabled(false);
			_btnAddText.setEnabled(false);
		}
		_file = file;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(_btnAddText)){
			_progress.setVisible(true);
			if(!_main.getOutName().equals("")){
				//Check if the given output name exists already
				File output = new File(_main.getOutName() + ".mp4");
				
				int optionPicked = 0;
				if(output.exists()){
					//Ask if we wish to overwrite the file
					Object[] options = {"Overwrite","Don't overwrite"};
					optionPicked = JOptionPane.showOptionDialog(this,
				    "Do you wish to also save audio?","Option",JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE,null,options,null);
				}
				
				if(optionPicked != 1){
					//Carry out command
					
					//An example avconv that'll append text to the beginning and end from 
					//http://stackoverflow.com/questions/6195872/applying-multiple-filters-at-once-with-ffmpeg
					//avconv -i themagician.rmvb -vf "drawtext=
					//fontfile='/usr/share/fonts/truetype/freefont/FreeMono.ttf':text='test text':
					//fontsize=24:fontcolor=white:draw='lt(t,10)', drawtext=
					//fontfile='/usr/share/fonts/truetype/freefont/FreeMono.ttf':text='test text':fontsize=24:
					//fontcolor=white:draw='gt(t,41)'" output3.avi
					
					//Check if the user has entered text in each field
					boolean startText = false;
					boolean endText = false;
					if(!_openText.getText().equals("") && _openClicked){
						startText = true;
					}
					if(!_creditText.getText().equals("") && _creditClicked){
						endText = true;
					}
					
					//Check that any input was given
					if(startText || endText){
						
						String cmd = "avconv -i " + _file + " -y -strict experimental -vf \"";
						String oFilter = "";
						String eFilter = "";
						
						//Get the input variables if required
						if(startText){
							//Get selected font
							String oFont = (String) _openFont.getSelectedItem();
							oFont = "/usr/share/fonts/truetype/freefont/Free" + oFont + ".ttf"; //I know this is cheap but it works
							
							//Get font size
							int oSize = (int) _openSize.getSelectedItem();
							
							//Get font colour
							String oColour = (String) _openColour.getSelectedItem();
							oColour = oColour.toLowerCase();
							
							//Set up the command
							oFilter = "drawtext=fontfile='" + oFont + "':text='" + _openText.getText() + 
									"':fontsize=" + oSize + ":fontcolor=" + oColour + ":draw='lt(t,10)'";
						}
						if(endText){
							//Get selected font
							String eFont = (String) _creditFont.getSelectedItem();
							eFont = "/usr/share/fonts/truetype/freefont/Free" + eFont + ".ttf"; //I know this is cheap but it works
							
							//Get font size
							int eSize = (int) _creditSize.getSelectedItem();
							
							//Get font colour
							String eColour = (String) _creditColour.getSelectedItem();
							eColour = eColour.toLowerCase();
							
							//Set up the command
							eFilter = "drawtext=fontfile='" + eFont + "':text='" + _creditText.getText() + 
									"':fontsize=" + eSize + ":fontcolor=" + eColour + ":draw='gt(t," +
									(_main.getLength()-10) + ")'";
						}
						
						if(startText && endText){
							//Both start and end text
							cmd = cmd + oFilter + ", " + eFilter + "\" " + _main.getOutName() + ".mp4";
						}else{
							//Only one text
							cmd = cmd + oFilter + eFilter + "\" " + _main.getOutName() + ".mp4";
						}
						
						//Send the command to bash
						_btnAddText.setEnabled(false);
						_progress.setVisible(true);
						TextWorker worker = new TextWorker(cmd);
						worker.execute();
						System.out.println(cmd);
					}else{
						//Tell the user that no input was given
						JOptionPane.showMessageDialog(this, "No text input.", "ERROR", JOptionPane.ERROR_MESSAGE);
					}
				}
			}else{
				//No output name specified
				JOptionPane.showMessageDialog(null, "Please specify an output name");
			}
		}
		else if(e.getSource().equals(_btnSaveButton)){
			//Save current state of VAMIX to a file
			String projName = JOptionPane.showInputDialog("Save file as?");
			String state = "oText=" + _openText.getText() + "\n" + 
			"oFont=" + _openFont.getSelectedIndex() + "\n" + 
			"oSize=" + _openSize.getSelectedIndex() + "\n" +
			"oColr=" + _openColour.getSelectedIndex() + "\n" +
			"cText=" + _creditText.getText() + "\n" +
			"cFont=" + _creditFont.getSelectedIndex() + "\n" + 
			"cSize=" + _creditSize.getSelectedIndex() + "\n" +
			"cColr=" + _creditColour.getSelectedIndex();
			
			//Write the current state to a file, logic from 
			//http://stackoverflow.com/questions/2885173/java-how-to-create-and-write-to-a-file
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(projName+".proj", "UTF-8");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			writer.println(state);
			writer.close();
		}
		
		else if(e.getSource().equals(_btnLoadState)){
			JFileChooser fc = new JFileChooser();
			fc.showOpenDialog(this);
			
			//Check that the selected file is a .proj file
			String chosenFile = fc.getSelectedFile().getName();
			if(chosenFile.contains(".proj")){
				try{
				BufferedReader br = new BufferedReader(new FileReader(fc.getSelectedFile()));
				String line = br.readLine();
				
				while(line != null){
					
					//Read the project file for the options
					String option = line.substring(0, 5);
					switch(option){
					case("oText"):
						if(line.length() < 7){
							//Text field was empty
							_openText.setText("");
						}else{
							_openText.setText(line.substring(6));
							_openClicked = true;
						}
					break;
					
					case("oFont"):
						_openFont.setSelectedIndex(Integer.parseInt(line.substring(6, 7)));
					break;
					
					case("oSize"):
						_openSize.setSelectedIndex(Integer.parseInt(line.substring(6, 7)));
					break;
				
					case("oColr"):
						_openColour.setSelectedIndex(Integer.parseInt(line.substring(6, 7)));
					break;
					
					case("cText"):
						if(line.length() < 7){
							//Text field was empty
							_creditText.setText("");
						}else{
							_creditText.setText(line.substring(6));
							_creditClicked = true;
						}
					break;
					
					case("cFont"):
						_creditFont.setSelectedIndex(Integer.parseInt(line.substring(6, 7)));
					break;
					
					case("cSize"):
						_creditSize.setSelectedIndex(Integer.parseInt(line.substring(6, 7)));
					break;
					
					case("cColr"):
						_creditColour.setSelectedIndex(Integer.parseInt(line.substring(6, 7)));
					break;
					}
					
					line = br.readLine();
				}

				br.close();
				}catch(IOException ex){
					ex.printStackTrace();
				}
			}else{
				//Tell user they selected incorrect file
				JOptionPane.showMessageDialog(null, "Please select a .proj file");
			}
		}
		
		else if(e.getSource().equals(_btnPreview)){
			//Disable preview button
			_btnPreview.setEnabled(false);
			_progress.setVisible(true);
			
			//Check if the user has entered text in each field
			boolean startText = false;
			boolean endText = false;
			if(!_openText.getText().equals("") && _openClicked){
				startText = true;
			}
			if(!_creditText.getText().equals("") && _creditClicked){
				endText = true;
			}
			
			//Check that any input was given
			if(startText || endText){
				
				String cmd = "avconv -i .temp.mp4 -y -strict experimental -vf \"";
				String oFilter = "";
				String eFilter = "";
				
				//Get the input variables if required
				if(startText){
					//Get selected font
					String oFont = (String) _openFont.getSelectedItem();
					oFont = "/usr/share/fonts/truetype/freefont/Free" + oFont + ".ttf"; //I know this is cheap but it works
					
					//Get font size
					int oSize = (int) _openSize.getSelectedItem();
					
					//Get font colour
					String oColour = (String) _openColour.getSelectedItem();
					oColour = oColour.toLowerCase();
					
					//Set up the command
					oFilter = "drawtext=fontfile='" + oFont + "':text='" + _openText.getText() + 
							"':fontsize=" + oSize + ":fontcolor=" + oColour + ":draw='lt(t,3)'";
				}
				if(endText){
					//Get selected font
					String eFont = (String) _creditFont.getSelectedItem();
					eFont = "/usr/share/fonts/truetype/freefont/Free" + eFont + ".ttf"; //I know this is cheap but it works
					
					//Get font size
					int eSize = (int) _creditSize.getSelectedItem();
					
					//Get font colour
					String eColour = (String) _creditColour.getSelectedItem();
					eColour = eColour.toLowerCase();
					
					//Set up the command
					eFilter = "drawtext=fontfile='" + eFont + "':text='" + _creditText.getText() + 
							"':fontsize=" + eSize + ":fontcolor=" + eColour + ":draw='gt(t,3)'";
				}
				
				if(startText && endText){
					//Both start and end text
					cmd = cmd + oFilter + ", " + eFilter + "\" " + ".preview.mp4";
				}else{
					//Only one text
					cmd = cmd + oFilter + eFilter + "\" " + ".preview.mp4";
				}
				
				//Send the command to the PreviewWorker
				PreviewWorker preview = new PreviewWorker(cmd);
				preview.execute();
				
			}else{
				//Tell the user that no input was given
				JOptionPane.showMessageDialog(this, "No text input.", "ERROR", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
