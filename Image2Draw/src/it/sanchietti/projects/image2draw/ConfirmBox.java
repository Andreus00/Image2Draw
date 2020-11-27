package it.sanchietti.projects.image2draw;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox {
	
	protected Stage window;
	private static boolean result;
	
	public static boolean display(String title, String msg) {
		Stage window = new Stage();
		window.setWidth(300);
		window.setHeight(250);
		window.setTitle(title);
		window.initModality(Modality.APPLICATION_MODAL);
		Label l = new Label();
		l.setText(msg);
		Button b1 = new Button();
		b1.setText("Yes");
		b1.setOnAction(e -> {result = true; window.close();});
		b1.setBorder(Border.EMPTY);
		b1.setTranslateX(-20);
		Button b2 = new Button();
		b2.setText("No");
		b2.setOnAction(e -> {result = false; window.close();});
		
		b2.setTranslateX(20);
				
		VBox vb = new VBox();
		vb.setAlignment(Pos.CENTER);
		
		HBox hb = new HBox();
		hb.setAlignment(Pos.CENTER);
		hb.getChildren().addAll(b1, b2);
		
		vb.getChildren().addAll(l, hb);
		
		Scene scene = new Scene(vb, 400, 300);
		
		window.setScene(scene);
		
		window.showAndWait();
		
		return result;
	}	
}
