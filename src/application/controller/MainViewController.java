package application.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

import application.model.Song;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.Media;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainViewController implements Initializable {
	
	static MediaPlayer mp;
	static Duration loopStart;
	static Duration loopEnd;
	static boolean loopClosed = false;
	boolean retroEQ = false;
	RotateTransition rotate;
	RotateTransition rotateR;

    @FXML private AnchorPane mainpane;
    @FXML private ImageView leftTape;
    @FXML private ImageView rightTape;
    @FXML private ListView<Song> list;
    @FXML private Button restartButton;

    @FXML private Button pauseButton;

    @FXML private Button stopButton;
    
    @FXML private Button playButton;
    
    @FXML private Button chooseFileButton;
    
    @FXML private Button rewindB;
    @FXML private Button twoButton;
    @FXML private Button oneFiveButton;
    @FXML Button switchVid;
    @FXML Slider volumeSlider;
    @FXML Slider songProgress;
    @FXML Button nextButton;
    @FXML Label nowPlaying;
    @FXML Button retroButton;
    
    
    @FXML void play(ActionEvent event) {
    	if ( retroEQ ) {
    		mp.getAudioEqualizer().getBands().add(new EqualizerBand(15000, 5000, -24));
    		mp.getAudioEqualizer().getBands().add(new EqualizerBand(20000, 5000, -24));
    	}
    	System.out.println("play");
    	mp.play();
    	playMedia();
    }

    @FXML void pause(ActionEvent event) {
    	System.out.println("pause");
    	mp.pause();
    }

    @FXML void stop(ActionEvent event) {
    	System.out.println("stop");
    	mp.stop();
    	rotate.stop();
		rotateR.stop();
    }

    @FXML void restart(ActionEvent event) {
    	System.out.println("restart");
    	mp.stop();
    	mp.play();
    	playMedia();
    }
    
    @FXML void loadAllDirectories() throws FileNotFoundException {
    	Scanner f = new Scanner(new File("./src/application/files/directoryList.txt"));
    	f.nextLine();					// Skips first line
    	while ( f.hasNextLine() ) {
    		String a = f.nextLine();
    		loadDirectory(a);
    	}
    	f.close();
    }
    
    @FXML private void loadDirectory(String a) {				// loads directory into the array
    	File directory = new File(a);
    	File[] fileArray = directory.listFiles();
    	
    	for ( File fa : fileArray ) {
    		if ( fa.getName().lastIndexOf(".") > -1 ) {
	    		String fileType = fa.getName().substring(fa.getName().lastIndexOf(".")+1);
	    		switch( fileType ) {
	    		case "wav":
	    		case "flac":
	    		case "mp3":
	    		case "ogg":
	    			list.getItems().add(new Song(fa.getName(), -1, fa));
	    			break;
	    		default:
	    			break;
	    		}
    		}
    	}
	}
    
    public boolean fileContains( File a, String b  ) throws FileNotFoundException {
    	Scanner f = new Scanner(a);
    	while ( f.hasNextLine() ) {
    		String c = f.nextLine();
    		if ( f.nextLine().equals(b) ) {
    			f.close();
    			return true;
    		}
    		
    	}
    	f.close();
    	return false;
    }

	@FXML void openFile(ActionEvent event) throws IOException {
    	
    	DirectoryChooser fc = new DirectoryChooser();
    	File directory = fc.showDialog(null);
    	
    	if(list.getItems().size()==0) {
    		loadDirectory(directory.getPath());
    	}
    	else {
    		//mp.stop();
    		//list.getItems().clear();
    		loadDirectory(directory.getPath());
    	}
    	
    	if ( !fileContains(new File("./src/application/files/directoryList.txt"), directory.getPath())) {
    		BufferedWriter writer = new BufferedWriter(new FileWriter("./src/application/files/directoryList.txt", true));
        	System.out.println("\n"+directory.getPath());
        	writer.append("\n"+directory.getPath());
        	writer.close();
        	
        	loadDirectory(directory.getPath());
    	}
    	else {
    		System.out.println("Directory already in use!");
    	}
    	
    }
    
    @FXML void playSong(MouseEvent event) {
    	isPlaying();
    	if ( event.getClickCount() == 1 ) {
    		//mp.stop();
    		File selected = list.getSelectionModel().getSelectedItem().getFile();
    		mp = new MediaPlayer( new Media(selected.toURI().toString() ) );
    	}
    	if ( event.getClickCount() == 2 ) {
    		File selected = list.getSelectionModel().getSelectedItem().getFile();
    		mp = new MediaPlayer( new Media(selected.toURI().toString() ) );
    		mp.play();
    		playMedia();
    		timerProgress();
    	}
    }
    
    @FXML void openLoop(ActionEvent event) {
    	loopStart = mp.getCurrentTime();
    }
    
    @FXML void closeLoop(ActionEvent event) {
    	loopEnd = mp.getCurrentTime();
    	loopClosed = true;
    	
    	LoopTask tsk = new LoopTask();
    	Thread t1 = new Thread(tsk);
    	t1.start();
    	// CHECK TO MAKE SURE NO MORE THAN 1 THREAD IS RUNNING FOR THIS!!!!
    	// ADD A BOOLEAN TO CHECK THAT THE OPEN WAS MADE
    }
    
    @FXML void stopLooping(ActionEvent event) {
    	loopClosed = false;
    }
    
    @FXML void twoSpeed(ActionEvent event) {
    	if ( mp.getRate() == 2 ) {				// Sets the speed back to 1.0 if they click twice ( kind of like an analog button )
    		mp.setRate(1);
        	rotate.stop();
        	rotateR.stop();
        	rotate.setDuration(Duration.millis(4100));
        	rotateR.setDuration(Duration.millis(4100));
        	rotate.play();
    		rotateR.play();
    	}
    	else {
    		mp.setRate(2);
        	rotate.stop();
        	rotateR.stop();
        	rotate.setDuration(Duration.millis(2050));
        	rotateR.setDuration(Duration.millis(2050));
        	rotate.play();
    		rotateR.play();
    	}
    }
    
    @FXML void halfSpeed(ActionEvent event) {
    	if ( mp.getRate() == 0.5 ) {			// Sets the speed back to 1.0 if they click twice ( kind of like an analog button )
    		mp.setRate(1);
    		rotate.stop();
	    	rotateR.stop();
	    	rotate.setDuration(Duration.millis(4100));
	    	rotateR.setDuration(Duration.millis(4100));
	    	rotate.play();
			rotateR.play();
    	}
    	else {
    		mp.setRate(0.5);
	    	rotate.stop();
	    	rotateR.stop();
	    	rotate.setDuration(Duration.millis(8200));
	    	rotateR.setDuration(Duration.millis(8200));
	    	rotate.play();
			rotateR.play();
    	}
    	
    }
    
    @FXML void retroEQ(ActionEvent event) {
    	if ( !retroEQ ) {
    		retroEQ = true;
    		mp.getAudioEqualizer().getBands().add(new EqualizerBand(15000, 5000, -24));
    		mp.getAudioEqualizer().getBands().add(new EqualizerBand(20000, 5000, -24));
        	mp.getAudioEqualizer().setEnabled(true);
    	}
    	else {
    		mp.getAudioEqualizer().setEnabled(false);
    	}
    }
    
    @FXML void isPlaying() {
    	if ( true ) {				// CHANGE TO PLAYING
    		rotate.play();
    		rotateR.play();
    	}
    }

	@Override public void initialize(URL arg0, ResourceBundle arg1) {
		
		volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				mp.setVolume(volumeSlider.getValue() * .01); 
			}
			
		});
		
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), actionEvent -> { 
    		if(mp.getCurrentTime().toSeconds()>1.0)
    		{
    			mp.seek(Duration.seconds(mp.getCurrentTime().toSeconds()-10.0));
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
		rotate = new RotateTransition();
		rotate.setNode(leftTape);
		rotate.setDuration(Duration.millis(4100));
		rotate.setCycleCount(TranslateTransition.INDEFINITE);
		rotate.setInterpolator(Interpolator.LINEAR);
		rotate.setByAngle(-360);
		rotateR = new RotateTransition();
		rotateR.setNode(rightTape);
		rotateR.setDuration(Duration.millis(4100));
		rotateR.setCycleCount(TranslateTransition.INDEFINITE);
		rotateR.setInterpolator(Interpolator.LINEAR);
		rotateR.setByAngle(-360);
		
		try {
			loadAllDirectories();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
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
	
	@FXML
    void previous(ActionEvent event) throws MalformedURLException
    {
    	if(0<list.getSelectionModel().getSelectedIndex())
    	{
    		mp.stop();
    		list.getSelectionModel().select(list.getSelectionModel().getSelectedIndex()-1);;
    		File selected = list.getSelectionModel().getSelectedItem().getFile();
    		mp = new MediaPlayer( new Media(selected.toURI().toString() ) );
    		mp.play();
    		playMedia();
    	}
    	else
    	{
    		mp.stop();
    		list.getSelectionModel().selectLast();
    		File selected = list.getSelectionModel().getSelectedItem().getFile();
    		mp = new MediaPlayer( new Media(selected.toURI().toString() ) );
    		mp.play();
    		playMedia();
    	}
    }
    
    @FXML
    void next(ActionEvent event) throws MalformedURLException
    {
    	if(list.getSelectionModel().getSelectedIndex()+1<list.getItems().size())
    	{
    		mp.stop();
    		list.getSelectionModel().select(list.getSelectionModel().getSelectedIndex()+1);;
    		File selected = list.getSelectionModel().getSelectedItem().getFile();
    		mp = new MediaPlayer( new Media(selected.toURI().toString() ) );
    		mp.play();
    		playMedia();
    	}
    	else
    	{
    		mp.stop();
    		list.getSelectionModel().selectFirst();
    		File selected = list.getSelectionModel().getSelectedItem().getFile();
    		mp = new MediaPlayer( new Media(selected.toURI().toString() ) );
    		mp.play();
    		playMedia();
    	}
    }
   
    @FXML
    void switchToVid(ActionEvent event) throws IOException {
    	URL url = new File("src/application/view/VideoPlayerView.fxml").toURI().toURL();
    	mainpane = FXMLLoader.load(url);
        Scene scene = new Scene(mainpane);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
   
    void timerProgress() {
		
		// initSliderColor();
		
		mp.setOnReady(()->{
			
			songProgress.setMin(0);
			songProgress.setValue(0);
			songProgress.setMax(mp.getTotalDuration().toSeconds());
			
			mp.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
				
				//int sPTime = (int) newValue.toSeconds();
				double sPTime = newValue.toSeconds();
				songProgress.setValue(sPTime);
				System.out.println(sPTime);
				
			});
			
			songProgress.setOnMousePressed((MouseEvent event) -> {
				mp.seek(Duration.seconds(songProgress.getValue()));
			});
			
		});
		
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

