import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import javax.swing.JProgressBar;


public class PlayerPanel extends VamixPanel implements ActionListener, ChangeListener{

	private static final long serialVersionUID = 1L;
	
	private EmbeddedMediaPlayerComponent player = new EmbeddedMediaPlayerComponent();
	private JPanel playerPanel = new JPanel();
	File playicon = new File("PlayButton.png");
	private JButton playPause = new JButton(new ImageIcon(playicon.getAbsolutePath()));
	//private JButton playPause = new JButton("Play");
	private JButton backwards = new JButton("Back");
	private JButton stop = new JButton("Stop");
	private JButton fastForward = new JButton("FastForward");
	File _file;
	
	private JButton mute = new JButton("Mute");
	private JSlider volume = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
	private Timer timer = new Timer(10, this);
	private JSlider _timeOfVideo = new JSlider();
	
	public PlayerPanel(){
		setBackground(Color.LIGHT_GRAY);
		
			
		Canvas canvas = new Canvas();
		canvas.setBackground(Color.black);
		canvas.setPreferredSize(new Dimension(400,320));
		playerPanel.setBounds(12, 2, 400, 320);
		playerPanel.setBackground(Color.LIGHT_GRAY);
		
		playerPanel.setPreferredSize(new Dimension(400,320));
		player.setPreferredSize(new Dimension(400,320));
		playerPanel.add(player);
		
		playerPanel.add(canvas);
		playerPanel.setVisible(true);
		setLayout(null);
		add(playerPanel);
		backwards.setBounds(12, 349, 67, 25);
		
		add(backwards);
		
		
		backwards.addActionListener(this);
		playPause.setBounds(91, 349, 80, 25);
		playPause.setMargin(new Insets(0, 0, 0, 0));
		add(playPause);	
		playPause.addActionListener(this);
		fastForward.setBounds(187, 349, 124, 25);

		add(fastForward);
		fastForward.addActionListener(this);
		mute.setBounds(12, 381, 70, 25);
				
		add(mute);
		mute.addActionListener(this);
		stop.setBounds(323, 349, 67, 25);
		
		add(stop);
		stop.addActionListener(this);
		
		JLabel label = new JLabel("Lower");
		label.setBounds(90, 391, 44, 15);
		add(label);
		volume.setBackground(Color.LIGHT_GRAY);
		volume.setBounds(152, 386, 200, 16);
		add(volume);
		volume.addChangeListener(this);
		JLabel label_1 = new JLabel("LOUD");
		label_1.setBounds(355, 391, 39, 15);
		add(label_1);
		
		_timeOfVideo.setBounds(12, 321, 400, 16);
		_timeOfVideo.setMaximum(100);
		_timeOfVideo.setValue(0);
		_timeOfVideo.setEnabled(false);
		add(_timeOfVideo);
		setVisible(true);
	}
	
	class MagicPaper extends SwingWorker<Void,Integer> {

		@Override
		protected Void doInBackground() throws Exception {
			while(true){
				publish(1);
			}
		}
		
		protected void process(Integer[] chunks){
			setPosition();
		}
	
	}
	
	public void newInput(File file) {
		//Set the currently playing file as given TODO
//		player.getMediaPlayer().playMedia(file.getAbsolutePath());
//		playPause.setText("Pause");
		MagicPaper job = new MagicPaper();
		job.execute();
	}
	
	public void actionPerformed(ActionEvent a) {
		//Disable the backwards timer as another event has taken place
		timer.stop();
		//Check which button was pressed
		if(a.getSource().equals(playPause)){
			//Check if the pause or play button was pressed
			if(!player.getMediaPlayer().isPlaying()){
				//Play button pressed
				player.getMediaPlayer().play();
				playPause.setText("Pause");
			}else if(player.getMediaPlayer().getRate() != 1){
				//Set the play rate back to standard
				player.getMediaPlayer().setRate(1);
				playPause.setText("Pause");
			}else{
				//Pause button pressed
				player.getMediaPlayer().pause();
				playPause.setText("Play");
			}
			
		}else if(a.getSource().equals(backwards)){
			//Begin the timer that will trigger backwards jumps and pause the video
			timer.start();
			player.getMediaPlayer().pause();
			
			//Change play button back to default
			playPause.setText("Play");
			player.getMediaPlayer().setRate(1);
			
		}else if(a.getSource().equals(fastForward)){
			//Set rate to fast forward and start playing if it isn't
			player.getMediaPlayer().play();
			player.getMediaPlayer().setRate(4);
			
			//Change the play button back to play
			playPause.setText("Play");
			
		}else if(a.getSource().equals(stop)){
			player.getMediaPlayer().stop();
			
			//Change the play button back to play
			playPause.setText("Play");
			
		}else if(a.getSource().equals(mute)){
			if(!player.getMediaPlayer().isMute()){
				player.getMediaPlayer().mute(true);
				mute.setText("Unmute");
			}else{
				player.getMediaPlayer().mute(false);
				mute.setText("Mute");
			}
		}else if(a.getSource().equals(timer)){
			player.getMediaPlayer().skip(-100);
			timer.start();
		}
	}

	public void stateChanged(ChangeEvent e) {
		//Volume slider was moved, change volume accordingly
		JSlider source = (JSlider)e.getSource();
		player.getMediaPlayer().setVolume(source.getValue());
	}
	
	/**
	 * Returns the length of the current video
	 * @return
	 */
	public long getLength(){
		return player.getMediaPlayer().getLength();
	}
	
	public void destroy(){
		player.getMediaPlayer().stop();
		player.getMediaPlayer().release();
	}
	
	public float getPosition(){
		return player.getMediaPlayer().getPosition();
	}
	
	public void setPosition(){
		_timeOfVideo.setValue((int) (getPosition()/getLength()));
	}
}
