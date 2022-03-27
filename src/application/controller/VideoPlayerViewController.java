package application.controller;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import application.model.Video;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class VideoPlayerViewController {

	static MediaPlayer mp;
	
    @FXML
    private Button pauseButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button playButton;

    @FXML
    private Button chooseDirButton;
    
    @FXML
    private MediaView mv;
    
    @FXML
    private ListView<Video> list;
    
    @FXML
    private Button switchAudio;
    
	@FXML
	private AnchorPane mainPane2;

    @FXML
    void playVid(ActionEvent event) {
    	System.out.println("play");
    	mp.play();
    }

    @FXML
    void pauseVid(ActionEvent event) {
    	System.out.println("pause");
    	mp.pause();
    }

    @FXML
    void stopVid(ActionEvent event) {
    	System.out.println("stop");
    	mp.stop();
    }

    @FXML
    void chooseDir(ActionEvent event) throws URISyntaxException{
    	DirectoryChooser fc = new DirectoryChooser();
    	File directory = fc.showDialog(null);
    	File[] fileArray = directory.listFiles();
    	
    	for ( File fa : fileArray ) {
    		if ( fa.getName().lastIndexOf(".") > -1 ) {
	    		String fileType = fa.getName().substring(fa.getName().lastIndexOf(".")+1);
	    		System.out.println(fileType);
	    		switch( fileType ) {
	    		case "mov":
	    		case "wmv":
	    		case "mp4":
	    		case "avi":
	    			list.getItems().add(new Video(fa.getName(), -1, fa));
	    			break;
	    		default:
	    			break;
	    		}
    		}
    	}
    	Media mVid = new Media(fileArray[0].toURI().toString());
    	mp = new MediaPlayer(mVid);
    }
    
    @FXML
    void playVideo(MouseEvent event) {
    	if ( event.getClickCount() == 1 ) {
    		//mp.stop();
    		File selected = list.getSelectionModel().getSelectedItem().getFile();
    		mp = new MediaPlayer( new Media(selected.toURI().toString() ) );
    	}
    	if ( event.getClickCount() == 2 ) {
    		//mp.stop();
    		File selected = list.getSelectionModel().getSelectedItem().getFile();
    		mp = new MediaPlayer( new Media(selected.toURI().toString() ) );
    		mp.play();
    		mv.setMediaPlayer(mp);
    	}
    }
    
    @FXML
    void switchToAudio(ActionEvent event) throws IOException {
    	URL url = new File("src/application/view/MainView.fxml").toURI().toURL();
    	mainPane2 = FXMLLoader.load(url);
        Scene scene = new Scene(mainPane2);
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

}