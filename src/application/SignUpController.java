package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SignUpController implements Initializable{
	
	@FXML
	TextField acctEmail, acctNumber, userName, passWord;
	@FXML
	Button createAcctBtn, logInBtn;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		createAcctBtn.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent evt) {
				Utilities.signUpUser(evt, acctNumber.getText(), userName.getText(), passWord.getText());
			}
		});
		
		logInBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				Utilities.changeScene(evt, "Main.fxml", "Simple Bank", null, null);
			}
		});
	}

}
