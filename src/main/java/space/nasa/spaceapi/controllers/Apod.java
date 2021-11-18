package space.nasa.spaceapi.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import space.nasa.spaceapi.models.APOD;
import space.nasa.spaceapi.utilities.API;

import java.net.URL;
import java.util.ResourceBundle;

public class Apod implements Initializable{
	
	@FXML
	private Label title;
	
	@FXML
	private ImageView image;
	
	@FXML
	private Label expl;
	
	@FXML
	void back(ActionEvent event) {
	
	}
	
	@FXML
	void next(ActionEvent event) {
	
	}
	
	@FXML
	void prev(ActionEvent event) {
	
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		APOD apod = API.getAPOD();
		expl.setText(apod.getExplanation());
		title.setText(apod.getTitle());
		image.setImage(new Image(String.valueOf(apod.getHdUrl())));
		
	}
}
