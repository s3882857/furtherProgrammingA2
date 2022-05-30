package application.view;

import javafx.application.Application;
import javafx.stage.Stage;

public class FrontEndFXMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			FactoryStageLoader factory = new FactoryStageLoader();
	
			primaryStage = factory.loadWindow(primaryStage,"SmartBoard - Login" , ApplicationViews.LOGIN_VIEW);
						
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
