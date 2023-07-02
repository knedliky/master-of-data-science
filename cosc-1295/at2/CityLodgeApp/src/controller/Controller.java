package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Menu;

public class Controller implements Initializable {

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
	private MenuItem close;
	@FXML
	private Menu edit;
	@FXML
	private MenuItem about;
	/*@FXML
	private Menu help;*/
	@FXML
	private Text confirmTitle;
	@FXML
	private Text confirmMessage;
	@FXML
	private Button openMenu;

	boolean exit = false;

	private void loadStage(String fxml) {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource(fxml));
			Stage stage = new Stage();
			stage.setScene(new Scene(parent));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	@FXML
	private void handleActionEvent(ActionEvent event) {

		if (event.getSource() == addRoom) {
			loadStage("/view/AddRoom.fxml");

		} else if (event.getSource() == editRoom) {
			loadStage("/view/EditRoom.fxml");

		} else if (event.getSource() == deleteRoom) {
			loadStage("/view/DeleteRoom.fxml");

		} else if (event.getSource() == addImage) {
			loadStage("/view/AddImage.fxml");

		} else if (event.getSource() == openMenu) {
			loadStage("/view/Menu.fxml");

		} else if (event.getSource() == close) {
			closeProgram();
			//System.exit(0);

		} else if (event.getSource() == about) {
			handleHelp(event);

		}
	}

	// Handles showing the help dialogue
	public void handleHelp(ActionEvent event) {
		Alert alert = new Alert (Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText("Information");
		alert.setContentText("Checking if this is legit. This is a program written by Simon Karumbi from RMIT University. It is a Hotel Booking program to manage rooms and their records");
		alert.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void closeProgram() {
		loadStage("/view/ConfirmBox.fxml");

		confirmTitle.setText("Exiting the program...");
		confirmMessage.setText("Are you sure that you want to exit the program?\nThe Database will be automatically saved on exit." );

	}

	// Opening adding room scene 
	/*public void handleAddRoom (ActionEvent event) throws IOException {
			AddRoomController.open(event);
		}*/

}
