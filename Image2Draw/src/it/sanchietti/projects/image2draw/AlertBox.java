package it.sanchietti.projects.image2draw;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AlertBox {
	
	protected Stage window;
	
	public static void display(String title, String msg) {
		Stage window = new Stage(StageStyle.TRANSPARENT);
		window.setHeight(150);
		window.setWidth(250);
		window.setTitle(title);
		window.initModality(Modality.APPLICATION_MODAL);
		Label l = new Label();
		l.setText(msg);
		Button b1 = new Button();
		b1.setText("close");
		b1.setOnAction(e -> window.close());
		b1.setTranslateY(30);
		
		StackPane vb = new StackPane();
		vb.getChildren().addAll(l, b1);
		
		Scene scene = new Scene(vb, 400, 300);
		
		window.setScene(scene);
		
		window.showAndWait();
	}	
}
