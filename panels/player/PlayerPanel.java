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


public class PlayerPanel extends VamixPanel implements ActionListener, ChangeListener{

	private static final long serialVersionUID = 1L;
	
	private URL _playIcon = Thread.currentThread().getContextClassLoader().getResource("PlayButton.png");
	private URL _pauseIcon = Thread.currentThread().getContextClassLoader().getResource("PauseButton.png");
	private URL _forwardIcon = Thread.currentThread().getContextClassLoader().getResource("ForwardButton.png");
	private URL _stopIcon = Thread.currentThread().getContextClassLoader().getResource("StopIcon.png");
	private URL _backIcon = Thread.currentThread().getContextClassLoader().getResource("BackButton.png");
	private URL _unmuteIcon = Thread.currentThread().getContextClassLoader().getResource("UnmuteIcon.png");
	private URL _muteIcon = Thread.currentThread().getContextClassLoader().getResource("MuteButton.png");
	
	private EmbeddedMediaPlayerComponent _player = new EmbeddedMediaPlayerComponent();
	private JPanel _playerPanel = new JPanel();
	
	
	private JButton _playPause = new JButton(new ImageIcon(_playIcon));
	private JButton _fastForward = new JButton(new ImageIcon(_forwardIcon));
	private JButton _stop = new JButton(new ImageIcon(_stopIcon));
	private JButton _backwards = new JButton(new ImageIcon(_backIcon));
	
	File _file;
	
	private JButton _mute = new JButton(new ImageIcon(_muteIcon));
	private JSlider _volume = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
	private Timer _timer = new Timer(10, this);
	private Timer _timerForUpdatePlay = new Timer(100,this);
	private JSlider _timeOfVideo = new JSlider();
	private JTextField _textTimeOfVideo;
	
	//Starting and setting of all parameters for the swing parts
	public PlayerPanel(){
		setBackground(Color.RED);
		_timerForUpdatePlay.start();
		
		_timerForUpdatePlay.addActionListener(this);
		Canvas canvas = new Canvas();
		canvas.setBackground(Color.black);
		canvas.setPreferredSize(new Dimension(400,320));
		_playerPanel.setBounds(12, 12, 400, 310);
		_playerPanel.setBackground(Color.BLACK);
		
		_playerPanel.setPreferredSize(new Dimension(400,320));
		_player.setPreferredSize(new Dimension(400,320));
		_playerPanel.add(_player);
		
		_playerPanel.add(canvas);
		_playerPanel.setVisible(true);
		setLayout(null);
		add(_playerPanel);
		_backwards.setBorderPainted(false);
		_backwards.setBackground(Color.GRAY);
		_backwards.setBounds(12, 349, 32, 32);
		
		add(_backwards);
		
		
		_backwards.addActionListener(this);
		_playPause.setBackground(Color.GRAY);
		_playPause.setBorderPainted(false);
		_playPause.setBounds(52, 342, 48, 48);
		_playPause.setMargin(new Insets(0, 0, 0, 0));
		add(_playPause);	
		_playPause.addActionListener(this);
		_fastForward.setBackground(Color.GRAY);
		_fastForward.setBorderPainted(false);
		_fastForward.setBounds(109, 349, 32, 32);

		add(_fastForward);
		_fastForward.addActionListener(this);
		_mute.setBackground(Color.GRAY);
		_mute.setBounds(194, 349, 32, 32);
		_mute.setBorderPainted(false);
				
		add(_mute);
		_mute.addActionListener(this);
		_stop.setBackground(Color.GRAY);
		_stop.setBounds(152, 349, 32, 32);
		_stop.setBorderPainted(false);
		
		add(_stop);
		_stop.addActionListener(this);
		
		JLabel label = new JLabel("Lower");
		label.setBounds(236, 366, 44, 15);
		add(label);
		_volume.setBackground(Color.GRAY);
		_volume.setBounds(230, 349, 100, 16);
		_volume.setValue(200);
		add(_volume);
		_volume.addChangeListener(this);
		JLabel label_1 = new JLabel("LOUD");
		label_1.setBounds(292, 366, 39, 15);
		add(label_1);
		
		_timeOfVideo.setBounds(12, 321, 400, 16);
		_timeOfVideo.setMaximum(1000);
		_timeOfVideo.setEnabled(false);
		add(_timeOfVideo);
		
		_textTimeOfVideo = new JTextField();
		_textTimeOfVideo.setBackground(Color.GRAY);
		_textTimeOfVideo.setBounds(342, 346, 70, 19);
		_textTimeOfVideo.setFocusable(false);
		add(_textTimeOfVideo);
		_textTimeOfVideo.setColumns(10);
		
		_playPause.setEnabled(false);
		_backwards.setEnabled(false);
		_fastForward.setEnabled(false);
		_mute.setEnabled(false);
		_stop.setEnabled(false);
		_volume.setEnabled(false);
		
		setVisible(true);
	}
	
	
	/**
	 * sets the player to the file input, and a boolean which is not used here
	 */
	public void newInput(File file,Boolean boo) {
		_player.getMediaPlayer().mute(false);
		_file = file;
		_player.getMediaPlayer().playMedia(file.getAbsolutePath());
		_player.getMediaPlayer().mute(false);
		_playPause.setEnabled(true);
		_backwards.setEnabled(true);
		_fastForward.setEnabled(true);
		_mute.setEnabled(true);
		_stop.setEnabled(true);
		_volume.setEnabled(true);
		_playPause.doClick();
	}
	
	//Basic actionperformed check
	public void actionPerformed(ActionEvent a) {
		//Check which button was pressed
		if(a.getSource().equals(_playPause)){
			//Disable the backwards timer as another event has taken place
			_timer.stop();
			
			//Check if the pause or play button was pressed
			if(!_player.getMediaPlayer().isPlaying()){
				//Play button pressed
				_player.getMediaPlayer().play();
				_playPause.setIcon(new ImageIcon(_pauseIcon));
			}else if(_player.getMediaPlayer().getRate() != 1){
				//Set the play rate back to standard
				_player.getMediaPlayer().setRate(1);
				_playPause.setIcon(new ImageIcon(_pauseIcon));
			}else{
				//Pause button pressed
				_player.getMediaPlayer().pause();
				_playPause.setIcon(new ImageIcon(_playIcon));
			}
			
		}else if(a.getSource().equals(_backwards)){
			//Begin the timer that will trigger backwards jumps and pause the video
			_timer.start();
			_player.getMediaPlayer().pause();
			
			//Change play button back to default
			_playPause.setIcon(new ImageIcon(_playIcon));
			_player.getMediaPlayer().setRate(1);
			
		}else if(a.getSource().equals(_fastForward)){
			//Disable the backwards timer as another event has taken place
			_timer.stop();
			
			//Set rate to fast forward and start playing if it isn't
			_player.getMediaPlayer().play();
			_player.getMediaPlayer().setRate(4);
			
			//Change the play button back to play
			_playPause.setIcon(new ImageIcon(_playIcon));
			
		}else if(a.getSource().equals(_stop)){
			//Disable the backwards timer as another event has taken place
			_timer.stop();
			
			_player.getMediaPlayer().stop();
			
			//Change the play button back to play
			_playPause.setIcon(new ImageIcon(_playIcon));
			_player.getMediaPlayer().setRate(1);
			
		}else if(a.getSource().equals(_mute)){
			//Disable the backwards timer as another event has taken place
			_timer.stop();
			
			if(!_player.getMediaPlayer().isMute()){
				_player.getMediaPlayer().mute(true);
				_mute.setIcon(new ImageIcon(_unmuteIcon));
			}else{
				_player.getMediaPlayer().mute(false);
				_mute.setIcon(new ImageIcon(_muteIcon));
			}
			
			//allows the rewinding
		}else if(a.getSource().equals(_timer)){
			_player.getMediaPlayer().skip(-100);
			
			//Updates the playbar under the play window to the current position of the video
		}else if(a.getSource().equals(_timerForUpdatePlay)){
			String hour,minute,second;
			long time = _player.getMediaPlayer().getTime()/1000;
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
			_textTimeOfVideo.setText(hour+":"+minute+":"+second);
			
			Float f = _player.getMediaPlayer().getPosition()*1000;
			
			_timeOfVideo.setValue(f.intValue());
			_timeOfVideo.repaint();
		}
	}

	public void stateChanged(ChangeEvent e) {
		//Volume slider was moved, change volume accordingly
		JSlider source = (JSlider)e.getSource();
		_player.getMediaPlayer().setVolume(source.getValue());
	}
	
	/**
	 * Returns the length of the current video
	 * @return
	 */
	public long getLength(){
		return _player.getMediaPlayer().getLength()/1000;
	}
	
	/**
	 * destroys all active non-automatically closing parts of playerpanel
	 */
	public void destroy(){
		_player.getMediaPlayer().mute(false);
		_playPause.doClick();
		_player.getMediaPlayer().stop();
		_player.getMediaPlayer().release();
		_timerForUpdatePlay.stop();
	}
	
	public void resetPlayer(){
		_player.getMediaPlayer().playMedia(_file.getAbsolutePath());
	}
	
	public File getFile(){
		return _file;
	}
}
