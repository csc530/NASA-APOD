package space.nasa.spaceapi.controllers;

import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.dlsc.formsfx.model.validators.RegexValidator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import java.util.*;

public class searchController implements Initializable{
	@FXML
	private ProgressBar progressBar;
	@FXML
	private DatePicker datePicker;
	@FXML
	private Spinner<Integer> randomSpinner;
	@FXML
	private DatePicker startDatePicker;
	@FXML
	private DatePicker endDatePicker;
	@FXML
	private ListView<APOD> apodsList;
	@FXML
	private VBox controls;
	@FXML
	private Label lblDateSort;
	@FXML
	private Label lblTitleSort;
	
	/**
	 * This will run to search a range of dates from the startDatePicker and endDatePicker {@link DatePicker}s It will create 2 new asynchronous
	 * {@linkplain Thread}s one to query the API and call for each {@link APOD} in the range the second Thread will run to update the progress bar of the
	 * API query The controls to search for APODs while calling the API to prevent a backlog of results and multiple Daemon Threads
	 *
	 * @throws IllegalStateException this is thrown as the APODs are populated outside the JFX Thread
	 */
	@FXML
	void
	rangeSearch() throws IllegalStateException{
		apodsList.getItems().clear();
		progressBar.setVisible(true);
		controls.setDisable(true);
		//API query
		new Thread(() -> {
			if(startDatePicker.getValue().isBefore(endDatePicker.getValue()) || startDatePicker.getValue().isEqual(endDatePicker.getValue()))
			{
				TreeSet<APOD> results = API.getAPODs(startDatePicker.getValue(), endDatePicker.getValue());
				API.setProgress(100);
				controls.setDisable(false);
				if(results != null) apodsList.getItems().addAll(results);
				else new Alert(Alert.AlertType.ERROR, "Sorry something went wrong, please try again.").show();
			}
			else new Alert(Alert.AlertType.WARNING, "Start date must be before the end date", ButtonType.OK).show();
			progressBar.setVisible(false);
		}).start();
		//Loading progress bar
		new Thread(() -> {
			progressBar.setDisable(false);
			float i = 0;
			while(API.getProgress() < 1)
			{
				progressBar.progressProperty().setValue(API.getProgress() + i);
				//used to have the progress bar continue to increase fluidly though it actually isn't
				i += .0000000008;
			}
		}, "Progress Bar").start();
		apodsList.requestFocus();
	}
	
	/**
	 * Populates the listview with a random amount of {@link APOD}s
	 *
	 * @throws IllegalStateException this is thrown as the APODs are populated outside the JFX Thread
	 */
	@FXML
	void random(ActionEvent event) throws IllegalStateException{
		//the amount of APODs to return
		final Integer countValue = randomSpinner.getValue();
		apodsList.getItems().clear();
		//Query API thread
		new Thread(() -> {
			API.setProgress(0);
			LocalDate rDate = getRandomDateInAPOD(0, 0);
			if(countValue >= 2)
			{
				int i = 0;
				HashMap<Integer, APOD> apodMap = new HashMap<>();
				//populate listview with random A.P.O.D.s until the desired amount is returned
				while(apodsList.getItems().size() != countValue)
				{
					apodMap.put(i++, API.getAPOD(rDate));
					apodsList.getItems().add(0, apodMap.get(i - 1));
					//update the API progress, to accurately reflect the progress to completion
					API.setProgress((float) apodsList.getItems().size() / countValue);
					rDate = getRandomDateInAPOD(0, 0);
				}
			}
			else //populates listview with one APOD
				apodsList.getItems().add(API.getAPOD(rDate));
			API.setProgress(100);
			//sorts list and gets rid of visible duplicates even though there aren't any
			//to see what I mean comment out the line below then run, then sort using the headers; the duplicates disappear
			Platform.runLater(() -> sortList(new Event(lblDateSort, null, null)));
		}, "query API").start();
		//Progress Bar loading Thread
		new Thread(() -> {
			//shows progress bar and disable API search controls
			progressBar.setVisible(true);
			controls.setDisable(true);
			//update the loading bar with the completion of the API call
			while(API.getProgress() < 1)
				progressBar.progressProperty().setValue(API.getProgress());
			controls.setDisable(false);
			progressBar.setVisible(false);
		}, "Loading").start();
	}
	
	/**
	 * Returns a random valid LocalDate which an astronomy picture of the day was posted
	 *
	 * @param daysFromPresent   the upper bound of date, the amount to subtract to allow for the most recent possible date to be returned. How many days
	 *                          ago should the upper bound be?
	 * @param daysFromBeginning the lower bound of date. How many days after the first posted astronomy picture of day should the lower bound be?
	 *
	 * @return a valid random LocalDate between given param bounds for NASA's astronomy picture of the day. Returns null if no possible could be returned
	 * 		with the given params
	 */
	public static LocalDate getRandomDateInAPOD(int daysFromPresent, int daysFromBeginning){
		long today = LocalDate.now().toEpochDay();
		long startDate = APOD.minDate.toEpochDay();
		return LocalDate.ofEpochDay((long) (Math.random() * (today - startDate - daysFromPresent) + (daysFromBeginning + startDate)));
	}
	
	/**
	 * This will sort the list of {@link APOD}s by the date or title depending on which label was clicked The sort will toggle between ascending,
	 * descending, and non-ordered
	 *
	 * @param event The {@link Event} called by the -Date and -Title label, used to determine which label was clicked
	 */
	@FXML
	void sortList(Event event){
		if(apodsList.getItems().isEmpty()) return;
		//get text of clicked label
		//passes in non-clicked label in ternary operator
		String txt = switchLabels(event, event.getSource() == lblTitleSort ? lblDateSort : lblTitleSort);
		Comparator<APOD> comparator;
		//set value of comparator to sort by date or title depending on clicked label
		if(event.getSource() == lblTitleSort) comparator = (a, c) -> {
			if(txt.charAt(0) == '⇧') return a.getTitle().compareTo(c.getTitle());
			else if(txt.charAt(0) == '⇩') return c.getTitle().compareTo(a.getTitle());
			return 0;
		};
		else comparator = (a, c) -> {
			if(txt.charAt(0) == '⇧') return a.getDate().compareTo(c.getDate());
			else if(txt.charAt(0) == '⇩') return c.getDate().compareTo(a.getDate());
			return 0;
		};
		//sort the list of A.P.O.D.s by correct comparator
		List<APOD> pods = apodsList.getItems().stream().sorted(comparator).toList();
		apodsList.getItems().clear();
		//updates the list with the sorted one
		apodsList.getItems().addAll(pods);
	}
	
	/**
	 * Used to return the string of the Date or Title label above the Listview
	 *
	 * @param event          The {@link MouseEvent} called by the -Date and -Title label, to determine which label was clicked
	 * @param unClickedLabel {@linkplain Label} of the non-clicked label
	 *
	 * @return the text of the clicked label as a {@link String}
	 */
	private String switchLabels(Event event, Label unClickedLabel){
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
	
	/**
	 * Changes scenes to the A.P.O.D. view
	 *
	 * @param event the {@link ActionEvent} used to get the stage and change scenes
	 */
	@FXML
	void search(ActionEvent event){
		final APOD apod = API.getAPOD(datePicker.getValue());
		String title = apod.getTitle() + " - " + apod.getDate();
		Transition.to(event, "apod-view.fxml", title, apod);
	}
	
	/**
	 * This will change the scene to the A.P.O.D. view when an {@link APOD} is double-clicked from the listview
	 *
	 * @param mouseEvent A {@link MouseEvent} used to determine if an item from the list view was double-clicked
	 */
	public void selectAPOD(MouseEvent mouseEvent){
		//https://stackoverflow.com/a/10950824/16929246
		apodsList.setOnMouseClicked(mouse -> {
			//if the user double-clicked with the primary mouse key and the listview isn't empty
			final APOD apod = apodsList.getSelectionModel().getSelectedItem();
			if(mouse.getButton().equals(MouseButton.PRIMARY) && mouse.getClickCount() == 2 && apod != null)
			{
				String title = apod.getTitle() + " - " + apod.getDate();
				Transition.to(mouseEvent, "apod-view.fxml", title, apod);
			}
		});
	}
	
	/**
	 * This closes the application and the main Thread and JavaFX Thread This does not stop any loading/progress bar Thread or Threads for API queries
	 *
	 * @param actionEvent to get the source to close its stage
	 */
	public void Exit(ActionEvent actionEvent){
		Transition.close(actionEvent);
	}
	
	/**
	 * This will initialize all nodes and with proper validation and styles when the application is loaded
	 *
	 * @param location  The location used to resolve relative paths for the root object, or {@code null} if the location is not known.
	 * @param resources The resources used to localize the root object, or {@code null}
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources){
		progressBar.setVisible(false);
		int amount = (int) (Math.random() * 7);
		LocalDate rDate = getRandomDateInAPOD(0, amount);
		randomSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000, (int) (Math.random() * 225 + 1)));
		randomSpinner.editableProperty().setValue(true);
		randomSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
			//replace all non-numeric from editor for the spinner's text editor
			if(!RegexValidator.forPattern("[^0-9]", "Eyo").validate(newValue).getResult())
				randomSpinner.getEditor().setText(newValue.replaceAll("[^0-9]", ""));
				//if the number is outside the valid range set it to the previous value
			else if(!IntegerRangeValidator.between(1, 10000, "Ayo").validate(Integer.valueOf(newValue)).getResult())
				randomSpinner.getValueFactory().setValue(Integer.parseInt(oldValue));
		});
		//add the date validation to each Datepicker
		addDateChecker(startDatePicker);
		addDateChecker(datePicker);
		addDateChecker(endDatePicker);
		//set random date interval for the range search of API
		startDatePicker.setValue(rDate.minusDays(amount));
		endDatePicker.setValue(rDate);
		datePicker.setValue(LocalDate.now());
		//add the bootstrap to the scene
		Transition.addStyle(apodsList);
	}
	
	/**
	 * This will disable each date outside the valid range for NASA's APOD API
	 *
	 * @param datepicker The {@link DatePicker} to add the validation to
	 */
	public void addDateChecker(DatePicker datepicker){
		//https://stackoverflow.com/a/53186959/16929246
		datepicker.setDayCellFactory(param -> new DateCell(){
			@Override
			public void updateItem(LocalDate item, boolean empty){
				super.updateItem(item, empty);
				setDisable(item.isAfter(APOD.maxDate) || item.isBefore(APOD.minDate));
			}
		});
	}
}
