package application.controller;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import application.model.Song;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.EqualizerBand;
import javafx.scene.media.Media;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

public class MainViewController implements Initializable {
	
	static MediaPlayer mp;
	static Duration loopStart;
	static Duration loopEnd;
	static boolean loopClosed = false;
	boolean retroEQ = false;
	RotateTransition rotate;
	RotateTransition rotateR;

    @FXML
    private ImageView leftTape;

    @FXML
    private ImageView rightTape;
	
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
    private Button chooseFileButton;
    
    @FXML
    void play(ActionEvent event) {
    	if ( retroEQ ) {
    		mp.getAudioEqualizer().getBands().add(new EqualizerBand(15000, 5000, -24));
    		mp.getAudioEqualizer().getBands().add(new EqualizerBand(20000, 5000, -24));
    	}
    	System.out.println("play");
    	mp.play();
    }

    @FXML
    void pause(ActionEvent event) {
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
    	mp.play();
    }
    
    @FXML
    void openFile(ActionEvent event) throws URISyntaxException {
    	
    	DirectoryChooser fc = new DirectoryChooser();
    	File directory = fc.showDialog(null);
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
    	Media mSong = new Media(fileArray[1].toURI().toString());
    	mp = new MediaPlayer(mSong);
    	
    }
    
    @FXML
    void playSong(MouseEvent event) {
    	isPlaying();
    	if ( event.getClickCount() == 1 ) {
    		mp.stop();
    		File selected = list.getSelectionModel().getSelectedItem().getFile();
    		mp = new MediaPlayer( new Media(selected.toURI().toString() ) );
    	}
    	if ( event.getClickCount() == 2 ) {
    		mp.stop();
    		File selected = list.getSelectionModel().getSelectedItem().getFile();
    		mp = new MediaPlayer( new Media(selected.toURI().toString() ) );
    		mp.play();
    	}
    }
    
    @FXML
    void openLoop(ActionEvent event) {
    	loopStart = mp.getCurrentTime();
    }
    
    @FXML
    void closeLoop(ActionEvent event) {
    	loopEnd = mp.getCurrentTime();
    	loopClosed = true;
    	
    	LoopTask tsk = new LoopTask();
    	Thread t1 = new Thread(tsk);
    	t1.start();
    	// CHECK TO MAKE SURE NO MORE THAN 1 THREAD IS RUNNING FOR THIS!!!!
    	// ADD A BOOLEAN TO CHECK THAT THE OPEN WAS MADE
    }
    
    @FXML
    void stopLooping(ActionEvent event) {
    	loopClosed = false;
    }
    
    @FXML
    void twoSpeed(ActionEvent event) {
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
    
    @FXML
    void halfSpeed(ActionEvent event) {
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
    
    @FXML
    void retroEQ(ActionEvent event) {
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
    
    @FXML
    void isPlaying() {
    	if ( true ) {				// CHANGE TO PLAYING
    		rotate.play();
    		rotateR.play();
    	}
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
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
	}
    
   

}

