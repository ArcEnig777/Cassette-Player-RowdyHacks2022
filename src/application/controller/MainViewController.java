package application.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import application.model.Song;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainViewController {
	
	static MediaPlayer mp;
	static Duration loopStart;
	static Duration loopEnd;
	static boolean loopClosed = false;

	@FXML
    private Button previousButton;

    @FXML
    private Button retroButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Button oneFiveButton;

    @FXML
    private Button chooseFileButton;

    @FXML
    private ListView<Song> list;

    @FXML
    private Label nowPlaying;

    @FXML
    private Button playButton;

    @FXML
    private Button switchVid;

    @FXML
    private Button nextButton;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Button twoButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button rewindB;

    @FXML
    private Button loopButton;

    @FXML
    private Slider songProgress;
    
    @FXML
    private AnchorPane mainPane;

    
    @FXML
    void play(ActionEvent event) {
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
	    		System.out.println(fileType);
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
    	Media mSong = new Media(fileArray[0].toURI().toString());
    	mp = new MediaPlayer(mSong);
    	
    }
    
    @FXML
    void playSong(MouseEvent event) {
    	if ( event.getClickCount() == 1 ) {
    		mp.stop();
    		File selected = list.getSelectionModel().getSelectedItem().getFile();
    		mp = new MediaPlayer( new Media(selected.toURI().toString() ) );
    	}
    	if ( event.getClickCount() == 2 ) {
    		mp.stop();
    		File selected = list.getSelectionModel().getSelectedItem().getFile();
    		mp = new MediaPlayer( new Media(selected.toURI().toString() ) );
    		nowPlaying.setText("Now playing " + selected.getName());
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
    }
    
    @FXML
    void stopLooping(ActionEvent event) {
    	loopClosed = false;
    }
    
    @FXML
    void switchToVid(ActionEvent event) throws IOException {
    	URL url = new File("src/application/view/VideoPlayerView.fxml").toURI().toURL();
    	mainPane = FXMLLoader.load(url);
        Scene scene = new Scene(mainPane);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    @FXML
    void retro(ActionEvent event) {

    }

    @FXML
    void previous(ActionEvent event) {

    }
    
    @FXML
    void rewind(ActionEvent event) {

    }

    @FXML
    void next(ActionEvent event) {

    }

    @FXML
    void oneFiveTimes(ActionEvent event) {

    }

    @FXML
    void twoTimes(ActionEvent event) {

    }

}

