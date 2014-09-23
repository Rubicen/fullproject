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
	
	File _file = new File("/home/logan/Documents/206/themagician.rmvb");
	
	
	JButton _replace = new JButton("Replace");
	JButton _strip = new JButton("Strip");
	JButton _overlay = new JButton("Overlay");
	JFileChooser _fileSelect = new JFileChooser();
	JTextArea _stripname = new JTextArea();
	JButton _getAudio = new JButton("Audio");
	JTextField _audioFile = new JTextField();
	JLabel _stripnamelabel = new JLabel("Name of the output file: ");
	
	
	public AudioPanel(){
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
		_stripname.setBounds(18, 152, 303, 19);
		_stripname.setPreferredSize(new Dimension(100,19));
		_stripnamelabel.setBounds(18, 117, 173, 15);
		add(_stripnamelabel);
		add(_stripname);
	}
	
	class MagicPaper extends SwingWorker<Void,Integer> {
		//General variables for use through the swingworker
		String outname;
		File filein;
		String called;
		String audioname;
		Process pro;
		int exitValue;
		
		public MagicPaper(String out,String option,String audio){
			outname = out;
			called = option;
			audioname = audio;
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			_replace.setEnabled(false);
			_strip.setEnabled(false);
			_overlay.setEnabled(false);
			
			if(called.equals("1")){
				exitValue = bashCommand("avconv -i "+audioname+" -i "+_file+" -vcodec copy -acodec copy -map 0:0 -map 1:0 "+_stripname.getText()+".mkv");
				
			}
			
			if(called.equals("2")){
				exitValue = bashCommand("avconv -i "+_file+" -f mp3 -ab 192000 -vn "+_stripname.getText()+".mp3");
				
			}
				
			if(called.equals("3")){
				//FIX THIS SHIT PLZ THX K BYWE
				exitValue = bashCommand("avconv -i "+_file+" -i "+audioname+" -filter_complex join=inputs=2 "+_stripname.getName());
			
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
				File f = new File(_stripname.getText());
				
				if(!(_stripname.getText().equals("") || f.exists())){
					MagicPaper job = new MagicPaper(_stripname.getText(),"1",_audioFile.getText());
					job.execute();
					
				}
			}
			
				
		}
		
		if(a.getSource().equals(_strip)){
			File f = new File(_stripname.getText());
			
			if(!(_stripname.getText().equals("") || f.exists())){
				MagicPaper job = new MagicPaper(_stripname.getText(),"2",null);
				job.execute();
				
			}
		}
		
		if(a.getSource().equals(_overlay)){
			if(!(_audioFile.getText().equals(null))){
				File f = new File(_stripname.getText());
				
				if(!(_stripname.getText().equals("") || f.exists())){
					MagicPaper job = new MagicPaper(_stripname.getText(),"3",_audioFile.getName());
					job.execute();
					
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
