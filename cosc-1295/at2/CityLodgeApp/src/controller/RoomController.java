package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class RoomController implements Initializable{

	public ImageView roomImage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		roomImage.setImage(new Image("/img/Standard.jpg"));
		
	}
	
}
