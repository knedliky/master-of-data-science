package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import com.sun.glass.ui.Application;

import hsqldb.Database;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import util.DateTime;
import model.*;

public class MenuController implements Initializable{

	@FXML
	private BorderPane bp;
	@FXML
	private Text date;
	@FXML
	private Text status;
	@FXML
	private ImageView roomImage;
	@FXML
	private Text features;
	@FXML
	private Text roomRate;
	@FXML
	private Text roomStatus;
	@FXML
	private Button actionA;
	@FXML
	private Button actionB;
	@FXML 
	private Button addRoom;
	@FXML
	private Button editRoom;
	@FXML
	private Button deleteRoom;
	@FXML 
	private Button addImage;
	@FXML 
	private Button displayRooms;
	@FXML
	private Menu file;
	@FXML
	private Menu edit;
	@FXML
	private Menu help;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		date.setText(DateTime.getCurrentTime());
		roomImage.setImage(new Image("file:img/std1.jpg"));

		//status.setText(Database.con.toString());
		//roomImage.setImage(new Image(CityLodge.selectByRoomId(123).getImageURL()));
		//features.setText(CityLodge.selectByRoomId(123).getFeatureSummary());
	}

	@FXML
	private void searchRooms(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
		try {
			List<Room> roomData = CityLodge.searchRooms();
			roomData.forEach(r -> System.out.println(r.getDetails()));

		} catch (SQLException e){
			System.out.println("Error occurred while getting room information from DB.\n" + e);
			throw e;
		}
	}

	//Set Room information to Text Area
	@FXML
	private void setRoomInfo (Room room) {
		features.setText("First Name: " + room.getFeatureSummary());
	}

	//Populate Employee for TableView and Display Employee on TextArea
	@FXML
	private void populateAndShowEmployee(Room room) throws ClassNotFoundException {
		if (room != null) {
			setRoomInfo(room);
		} else {
			features.setText("This room does not exist!\n");
		}
	}

	public void handleMouseAction (ActionEvent event) throws IOException {
		Parent menu = FXMLLoader.load(getClass().getResource("/view/Room.fxml"));
		Scene scene = new Scene(menu);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

		window.setScene(scene);
		window.show();
	}

	// Handles clicking on ActionA button
	public void handleActionA (ActionEvent event) throws SQLException, ClassNotFoundException {
		System.out.println("1");

		features.setText(CityLodge.selectByRoomId(123).getFeatureSummary());
		System.out.println("2");
		roomStatus.setText(CityLodge.selectByRoomId(123).getStatus().toString().toUpperCase());
		System.out.println("3");
		roomRate.setText("$" + Double.toString(CityLodge.selectByRoomId(123).getRentalRate()) + " per night");
		System.out.println("4");

		if(roomStatus.getText().equalsIgnoreCase("Available")) {
			System.out.println("Green");
			roomStatus.setStyle("-fx-text-fill:#68e468;");
			
		} else if(roomStatus.getText().equalsIgnoreCase("Rented")) {
			roomStatus.setStyle("-fx-text-fill:#ee4025;");

		} else if(roomStatus.getText().equalsIgnoreCase("Rented")) {
			roomStatus.setStyle("-fx-text-fill:#b4c9dd;");

		}
		/* switch (roomStatus.getText()) {
		case "Available" :
			System.out.println("Available");
			roomStatus.setStyle("-fx-text-fill:#68e468;");
			break;
		case "Rented" :
			roomStatus.setStyle("-fx-text-fill:#ee4025;");
			break;
		case "Maintenance" :
			roomStatus.setStyle("-fx-text-fill:#b4c9dd;");
			break;

		}*/
	}

	public void handleActionB (ActionEvent event) throws SQLException, ClassNotFoundException {
	}

	// Opening adding room scene 
	public void handleAddRoom (ActionEvent event) throws IOException {
		AddRoomController.open(event);
	}

	public void handleEditRoom (MouseEvent event) {

	}

	public void handleDeleteRoom (MouseEvent event) {

	}

	public void handleAddImage (MouseEvent event) {

	}

	public void handleShowRooms (MouseEvent event) {

	}
	// Handles exiting the application
	public void handleExit(ActionEvent actionEvent) {
		System.out.println("Closing and saving program");
		Stage stage = (Stage)bp.getScene().getWindow();
		stage.hide();
	}

	public void handleMouseOverRoom (MouseEvent mouseEvent) {
	}

	// Handles showing the help dialogue
	public void handleHelp(ActionEvent actionEvent) {
		Alert alert = new Alert (Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText("Information");
		alert.setContentText("This is a program written by Simon Karumbi from RMIT University. It is a Hotel Booking program to manage rooms and their records");
		alert.show();
	}
}
