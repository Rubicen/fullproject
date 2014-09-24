import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;


@SuppressWarnings("serial")
public class AudioPanel extends VamixPanel implements ActionListener{
	
	File _file;
	VAMIX _main;
	String _outname;
	JButton _replace = new JButton("Replace");
	JButton _strip = new JButton("Strip");
	JButton _overlay = new JButton("Overlay");
	JFileChooser _fileSelect = new JFileChooser();
	JButton _getAudio = new JButton("Audio");
	JTextField _audioFile = new JTextField();
	
	
	public AudioPanel(VAMIX main){
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
		File filein;
		String called;
		String audioname;
		Process pro;
		int exitValue;
		
		public MagicPaper(VAMIX vam,String option,String audio){
			outname = vam.getOutName();
			called = option;
			audioname = audio;
		}
		
		@Override
		protected Void doInBackground() throws Exception {

			_outname = _main.getOutName();
			_replace.setEnabled(false);
			_strip.setEnabled(false);
			_overlay.setEnabled(false);
			
			if(called.equals("1")){
				exitValue = bashCommand("avconv -y -i "+audioname+" -i "+_file+" -vcodec copy -acodec copy -map 0:0 -map 1:0 "+_outname+".mp4");
			}
			
			if(called.equals("21")){
				exitValue = bashCommand("avconv -y -i "+_file+" -an -c:v copy "+_outname+".mp4");
				if(exitValue == 0){
					exitValue = bashCommand("avconv -y -i "+_file+" -f mp3 -ab 192000 -vn "+_outname+"_audio.mp3");
				}
				
			}
			
			if(called.equals("22")){
				exitValue = bashCommand("avconv -y -i "+_file+" -an -c:v copy "+_outname+".mp4");
				
			}
				
			if(called.equals("3")){
				exitValue = bashCommand("avconv -y -i "+audioname+" -i "+_file+" -filter_complex amix=inputs=2:duration=longest:dropout_transition=3 -strict experimental "+_outname+".mp4");
				
			}
			return null;
		}
		
		protected void done(){
			if(exitValue!=0){
				JOptionPane.showMessageDialog(null,"OOPS");
				
			}else{
				JOptionPane.showMessageDialog(null,"DID IT");
			}
			_replace.setEnabled(true);
			_strip.setEnabled(true);
			_overlay.setEnabled(true);
		}
	}
	
	public void actionPerformed(ActionEvent a){
		//TODO MAKE IT SO YOU CAN CHOOSE OVERWRITE WHEN THE DIALOG POPS UP
		//TODO
		//TODO
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
		
		if(a.getSource().equals(_replace)){
			if(!(_audioFile.getText().equals(null))){
				File f = new File(_main.getOutName()+".mp4");
				
				if(!(_main.getOutName().equals("") && !f.exists())){
					MagicPaper job = new MagicPaper(_main,"1",_audioFile.getText());
					job.execute();
					
				}else{
					Object[] options = {"Overwrite","Don't overwrite"};
					int optionPicked = JOptionPane.showOptionDialog(this,
				    "Do you wish to also save audio?","Option",JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE,null,options,null);
					if(optionPicked == 0){
						MagicPaper job = new MagicPaper(_main,"1",_audioFile.getText());
						job.execute();
					}else if(optionPicked == 1){
						JOptionPane.showMessageDialog(this,"File name taken. Change the outname");
					}
				}
			}
			
				
		}
		
		if(a.getSource().equals(_strip)){
			File f = new File(_main.getOutName()+"_audio.mp3");
			File f2 = new File(_main.getOutName()+".mp4");
			
			if(!(_main.getOutName().equals("") && !f.exists()) && !f2.exists()){
				Object[] options = {"Yes","No"};
				int optionPicked = JOptionPane.showOptionDialog(this,
			    "Do you wish to also save audio?","Option",JOptionPane.YES_NO_OPTION,
			    JOptionPane.QUESTION_MESSAGE,null,options,null);
				if(optionPicked == 0){
					MagicPaper job = new MagicPaper(_main,"21",null);
					job.execute();
				}else if(optionPicked==1){
					MagicPaper job = new MagicPaper(_main,"22",null);
					job.execute();
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
						MagicPaper job = new MagicPaper(_main,"21",null);
						job.execute();
					}else if(optionPicked==1){
						MagicPaper job = new MagicPaper(_main,"22",null);
						job.execute();
					}
				}else if(optionPicked==1){
					JOptionPane.showMessageDialog(this, "Please rename the outname");
				}
			}
		}
		
		if(a.getSource().equals(_overlay)){
			if(!(_audioFile.getText().equals(null))){
				File f = new File(_main.getOutName()+".mp4");
				
				if(!(_main.getOutName().equals("") && !f.exists())){
					MagicPaper job = new MagicPaper(_main,"3",_audioFile.getText());
					job.execute();
					
				}else{
					Object[] options = {"Overwrite","Don't overwrite"};
					int optionPicked = JOptionPane.showOptionDialog(this,
				    "Do you wish to also save audio?","Option",JOptionPane.YES_NO_OPTION,
				    JOptionPane.QUESTION_MESSAGE,null,options,null);
					if(optionPicked == 0){
						MagicPaper job = new MagicPaper(_main,"3",_audioFile.getText());
						job.execute();
					}else if(optionPicked == 1){
						JOptionPane.showMessageDialog(this,"File name taken. Change the outname");
					}
					
				}
			}
		}
	}

	@Override
	void newInput(File file) {
		_file = file;
		_replace.setEnabled(true);
		_strip.setEnabled(true);
		_overlay.setEnabled(true);
		
	}
}
