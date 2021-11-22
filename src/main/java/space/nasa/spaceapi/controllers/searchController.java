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
	
	/**
	 * Returns a random valid LocalDate which an astronmy picture of the day was posted
	 * @param daysFromPresent   the upper bound of date, the amount to subtract to allow for the most recent possible
	 *                          date to be returned. How many days ago should the upper bound be?
	 * @param daysFromBeginning the lower bound of date. How many days after the first posted astronomy picture of
	 *                          day should the lower bound be?
	 * @return a valid random LocalDate between given param bounds for NASA's astronomy picture of the day. Returns
	 * 		null if no possible could be returned with the given params
	 */
	public static LocalDate getRandomDateInAPOD(int daysFromPresent, int daysFromBeginning){
		long today = LocalDate.now().toEpochDay();
		long startDate = minDate.toEpochDay();
		return LocalDate.ofEpochDay((long) (Math.random() * (today - startDate - daysFromPresent) + (daysFromBeginning + startDate)));
	}
	
	@FXML
	void back(ActionEvent event){
	}
	
	@FXML
	void rSearch(ActionEvent event){
		apods.getItems().clear();
		apods.getItems().addAll((API.getAPODs(start.getValue(), end.getValue())));
	}
	
	@FXML
	void random(ActionEvent event){
		LocalDate rDate = getRandomDateInAPOD(0, 0);
		apods.getItems().clear();
		final Integer countValue = count.getValue();
		if(countValue < 2)
			apods.getItems().add(API.getAPOD(rDate));
		else
		{
			rDate = getRandomDateInAPOD(countValue, 0);
			//minus 1 to account that start date count as one as well as end date
			apods.getItems().addAll(API.getAPODs(rDate, rDate.plusDays(countValue-1)));
		}
	}
	
	@FXML
	void search(ActionEvent event) throws IOException{
		Transition.to(event, "apod-view.fxml", "");
		apodController.setApod(API.getAPOD(date.getValue()));
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		int amount = (int) (Math.random()*7);
		LocalDate rDate = getRandomDateInAPOD(0, amount);
		count.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, 1));
		addDateChecker(start);
		addDateChecker(date);
		addDateChecker(end);
		start.setValue(rDate.minusDays(amount));
		end.setValue(rDate);
		date.setValue(getRandomDateInAPOD(0, 0));
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
