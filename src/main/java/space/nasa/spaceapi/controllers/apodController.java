package space.nasa.spaceapi.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import space.nasa.spaceapi.models.APOD;
import space.nasa.spaceapi.utilities.API;
import space.nasa.spaceapi.utilities.Transition;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class apodController implements InitializableAPOD, Initializable{
	//credits to EliteArena on Pinterest: https://www.pinterest.ca/Elite_Arena/
	//link to pin = https://www.pinterest.ca/pin/750834569105233692/
	private static final String notFound404 = "https://i.pinimg.com/originals/13/3d/62/133d62f4c7611596b265b81bfb9be08c.gif";
	@FXML
	private WebView video;
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
	 * Calls the API to fetch the previous day's A.P.O.D.
	 * This will not fetch dates before {@link APOD#minDate} As the API has no records and will return an error
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
	 * Calls the API to fetch next day's A.P.O.D.
	 * It will not fetch future dates as the API will return an error
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
	
	@FXML
	void search(ActionEvent event) throws IOException{
		Transition.to(event, "search-view.fxml", "Search NASA's Astronomy Picture of the Day API");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		Transition.addStyle(date);
	}
	
	private void updateAPOD(){
		video.setVisible(false);
		video.toBack();
		video.getEngine().load("");
		image.setVisible(true);
		if(apod.getUrl() == null)
			image.setImage(new Image(notFound404));
		else if(this.apod.getMediaType().equals("video"))
		{
			video.getEngine().load(apod.getUrl().toString());
			image.setVisible(false);
			video.toFront();
			image.toBack();
			video.setVisible(true);
		}
		else
			image.setImage(apod.getImage());
		explanation.setText(apod.getExplanation());
		title.setText(apod.getTitle());
		date.setText(apod.getDateString());
		copyright.setText(apod.getCopyright());
	}
	
	@Override
	public void initializeAPOD(APOD apod){
		this.apod = apod == null ? API.getAPOD() : apod;
		updateAPOD();
	}
	
	public void openImage(MouseEvent mouseEvent){
	}
}
