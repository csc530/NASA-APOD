package space.nasa.spaceapi.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebView;
import space.nasa.spaceapi.models.APOD;
import space.nasa.spaceapi.utilities.API;
import space.nasa.spaceapi.utilities.Transition;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class apodController implements InitializableAPOD, Initializable{
	/**
	 * link to a 404 resource not found image<br/> credits to <a href=" https://www.pinterest.ca/Elite_Arena/">EliteArena on Pinterest</a><br/>
	 * <a href="https://www.pinterest.ca/pin/750834569105233692/">to pin</a>
	 **/
	private static final String notFound404 = "https://i.pinimg.com/originals/13/3d/62/133d62f4c7611596b265b81bfb9be08c.gif";
	@FXML
	private WebView webView;
	private APOD apod;
	@FXML
	private Label title;
	@FXML
	private Label date;
	@FXML
	private Label copyright;
	@FXML
	private ImageView image;
	@FXML
	private Label explanation;
	
	/**
	 * Calls the API to fetch the previous day's A.P.O.D. This will not fetch dates before {@link APOD#minDate} As the API has no records and will return
	 * an error
	 */
	@FXML
	void prev(){
		if(apod.getDate().isEqual(APOD.minDate))
			new Alert(Alert.AlertType.INFORMATION, "there are no images for NASA's astronomy picture of the day " +
			                                       "before " + APOD.minDate + ".", ButtonType.OK).show();
		else
		{
			apod = API.getAPOD(apod.getDate().minusDays(1));
			updateAPOD();
		}
	}
	
	/**
	 * Updates the view to reflect the current {@link APOD}'s information in the controller's
	 */
	private void updateAPOD(){
		webView.setVisible(false);
		webView.toBack();
		/* clear webview of contents */
		webView.getEngine().load("");
		image.setVisible(true);
		/* add image, video, or 404 image depending on APOD */
		if(apod.getUrl() == null)
			image.setImage(new Image(notFound404));
		else if(this.apod.getMediaType().equals("video"))
		{
			webView.getEngine().load(apod.getUrl().toString());
			image.setVisible(false);
			webView.toFront();
			image.toBack();
			webView.setVisible(true);
		}
		else
			image.setImage(apod.getImage());
		explanation.setText(apod.getExplanation());
		title.setText(apod.getTitle());
		date.setText(apod.getDateString());
		copyright.setText(apod.getCopyright());
	}
	
	/**
	 * Calls the API to fetch next day's A.P.O.D. It will not fetch future dates as the API will return an error
	 */
	@FXML
	void next(){
		if(apod.getDate().isEqual(LocalDate.now()))
			new Alert(Alert.AlertType.INFORMATION, "there are no images for NASA's future astronomy picture of the day", ButtonType.OK).show();
		else
		{
			apod = API.getAPOD(apod.getDate().plusDays(1));
			updateAPOD();
		}
	}
	
	/**
	 * Changes scenes to search A.P.O.D.s
	 *
	 * @param event used to get the stage and switch scenes
	 */
	@FXML
	void search(ActionEvent event){
		Transition.to(event, "search-view.fxml", "Search NASA's Astronomy Picture of the Day API");
	}
	
	/**
	 * Adds styles to scene
	 */
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		Transition.addStyle(date);
	}
	
	@Override
	public void initializeAPOD(APOD apod){
		//if apod is null get current dates APOD from API
		this.apod = apod == null ? API.getAPOD() : apod;
		updateAPOD();
	}
}
