package application.controller;

import java.io.File;
import java.net.URISyntaxException;

import application.model.Song;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;

public class MainViewController {
	
	static MediaPlayer mp;
	static Duration loopStart;
	static Duration loopEnd;
	static boolean loopClosed = false;

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
    	Media mSong = new Media(fileArray[1].toURI().toString());
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

}

