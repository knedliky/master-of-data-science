package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.stage.Stage;
import util.DateTime;

public class StartController implements Initializable {
	
	@FXML
	private Text date;	
	@FXML
	private Button openApp;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		date.setText(DateTime.getCurrentTime());
	}
	
	public void handleActionEvent (ActionEvent event) throws IOException {
		Parent menu = FXMLLoader.load(getClass().getResource("/view/Menu.fxml"));
		Scene scene = new Scene(menu);
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		window.setScene(scene);
		window.show();
		
	}
}


