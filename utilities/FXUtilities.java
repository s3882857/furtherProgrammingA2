package utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FXUtilities extends Utilities{


	/*
	 * Build a basic/generic input box. FX style
	 */
	public String fxInputBox(String title, String prompt) {

		String inputString = "";

		TextInputDialog inputDialog = new TextInputDialog();

		inputDialog.setTitle(title);

		inputDialog.setGraphic(null);

		inputDialog.setHeaderText("");

		inputDialog.getDialogPane().setContentText(prompt);

		inputDialog.showAndWait();

		TextField input = inputDialog.getEditor();

		inputString = input.getText();

		return inputString;

	}

	/*
	 * Build a basic/generic alert message.
	 */
	public void alertMessage(String headerMsg, String msg, AlertType alertType, Stage stage) {

		if(alertType==null) {
			alertType = AlertType.WARNING;
		}
		
		Alert alert = new Alert(alertType);
		
		
		alert.initModality(Modality.APPLICATION_MODAL);
		//alert.initOwner(stage);

		alert.getDialogPane().setContentText(msg);
		alert.getDialogPane().setHeaderText(headerMsg);

		alert.showAndWait();

	}
}
