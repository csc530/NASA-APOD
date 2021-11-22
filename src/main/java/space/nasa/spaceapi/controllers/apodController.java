package space.nasa.spaceapi.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import space.nasa.spaceapi.models.APOD;
import space.nasa.spaceapi.utilities.API;
import space.nasa.spaceapi.utilities.Transition;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class apodController implements Initializable{
	private static APOD apod;
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
	
	public static void setApod(APOD apod){
		apodController.apod = apod;
	}
	
	@FXML
	void prev(ActionEvent event){
		if(apod.getDate().isEqual(searchController.minDate))
			new Alert(Alert.AlertType.INFORMATION, "there are no images for NASA's astronomy picture of the day " +
					"before " + searchController.minDate + ".", ButtonType.OK).show();
		else
		{
			apod = API.getAPOD(apod.getDate().minusDays(1));
			updateAPOD();
		}
	}
	
	@FXML
	void next(ActionEvent event){
		if(apod.getDate().isEqual(LocalDate.now()))
			new Alert(Alert.AlertType.INFORMATION, "there are no images for NASA's future astronomy picture of the day", ButtonType.OK).show();
		else
		{
			apod = API.getAPOD(apod.getDate().plusDays(1));
			updateAPOD();
		}
	}
	
	@FXML
	void home(ActionEvent event) throws IOException{
		Transition.to(event,"home.fxml","NASA's APIs");
	}
	
	@FXML
	void search(ActionEvent event) throws IOException{
		Transition.to(event,"search-view.fxml","Search NASA's Astronomy Picture of the Day API");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		if(apod == null)
			apod = API.getAPOD();
		updateAPOD();
		Transition.addStyle(date);
	}
	
	private void updateAPOD(){
		explanation.setText(apod.getExplanation());
		title.setText(apod.getTitle());
		image.setImage(apod.getImage());
		date.setText(apod.getDateString());
		copyright.setText(apod.getCopyright());
	}
}
