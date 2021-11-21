package space.nasa.spaceapi.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import space.nasa.spaceapi.models.APOD;
import space.nasa.spaceapi.utilities.API;
import space.nasa.spaceapi.utilities.Transition;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class searchController implements Initializable{
	public static final LocalDate minDate = LocalDate.parse("1995-06-16");
	@FXML
	private Label explanation;
	@FXML
	private Label title;
	@FXML
	private DatePicker date;
	@FXML
	private Spinner<Integer> count;
	@FXML
	private DatePicker start;
	@FXML
	private DatePicker end;
	@FXML
	private ListView<APOD> apods;
	
	@FXML
	void back(ActionEvent event){
	}
	
	@FXML
	void rSearch(ActionEvent event){
		apods.getItems().addAll((API.getAPODs(start.getValue(), end.getValue())));
	}
	
	@FXML
	void random(ActionEvent event){
//		apods.getItems().clear();
//		if(count.getValue() > 1)
//			apods.getItems().addAll(API.getAPODs());
//		else apods.getItems().addAll(API.getAPOD());
	}
	
	@FXML
	void search(ActionEvent event) throws IOException{
		Transition.to(event, "/views/apod-view.fxml", "");
		apodController.setApod(API.getAPOD(date.getValue()));
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		count.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1));
		addDateChecker(start);
		addDateChecker(date);
		addDateChecker(end);
		start.setValue(LocalDate.now().minusDays(7));
		end.setValue(LocalDate.now());
		date.setValue(LocalDate.ofEpochDay((long) (Math.random() *(LocalDate.now().toEpochDay()-minDate.toEpochDay()  ) + minDate.toEpochDay())));
	}
	
	public void addDateChecker(DatePicker datepicker){
		//https://stackoverflow.com/a/53186959/16929246
		datepicker.setDayCellFactory(param -> new DateCell(){
			@Override
			public void updateItem(LocalDate item, boolean empty){
				super.updateItem(item, empty);
				setDisable(item.isAfter(LocalDate.now()) || item.isBefore(minDate));
			}
		});
	}
}
