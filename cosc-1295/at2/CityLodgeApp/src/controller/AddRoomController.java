package controller;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import hsqldb.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Room;
import model.RoomLogic;
import model.RoomStatus;
import model.Standard;
import model.Suite;
import util.DateTime;

public class AddRoomController implements Initializable {

	@FXML 
	private Button OK;
	private Button cancel;
	private Text date;
	private TextField roomId;
	private ComboBox roomType;
	private ObservableList<Room> comboBoxData = FXCollections.observableArrayList();
	private DatePicker lastMaintenanced;
	private ToggleGroup numBeds;
	private TextArea featureSummary;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("Add Room is working");
		date.setText(DateTime.getCurrentTime());
		roomType.getItems().addAll("Standard", "Suite");
		roomType.setValue("Standard");
	}

	@FXML
	private void handleComboBoxAction() {
		Room selectedRoom = (Room)roomType.getSelectionModel().getSelectedItem();
		System.out.println(("ComboBox Action (selected: " + selectedRoom + ")\n"));
	}

	public void handleGetRoomID(KeyEvent event) throws NumberFormatException, Exception {
		if (RoomLogic.getRoom(Integer.parseInt(roomId.getText())) == null) {
			roomId.setStyle("-fx-text-color=red;");

		} else {
			roomId.setStyle("-fx-text-color=green;");

		}
	}

	// Adding rooms method
	public void handleActionEvent (ActionEvent event) throws NumberFormatException, Exception {
		if(event.getSource() == OK && (roomType.getSelectionModel().getSelectedItem() instanceof Suite))  {
			int day = lastMaintenanced.getValue().getDayOfMonth();
			int month = lastMaintenanced.getValue().getMonthValue();
			int year = lastMaintenanced.getValue().getYear();
			RoomLogic.addRoom(new Suite(Integer.parseInt(roomId.getText()), featureSummary.getText(), new DateTime(day, month, year)));

		}
	}


}
