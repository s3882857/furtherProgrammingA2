module FPAssignment2FX {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.graphics;
	requires java.desktop;
	requires junit;
	requires java.sql;
	requires javafx.swing;
	
	opens application.view to javafx.graphics,javafx.fxml;

}
