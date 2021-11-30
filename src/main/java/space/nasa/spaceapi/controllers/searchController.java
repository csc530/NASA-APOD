package space.nasa.spaceapi.controllers;

import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.dlsc.formsfx.model.validators.RegexValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import space.nasa.spaceapi.models.APOD;
import space.nasa.spaceapi.utilities.API;
import space.nasa.spaceapi.utilities.Transition;

import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeSet;

public class searchController implements Initializable{
	@FXML
	private ProgressBar progress;
	@FXML
	private Button btnSearch;
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
	private VBox controls;
	@FXML
	private Label dateSort;
	@FXML
	private Label titleSort;
	
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
		long startDate = APOD.minDate.toEpochDay();
		return LocalDate.ofEpochDay((long) (Math.random() * (today - startDate - daysFromPresent) + (daysFromBeginning + startDate)));
	}
	
	@FXML
	void rangeSearch(ActionEvent event) throws IllegalStateException{
		apods.getItems().clear();
		progress.setVisible(true);
		controls.setDisable(true);
		//Thread query
		new Thread(() -> {
			if(start.getValue().isBefore(end.getValue()) || start.getValue().isEqual(end.getValue()))
			{
				TreeSet<APOD> results = API.getAPODs(start.getValue(), end.getValue());
				API.setProgress(100);
				controls.setDisable(false);
				if(results != null)
					apods.getItems().addAll(results);
				else
					new Alert(Alert.AlertType.ERROR, "Sorry something went wrong, please try again.").show();
			}
			else
				new Alert(Alert.AlertType.WARNING, "Start date must be before the end date", ButtonType.OK).show();
			progress.setVisible(false);
		}).start();
		//Thread loading
		new Thread(() -> {
			progress.setDisable(false);
			progress.getStyleClass().remove("progress-bar-success");
			float i = 0;
			while(API.getProgress() < 1)
			{
				progress.progressProperty().setValue(API.getProgress() + i);
				i += .000000008;
				System.out.println(i);
			}
			progress.getStyleClass().add("progress-bar-success");
		}, "Progress Bar").start();
		apods.requestFocus();
	}
	
	@FXML
	void sortList(MouseEvent event){
		if(apods.getItems().isEmpty())
			return;
		String txt = switchLabels(event, event.getSource() == titleSort ? dateSort : titleSort);
		Comparator<APOD> comparator;
		if(event.getSource() == titleSort) comparator = (a, c) -> {
			if(txt.charAt(0) == '⇧')
				return a.getTitle().compareTo(c.getTitle());
			else if(txt.charAt(0) == '⇩')
				return c.getTitle().compareTo(a.getTitle());
			return 0;
		};
		else comparator = (a, c) -> {
			if(txt.charAt(0) == '⇧')
				return a.getDate().compareTo(c.getDate());
			else if(txt.charAt(0) == '⇩')
				return c.getDate().compareTo(a.getDate());
			return 0;
		};
		List<APOD> pods = apods.getItems().stream().sorted(comparator).toList();
		apods.getItems().clear();
		apods.getItems().addAll(pods);
	}
	
	private String switchLabels(MouseEvent event, Label unClickedLabel){
		unClickedLabel.setText("-" + unClickedLabel.getText().substring(1));
		final Label target = (Label) event.getSource();
		String txt = target.getText();
		txt = switch(txt.charAt(0))
				{
					case '-' -> "⇧" + txt.substring(1);
					case '⇧' -> "⇩" + txt.substring(1);
					default -> "-" + txt.substring(1);
				};
		target.setText(txt);
		return txt;
	}
	
	@FXML
	void random(ActionEvent event) throws IllegalStateException{
		final Integer countValue = count.getValue();
		apods.getItems().clear();
		new Thread(() -> {
			API.setProgress(0);
			LocalDate rDate = getRandomDateInAPOD(0, 0);
			if(countValue < 2)
				apods.getItems().add(API.getAPOD(rDate));
			else
			{
				while(apods.getItems().size() != countValue)
				{
					apods.getItems().add(0, API.getAPOD(getRandomDateInAPOD(0, 0)));
					API.setProgress((float) apods.getItems().size() / countValue);
				}
			}
			API.setProgress(100);
		}, "query API").start();
		new Thread(() -> {
			progress.setVisible(true);
			progress.setDisable(false);
			controls.setDisable(true);
			progress.getStyleClass().remove("progress-bar-success");
			while(API.getProgress() < 1)
				progress.progressProperty().setValue(API.getProgress());
			controls.setDisable(false);
			progress.getStyleClass().add("progress-bar-success");
			progress.setVisible(false);
		}, "Loading").start();
	}
	
	@FXML
	void search(ActionEvent event){
		final APOD apod = API.getAPOD(date.getValue());
		Transition.to(event, "apod-view.fxml", apod.getTitle() + " - " + apod.getDate(), apod);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle){
		progress.setVisible(false);
		int amount = (int) (Math.random() * 7);
		LocalDate rDate = getRandomDateInAPOD(0, amount);
		count.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000,
		                                                                         (int) (Math.random() * 225 + 1)));
		//replace all non numeric from editor
		count.editableProperty().setValue(true);
		count.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
			if(!RegexValidator.forPattern("[^0-9]", "Eyo").validate(newValue).getResult())
				count.getEditor().setText(newValue.replaceAll("[^0-9]", ""));
			else if(!IntegerRangeValidator.between(1, 10000, "Ayo").validate(Integer.valueOf(newValue)).getResult())
				count.getValueFactory().setValue(Integer.parseInt(oldValue));
		});
		addDateChecker(start);
		addDateChecker(date);
		addDateChecker(end);
		start.setValue(rDate.minusDays(amount));
		end.setValue(rDate);
		date.setValue(LocalDate.now());
		Transition.addStyle(apods);
	}
	
	public void addDateChecker(DatePicker datepicker){
		//https://stackoverflow.com/a/53186959/16929246
		datepicker.setDayCellFactory(param -> new DateCell(){
			@Override
			public void updateItem(LocalDate item, boolean empty){
				super.updateItem(item, empty);
				setDisable(item.isAfter(LocalDate.now()) || item.isBefore(APOD.minDate));
			}
		});
	}
	
	public void selectAPOD(MouseEvent mouseEvent){
		//https://stackoverflow.com/a/10950824/16929246
		apods.setOnMouseClicked(mouse -> {
			if(mouse.getButton().equals(MouseButton.PRIMARY) && mouse.getClickCount() == 2 && apods.getSelectionModel().getSelectedItems().size() != 0)
			{
				date.setValue(apods.getSelectionModel().getSelectedItem().getDate());
				btnSearch.fire();
			}
		});
	}
	
	public void Exit(ActionEvent actionEvent){
		Transition.close(actionEvent);
	}
}
