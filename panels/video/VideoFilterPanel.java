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
	MagicPaper worker;
	JTextField textField;
	JLabel _outputNameLabel= new JLabel("Output name: ");
	JButton _generateButton = new JButton("Generate");
	String[] flipoptions = {"Horizontal","Vertical"};
	String[] rotateoptions = {"90","180","270"};
	
	JComboBox<?> _comboBoxFlip = new JComboBox<Object>(flipoptions);
	JComboBox<?> _comboBoxRotate = new JComboBox<Object>(rotateoptions);
	private final JRadioButton rdbtnRotate = new JRadioButton("Rotate");
	JRadioButton rdbtnFlip;
	JRadioButton rdbtntrim;
	private final JSpinner spinner2 = new JSpinner();
	JSpinner spinner1;
	PlayerPanel _player = new PlayerPanel();
	ButtonGroup buttonGroup = new ButtonGroup();
	private final JPanel panel = new JPanel();
	private final JPanel panel_1 = new JPanel();
	private final JPanel panel_2 = new JPanel();
	
	
	public VideoFilterPanel(PlayerPanel player){
		_player = player;
		
		setBackground(Color.LIGHT_GRAY);
		setPreferredSize(new Dimension(750,200));
		setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(128, 151, 356, 22);
		add(textField);
		textField.setColumns(10);
		
		_outputNameLabel.setBounds(25, 153, 102, 15);
		add(_outputNameLabel);
		
		
		_generateButton.setBounds(595, 151, 155, 23);
		_generateButton.addActionListener(this);
		add(_generateButton);
		
		_comboBoxFlip.setBounds(318, 39, 129, 24);
		add(_comboBoxFlip);
		
		_comboBoxRotate.setBounds(603, 39, 129, 24);
		add(_comboBoxRotate);
		
		rdbtntrim = new JRadioButton("Trim");
		rdbtntrim.setBackground(UIManager.getColor("Button.background"));
		rdbtntrim.setBounds(33, 8, 149, 23);
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
		_generateButton.setEnabled(false);
		panel.setBounds(25, 4, 157, 74);
		
		add(panel);
		panel_1.setBounds(301, 4, 157, 74);
		
		add(panel_1);
		panel_2.setBounds(593, 4, 157, 74);
		
		add(panel_2);
		panel_2.add(rdbtnRotate);
		rdbtnRotate.setBackground(UIManager.getColor("Button.background"));
		buttonGroup.add(rdbtnRotate);
	}
	
	class MagicPaper extends SwingWorker<Void,Integer> {
		String choice;
		public MagicPaper(String option){
			choice = option;
		}
		@Override
		protected Void doInBackground() throws Exception {
			if(choice.equals("trim")){
				int dur = (int) (Float.parseFloat(spinner2.getValue().toString())-Float.parseFloat(spinner1.getValue().toString()));
				bashCommand("avconv -y -ss "+spinner1.getValue()+" -i "+_player.getFile().getAbsolutePath()+" -t "+dur+" -vcodec copy -acodec copy "+textField.getText()+".mp4");
				System.out.println("avconv -y -ss "+spinner1.getValue()+" -i "+_player.getFile().getAbsolutePath()+" -t "+spinner2.getValue()+" -vcodec copy -acodec copy "+textField.getText()+".mp4");
			}else if(choice.equals("flip")){
				if(_comboBoxFlip.getSelectedItem().equals("Horizontal")){
					bashCommand("avconv -y -i "+_player.getFile().getAbsolutePath()+" -c:v libx264 -c:a copy -vf \"hflip\" "+textField.getText()+".mp4");
				}else if(_comboBoxFlip.getSelectedItem().equals("Vertical")){
					bashCommand("avconv -y -i "+_player.getFile().getAbsolutePath()+" -c:v libx264 -c:a copy -vf \"vflip\" "+textField.getText()+".mp4");
				}
				
			}else if(choice.equals("rotate")){
				if(_comboBoxRotate.getSelectedItem().equals("90")){
					bashCommand("avconv -y -i "+_player.getFile().getAbsolutePath()+" -c:a copy -q 1 -r 23.967 -vf \"transpose=1\" "+textField.getText()+".mp4");
				}else if(_comboBoxRotate.getSelectedItem().equals("180")){
					bashCommand("avconv -y -i "+_player.getFile().getAbsolutePath()+" -c:a copy -q 1 -r 23.967 -vf \"transpose=1\",\"transpose=1\" "+textField.getText()+".mp4");
				}else if(_comboBoxRotate.getSelectedItem().equals("270")){
					bashCommand("avconv -y -i "+_player.getFile().getAbsolutePath()+" -c:a copy -q 1 -r 23.967 -vf \"transpose=1\",\"transpose=1\",\"transpose=1\" "+textField.getText()+".mp4");
				}
			}
			return null;
		}
		
		protected void process(){
			
		}
		
		protected void done(){
			_generateButton.setEnabled(true);
			
		}
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(_generateButton)){
			if(textField.getText().equals(null)||textField.getText().equals("")){
			}else{
				File f = new File(textField.getText()+".mp4");
				if(f.exists()){
					Object[] options = {"Overwrite","Don't overwrite"};
					System.out.println(_player.getFile().getName());
					if(_player.getFile().getName().equals(textField.getText()+".mp4")){
						Object[] options2 = {"Ok"};
						JOptionPane.showOptionDialog(this,
					    "The file you are entering is the file you are working on, please pick another.","Option",JOptionPane.OK_OPTION,
					    JOptionPane.QUESTION_MESSAGE,null,options2,null);
					}else{
						int optionPicked = JOptionPane.showOptionDialog(this,
					    "Overwrite the file named "+textField.getText(),"Option",JOptionPane.YES_NO_OPTION,
					    JOptionPane.QUESTION_MESSAGE,null,options,null);
						System.out.println("fuck");
						if(optionPicked == 0){
							if(rdbtntrim.isSelected()){
								_generateButton.setEnabled(false);
								worker = new MagicPaper("trim");
								worker.execute();
							}else if(rdbtnRotate.isSelected()){
								_generateButton.setEnabled(false);
								worker = new MagicPaper("rotate");
								worker.execute();
							}else if(rdbtnFlip.isSelected()){
								System.out.println("fuck2");
								_generateButton.setEnabled(false);
								worker = new MagicPaper("flip");
								worker.execute();
							}
						}else if(optionPicked == 1){
							JOptionPane.showMessageDialog(this,"File name taken. Change the outname");
						}
					}
				}else{
					
					if(rdbtntrim.isSelected()){
						System.out.println("here");
						if(spinner2.getValue().equals(spinner1.getValue())||Float.parseFloat(spinner2.getValue().toString())<Float.parseFloat(spinner1.getValue().toString())){
							JOptionPane.showMessageDialog(this,"The spinner values are not acceptable, change them please.");
						}else{
							_generateButton.setEnabled(false);
							worker = new MagicPaper("trim");
							worker.execute();
						}
					}else if(rdbtnRotate.isSelected()){
						_generateButton.setEnabled(false);
						worker = new MagicPaper("rotate");
						worker.execute();
					}else if(rdbtnFlip.isSelected()){
						_generateButton.setEnabled(false);
						worker = new MagicPaper("flip");
						worker.execute();
					}
				}
			}
			
		
		}
		
	}

	@Override
	public void newInput(File file, Boolean boo) {
		
		if(boo){
			System.out.println(_player.getLength());
			SpinnerModel model = new SpinnerNumberModel(0,0,_player.getLength(),1);
			SpinnerModel model2 = new SpinnerNumberModel(0,0,_player.getLength(),1);
			spinner1.setModel(model);
			spinner2.setModel(model2);
			_generateButton.setEnabled(true);
		}else{
			_generateButton.setEnabled(false);
			SpinnerModel model = new SpinnerNumberModel(0,0,0,1);
			spinner1.setModel(model);
			spinner2.setModel(model);
		}
	}
}
