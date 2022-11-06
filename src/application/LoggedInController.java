package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class LoggedInController implements Initializable {
	
	@FXML
	private Button acctBtn, transferBtn, depositBtn, moreServiceBtn, logOutBtn;
	
	@FXML
	private Label welcomeLabel;

	public void setUserInformation(String userName) {
		
		welcomeLabel.setText("Welcome "+userName+".");
		
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		logOutBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				Utilities.changeScene(evt, "Main.fxml", "Simple Bank", null, null);
			}
		});
		
	}// end of init()

}
