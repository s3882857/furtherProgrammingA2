package smartBoard.FrontEnd;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class FixedCircle extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		Pane pane = new Pane();
		
		Circle circle = new Circle(200,200,50);
		
		circle.centerXProperty().bind(pane.widthProperty().divide(2));
		circle.centerYProperty().bind(pane.heightProperty().divide(2));
		circle.setRadius(50);
		circle.setStroke(Color.BLACK);
		circle.setFill(Color.BLUE);
				
		pane.getChildren().add(circle);
		
		Scene scene = new Scene(pane, 500, 550);
		primaryStage.setTitle("Show Circle Centered!!!!");
		primaryStage.setScene(scene);
		primaryStage.show();	
		
	}
	
	public static void main(String[] args) {
		
		Application.launch(args);
		
	}

}
