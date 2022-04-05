module Rob {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.swing;
	requires java.desktop;
	
	opens application to javafx.graphics, javafx.fxml;
}
