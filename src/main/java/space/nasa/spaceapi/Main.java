package space.nasa.spaceapi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application{
	public static void main(String[] args){
		launch();
	}
	
	@Override
	public void start(Stage stage) throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/search-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load());
		stage.setTitle("⭐Explore NASA's API ⭐ Astronomy Picture Of The Day⭐");
		stage.setScene(scene);
		stage.show();
		stage.centerOnScreen();
	}
}