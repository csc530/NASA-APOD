package space.nasa.spaceapi.utilities;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;
import space.nasa.spaceapi.Main;
import space.nasa.spaceapi.controllers.InitializableAPOD;
import space.nasa.spaceapi.models.APOD;

import java.io.IOException;

public class Transition{
	public static FXMLLoader to(Event event, String fxmlFile, String title) throws IOException{
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/" + fxmlFile));
		Scene scene = new Scene(fxmlLoader.load());
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setTitle(title);
		stage.setScene(scene);
		stage.show();
		return fxmlLoader;
	}
	
	public static void to(Event event, String fxmlFile, String title, APOD apod){
		InitializableAPOD controller = null;
		try
		{
			controller = to(event, fxmlFile, title).getController();
		}
		catch(IOException ignored) {}
		if(controller != null)
		controller.initializeAPOD(apod);
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
