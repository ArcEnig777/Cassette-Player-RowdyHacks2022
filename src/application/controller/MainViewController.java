package application.controller;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import application.model.Song;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

public class MainViewController implements Initializable{
	
	//	File song;
	Duration songPos;
	MediaPlayer mp;
	private Timer timer;
	private TimerTask task;
	private boolean running;
	
    @FXML
    private ListView<Song> list;
    
    @FXML
    private Button restartButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button stopButton;
    
    @FXML
    private Button playButton;
    
    @FXML
    private Button chooseFileButton, rewindB;
    
    @FXML
    private Slider volumeSlider, songProgress;
    
    
    
    @FXML
    void play(ActionEvent event) {
    	System.out.println("play");
    	beginTimer();
    	mp.setVolume(volumeSlider.getValue() * .01);
    	mp.play();
    }

    @FXML
    void pause(ActionEvent event) {
    	cancelTimer();
    	System.out.println("pause");
    	mp.pause();
    }

    @FXML
    void stop(ActionEvent event) {
    	System.out.println("stop");
    	mp.stop();
    }

    @FXML
    void restart(ActionEvent event) {
    	System.out.println("restart");
    	mp.stop();
    	beginTimer();
    	mp.play();
    	
    }
    
    @FXML
    void openFile(ActionEvent event) throws URISyntaxException {
    	
    	DirectoryChooser fc = new DirectoryChooser();
    	File directory = fc.showDialog(null);
    	File[] fileArray = directory.listFiles();
    	
    	for ( File fa : fileArray ) {
    		list.getItems().add(new Song(fa.getName(), -1, fa));
    	}
    	Media mSong = new Media(fileArray[1].toURI().toString());
    	mp = new MediaPlayer(mSong);
    	AutoP();
    }
    
    @FXML
    void playSong(MouseEvent event) {
    	
    	System.out.println(volumeSlider.getValue());
    	if ( event.getClickCount() == 2 ) {
    		mp.stop();
    		File selected = list.getSelectionModel().getSelectedItem().getFile();
    		mp = new MediaPlayer( new Media(selected.toURI().toString() ) );
    		mp.setVolume(volumeSlider.getValue() * .01); // volume
    		
    		mp.play();
    		playMedia();
    		
    		timerProgress();
    	}
    	
    	
    }
   
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				mp.setVolume(volumeSlider.getValue() * .01); 
			}
			
		});
		
		//
		//initSliderColor();
		rewind();
	}
	
	
	public void playMedia()
    {
        mp.setOnEndOfMedia(new Runnable(){
            @Override
            public void run() {
                mp.stop();
                if(list.getSelectionModel().getSelectedIndex()+1<list.getItems().size())
                {
                    list.getSelectionModel().selectNext();
                    File selected = list.getSelectionModel().getSelectedItem().getFile();
                    mp = new MediaPlayer( new Media(selected.toURI().toString() ) );
                }
                else
                {
                    list.getSelectionModel().selectFirst();
                    File selected = list.getSelectionModel().getSelectedItem().getFile();
                    mp = new MediaPlayer(new Media(selected.toURI().toString() ) );
                }
                mp.play();
                timerProgress();
                playMedia();
            }
        });
        
        
    }
	
	
    void rewind() {
		/*
        if (event.equals(MouseEvent.ANY))
        {
            mp.setRate(8.0);
        }
        else if (event.equals(MouseEvent.MOUSE_RELEASED))
        {
            mp.setRate(1.0);
        }
		 */
		
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), actionEvent -> { 
            if(mp.getCurrentTime().toSeconds()>1.0)
            {
                mp.seek(Duration.seconds(mp.getCurrentTime().toSeconds()-10.0));
            }
            else if(mp.getTotalDuration().toSeconds() < 10.0) 
            {
            	mp.seek(Duration.seconds(mp.getCurrentTime().toSeconds()-0.10));
            }
            else
            {
                mp.pause();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);

        rewindB.armedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if(newValue) {

                    timeline.play();
                    mp.setRate(0.50);

                } else {

                    timeline.stop();
                    mp.setRate(1.0);
                    mp.play();
                    playMedia();

                }

            }
        });
		
		
    }
	
	void AutoP()
    {
        mp.setOnEndOfMedia(new Runnable(){
            @Override
            public void run() {
                mp.stop();
                if(list.getSelectionModel().getSelectedIndex()+1<list.getItems().size())
                {
                    list.getSelectionModel().selectNext();
                    File selected = list.getSelectionModel().getSelectedItem().getFile();
                    mp = new MediaPlayer( new Media(selected.toURI().toString() ) );
                }
                else
                {
                    list.getSelectionModel().selectFirst();
                    File selected = list.getSelectionModel().getSelectedItem().getFile();
                    mp = new MediaPlayer(new Media(selected.toURI().toString() ) );
                }

                playMedia();
            }
        });
    }
	
	
	
	
	void beginTimer() {
		timer = new Timer();
		
		task = new TimerTask() {
		
						
			public void run() {
				
				running = true;
				double current = mp.getCurrentTime().toSeconds();
				double end = mp.getCycleDuration().toSeconds();
				songProgress.setValue(current/end);
				
				if (current/end == 1) {
					cancelTimer();
				}
			}
			
		};
	}
	
	void cancelTimer() {
		running = false;
		timer.cancel();
	}
	
	void timerProgress() {
		
		initSliderColor();
		
		mp.setOnReady(()->{
			
			songProgress.setMin(0);
			songProgress.setValue(0);
			songProgress.setMax(mp.getTotalDuration().toSeconds());
			
			mp.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
				
				int sPTime = (int) newValue.toSeconds();
				songProgress.setValue(sPTime);
				System.out.println(sPTime);
				
			});
			
			songProgress.setOnMousePressed((MouseEvent event) -> {
				mp.seek(Duration.seconds(songProgress.getValue()));
			});
			
		});
		
		
		
		
		/*
		 songProgress.maxProperty().bind(Bindings.createDoubleBinding(
				()-> mp.getTotalDuration().toSeconds(),
				mp.totalDurationProperty()));
		
		
		songProgress.setOnMouseClicked((MouseEvent event) -> {
			mp.seek(Duration.seconds(songProgress.getValue() - mp.getCurrentTime().toSeconds()));
		});
		
		
		mp.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
			songProgress.setValue(newValue.toSeconds());
			System.out.println(newValue.toSeconds());
			
		});
		
		System.out.println(mp.getCurrentTime().toSeconds());
		
		 */
		
		/*
		 if(songProgress.getOnMouseClicked() != null || songProgress.getOnMouseDragged() != null) {
			
			
		}
		else {
			
		}
		 */
		
	}
	
	
	void initSliderColor() {
		 StackPane trackPane = (StackPane) songProgress.lookup(".track");
		 int Totaltime =   (int) mp.getTotalDuration().toSeconds();
		 int currentTime = (int) mp.getCurrentTime().toSeconds();
	    
		 songProgress.valueProperty().addListener(new ChangeListener<Number>() {
	            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
	                String style = String.format("-fx-background-color: linear-gradient(to right, #2D819D %d%%, #969696 %d%%);",
	                        newValue.intValue(), Totaltime);
	                trackPane.setStyle(style);
	            }
	        });

	        trackPane.setStyle("-fx-background-color: linear-gradient(to right, #2D819D 0%, #969696 0%);");

	}

}

