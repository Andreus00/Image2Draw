package it.sanchietti.projects.image2draw;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class I2DApp extends Application{
	
	private Stage window;
	private Rectangle r;
	private int l1width = 300;
	private int l1height = 250;
	private Scene scene1, scene2;
	private Button button1, button3;
	private ButtonHandler bh = new ButtonHandler();
	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		
		//save scene and set title
		window = primaryStage;
		window.setTitle("I2D");
		
		//Button1
		button1 = new Button();
		button1.setText("Click me");
		//i use an eventHandler to perform this 
		//button's action instead of using a lambda
		button1.setOnAction(this.bh);
		
		//create a rectangle
		r = new Rectangle(50, 30, Color.rgb(140, 56, 10));
		r.setVisible(true);
		//r.setTranslateX(r.getWidth()/2);
		//r.setTranslateY(r.getHeight()/2);
	
		
		//create a StackPane
		VBox layout1 = new VBox();
		layout1.getChildren().add(button1);
		layout1.getChildren().add(r);
		
		//create Scene
		scene1 = new Scene(layout1, l1width, l1height);
		window.setScene(scene1);
		
		
		
		//create a second scene
		Button button2 = new Button();
		button2.setText("helo world");
		button2.setOnAction(x -> window.setScene(scene1));
		
		button3 = new Button();
		button3.setText("quit");
		button3.setOnAction(new ButtonHandler());
		button3.setTranslateY(30);
		
		Button alertButton = new Button();
		alertButton.setText("Alert");
		alertButton.setOnAction(e -> AlertBox.display("Threre is a Problem", "Error 42"));
		
		
		StackPane layout2 = new StackPane();
		layout2.getChildren().addAll(button2, alertButton, button3);
		

		
		scene2 = new Scene(layout2, 100,100);
		
		
		window.setOnCloseRequest(e -> {
			e.consume();
			closeProgram();
		});
		
		window.show();
	}
	
	
	/**
	 * This is an handler. I can use it instead of using a lambda to handle an event.
	 * In this case it is used by button1 to perform his action.
	 * I could have implemented the EventHandler in the main class too.
	 * I can get the source of an event with a method in order to understand who has to perform
	 * the action
	 * @author Andrea
	 *
	 */
	private class ButtonHandler implements EventHandler<ActionEvent>{



		@Override
		public void handle(ActionEvent e) {
			if(e.getSource() == button1) {
				window.setScene(scene2);
			}
			else if(e.getSource() == button3)
				closeProgram();
			
		}
	}
	
	private void closeProgram() {
		boolean close = ConfirmBox.display("Exit Program", "Are you sure you want to quit?");
		if(close)
			window.close();
	}
	
}
