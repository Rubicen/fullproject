package panels.text;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import panels.basic.VamixPanel;

import main.VAMIX;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

/**
 * 
 * @author logan
 *	Most of the commands in the workers are implemented from sources off of the internet
 */
public class TextPanel extends VamixPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private File file;
	
	private VAMIX main;
	
	//Lists containing font options
	private String[] fonts = {"Mono", "MonoBold", "Sans", "SansBold", "Serif", "SerifBold"};
	private String[] fontColours = {"White", "Black", "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Violet"};
	private Integer[] fontSizes = {16, 18, 20, 22, 24, 28, 32, 36, 40, 44, 48, 54, 60, 66, 72};
	
	private JLabel openLabel = new JLabel("Opening Text - Max 80 characters");
	private JTextArea openText = new JTextArea("Enter Text Here");
	
	
	private JLabel creditLabel = new JLabel("Closing Text - Max 80 characters");
	private JTextArea creditText = new JTextArea("Enter Text Here");
	
	
	private boolean booOpenClicked = false;
	private boolean booCreditClicked = false;
	private final JComboBox<String> openFontCombo = new JComboBox<String>(fonts);
	private final JComboBox<Integer> openSizeCombo = new JComboBox<Integer>(fontSizes);
	private final JComboBox<String> openColourCombo = new JComboBox<String>(fontColours);
	private final JComboBox<String> creditFontCombo = new JComboBox<String>(fonts);
	private final JComboBox<Integer> creditSizeCombo = new JComboBox<Integer>(fontSizes);
	private final JComboBox<String> creditColourCombo = new JComboBox<String>(fontColours);
	private final JButton btnLoadState = new JButton("Load State");
	private final JButton btnAddText = new JButton("Add text");
	private final JButton btnSaveButton = new JButton("Save State");
	private final JButton btnPreview = new JButton("Preview");
	private final JProgressBar progressBar = new JProgressBar();
	private final JTextField outnameTextField = new JTextField();
	private EmbeddedMediaPlayerComponent mediaComponent;
	
	JFrame previewFrame;

	public TextPanel(VAMIX m){
		progressBar.setBounds(300,158,200,19);
		progressBar.setIndeterminate(true);
		progressBar.setVisible(false);
		add(progressBar);
		btnPreview.setEnabled(false);
		btnAddText.setEnabled(false);
		setBackground(Color.LIGHT_GRAY);
		//Set main
		main = m;
		openText.setBounds(12, 26, 257, 124);
		
		//Adding opening text options
		openText.setPreferredSize(new Dimension(600,19));
		openText.setLineWrap(true);
		setLayout(null);
		openLabel.setBounds(12, 5, 250, 15);
		add(openLabel);
		add(openText);
		//Logic that makes the enter text message vanish when clicked
		openText.addMouseListener(new MouseAdapter(){
			@Override
            public void mouseClicked(MouseEvent e){
				if(!booOpenClicked){
					openText.setText("");
					booOpenClicked = true;
				}
            }
		});
		openText.setDocument(new JTextFieldLimit(80));
		creditText.setBounds(387, 26, 268, 124);
		
		//Adding closing text options
		creditText.setPreferredSize(new Dimension(600,19));
		creditText.setLineWrap(true);
		creditText.setDocument(new JTextFieldLimit(80));
		creditLabel.setBounds(388, 5, 250, 15);
		add(creditLabel);
		add(creditText);
		
		openFontCombo.setBounds(281, 26, 94, 24);
		add(openFontCombo);
		openSizeCombo.setBounds(281, 64, 94, 24);
		
		add(openSizeCombo);
		openColourCombo.setBounds(281, 101, 94, 24);
		
		add(openColourCombo);
		creditFontCombo.setBounds(667, 26, 94, 24);
		
		add(creditFontCombo);
		creditSizeCombo.setBounds(667, 64, 94, 24);
		
		add(creditSizeCombo);
		creditColourCombo.setBounds(667, 101, 94, 24);
		
		add(creditColourCombo);
		
		
		btnSaveButton.setBounds(12, 154, 117, 25);
		add(btnSaveButton);
		btnSaveButton.addActionListener(this);
		btnLoadState.setBounds(152, 154, 117, 25);
		
		add(btnLoadState);
		btnLoadState.addActionListener(this);
		btnAddText.setBounds(644, 154, 117, 25);
		
		add(btnAddText);
		btnAddText.addActionListener(this);
		
		
		btnPreview.setBounds(515, 154, 117, 25);
		add(btnPreview);
		btnPreview.addActionListener(this);
		
		JLabel outnamelabel = new JLabel("Outname: ");
		outnamelabel.setBounds(430,182,180,20);
		outnamelabel.setBackground(Color.LIGHT_GRAY);
		add(outnamelabel);
		outnameTextField.setBounds(515,182,245,18);
		add(outnameTextField);
		
		//Logic that makes the enter text message vanish when clicked
		creditText.addMouseListener(new MouseAdapter(){
			@Override
            public void mouseClicked(MouseEvent e){
				if(!booCreditClicked){
					creditText.setText("");
					booCreditClicked = true;
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
			progressBar.setVisible(false);
			btnAddText.setEnabled(true);
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
			
			bashCommand("avconv -y -ss 00:00:00 -i " + file + " -codec copy -t 00:00:06 .temp.mp4");
			int result = bashCommand(cmd);
			return result;
		}
		
		protected void done(){
			//Enable the button again
			progressBar.setVisible(false);
			btnPreview.setEnabled(true);
			
			previewFrame = new JFrame();
			
			//Display the preview video on a new frame
			previewFrame.setBounds(100,100,420,300);
			mediaComponent = new EmbeddedMediaPlayerComponent();
			mediaComponent.setBounds(0,0,420,300);
			previewFrame.add(mediaComponent);
			previewFrame.setVisible(true);			
			mediaComponent.getMediaPlayer().playMedia(".preview.mp4");
			
		}
	}
	
	public void newInput(File f,Boolean boo) {
		//Set the current file to the _file field
		if(boo){
			btnPreview.setEnabled(true);
			btnAddText.setEnabled(true);
		}else{
			btnPreview.setEnabled(false);
			btnAddText.setEnabled(false);
		}
		file = f;
	}
	
	/**
	 * Closes the preview panel if it's open
	 */
	public void destroy(){
		mediaComponent.getMediaPlayer().stop();
		mediaComponent.release();		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource().equals(btnAddText)){
			
			if(!outnameTextField.getText().equals("")){
				progressBar.setVisible(true);
				//Check if the given output name exists already
				File output = new File(outnameTextField.getText() + ".mp3");
				int optionPicked = 0;
				if(output.getName().equals(file.getName())){
					Object[] options = {"Ok"};
					JOptionPane.showOptionDialog(this,
				    "The filename is the same as the file you input. Change please.","Option",JOptionPane.OK_OPTION,
				    JOptionPane.QUESTION_MESSAGE,null,options,null);
				}else if(output.exists()){
					//Ask if we wish to overwrite the file
					Object[] options = {"Overwrite","Don't overwrite"};
					optionPicked = JOptionPane.showOptionDialog(this,
				    "Do you wish to overwrite existing file?","Option",JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE,null,options,null);
					
					if(optionPicked != 1){
						//Carry out command
						
						//avconv command that'll append text to the beginning and end from 
						//http://stackoverflow.com/questions/6195872/applying-multiple-filters-at-once-with-ffmpeg
						
						
						//Check if the user has entered text in each field
						boolean startText = false;
						boolean endText = false;
						if(!openText.getText().equals("") && booOpenClicked){
							startText = true;
						}
						if(!creditText.getText().equals("") && booCreditClicked){
							endText = true;
						}
						
						//Check that any input was given
						if(startText || endText){
							
							String cmd = "avconv -i " + file + " -y -strict experimental -vf \"";
							String oFilter = "";
							String eFilter = "";
							
							//Get the input variables if required
							if(startText){
								//Get selected font
								String oFont = (String) openFontCombo.getSelectedItem();
								oFont = "/usr/share/fonts/truetype/freefont/Free" + oFont + ".ttf"; //I know this is cheap but it works
								
								//Get font size
								int oSize = (int) openSizeCombo.getSelectedItem();
								
								//Get font colour
								String oColour = (String) openColourCombo.getSelectedItem();
								oColour = oColour.toLowerCase();
								
								//Set up the command
								oFilter = "drawtext=fontfile='" + oFont + "':text='" + openText.getText() + 
										"':fontsize=" + oSize + ":fontcolor=" + oColour + ":draw='lt(t,10)'";
							}
							if(endText){
								//Get selected font
								String eFont = (String) creditFontCombo.getSelectedItem();
								eFont = "/usr/share/fonts/truetype/freefont/Free" + eFont + ".ttf"; //I know this is cheap but it works
								
								//Get font size
								int eSize = (int) creditSizeCombo.getSelectedItem();
								
								//Get font colour
								String eColour = (String) creditColourCombo.getSelectedItem();
								eColour = eColour.toLowerCase();
								
								//Set up the command
								eFilter = "drawtext=fontfile='" + eFont + "':text='" + creditText.getText() + 
										"':fontsize=" + eSize + ":fontcolor=" + eColour + ":draw='gt(t," +
										(main.getLength()-10) + ")'";
							}
							
							if(startText && endText){
								//Both start and end text
								cmd = cmd + oFilter + ", " + eFilter + "\" " + outnameTextField + ".mp4";
							}else{
								//Only one text
								cmd = cmd + oFilter + eFilter + "\" " + outnameTextField + ".mp4";
							}
							
							//Send the command to bash
							btnAddText.setEnabled(false);
							progressBar.setVisible(true);
							TextWorker worker = new TextWorker(cmd);
							worker.execute();
							System.out.println(cmd);
						}else{
							//Tell the user that no input was given
							JOptionPane.showMessageDialog(this, "No text input.", "ERROR", JOptionPane.ERROR_MESSAGE);
							
						}
					}
				}
				
				
			}else{
				//No output name specified
				JOptionPane.showMessageDialog(null, "Please specify an output name");
			}
			progressBar.setVisible(false);
		}
		else if(e.getSource().equals(btnSaveButton)){
			//Save current state of VAMIX to a file
			String projName = JOptionPane.showInputDialog("Save file as?");
			String state = "oText=" + openText.getText() + "\n" + 
			"oFont=" + openFontCombo.getSelectedIndex() + "\n" + 
			"oSize=" + openSizeCombo.getSelectedIndex() + "\n" +
			"oColr=" + openColourCombo.getSelectedIndex() + "\n" +
			"cText=" + creditText.getText() + "\n" +
			"cFont=" + creditFontCombo.getSelectedIndex() + "\n" + 
			"cSize=" + creditSizeCombo.getSelectedIndex() + "\n" +
			"cColr=" + creditColourCombo.getSelectedIndex();
			
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
		
		else if(e.getSource().equals(btnLoadState)){
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
							openText.setText("");
						}else{
							openText.setText(line.substring(6));
							booOpenClicked = true;
						}
					break;
					
					case("oFont"):
						openFontCombo.setSelectedIndex(Integer.parseInt(line.substring(6, 7)));
					break;
					
					case("oSize"):
						openSizeCombo.setSelectedIndex(Integer.parseInt(line.substring(6, 7)));
					break;
				
					case("oColr"):
						openColourCombo.setSelectedIndex(Integer.parseInt(line.substring(6, 7)));
					break;
					
					case("cText"):
						if(line.length() < 7){
							//Text field was empty
							creditText.setText("");
						}else{
							creditText.setText(line.substring(6));
							booCreditClicked = true;
						}
					break;
					
					case("cFont"):
						creditFontCombo.setSelectedIndex(Integer.parseInt(line.substring(6, 7)));
					break;
					
					case("cSize"):
						creditSizeCombo.setSelectedIndex(Integer.parseInt(line.substring(6, 7)));
					break;
					
					case("cColr"):
						creditColourCombo.setSelectedIndex(Integer.parseInt(line.substring(6, 7)));
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
		
		else if(e.getSource().equals(btnPreview)){
			//Disable preview button
			btnPreview.setEnabled(false);
			progressBar.setVisible(true);
			
			//Check if the user has entered text in each field
			boolean startText = false;
			boolean endText = false;
			if(!openText.getText().equals("") && booOpenClicked){
				startText = true;
			}
			if(!creditText.getText().equals("") && booCreditClicked){
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
					String oFont = (String) openFontCombo.getSelectedItem();
					oFont = "/usr/share/fonts/truetype/freefont/Free" + oFont + ".ttf"; //I know this is cheap but it works
					
					//Get font size
					int oSize = (int) openSizeCombo.getSelectedItem();
					
					//Get font colour
					String oColour = (String) openColourCombo.getSelectedItem();
					oColour = oColour.toLowerCase();
					
					//Set up the command
					oFilter = "drawtext=fontfile='" + oFont + "':text='" + openText.getText() + 
							"':fontsize=" + oSize + ":fontcolor=" + oColour + ":draw='lt(t,3)'";
				}
				if(endText){
					//Get selected font
					String eFont = (String) creditFontCombo.getSelectedItem();
					eFont = "/usr/share/fonts/truetype/freefont/Free" + eFont + ".ttf"; //I know this is cheap but it works
					
					//Get font size
					int eSize = (int) creditSizeCombo.getSelectedItem();
					
					//Get font colour
					String eColour = (String) creditColourCombo.getSelectedItem();
					eColour = eColour.toLowerCase();
					
					//Set up the command
					eFilter = "drawtext=fontfile='" + eFont + "':text='" + creditText.getText() + 
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
