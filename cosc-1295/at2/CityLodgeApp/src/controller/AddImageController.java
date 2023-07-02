package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import util.DateTime;

public class AddImageController implements Initializable {
	
	//@FXML
	//private Text date;
	@FXML
	private TextField imageURL;
	
	public void handleMouseEvent(MouseEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(Main.getPrimaryStage());
		imageURL.setText(file.toString());
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//date.setText(DateTime.getCurrentTime());
	}

}
