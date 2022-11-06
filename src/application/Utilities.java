package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Utilities {
	
	// Main Page & Sign Up Utilities
	
	public static void changeScene(ActionEvent evt, String fxmlFile, String title, String userName, String passWord) {
		Parent root = null;
		
		// If a username and password are submitted, the changeScene method will pass 
		// the user's information to the logged in controller
		if(userName !=null && passWord != null) {
			try {
				FXMLLoader loader = new FXMLLoader(Utilities.class.getResource(fxmlFile));
				root = loader.load();
				LoggedInController loggedInController = loader.getController();
				loggedInController.setUserInformation(userName);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		// Otherwise it will load the selected fxml file
		else {
			try {
				root = FXMLLoader.load(Utilities.class.getResource(fxmlFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Stage stage = (Stage)((Node) evt.getSource()).getScene().getWindow();
		stage.setTitle(title);
		stage.setScene(new Scene(root, 600, 400));
		stage.show();
	}// end of changeScene
	
	public static void signUpUser(ActionEvent evt, String accNum, String userName, String passWord) {
		
		Connection connection = null;
		PreparedStatement psInsert = null;
		PreparedStatement psCheckUserExists = null;
		PreparedStatement psCheckAccExists = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		String custID = "";
		
		try {
		// First create the connection to the right database
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/simplebank", "root", "toor");
			
		// Then check the appropriate table for the account number
			psCheckAccExists = connection.prepareStatement("Select * from account where accNum = ?");
			psCheckAccExists.setString(1, accNum);
			resultSet = psCheckAccExists.executeQuery();
			
		// If the account number does not exist in the database, the user cannot create a mobile login
			if(!resultSet.isBeforeFirst()) {
				
				System.out.println("Account does not exist.");
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Invalid account number. Please contact Customer Service at support@simplebank.com");
				alert.show();
				
			}
		// If the account number is valid, extract the customer ID from the account table
			else {
				while(resultSet.next()) {
					String accID = resultSet.getString("accNum");
					String cusID = resultSet.getString("custID");
					System.out.println(accID+" "+cusID);	
					custID = cusID; // Assigning local variable to method variable
				}
				
		// Then verify that the new user's chosen login name isn't already taken			
				psCheckUserExists = connection.prepareStatement("SELECT * from mobilelogin where accName = ?");
				psCheckUserExists.setString(1, userName);
				resultSet2 = psCheckUserExists.executeQuery();
			
		// If the login id exists already, recommend user contacts customer service
				if(resultSet2.isBeforeFirst()) {
					
					System.out.println("User already exists.");
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setContentText("You cannot use this username. Contact Customer Service at support@simplebank.com");
					alert.show();
					
				}
		// If the login id is valid, insert the new user information into the database and redirect them to the logged-in screen.
				else {
					
					psInsert = connection.prepareStatement("Insert Into mobilelogin (custID, accName, passWd) Values(?, ?, ?)");
					
					psInsert.setString(1, custID);
					psInsert.setString(2, userName);
					psInsert.setString(3, passWord);
					psInsert.executeUpdate();
					
					changeScene(evt, "loggedin.fxml", "Welcome!", userName, passWord);
				
				}
			}
		}
		// Catch all SQL exceptions
		catch(SQLException e) {
			e.printStackTrace();
		}
		// Close out all connections created
		finally {
		
			if(resultSet != null) {
				try {
					resultSet.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(resultSet2 != null) {
				try {
					resultSet2.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(psCheckUserExists != null) {
				try {
					psCheckUserExists.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(psCheckAccExists != null) {
				try {
					psCheckAccExists.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(psInsert != null) {
				try {
					psInsert.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
			
			if(connection != null) {
				try {
					connection.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}// end of signUpUser

	public static void logInUser(ActionEvent evt, String userName, String passWord) {
		
		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet rSet = null;
		
		// First verify that the username and password are not blank
		if(userName == "" || passWord == "") {
			System.out.println("Username or Password are null");
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Invalid login information. Contact Customer Service at support@simplebank.com");
			alert.show();
		}
		else {
			try {
		// Next connect to the correct database
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/simplebank", "root", "toor");
		
		// Then check to see if the username exists in the table
				pStatement = connection.prepareStatement("SELECT passWd from mobilelogin where accName = ?");
				pStatement.setString(1, userName);
				rSet = pStatement.executeQuery();
		
		// If the username doesn't exist, suggest contacting customer service
				if(!rSet.isBeforeFirst()) {
					System.out.println("User not found in DB");
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setContentText("Invalid login information. Contact Customer Service at support@simplebank.com");
					alert.show();
				}
		// If the username is valid, extract the password from the table and compare the stored to the submission
				else {
					while(rSet.next()) {
						String storedPW = rSet.getString("passWd");
					
		// If the password is valid, redirect user to logged-in screen
						if(storedPW.equals(passWord)) {
							changeScene(evt, "loggedin.fxml", "Welcome!", userName, passWord);
						}
		// If the password is invalid, suggest contacting customer service
						else {
							System.out.println("Incorrect password");
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setContentText("Invalid login information. Contact Customer Service at support@simplebank.com");
							alert.show();
						}
					}
				}
			} 
		// Catch all SQL Exceptions
			catch(SQLException e) {
				e.printStackTrace();
			} 
		// Close all connections created
			finally {
			
				if(rSet != null) {
					try {
						rSet.close();
					} catch(SQLException e) {
						e.printStackTrace();
				}
				}
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch(SQLException e) {
						e.printStackTrace();
					}
				}
				if(connection != null) {
					try {
						connection.close();
					} catch(SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}// end of logInUser

	// Logged In Utilities
	
	public static String getUserName(String userName) {
		
		Connection connection = null;
		PreparedStatement pStatement = null;
		PreparedStatement pStatement2 = null;
		ResultSet rSet = null;
		ResultSet rSet2 = null;
		String cusID = null, fullName = "";
		
		try {
		// First open a connection to the database
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/simplebank", "root", "toor");
		
		// Then locate the username in the log in table and assign the customer ID to variable
			pStatement = connection.prepareStatement("SELECT custID from mobilelogin where accName = ?");
			pStatement.setString(1, userName);
			rSet = pStatement.executeQuery();
			
			while(rSet.next()) {
				String custID = rSet.getString("custID");
				cusID = custID;
			}
			
		
		// Next use the retrieved customer ID to return the name to the method call
			pStatement2 = connection.prepareStatement("Select fName, lName from customer where custID = ?");
			pStatement2.setString(1, cusID);
			rSet2 = pStatement2.executeQuery();
			
			while(rSet2.next()) {
				String fName = rSet2.getString("fName");
				String lName = rSet2.getString("lName");
				
				fullName = fName+" "+lName;
			}
			
			return fullName;
			
		}
		// Catch any SQL exceptions
		catch(SQLException e) {
			e.printStackTrace();
		}
		// Close all opened connections
		finally {
			if(rSet != null) {
				try {
					rSet.close();
				} catch(SQLException e) {
					e.printStackTrace();
			}
			}
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(connection != null) {
				try {
					connection.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return fullName;
		
	}// end of getUserName
	
}// end of Utilities
