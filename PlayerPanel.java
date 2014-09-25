import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
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
	File pauseicon = new File("PauseButton.png");
	private JButton playPause = new JButton(new ImageIcon(playicon.getAbsolutePath()));
	//private JButton playPause = new JButton("Play");
	File forwardicon = new File("ForwardButton.png");
	private JButton fastForward = new JButton(new ImageIcon(forwardicon.getAbsolutePath()));
	File stopicon = new File("StopIcon.png");
	private JButton stop = new JButton(new ImageIcon(stopicon.getAbsolutePath()));
	File backicon = new File("BackButton.png");
	private JButton backwards = new JButton(new ImageIcon(backicon.getAbsolutePath()));
	
	File _file;
	
	File unmuteicon = new File("UnmuteIcon.png");
	File muteicon = new File("MuteButton.png");
	private JButton mute = new JButton(new ImageIcon(muteicon.getAbsolutePath()));
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
		backwards.setBorderPainted(false);
		backwards.setBackground(Color.LIGHT_GRAY);
		backwards.setBounds(12, 349, 32, 32);
		
		add(backwards);
		
		
		backwards.addActionListener(this);
		playPause.setBackground(Color.LIGHT_GRAY);
		playPause.setBorderPainted(false);
		playPause.setBounds(54, 349, 48, 48);
		playPause.setMargin(new Insets(0, 0, 0, 0));
		add(playPause);	
		playPause.addActionListener(this);
		fastForward.setBackground(Color.LIGHT_GRAY);
		fastForward.setBounds(109, 349, 32, 32);

		add(fastForward);
		fastForward.addActionListener(this);
		mute.setBackground(Color.LIGHT_GRAY);
		mute.setBounds(199, 349, 32, 32);
		mute.setBorderPainted(false);
				
		add(mute);
		mute.addActionListener(this);
		stop.setBackground(Color.LIGHT_GRAY);
		stop.setBounds(153, 349, 32, 32);
		stop.setBorderPainted(false);
		
		add(stop);
		stop.addActionListener(this);
		
		JLabel label = new JLabel("Lower");
		label.setBounds(236, 366, 44, 15);
		add(label);
		volume.setBackground(Color.LIGHT_GRAY);
		volume.setBounds(230, 349, 182, 16);
		add(volume);
		volume.addChangeListener(this);
		JLabel label_1 = new JLabel("LOUD");
		label_1.setBounds(373, 366, 39, 15);
		add(label_1);
		
		_timeOfVideo.setBounds(12, 321, 400, 16);
		_timeOfVideo.setMaximum(100);
		_timeOfVideo.setValue(0);
		_timeOfVideo.setEnabled(false);
		add(_timeOfVideo);
		setVisible(true);
	}
	
	
	
	public void newInput(File file,Boolean boo) {
		player.getMediaPlayer().mute(false);
		player.getMediaPlayer().playMedia(file.getAbsolutePath());
		playPause.setIcon(new ImageIcon(pauseicon.getAbsolutePath()));
		
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
				playPause.setIcon(new ImageIcon(pauseicon.getAbsolutePath()));
			}else if(player.getMediaPlayer().getRate() != 1){
				//Set the play rate back to standard
				player.getMediaPlayer().setRate(1);
				playPause.setIcon(new ImageIcon(pauseicon.getAbsolutePath()));
			}else{
				//Pause button pressed
				player.getMediaPlayer().pause();
				playPause.setIcon(new ImageIcon(playicon.getAbsolutePath()));
			}
			
		}else if(a.getSource().equals(backwards)){
			//Begin the timer that will trigger backwards jumps and pause the video
			timer.start();
			player.getMediaPlayer().pause();
			
			//Change play button back to default
			playPause.setIcon(new ImageIcon(playicon.getAbsolutePath()));
			player.getMediaPlayer().setRate(1);
			
		}else if(a.getSource().equals(fastForward)){
			//Set rate to fast forward and start playing if it isn't
			player.getMediaPlayer().play();
			player.getMediaPlayer().setRate(4);
			
			//Change the play button back to play
			playPause.setIcon(new ImageIcon(playicon.getAbsolutePath()));
			
		}else if(a.getSource().equals(stop)){
			player.getMediaPlayer().stop();
			
			//Change the play button back to play
			playPause.setIcon(new ImageIcon(playicon.getAbsolutePath()));
			
		}else if(a.getSource().equals(mute)){
			if(!player.getMediaPlayer().isMute()){
				player.getMediaPlayer().mute(true);
				mute.setIcon(new ImageIcon(unmuteicon.getAbsolutePath()));
			}else{
				player.getMediaPlayer().mute(false);
				mute.setIcon(new ImageIcon(muteicon.getAbsolutePath()));
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
		return player.getMediaPlayer().getLength()/1000;
	}
	
	public void destroy(){
		player.getMediaPlayer().stop();
		player.getMediaPlayer().release();
	}
	
	public float getPosition(){
		System.out.println(player.getMediaPlayer().getPosition());
		return player.getMediaPlayer().getPosition();
		
	}
	
	public void setPosition(){
		_timeOfVideo.setValue((int) (getPosition()/getLength())*100);
	}
}
