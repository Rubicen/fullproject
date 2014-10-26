package panels.audio;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import panels.basic.VamixPanel;
import panels.player.PlayerPanel;

import main.VAMIX;

/**
 * 
 * @author logan
 *	Most of the commands in the workers are implemented from sources off of the internet
 */
@SuppressWarnings("serial")
public class AudioPanel extends VamixPanel implements ActionListener{
	
	File file;
	VAMIX main;
	JButton replaceButton = new JButton("Replace");
	JButton stripButton = new JButton("Strip");
	JButton overlayButton = new JButton("Overlay");
	JFileChooser fileChoooser = new JFileChooser();
	JButton getAudioButton = new JButton("Audio");
	JTextField audioFileTextField = new JTextField();
	JProgressBar fakeProgressBar = new JProgressBar(0);
	PlayerPanel player = new PlayerPanel();
	
	public AudioPanel(VAMIX m,PlayerPanel p){
		player = p;
		fakeProgressBar.setLocation(70, 104);
		fakeProgressBar.setSize(200, 19);
		fakeProgressBar.setVisible(false);
		fakeProgressBar.setIndeterminate(true);
		add(fakeProgressBar);
		replaceButton.setEnabled(false);
		stripButton.setEnabled(false);
		overlayButton.setEnabled(false);
		main = m;
		getAudioButton.setBounds(12, 5, 74, 25);
		getAudioButton.addActionListener(this);
		replaceButton.setBounds(116, 68, 90, 25);
		replaceButton.addActionListener(this);
		stripButton.setBounds(18, 68, 68, 25);
		stripButton.addActionListener(this);
		overlayButton.setBounds(233, 68, 88, 25);
		overlayButton.addActionListener(this);
		audioFileTextField.setBounds(91, 8, 230, 19);
		audioFileTextField.setEnabled(false);
		audioFileTextField.setPreferredSize(new Dimension(100,19));
		setLayout(null);
		add(getAudioButton);
		add(audioFileTextField);
		add(replaceButton);
		add(stripButton);
		add(overlayButton);
		
	}
	
	class AudioWorker extends SwingWorker<Void,Integer> {
		//General variables for use through the swingworker
		String outname;
		String called;
		String audioname;
		int exitValue;
		
		public AudioWorker(VAMIX vam,String option,String audio){
			outname = vam.getOutName();
			called = option;
			audioname = audio;
		}
		
		//Depending on input string, the swingworker completes the correct operation
		protected Void doInBackground() throws Exception {
			
			if(called.equals("1")){
				exitValue = bashCommand("avconv -y -i "+audioname+" -i '"+file+"' -vcodec copy -acodec copy -map 0:0 -map 1:0 '"+outname+".mp4'");
			}
			
			if(called.equals("21")){
				exitValue = bashCommand("avconv -y -i '"+file+"' -an -c:v copy '"+outname+".mp4'");
				if(exitValue == 0){
					exitValue = bashCommand("avconv -y -i '"+file+"' -f mp3 -ab 192000 -vn '"+outname+"_audio.mp3'");
				}
				
			}
			
			if(called.equals("22")){
				exitValue = bashCommand("avconv -y -i '"+file+"' -an -c:v copy '"+outname+"'.mp4");
				
			}
				
			if(called.equals("3")){
				exitValue = bashCommand("avconv -y -i '"+audioname+"' -i '"+file+"' -filter_complex amix=inputs=2:duration=longest:dropout_transition=3 -strict experimental '"+outname+".mp4'");
				
			}
			return null;
		}
		
		//When the swingworker competes, it reneables everything, and tells a message depending on completion status
		protected void done(){
			if(exitValue!=0){
				JOptionPane.showMessageDialog(null,"An error had occured, refer to README.txt for information");
				
			}else{
				JOptionPane.showMessageDialog(null,"Completed successfully");
			}
			replaceButton.setEnabled(true);
			stripButton.setEnabled(true);
			overlayButton.setEnabled(true);
			fakeProgressBar.setVisible(false);
		}
	}
	
	public void actionPerformed(ActionEvent a){
		
		//if is get audio button, enters here
		if(a.getSource().equals(getAudioButton)){
			fileChoooser.showOpenDialog(this);
			File file = fileChoooser.getSelectedFile();
			String type;
			try {
				type = Files.probeContentType(file.toPath());
				if(!(type.contains("audio"))){
					JOptionPane.showMessageDialog(this,"You have entered a wrong file");
				}else{
					audioFileTextField.setText(file.getPath());
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//if replace button then enters here
		if(a.getSource().equals(replaceButton)){
			
			//checks if the audiofile is null
			if(!(audioFileTextField.getText().equals(""))){
				File f = new File(main.getOutName()+".mp4");
				
				//checks if the name is not viable or allowed
				if(player.getFile().getName().equals(main.getOutName()+".mp4")){
					Object[] options2 = {"Ok"};
					JOptionPane.showOptionDialog(this,
				    "The file you are entering is the file you are working on, please pick another.","Option",JOptionPane.OK_OPTION,
				    JOptionPane.QUESTION_MESSAGE,null,options2,null);
				}else if(!(main.getOutName().equals("") || f.exists())){
					swingMaker(main,"1",audioFileTextField.getText());
					
				}else{
					Object[] options = {"Overwrite","Don't overwrite"};
					int optionPicked = JOptionPane.showOptionDialog(this,
				    "Do you wish to overwrite existing file?","Option",JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE,null,options,null);
					if(optionPicked == 0){
						swingMaker(main,"1",audioFileTextField.getText());
					}else if(optionPicked == 1){
						JOptionPane.showMessageDialog(this,"File name taken. Change the outname");
					}
				}
			}else{
				JOptionPane.showMessageDialog(this,"Please enter a audio file to replace the audio with.");
			}
			
				
		}
		
		//if is the strip button, enters here
		if(a.getSource().equals(stripButton)){
			File f = new File(main.getOutName()+"_audio.mp3");
			File f2 = new File(main.getOutName()+".mp4");
			
			//if the video has audio then enters here
			if(hasAudio()){
				if(player.getFile().getName().equals(main.getOutName()+".mp4")){
					Object[] options2 = {"Ok"};
					JOptionPane.showOptionDialog(this,
				    "The file you are entering is the file you are working on, please pick another.","Option",JOptionPane.OK_OPTION,
				    JOptionPane.QUESTION_MESSAGE,null,options2,null);
				}else
				//if the outname is viable/allowed enters here
				if(!(main.getOutName().equals("") || f.exists() || f2.exists())){
					Object[] options = {"Yes","No"};
					int optionPicked = JOptionPane.showOptionDialog(this,
				    "Do you wish to also save audio?","Option",JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE,null,options,null);
					if(optionPicked == 0){
						swingMaker(main,"21",null);
					}else if(optionPicked==1){
						swingMaker(main,"22",null);
					}
				}else{
					Object[] options = {"Overwrite","Don't overwrite"};
					int optionPicked = JOptionPane.showOptionDialog(this,
				    "Do you wish to overwrite existing file?","Option",JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE,null,options,null);
					if(optionPicked == 0){
						Object[] options2 = {"Yes","No"};
						optionPicked = JOptionPane.showOptionDialog(this,
					    "Do you wish to also save audio?","Option",JOptionPane.YES_NO_OPTION,
					    JOptionPane.QUESTION_MESSAGE,null,options2,null);
						if(optionPicked == 0){
							swingMaker(main,"21",null);
						}else if(optionPicked==1){
							swingMaker(main,"22",null);
						}
					}else if(optionPicked==1){
						JOptionPane.showMessageDialog(this, "Please rename the outname");
					}
				}
				
			//if the file does not contain audio, enters here
			}else{
				JOptionPane.showMessageDialog(this, "The file "+file+" does not contain audio");
			}
		}
		
		//if the overlay button, then enters here
		if(a.getSource().equals(overlayButton)){
			
			//if the video contains audio, enters here
			if(hasAudio()){
				
				//if the audiofile selected isnt null, enters here
				if(!(audioFileTextField.getText().equals(""))){
					File f = new File(main.getOutName()+".mp4");
					
					if(player.getFile().getName().equals(main.getOutName()+".mp4")){
						Object[] options2 = {"Ok"};
						JOptionPane.showOptionDialog(this,
					    "The file you are entering is the file you are working on, please pick another.","Option",JOptionPane.OK_OPTION,
					    JOptionPane.QUESTION_MESSAGE,null,options2,null);
					}else
					//if the main name is viable, enters here
					if(!(main.getOutName().equals("") || f.exists())){
						swingMaker(main,"3",audioFileTextField.getText());
						//otherwise enters here
					}else{
						Object[] options = {"Overwrite","Don't overwrite"};
						int optionPicked = JOptionPane.showOptionDialog(this,
					    "Do you wish to overwrite existing file?","Option",JOptionPane.YES_NO_OPTION,
					    JOptionPane.QUESTION_MESSAGE,null,options,null);
						if(optionPicked == 0){
							swingMaker(main,"3",audioFileTextField.getText());
						}else if(optionPicked == 1){
							JOptionPane.showMessageDialog(this,"File name taken. Change the outname");
						}
						
					}
				}else{
					JOptionPane.showMessageDialog(this,"Please enter a audio file to overlay the audio with.");
				}
			}else{
				JOptionPane.showMessageDialog(this,"This file has no audio to overlay. \nChoose a file with audio streams or replace audio to add the audio in.");
			}
		}
	}

	//new input sets things enabled or not enabled depending on if the file is a video or audio
	public void newInput(File f,Boolean boo) {
		file = f;
		if(boo){
			replaceButton.setEnabled(true);
			stripButton.setEnabled(true);
			overlayButton.setEnabled(true);
		}else{
			replaceButton.setEnabled(false);
			stripButton.setEnabled(false);
			overlayButton.setEnabled(false);
		}
		
	}
	
	public void swingMaker(VAMIX vam,String option,String audio){
		replaceButton.setEnabled(false);
		stripButton.setEnabled(false);
		overlayButton.setEnabled(false);
		fakeProgressBar.setValue(0);
		fakeProgressBar.setVisible(true);
		
		AudioWorker job = new AudioWorker(vam,option,audio);
		job.execute();
	}
	
	public Boolean hasAudio(){
		if(bashCommand("avprobe "+file+" -show_streams | grep -i \"audio\"")==0){
			return true;
		}else{
			return false;
		}
	}
	
}
