import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import java.awt.Color;


public class DownloadPanel extends VamixPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private boolean _clickedOnce = false;
	private boolean _canDownload = true;
	
	private JPanel _urlPanel = new JPanel();
	private JTextField _url = new JTextField("Please enter a URL");
	
	private JPanel _buttonPanel = new JPanel();
	private JPanel _checkPanel = new JPanel();
	private JButton _dButton = new JButton("Download");
	private JButton _cButton = new JButton("Cancel");
	private JCheckBox _openSource = new JCheckBox("I confirm the URL is open source.");
	
	private JPanel _progressPanel = new JPanel();
	private JProgressBar _progress = new JProgressBar(0, 100);
	
	private DownloadWorker _dlWork;
	
	/**
	 * SwingWorker inner class for the download process
	 */
	class DownloadWorker extends SwingWorker<Void, Integer>{
		
		private String mp3;
		private Process process;
		
		public DownloadWorker(String input){
			mp3 = input;
		}

		@Override
		protected Void doInBackground() throws Exception {
			ProcessBuilder builder = new ProcessBuilder("wget", "--progress=dot", "-c", mp3);
			
			process = builder.start();
			
			InputStream error = process.getErrorStream();
			BufferedReader stderr = new BufferedReader(new InputStreamReader(error));
			
			Pattern percent = Pattern.compile("\\d*%");
			String line = null;
			while ((line = stderr.readLine()) != null ) {
				if(this.isCancelled()){
					process.destroy();
					return null;
				}else{
					Matcher match = percent.matcher(line);
					if(match.find()){
						String progress = match.group();
						publish(Integer.parseInt(progress.substring(0, progress.length() - 1)));
					}
				}
			}
			process.waitFor();
			return null;
		}
		
		protected void done() {
			try {
				if(!isCancelled()){
					switch(process.waitFor()){
					case 0: //Download completed successfully
						JOptionPane.showMessageDialog(_buttonPanel, "Download Successful!");
						break;
					case 1: //Download failed - generic
						JOptionPane.showMessageDialog(_buttonPanel, "Download Failed.");
						break;
					case 2: //Failed - parse error
						JOptionPane.showMessageDialog(_buttonPanel, "Download Failed - Parse Error.");
						break;
					case 3: //Failed - I/O error
						JOptionPane.showMessageDialog(_buttonPanel, "Download Failed - File I/O Error.");
						break;
					case 4: //Failed - Network failure
						JOptionPane.showMessageDialog(_buttonPanel, "Download Failed - Network Failure.");
						break;
					case 5: //Failed - SSL Verification failure
						JOptionPane.showMessageDialog(_buttonPanel, "Download Failed - SSL Verification Failure.");
						break;
					case 6: //Failed - Authentication failure
						JOptionPane.showMessageDialog(_buttonPanel, "Download Failed - Authentication Failure.");
						break;
					case 7: //Failed - Protocol errors
						JOptionPane.showMessageDialog(_buttonPanel, "Download Failed - Protocol Error.");
						break;
					case 8: //Failed - Server issued error response
						JOptionPane.showMessageDialog(_buttonPanel, "Download Failed - Server Issued Error Response.");
						break;
					}
				}
				
				_openSource.setSelected(false);
				_dButton.setEnabled(false);
				_progress.setVisible(false);
				_cButton.setEnabled(false);
				_canDownload = true;
				
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		protected void process(List<Integer> chunks) {
			for (int value : chunks) {
				_progress.setValue(value);
			}
		}
		
	}
	
	/**
	 * Constructor method
	 */
	public DownloadPanel(){
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		_urlPanel.setBackground(Color.LIGHT_GRAY);
		
		add(_urlPanel);
		_urlPanel.add(_url);
		_urlPanel.setPreferredSize(new Dimension(20, 25));
		_url.setPreferredSize(new Dimension(300, 20));
		_checkPanel.setBackground(Color.LIGHT_GRAY);
		
		add(_checkPanel);
		_openSource.setBackground(Color.LIGHT_GRAY);
		_checkPanel.add(_openSource);
		_openSource.addActionListener(this);
		_openSource.setAlignmentX(JCheckBox.LEFT_ALIGNMENT);
		_buttonPanel.setBackground(Color.LIGHT_GRAY);
		
		add(_buttonPanel);
		_buttonPanel.add(_dButton);
		_buttonPanel.add(_cButton);
		_dButton.addActionListener(this);
		_dButton.setEnabled(false);
		_cButton.addActionListener(this);
		_cButton.setEnabled(false);
		_progressPanel.setBackground(Color.LIGHT_GRAY);
		
		add(_progressPanel);
		_progress.setBackground(Color.WHITE);
		_progressPanel.add(_progress);
		_progress.setVisible(false);
		_progress.setValue(0);
		
		_url.addMouseListener(new MouseAdapter(){
			@Override
            public void mouseClicked(MouseEvent e){
				if(!_clickedOnce){
					_url.setText("");
					_clickedOnce = true;
				}
            }
		});
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if(a.getSource().equals(_openSource) && _openSource.isSelected() && _canDownload){
			//Checked open source, enable download button
			_dButton.setEnabled(true);
			
		}else if(a.getSource().equals(_openSource)){
			//Open source unchecked, disable download button
			_dButton.setEnabled(false);
			
		}else if(a.getSource().equals(_dButton)){
			String mp3 = _url.getText();
			int n = 1;
			//Download button pressed, check if file exists
			String cmd = "[ -e $(basename " + mp3 + ") ]";
			int fileExists = bashCommand(cmd);
			if(!(mp3.equals("Please enter a URL")||mp3.equals(""))){
			
				if(fileExists == 0){
					//File exists, ask what user wants to do
					Object[] options = {"Overwrite",
		                    "Continue",
		                    "Cancel"};
					n = JOptionPane.showOptionDialog(this,
							"File already exists, do you wish to overwrite?",
							"File Exists",
							JOptionPane.YES_NO_CANCEL_OPTION,
							JOptionPane.WARNING_MESSAGE,
							null,
							options,
							null);
				}
				
				//Carry out the download
				if(n == 0){
					//Overwrite, delete the file first
					cmd = "rm $(basename " + mp3 + ")";
					bashCommand(cmd);
				}if(n != 2){
					//Didn't choose to cancel, download
					_progress.setValue(0);
					_progress.setVisible(true);
					_dlWork = new DownloadWorker(mp3);
					_dlWork.execute();
					_dButton.setEnabled(false);
					_cButton.setEnabled(true);
					
					_canDownload = false;
				}
			}
		}else{
			//Cancel button pressed, stop download
			_dlWork.cancel(true);
		}
		
	}

	void newInput(File file,Boolean boo) {
		//Doesn't have to do anything when a file is selected
	}

		

}
