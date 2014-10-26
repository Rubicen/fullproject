package panels.player;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import javax.swing.JTextField;

import panels.basic.VamixPanel;

/**
 * 
 * @author logan and sam
 *	this class is a combined class from the joined section (assignment 3)
 */
public class PlayerPanel extends VamixPanel implements ActionListener, ChangeListener{

	private static final long serialVersionUID = 1L;
	
	private URL playIcon = Thread.currentThread().getContextClassLoader().getResource("PlayButton.png");
	private URL pauseIcon = Thread.currentThread().getContextClassLoader().getResource("PauseButton.png");
	private URL forwardIcon = Thread.currentThread().getContextClassLoader().getResource("ForwardButton.png");
	private URL stopIcon = Thread.currentThread().getContextClassLoader().getResource("StopIcon.png");
	private URL backIcon = Thread.currentThread().getContextClassLoader().getResource("BackButton.png");
	private URL unmuteIcon = Thread.currentThread().getContextClassLoader().getResource("UnmuteIcon.png");
	private URL muteIcon = Thread.currentThread().getContextClassLoader().getResource("MuteButton.png");
	
	private EmbeddedMediaPlayerComponent player = new EmbeddedMediaPlayerComponent();
	private JPanel playerPanel = new JPanel();
	
	
	private JButton playPause = new JButton(new ImageIcon(playIcon));
	private JButton fastForward = new JButton(new ImageIcon(forwardIcon));
	private JButton stop = new JButton(new ImageIcon(stopIcon));
	private JButton backwards = new JButton(new ImageIcon(backIcon));
	Boolean timerChanging = false;
	
	File file;
	
	private JButton mute = new JButton(new ImageIcon(muteIcon));
	private JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
	private Timer timer = new Timer(10, this);
	private Timer timerForUpdatePlay = new Timer(100,this);
	private JSlider timeOfVideo = new JSlider();
	private JTextField textTimeOfVideo;
	
	//Starting and setting of all parameters for the swing parts
	public PlayerPanel(){
		setBackground(Color.RED);
		timerForUpdatePlay.start();
		
		timerForUpdatePlay.addActionListener(this);
		Canvas canvas = new Canvas();
		canvas.setBackground(Color.black);
		canvas.setPreferredSize(new Dimension(400,320));
		playerPanel.setBounds(12, 12, 400, 310);
		playerPanel.setBackground(Color.BLACK);
		
		playerPanel.setPreferredSize(new Dimension(400,320));
		player.setPreferredSize(new Dimension(400,320));
		playerPanel.add(player);
		
		playerPanel.add(canvas);
		playerPanel.setVisible(true);
		setLayout(null);
		add(playerPanel);
		backwards.setBorderPainted(false);
		backwards.setBackground(Color.GRAY);
		backwards.setBounds(12, 349, 32, 32);
		
		add(backwards);
		
		
		backwards.addActionListener(this);
		playPause.setBackground(Color.GRAY);
		playPause.setBorderPainted(false);
		playPause.setBounds(52, 342, 48, 48);
		playPause.setMargin(new Insets(0, 0, 0, 0));
		add(playPause);	
		playPause.addActionListener(this);
		fastForward.setBackground(Color.GRAY);
		fastForward.setBorderPainted(false);
		fastForward.setBounds(109, 349, 32, 32);

		add(fastForward);
		fastForward.addActionListener(this);
		mute.setBackground(Color.GRAY);
		mute.setBounds(194, 349, 32, 32);
		mute.setBorderPainted(false);
				
		add(mute);
		mute.addActionListener(this);
		stop.setBackground(Color.GRAY);
		stop.setBounds(152, 349, 32, 32);
		stop.setBorderPainted(false);
		
		add(stop);
		stop.addActionListener(this);
		
		JLabel label = new JLabel("Lower");
		label.setBounds(236, 366, 44, 15);
		add(label);
		volumeSlider.setBackground(Color.GRAY);
		volumeSlider.setBounds(230, 349, 100, 16);
		volumeSlider.setValue(200);
		add(volumeSlider);
		volumeSlider.addChangeListener(this);
		JLabel label_1 = new JLabel("LOUD");
		label_1.setBounds(292, 366, 39, 15);
		add(label_1);
		timeOfVideo.addChangeListener(this);
		
		
		timeOfVideo.setBounds(12, 321, 400, 16);
		timeOfVideo.setMaximum(1000);
		add(timeOfVideo);
		
		textTimeOfVideo = new JTextField();
		textTimeOfVideo.setBackground(Color.GRAY);
		textTimeOfVideo.setBounds(342, 346, 70, 19);
		textTimeOfVideo.setFocusable(false);
		add(textTimeOfVideo);
		textTimeOfVideo.setColumns(10);
		
		playPause.setEnabled(false);
		backwards.setEnabled(false);
		fastForward.setEnabled(false);
		mute.setEnabled(false);
		stop.setEnabled(false);
		volumeSlider.setEnabled(false);
		
		setVisible(true);
	}
	
	
	/**
	 * sets the player to the file input, and a boolean which is not used here
	 */
	public void newInput(File f,Boolean boo) {
		player.getMediaPlayer().mute(false);
		file = f;
		player.getMediaPlayer().playMedia(file.getAbsolutePath());
		player.getMediaPlayer().mute(false);
		playPause.setEnabled(true);
		backwards.setEnabled(true);
		fastForward.setEnabled(true);
		mute.setEnabled(true);
		stop.setEnabled(true);
		volumeSlider.setEnabled(true);
		playPause.doClick();
	}
	
	//Basic actionperformed check
	public void actionPerformed(ActionEvent a) {
		//Check which button was pressed
		if(a.getSource().equals(playPause)){
			//Disable the backwards timer as another event has taken place
			timer.stop();
			
			//Check if the pause or play button was pressed
			if(!player.getMediaPlayer().isPlaying()){
				//Play button pressed
				player.getMediaPlayer().play();
				playPause.setIcon(new ImageIcon(pauseIcon));
			}else if(player.getMediaPlayer().getRate() != 1){
				//Set the play rate back to standard
				player.getMediaPlayer().setRate(1);
				playPause.setIcon(new ImageIcon(pauseIcon));
			}else{
				//Pause button pressed
				player.getMediaPlayer().pause();
				playPause.setIcon(new ImageIcon(playIcon));
			}
			
		}else if(a.getSource().equals(backwards)){
			//Begin the timer that will trigger backwards jumps and pause the video
			timer.start();
			player.getMediaPlayer().pause();
			
			//Change play button back to default
			playPause.setIcon(new ImageIcon(playIcon));
			player.getMediaPlayer().setRate(1);
			
		}else if(a.getSource().equals(fastForward)){
			//Disable the backwards timer as another event has taken place
			timer.stop();
			
			//Set rate to fast forward and start playing if it isn't
			player.getMediaPlayer().play();
			player.getMediaPlayer().setRate(4);
			
			//Change the play button back to play
			playPause.setIcon(new ImageIcon(playIcon));
			
		}else if(a.getSource().equals(stop)){
			//Disable the backwards timer as another event has taken place
			timer.stop();
			
			player.getMediaPlayer().stop();
			
			//Change the play button back to play
			playPause.setIcon(new ImageIcon(playIcon));
			player.getMediaPlayer().setRate(1);
			
		}else if(a.getSource().equals(mute)){
			//Disable the backwards timer as another event has taken place
			timer.stop();
			
			if(!player.getMediaPlayer().isMute()){
				player.getMediaPlayer().mute(true);
				mute.setIcon(new ImageIcon(unmuteIcon));
			}else{
				player.getMediaPlayer().mute(false);
				mute.setIcon(new ImageIcon(muteIcon));
			}
			
			//allows the rewinding
		}else if(a.getSource().equals(timer)){
			player.getMediaPlayer().skip(-100);
			
			//Updates the playbar under the play window to the current position of the video
		}else if(a.getSource().equals(timerForUpdatePlay)){
			String hour,minute,second;
			long time = player.getMediaPlayer().getTime()/1000;
			long hours = time/3600;
			time = time-3600*hours;
			long minutes = time/60;
			time = time-60*minutes;
			long seconds = time;
			if(hours<10){
				hour="0"+Long.toString(hours);
			}else{
				hour=Long.toString(hours);
			}
			if(minutes<10){
				minute="0"+Long.toString(minutes);
			}else{
				minute=Long.toString(minutes);
			}
			if(seconds<10){
				second="0"+Long.toString(seconds);
			}else{
				second=Long.toString(seconds);
			}
			textTimeOfVideo.setText(hour+":"+minute+":"+second);
			
			Float f = player.getMediaPlayer().getPosition()*1000;
			timerChanging = true;
			timeOfVideo.setValue(f.intValue());
			timerChanging = false;
			timeOfVideo.repaint();
		}
	}

	public void stateChanged(ChangeEvent e) {
		//Volume slider was moved, change volume accordingly
		if(e.getSource().equals(volumeSlider)){
			JSlider source = (JSlider)e.getSource();
			player.getMediaPlayer().setVolume(source.getValue());
		}else{
			if(!timerChanging){
				Float timerPosition = (float) timeOfVideo.getValue()/1000;
				player.getMediaPlayer().setPosition(timerPosition);
			}
		}
		
		
	}
	
	/**
	 * Returns the length of the current video
	 * @return
	 */
	public long getLength(){
		return player.getMediaPlayer().getLength()/1000;
	}
	
	/**
	 * destroys all active non-automatically closing parts of playerpanel
	 */
	public void destroy(){
		player.getMediaPlayer().mute(false);
		playPause.doClick();
		player.getMediaPlayer().stop();
		player.getMediaPlayer().release();
		timerForUpdatePlay.stop();
	}
	
	/**
	 * resets the player, used by subtitlepanel to reset the subtitles to the new file
	 */
	public void resetPlayer(){
		player.getMediaPlayer().playMedia(file.getAbsolutePath());
	}
	
	/**
	 * simple getter for the video file
	 * @return
	 */
	public File getFile(){
		return file;
	}
}
