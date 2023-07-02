package application;

import java.sql.SQLException;
import hsqldb.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {

	private static Stage primaryStage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/view/Start.fxml"));
		primaryStage.setTitle("City Lodge Booking Application");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		primaryStage.setOnCloseRequest(e -> {
			e.consume();
			closeProgram();
		});
		
	}

	@Override
	public void init () {
		Database.connect();

	}

	@Override
	public void stop() {
		System.out.println("Closing DB");
		System.out.println("Annddd I give up!");
		
	}

	public static void closeProgram() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setContentText("Are you sure you want to quit?");
		alert.show();
		//boolean confirm = ConfirmBoxController.display("", "Are you sure you want to quit?"); 
		//Database.updateQuery(CityLodge.room);
		System.out.println("Closing DB");

	}

	@SuppressWarnings("unused")
	private static void setPrimaryStage(Stage stage) {
		Main.primaryStage = stage;
	}

	static public Stage getPrimaryStage() {
		return Main.primaryStage;
	}

	public static void main(String[] args) throws SQLException {
		launch(args);

	}



}
