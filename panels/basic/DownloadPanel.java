package panels.basic;
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
	
	private boolean booClickedOnce = false;
	private boolean booCanDownload = true;
	
	private JPanel urlPanel = new JPanel();
	private JTextField urlTextField = new JTextField("Please enter a URL");
	
	private JPanel buttonPanel = new JPanel();
	private JPanel checkPanel = new JPanel();
	private JButton downloadButton = new JButton("Download");
	private JButton cancelButton = new JButton("Cancel");
	private JCheckBox openSourceCheck = new JCheckBox("I confirm the URL is open source.");
	
	private JPanel progressPanel = new JPanel();
	private JProgressBar progressBar = new JProgressBar(0, 100);
	
	private DownloadWorker downloadWorker;
	
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
						JOptionPane.showMessageDialog(buttonPanel, "Download Successful!");
						break;
					case 1: //Download failed - generic
						JOptionPane.showMessageDialog(buttonPanel, "Download Failed.");
						break;
					case 2: //Failed - parse error
						JOptionPane.showMessageDialog(buttonPanel, "Download Failed - Parse Error.");
						break;
					case 3: //Failed - I/O error
						JOptionPane.showMessageDialog(buttonPanel, "Download Failed - File I/O Error.");
						break;
					case 4: //Failed - Network failure
						JOptionPane.showMessageDialog(buttonPanel, "Download Failed - Network Failure.");
						break;
					case 5: //Failed - SSL Verification failure
						JOptionPane.showMessageDialog(buttonPanel, "Download Failed - SSL Verification Failure.");
						break;
					case 6: //Failed - Authentication failure
						JOptionPane.showMessageDialog(buttonPanel, "Download Failed - Authentication Failure.");
						break;
					case 7: //Failed - Protocol errors
						JOptionPane.showMessageDialog(buttonPanel, "Download Failed - Protocol Error.");
						break;
					case 8: //Failed - Server issued error response
						JOptionPane.showMessageDialog(buttonPanel, "Download Failed - Server Issued Error Response.");
						break;
					}
				}
				
				openSourceCheck.setSelected(false);
				downloadButton.setEnabled(false);
				progressBar.setVisible(false);
				cancelButton.setEnabled(false);
				booCanDownload = true;
				
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		protected void process(List<Integer> chunks) {
			for (int value : chunks) {
				progressBar.setValue(value);
			}
		}
		
	}
	
	/**
	 * Constructor method
	 */
	public DownloadPanel(){
		setBackground(Color.DARK_GRAY);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		urlPanel.setBackground(Color.GRAY);
		
		add(urlPanel);
		urlPanel.add(urlTextField);
		urlPanel.setPreferredSize(new Dimension(20, 25));
		urlTextField.setPreferredSize(new Dimension(300, 20));
		checkPanel.setBackground(Color.GRAY);
		
		add(checkPanel);
		openSourceCheck.setBackground(Color.GRAY);
		checkPanel.add(openSourceCheck);
		openSourceCheck.addActionListener(this);
		openSourceCheck.setAlignmentX(JCheckBox.LEFT_ALIGNMENT);
		buttonPanel.setBackground(Color.GRAY);
		
		add(buttonPanel);
		buttonPanel.add(downloadButton);
		buttonPanel.add(cancelButton);
		downloadButton.addActionListener(this);
		downloadButton.setEnabled(false);
		cancelButton.addActionListener(this);
		cancelButton.setEnabled(false);
		progressPanel.setBackground(Color.GRAY);
		
		add(progressPanel);
		progressBar.setBackground(Color.WHITE);
		progressPanel.add(progressBar);
		progressBar.setVisible(false);
		progressBar.setValue(0);
		
		urlTextField.addMouseListener(new MouseAdapter(){
			@Override
            public void mouseClicked(MouseEvent e){
				if(!booClickedOnce){
					urlTextField.setText("");
					booClickedOnce = true;
				}
            }
		});
	}

	@Override
	public void actionPerformed(ActionEvent a) {
		if(a.getSource().equals(openSourceCheck) && openSourceCheck.isSelected() && booCanDownload){
			//Checked open source, enable download button
			downloadButton.setEnabled(true);
			
		}else if(a.getSource().equals(openSourceCheck)){
			//Open source unchecked, disable download button
			downloadButton.setEnabled(false);
			
		}else if(a.getSource().equals(downloadButton)){
			String mp3 = urlTextField.getText();
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
					progressBar.setValue(0);
					progressBar.setVisible(true);
					downloadWorker = new DownloadWorker(mp3);
					downloadWorker.execute();
					downloadButton.setEnabled(false);
					cancelButton.setEnabled(true);
					
					booCanDownload = false;
				}
			}
		}else{
			//Cancel button pressed, stop download
			downloadWorker.cancel(true);
		}
		
	}

	public void newInput(File file,Boolean boo) {
		//Doesn't have to do anything when a file is selected
	}

		

}
