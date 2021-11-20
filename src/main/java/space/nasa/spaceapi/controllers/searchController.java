package space.nasa.spaceapi.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import space.nasa.spaceapi.models.APOD;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class searchController implements Initializable{
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
	}
	
	@FXML
	void random(ActionEvent event){
	}
	
	@FXML
	void search(ActionEvent event){
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		count.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1));
		start.setShowWeekNumbers(true);
		end.setShowWeekNumbers(true);
		date.setShowWeekNumbers(true);
		addDateChecker(start);
		addDateChecker(date);
		addDateChecker(end);
		
	}
	
	public void addDateChecker(DatePicker datepicker){
		datepicker.dayCellFactoryProperty().setValue(datePicker -> {
			DateCell dateCell = new DateCell();
			dateCell.setItem(datePicker.getValue());
			if(datePicker.getValue().isBefore(LocalDate.parse("1995-06-16")))
				dateCell.setItem(LocalDate.parse("1996-06-16"));
			else if(datePicker.getValue().isAfter(LocalDate.now()))
				dateCell.setItem(LocalDate.now());
			return dateCell;
		});
	}
}
