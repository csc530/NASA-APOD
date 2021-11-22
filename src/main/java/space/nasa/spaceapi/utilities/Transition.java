package space.nasa.spaceapi.utilities;

import javafx.css.Style;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import space.nasa.spaceapi.Main;

import java.io.File;
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
	
	public static void addStyle(Node node){
		//gets the anchorpane/root node from any given node in the scene
		while(node.getClass() != AnchorPane.class)
			node = node.getParent();
		AnchorPane root = (AnchorPane) node;
		//adds maven dependcy bootstrap stlyesheet to anchorpane
		root.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
	}
}
