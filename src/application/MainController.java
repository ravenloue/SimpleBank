package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class MainController implements Initializable{
	
	@FXML
	TextField userName;
	@FXML
	PasswordField passWord;
	@FXML
	Button loginBtn, forgotPassBtn, signUpBtn;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		loginBtn.setOnAction(new EventHandler<ActionEvent> () {
			@Override
			public void handle(ActionEvent evt) {
				Utilities.logInUser(evt, userName.getText().trim(), passWord.getText().trim());
			}
		});
		
		signUpBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				Utilities.changeScene(evt, "signup.fxml", "Sign Up", null, null);
			}
		});
		
		forgotPassBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				System.out.println("User forgot their password");
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("In the case of a forgotten username or password, please contact Customer Service at support@simplebank.com");
				alert.show();
			}
		});
		
		
	}// end of init()

}
