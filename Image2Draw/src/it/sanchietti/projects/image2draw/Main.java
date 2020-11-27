package it.sanchietti.projects.image2draw;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application{

	private Stage window;
	private Button button;

	@Override
	public void start(Stage arg0) throws Exception {
		window = arg0;
		window.setTitle("Validate Input");
		
		TextField nameInput = new TextField();
		nameInput.setPromptText("Enter ID");
		
		nameInput.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.ENTER)
				button.fire();
		});;
		
		button = new Button("Validate");
		button.setOnAction(e -> validate(nameInput.getText()));
		
		
		VBox layout = new VBox();
		layout.setPadding(new Insets(20, 20, 20, 20));
		layout.getChildren().addAll(nameInput, button);
		
		Scene scene1 = new Scene(layout);
				
		window.setScene(scene1);
		
		window.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void validate(String input) {
		try {
			if(input.length() != 6)
				throw new IllegalArgumentException();
			int i = Integer.parseInt(input);
			System.out.println(input);
		}
		catch(Exception e) {
			if(e instanceof NumberFormatException)
				AlertBox.display("Number format exception","The ID can only contain numbers");
			else if(e instanceof IllegalArgumentException)
				AlertBox.display("Length error", "Error of the lenght of the ID");
		}
	}
	
}
