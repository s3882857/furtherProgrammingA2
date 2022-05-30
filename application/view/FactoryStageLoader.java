package application.view;

import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/*
 * A factory of sorts designed to instantiate each window. Standardization.
 */
public class FactoryStageLoader {

    private Stage stage;
    private Scene scene;
    private Parent root;
    
    /*
     * Instantiate Factory loader for all screens
     */
	public FactoryStageLoader() {
		
		this.root = null;
		this.scene = null;
		this.stage = null;
						
	}
	
	/*
	 * The guts. Loads all windows.
	 */
	public Stage loadWindow(Stage stage, String title, String urlResource) throws IOException {
		
		this.stage = stage;
		
		URL url = getClass().getResource(urlResource);
			
		FXMLLoader loader = new FXMLLoader(url);
		
		this.root = loader.load();
		
		int height = 500, width = 500;
					
		switch(urlResource) {
		
		case ApplicationViews.LOGIN_VIEW:
			
			width = 260;
			height = 320;
			
			break;
			
		case ApplicationViews.PROFILE_VIEW:
			
			width = 500;
			height = 600;
			
			break;
			
		case ApplicationViews.WORKSPACE_VIEW:
			
			width = 1200;
			height = 650;
			break;
			
		}
		this.scene = new Scene(this.root,height,width);
		this.scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
		// Create a new screen page, make sure only one is open at a time.
		if(this.stage==null) {
			this.stage = new Stage();
		}
		
		this.stage.setScene(this.scene);
		
		this.stage.setTitle(title);
		this.stage.setHeight(height);
		this.stage.setWidth(width);
		
		/*
		 *  Little bit of hard coding here, but easier to maintain and keep all versions
		 *  of each screen consistent.
		 */		
		switch(urlResource) {
		
		case ApplicationViews.LOGIN_VIEW:
			
			this.stage.centerOnScreen();
			this.stage.setMaxHeight(height);
			this.stage.setMaxWidth(width);
			this.stage.setMaximized(false);
			this.stage.resizableProperty().set(false);
			
			break;
			
		case ApplicationViews.PROFILE_VIEW:
			
			this.stage.setMaxHeight(height);
			this.stage.setMaxWidth(width);
			this.stage.setMaximized(false);
			this.stage.resizableProperty().set(false);
						
			break;
			
		case ApplicationViews.WORKSPACE_VIEW:
			
			break;
			
		}
				
		return this.stage;
		
	}
	
}
