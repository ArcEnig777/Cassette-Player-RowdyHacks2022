package application;
	
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			URL url = new File("src/application/view/MainView.fxml").toURI().toURL();
			AnchorPane root = (AnchorPane)FXMLLoader.load(url);
			Scene scene = new Scene(root,800,800);
			Image icon = new Image(new FileInputStream("./src/application/files/logo.png"));
			primaryStage.getIcons().add(icon);
			primaryStage.setTitle("Cassette Tape Media Player");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
