package space.nasa.spaceapi.utilities;

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

/**
 * To change scenes on the same stage
 * or change and effect the scene or stage
 */
public class Transition{
	/**
	 * Will change the scene within the same stage
	 * if there is an error loading the scene the application will reload the current scene as is
	 * @param event    any even from the stage you wish to change its scene, used to get the {@link Stage}
	 * @param fxmlFile the fxml file to load as the new {@link Scene}
	 * @param title    the title for the stage for the new scene if empty the previous title will stay
	 * @return the {@link FXMLLoader} used to load the new {@link Scene}
	 */
	public static FXMLLoader to(Event event, String fxmlFile, String title){
		FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("views/" + fxmlFile));
		Scene scene;
		try
		{
			scene = new Scene(fxmlLoader.load());
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setTitle(title);
			stage.setScene(scene);
			stage.show();
		}
		catch(IOException e)
		{
			scene = ((Node) event.getSource()).getScene();
			Scene copy = scene;
			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setTitle(title);
			stage.setScene(copy);
			stage.show();
		}
		return fxmlLoader;
	}
	
	/**
	 * <p>
	 * This is used to change scenes with a controller that {@code implements} {@link InitializableAPOD}.
	 * Upon change scenes the {@link InitializableAPOD#initializeAPOD(APOD)} method will be called to set up the
	 * given {@link APOD} immediately
	 * </p>
	 * Will change the scene within the same stage
	 * if there is an error loading the scene the application will reload the current scene as is
	 * @param event    any even from the stage you wish to change its scene, used to get the {@link Stage}
	 * @param fxmlFile the fxml file to load as the new {@link Scene}
	 * @param title    the title for the stage for the new scene if empty the previous title will stay
	 * @param apod     the desired APOD to send to the view's controller
	 */
	public static void to(Event event, String fxmlFile, String title, APOD apod){
		InitializableAPOD controller = to(event, fxmlFile, title).getController();
		if(controller != null)
			controller.initializeAPOD(apod);
	}
	
	/**
	 * This will close the current running application stage and JavaFx {@link Thread}
	 * @param event an event from the stage to close
	 */
	public static void close(Event event){
		((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
	}
	
	/**
	 * this will add the Bootstrap style effect to a given scene from {@link BootstrapFX}
	 * @param node A {@link Node} from the scene to add the BootstrapFX stylesheet
	 */
	public static void addStyle(Node node){
		//gets the anchor-pane/root node from any given node in the scene
		while(node.getClass() != AnchorPane.class) node = node.getParent();
		AnchorPane root = (AnchorPane) node;
		//adds maven dependency bootstrap stylesheet to anchor-pane
		root.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
		root.getStylesheets().add(Main.class.getResource("styles/style.css").toString());
	}
}
