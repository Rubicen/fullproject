package panels;
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

import main.VAMIX;


@SuppressWarnings("serial")
public class AudioPanel extends VamixPanel implements ActionListener{
	
	File _file;
	VAMIX _main;
	JButton _replace = new JButton("Replace");
	JButton _strip = new JButton("Strip");
	JButton _overlay = new JButton("Overlay");
	JFileChooser _fileSelect = new JFileChooser();
	JButton _getAudio = new JButton("Audio");
	JTextField _audioFile = new JTextField();
	JProgressBar _fakeProgress = new JProgressBar(0);
	//TODO maybe add loading bar (fake) for this
	
	
	public AudioPanel(VAMIX main){
		_fakeProgress.setLocation(70, 104);
		_fakeProgress.setSize(200, 19);
		_fakeProgress.setVisible(false);
		_fakeProgress.setIndeterminate(true);
		add(_fakeProgress);
		_replace.setEnabled(false);
		_strip.setEnabled(false);
		_overlay.setEnabled(false);
		_main = main;
		_getAudio.setBounds(12, 5, 74, 25);
		_getAudio.addActionListener(this);
		_replace.setBounds(116, 68, 90, 25);
		_replace.addActionListener(this);
		_strip.setBounds(18, 68, 68, 25);
		_strip.addActionListener(this);
		_overlay.setBounds(233, 68, 88, 25);
		_overlay.addActionListener(this);
		_audioFile.setBounds(91, 8, 230, 19);
		_audioFile.setEnabled(false);
		_audioFile.setPreferredSize(new Dimension(100,19));
		setLayout(null);
		add(_getAudio);
		add(_audioFile);
		add(_replace);
		add(_strip);
		add(_overlay);
		
	}
	
	class MagicPaper extends SwingWorker<Void,Integer> {
		//General variables for use through the swingworker
		String outname;
		String called;
		String audioname;
		int exitValue;
		
		public MagicPaper(VAMIX vam,String option,String audio){
			outname = vam.getOutName();
			called = option;
			audioname = audio;
		}
		
		//Depending on input string, the swingworker completes the correct operation
		protected Void doInBackground() throws Exception {
			
			if(called.equals("1")){
				exitValue = bashCommand("avconv -y -i "+audioname+" -i "+_file+" -vcodec copy -acodec copy -map 0:0 -map 1:0 "+outname+".mp4");
			}
			
			if(called.equals("21")){
				exitValue = bashCommand("avconv -y -i "+_file+" -an -c:v copy "+outname+".mp4");
				if(exitValue == 0){
					exitValue = bashCommand("avconv -y -i "+_file+" -f mp3 -ab 192000 -vn "+outname+"_audio.mp3");
				}
				
			}
			
			if(called.equals("22")){
				exitValue = bashCommand("avconv -y -i "+_file+" -an -c:v copy "+outname+".mp4");
				
			}
				
			if(called.equals("3")){
				exitValue = bashCommand("avconv -y -i "+audioname+" -i "+_file+" -filter_complex amix=inputs=2:duration=longest:dropout_transition=3 -strict experimental "+outname+".mp4");
				
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
			_replace.setEnabled(true);
			_strip.setEnabled(true);
			_overlay.setEnabled(true);
			_fakeProgress.setVisible(false);
		}
	}
	
	public void actionPerformed(ActionEvent a){
		
		//if is get audio button, enters here
		if(a.getSource().equals(_getAudio)){
			_fileSelect.showOpenDialog(this);
			File file = _fileSelect.getSelectedFile();
			String type;
			try {
				type = Files.probeContentType(file.toPath());
				if(!(type.contains("audio"))){
					JOptionPane.showMessageDialog(this,"You have entered a wrong file");
				}else{
					_audioFile.setText(file.getPath());
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//if replace button then enters here
		if(a.getSource().equals(_replace)){
			
			//checks if the audiofile is null
			if(!(_audioFile.getText().equals(""))){
				File f = new File(_main.getOutName()+".mp4");
				
				//checks if the name is not viable or allowed
				if(!(_main.getOutName().equals("") || f.exists())){
					swingMaker(_main,"1",_audioFile.getText());
					
				}else{
					Object[] options = {"Overwrite","Don't overwrite"};
					int optionPicked = JOptionPane.showOptionDialog(this,
				    "Do you wish to also save audio?","Option",JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE,null,options,null);
					if(optionPicked == 0){
						swingMaker(_main,"1",_audioFile.getText());
					}else if(optionPicked == 1){
						JOptionPane.showMessageDialog(this,"File name taken. Change the outname");
					}
				}
			}
			
				
		}
		
		//if is the strip button, enters here
		if(a.getSource().equals(_strip)){
			File f = new File(_main.getOutName()+"_audio.mp3");
			File f2 = new File(_main.getOutName()+".mp4");
			
			//if the video has audio then enters here
			if(hasAudio()){
				
				//if the outname is viable/allowed enters here
				if(!(_main.getOutName().equals("") || f.exists() || f2.exists())){
					Object[] options = {"Yes","No"};
					int optionPicked = JOptionPane.showOptionDialog(this,
				    "Do you wish to also save audio?","Option",JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE,null,options,null);
					if(optionPicked == 0){
						swingMaker(_main,"21",null);
					}else if(optionPicked==1){
						swingMaker(_main,"22",null);
					}
				}else{
					Object[] options = {"Overwrite","Don't overwrite"};
					int optionPicked = JOptionPane.showOptionDialog(this,
				    "Do you wish to also save audio?","Option",JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE,null,options,null);
					if(optionPicked == 0){
						Object[] options2 = {"Yes","No"};
						optionPicked = JOptionPane.showOptionDialog(this,
					    "Do you wish to also save audio?","Option",JOptionPane.YES_NO_OPTION,
					    JOptionPane.QUESTION_MESSAGE,null,options2,null);
						if(optionPicked == 0){
							swingMaker(_main,"21",null);
						}else if(optionPicked==1){
							swingMaker(_main,"22",null);
						}
					}else if(optionPicked==1){
						JOptionPane.showMessageDialog(this, "Please rename the outname");
					}
				}
				
			//if the file does not contain audio, enters here
			}else{
				JOptionPane.showMessageDialog(this, "The file "+_file+" does not contain audio");
			}
		}
		
		//if the overlay button, then enters here
		if(a.getSource().equals(_overlay)){
			
			//if the video contains audio, enters here
			if(hasAudio()){
				
				//if the audiofile selected isnt null, enters here
				if(!(_audioFile.getText().equals(""))){
					File f = new File(_main.getOutName()+".mp4");
					
					//if the main name is viable, enters here
					if(!(_main.getOutName().equals("") || f.exists())){
						swingMaker(_main,"3",_audioFile.getText());
						//otherwise enters here
					}else{
						Object[] options = {"Overwrite","Don't overwrite"};
						int optionPicked = JOptionPane.showOptionDialog(this,
					    "Do you wish to also save audio?","Option",JOptionPane.YES_NO_OPTION,
					    JOptionPane.QUESTION_MESSAGE,null,options,null);
						if(optionPicked == 0){
							swingMaker(_main,"3",_audioFile.getText());
						}else if(optionPicked == 1){
							JOptionPane.showMessageDialog(this,"File name taken. Change the outname");
						}
						
					}
				}
			}
		}
	}

	//new input sets things enabled or not enabled depending on if the file is a video or audio
	public void newInput(File file,Boolean boo) {
		_file = file;
		if(boo){
			_replace.setEnabled(true);
			_strip.setEnabled(true);
			_overlay.setEnabled(true);
		}else{
			_replace.setEnabled(false);
			_strip.setEnabled(false);
			_overlay.setEnabled(false);
		}
		
	}
	
	public void swingMaker(VAMIX vam,String option,String audio){
		_replace.setEnabled(false);
		_strip.setEnabled(false);
		_overlay.setEnabled(false);
		_fakeProgress.setValue(0);
		_fakeProgress.setVisible(true);
		
		MagicPaper job = new MagicPaper(vam,option,audio);
		job.execute();
	}
	
	public Boolean hasAudio(){
		if(bashCommand("avprobe "+_file+" -show_streams | grep -i \"audio\"")==0){
			return true;
		}else{
			return false;
		}
	}
	
}