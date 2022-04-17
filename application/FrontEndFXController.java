package application;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import smartBoard.BackEnd;
//import smartBoard.User.SmartBoardLoginException;

public class FrontEndFXController {
	
	private BackEnd backEnd; 
	@FXML
	private TextField txtUserName;
	
	@FXML
	private TextField txtPassword;
	
	@FXML
	private Label lblUserName;
	
	@FXML
	private Label lblPassword;
	
	@FXML
	private Button btnLogIn;
	
	@FXML
	private Button btnClose;
	
	
	public FrontEndFXController() {
		
		this.backEnd = new BackEnd();
		
	}
	
	@FXML
	public void loginButtonClicked(Event e) {
		
		
		//System.out.println("Login Button Clicked User Name = " + txtUserName.getText());
		//System.out.println("Login Button Clicked Password = " + txtPassword.getText());
		
		this.backEnd.getUser(txtUserName.getText());
		
		System.out.println("Password check = " + this.backEnd.passwordCheck(txtPassword.getText()));
		
	}
	
	@FXML
	public void closeButtonClicked(Event e) {
		System.out.println("Close Button Clicked");
		System.exit(0);
	}
}	
