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

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class apodController implements Initializable{
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
	private APOD apod;
	
	@FXML
	void back(ActionEvent event){
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
	void prev(ActionEvent event){
		apod = API.getAPOD(apod.getDate().minusDays(1));
		updateAPOD();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		apod = API.getAPOD();
		updateAPOD();
	}
	
	private void updateAPOD(){
		explanation.setText(apod.getExplanation());
		title.setText(apod.getTitle());
		image.setImage(apod.getImage());
		date.setText(apod.getDateString());
		copyright.setText(apod.getCopyright());
	}
}
