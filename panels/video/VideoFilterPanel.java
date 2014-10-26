package panels.video;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;

import java.awt.Color;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JPanel;
import javax.swing.UIManager;

import panels.basic.VamixPanel;
import panels.player.PlayerPanel;

/**
 * 
 * @author logan
 *	Most of the commands in the workers are implemented from sources off of the internet
 */
@SuppressWarnings("serial")
public class VideoFilterPanel extends VamixPanel implements ActionListener{
	VideoFilterWorker worker;
	JTextField textField;
	JLabel outputNameLabel= new JLabel("Output name: ");
	JButton generateButton = new JButton("Generate");
	String[] flipoptions = {"Horizontal","Vertical"};
	String[] rotateoptions = {"90","180","270"};
	
	JComboBox<?> comboBoxFlip = new JComboBox<Object>(flipoptions);
	JComboBox<?> comboBoxRotate = new JComboBox<Object>(rotateoptions);
	private final JRadioButton rdbtnRotate = new JRadioButton("Rotate");
	JRadioButton rdbtnFlip;
	JRadioButton rdbtntrim;
	private final JSpinner spinner2 = new JSpinner();
	JSpinner spinner1;
	PlayerPanel player = new PlayerPanel();
	ButtonGroup buttonGroup = new ButtonGroup();
	private final JPanel panel = new JPanel();
	private final JPanel panel_1 = new JPanel();
	private final JPanel panel_2 = new JPanel();
	private final JLabel toLabel = new JLabel("To");
	
	
	public VideoFilterPanel(PlayerPanel p){
		player = p;
		
		setBackground(Color.LIGHT_GRAY);
		setPreferredSize(new Dimension(750,200));
		setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(128, 151, 356, 22);
		add(textField);
		textField.setColumns(10);
		
		outputNameLabel.setBounds(25, 153, 102, 15);
		add(outputNameLabel);
		
		
		generateButton.setBounds(595, 151, 155, 23);
		generateButton.addActionListener(this);
		add(generateButton);
		
		comboBoxFlip.setBounds(318, 39, 129, 24);
		add(comboBoxFlip);
		
		comboBoxRotate.setBounds(603, 39, 129, 24);
		add(comboBoxRotate);
		
		JLabel fromLabel = new JLabel("From");
		fromLabel.setBounds(34, 64, 35, 15);
		add(fromLabel);
		toLabel.setBounds(151, 64, 17, 15);
		
		add(toLabel);
		
		rdbtntrim = new JRadioButton("Trim");
		rdbtntrim.setBackground(UIManager.getColor("Button.background"));
		rdbtntrim.setBounds(65, 8, 117, 23);
		add(rdbtntrim);
		
		rdbtnFlip = new JRadioButton("Flip");
		rdbtnFlip.setBackground(UIManager.getColor("Button.background"));
		rdbtnFlip.setBounds(353, 8, 70, 23);
		add(rdbtnFlip);
		buttonGroup.add(rdbtntrim);
		buttonGroup.add(rdbtnFlip);
		
		SpinnerModel model = new SpinnerNumberModel(0,0,0,1);
		SpinnerModel model2 = new SpinnerNumberModel(0,0,0,1);
		spinner1 = new JSpinner();
		spinner1.setBounds(33, 42, 61, 20);
		spinner1.setModel(model);
		add(spinner1);
		
		spinner2.setBounds(109, 42, 61, 20);
		spinner2.setModel(model2);
		add(spinner2);
		generateButton.setEnabled(false);
		panel.setBounds(25, 4, 157, 91);
		
		add(panel);
		panel_1.setBounds(301, 4, 157, 91);
		
		add(panel_1);
		panel_2.setBounds(593, 4, 157, 85);
		
		add(panel_2);
		panel_2.add(rdbtnRotate);
		rdbtnRotate.setBackground(UIManager.getColor("Button.background"));
		buttonGroup.add(rdbtnRotate);
	}
	
	//Swingworker for the videofilterpanel, does all the options, selected by a button on UI
	class VideoFilterWorker extends SwingWorker<Void,Integer> {
		String choice;
		public VideoFilterWorker(String option){
			choice = option;
		}
		@Override
		protected Void doInBackground() throws Exception {
			
			//trim selector was selected
			if(choice.equals("trim")){
				int dur = (int) (Float.parseFloat(spinner2.getValue().toString())-Float.parseFloat(spinner1.getValue().toString()));
				bashCommand("avconv -y -ss "+spinner1.getValue()+" -i "+player.getFile().getAbsolutePath()+" -t "+dur+" -vcodec copy -acodec copy "+textField.getText()+".mp4");
							
			//flip selector was selected
			}else if(choice.equals("flip")){
				if(comboBoxFlip.getSelectedItem().equals("Horizontal")){
					bashCommand("avconv -y -i "+player.getFile().getAbsolutePath()+" -c:v libx264 -c:a copy -vf \"hflip\" "+textField.getText()+".mp4");
				}else if(comboBoxFlip.getSelectedItem().equals("Vertical")){
					bashCommand("avconv -y -i "+player.getFile().getAbsolutePath()+" -c:v libx264 -c:a copy -vf \"vflip\" "+textField.getText()+".mp4");
				}
			
			//rotate selector was selected	
			}else if(choice.equals("rotate")){
				if(comboBoxRotate.getSelectedItem().equals("90")){
					bashCommand("avconv -y -i "+player.getFile().getAbsolutePath()+" -c:a copy -q 1 -r 23.967 -vf \"transpose=1\" "+textField.getText()+".mp4");
				}else if(comboBoxRotate.getSelectedItem().equals("180")){
					bashCommand("avconv -y -i "+player.getFile().getAbsolutePath()+" -c:a copy -q 1 -r 23.967 -vf \"transpose=1\",\"transpose=1\" "+textField.getText()+".mp4");
				}else if(comboBoxRotate.getSelectedItem().equals("270")){
					bashCommand("avconv -y -i "+player.getFile().getAbsolutePath()+" -c:a copy -q 1 -r 23.967 -vf \"transpose=1\",\"transpose=1\",\"transpose=1\" "+textField.getText()+".mp4");
				}
			}
			return null;
		}
		
		protected void process(){
			
		}
		
		protected void done(){
			generateButton.setEnabled(true);
			
		}
	}
	public void actionPerformed(ActionEvent e) {
		
		//if the generate button was selected
		if(e.getSource().equals(generateButton)){
			
			//if the textfield is empty for the output name
			if(textField.getText().equals(null)||textField.getText().equals("")){
			}
			
			//if the text field contains text for the output name
			else{
				File f = new File(textField.getText()+".mp4");
				
				//if the output file already exists
				if(f.exists()){
					Object[] options = {"Overwrite","Don't overwrite"};
										
					//If the file you are trying to overwrite is the one you are trying to work on, go here
					if(player.getFile().getName().equals(textField.getText()+".mp4")){
						Object[] options2 = {"Ok"};
						JOptionPane.showOptionDialog(this,
					    "The file name you are entering is the name of the \nfile you are working on, please pick another.","Option",JOptionPane.OK_OPTION,
					    JOptionPane.QUESTION_MESSAGE,null,options2,null);
					}
					
					//If the file is able to be overwritten
					else{
						int optionPicked = JOptionPane.showOptionDialog(this,
					    "Overwrite the file named "+textField.getText()+"?","Option",JOptionPane.YES_NO_OPTION,
					    JOptionPane.QUESTION_MESSAGE,null,options,null);
						
						//if chosen to overwrite
						if(optionPicked == 0){
							if(rdbtntrim.isSelected()){
								generateButton.setEnabled(false);
								worker = new VideoFilterWorker("trim");
								worker.execute();
							}else if(rdbtnRotate.isSelected()){
								generateButton.setEnabled(false);
								worker = new VideoFilterWorker("rotate");
								worker.execute();
							}else if(rdbtnFlip.isSelected()){
								generateButton.setEnabled(false);
								worker = new VideoFilterWorker("flip");
								worker.execute();
							}else{
								JOptionPane.showMessageDialog(this,"You have not selected an option. \nSelect an option to continue");
							}
						//if the file is able to be overwritten and chosen not to overwrite
						}else if(optionPicked == 1){
							JOptionPane.showMessageDialog(this,"File name taken. Change the outname");
						}
					}
				//if the file didn't exist already
				}else{
					
					
					if(rdbtntrim.isSelected()){
						if(spinner2.getValue().equals(spinner1.getValue())||Float.parseFloat(spinner2.getValue().toString())<Float.parseFloat(spinner1.getValue().toString())){
							JOptionPane.showMessageDialog(this,"The spinner values are not acceptable, change them please.");
						}else{
							generateButton.setEnabled(false);
							worker = new VideoFilterWorker("trim");
							worker.execute();
						}
					}else if(rdbtnRotate.isSelected()){
						generateButton.setEnabled(false);
						worker = new VideoFilterWorker("rotate");
						worker.execute();
					}else if(rdbtnFlip.isSelected()){
						generateButton.setEnabled(false);
						worker = new VideoFilterWorker("flip");
						worker.execute();
					}
				}
			}
			
		
		}
		
	}

	@Override
	public void newInput(File file, Boolean boo) {
		//if the file is video
		if(boo){
			SpinnerModel model = new SpinnerNumberModel(0,0,player.getLength(),1);
			SpinnerModel model2 = new SpinnerNumberModel(0,0,player.getLength(),1);
			spinner1.setModel(model);
			spinner2.setModel(model2);
			generateButton.setEnabled(true);
		//if the file is not video
		}else{
			generateButton.setEnabled(false);
			SpinnerModel model = new SpinnerNumberModel(0,0,0,1);
			spinner1.setModel(model);
			spinner2.setModel(model);
		}
	}
}
