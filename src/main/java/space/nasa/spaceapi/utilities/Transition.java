package space.nasa.spaceapi.utilities;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import space.nasa.spaceapi.Main;

import java.io.IOException;

public class Transition{
	public static void to(Event event, String fxmlFile, String title) throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/" + fxmlFile));
		Scene scene = new Scene(fxmlLoader.load());
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setTitle(title);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void close(ActionEvent event){
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
		
	}
}
