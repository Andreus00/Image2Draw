package it.sanchietti.projects.image2draw.application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import it.sanchietti.projects.image2draw.AlertBox;
import it.sanchietti.projects.image2draw.ConfirmBox;
import it.sanchietti.projects.image2draw.runnable.DrawThread;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.GestureEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;

public class Main extends Application{

	private Stage window ;
	private ImageView iv = new ImageView();
	private Scene mainScene;
	private BorderPane mainPane;
	private VBox pane;
	private MenuBar menuBar;
	private Pane p;
	private VBox leftBox;
	private VBox rightBox;
	private VBox bottomBox;
	private Button draw;
	private double dragXBegin;
	private double dragYBegin;
	volatile private GridPane dataBox;
	private boolean notDragged = true;
	private PixelReader pxreader;
	private ScrollPane scrollPane;
	
	private EventHandler<MouseEvent> c;
	private MenuItem menuItem1;
	
	@Override
	public void start(Stage arg0) throws Exception {
		window = arg0;
		window.setWidth(1920);
		window.setHeight(1080);
		window.setTitle("Image To Draw");
		
		//menu bar
		Menu menu1 = new Menu("File");
		menuItem1 = new MenuItem("Apri                          ");
		menuItem1.setOnAction(e -> open());
		MenuItem menuItem2 = new MenuItem("Salva");
		
		MenuItem menuItem3 = new MenuItem("Esci");
		menuItem3.setOnAction(e -> {
			if(ConfirmBox.display("Exit", "Are yousure you want to exit?"))
					window.close();
		});
		
		menu1.getItems().addAll(menuItem1, menuItem2, new SeparatorMenuItem(), menuItem3);
		menuBar = new MenuBar();
		menuBar.setBackground(new Background(new BackgroundFill(Color.grayRgb(190), CornerRadii.EMPTY, Insets.EMPTY)));
		menuBar.setMinHeight(25);

		menuBar.getMenus().add(menu1);
		
		pane = new VBox();
		pane.getChildren().add(menuBar);
		
		mainPane = new BorderPane();
		
		mainPane.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
		
		c = e -> {
			if(notDragged) {
				if(e.getButton() == MouseButton.PRIMARY)
					addPixel((int)e.getX(), (int)e.getY(), pxreader.getColor((int)e.getX(), (int)e.getY()));
			}
			notDragged = true;
		};
		
		//iv.setOnMouseReleased(c);
		
		
		
		p = new Pane();
		p.setBackground(new Background(new BackgroundFill(Color.grayRgb(50), new CornerRadii(10), new Insets(0))));
		
		p.getChildren().add(iv);

		p.setOnScroll(e -> {
			double delta = e.getDeltaY();
			iv.setScaleX(iv.getScaleX()*(delta > 0 ? 1.05 : 0.95));
			iv.setScaleY(iv.getScaleX());
		});
		p.setOnMousePressed(e ->{
			dragXBegin = e.getX();
			dragYBegin = e.getY();
		});
		p.setOnMouseDragged(e -> {
			double deltaX = e.getX() - dragXBegin;
			double deltaY = e.getY() - dragYBegin;
			dragYBegin = e.getY();
			dragXBegin = e.getX();
			iv.setTranslateX(iv.getTranslateX() + deltaX);
			iv.setTranslateY(iv.getTranslateY() + deltaY);
			notDragged = false;
		});

		
		leftBox = new VBox();
		leftBox.setAlignment(Pos.CENTER);
		leftBox.setMinWidth(70);
		
		draw = new Button("Draw");
		draw.setBackground(new Background(new BackgroundFill(Color.grayRgb(160), new CornerRadii(6), Insets.EMPTY)));
		draw.setOnMousePressed(e -> {
			draw.setBackground(new Background(new BackgroundFill(Color.grayRgb(120), new CornerRadii(6), Insets.EMPTY)));
		});
		
		draw.setOnAction(e -> draw());
		draw.setOnMouseReleased(e -> {
			draw.setBackground(new Background(new BackgroundFill(Color.grayRgb(190), new CornerRadii(6), Insets.EMPTY)));
		});
		
		draw.setDisable(true);
		
		dataBox = new GridPane();
		dataBox.setHgap(10);
		dataBox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,new CornerRadii(7), Insets.EMPTY)));
		
		dataBox.add(new Label("Pixel"), 1, 0);
		dataBox.add(new Label("Color"), 2, 0);
		dataBox.add(new Label("   "), 3, 0);
		leftBox.setSpacing(80);
		scrollPane = new ScrollPane(dataBox);
		scrollPane.setMaxHeight(300);
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,new CornerRadii(10), Insets.EMPTY)));
		leftBox.getChildren().addAll(draw,new Separator(), scrollPane);
		leftBox.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, new Insets(0))));
		
		
		
		bottomBox = new VBox();
		bottomBox.setMinHeight(20);
		bottomBox.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, new Insets(0))));
		rightBox = new VBox();
		rightBox.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, new Insets(0))));
		rightBox.setMinWidth(10);
		

		spawnThings();
		
		mainScene = new Scene(mainPane);
		
		
		mainScene.setOnKeyPressed(e -> {
			if(e.getCode() == KeyCode.R) {
				iv.setTranslateX(0);
				iv.setTranslateY(0);
				iv.setScaleX(1);
				iv.setScaleY(1);
			}
		});
		
		window.setScene(mainScene);
		
		window.show();
	}
	
	private void draw() {
		draw.setDisable(true);
		iv.setOnMouseReleased(null);
		menuItem1.setDisable(true);
		Consumer<Image> s = (newImg) -> {
			//code to show the image
			changeImage(newImg);
		};
		Thread t = new Thread(
				new DrawThread(iv.getImage(),
						dataBox.getChildren().stream().filter(node -> node != null && node instanceof Label && ((Label)node).getText().equals("")).map(x -> ((Label)x).getBackground().getFills().get(0).getFill()).collect(Collectors.toList()),
				s));

		t.start();
	}
	
	private synchronized void changeImage(Image newImg) {
		iv.setScaleX(1);
		iv.setScaleY(1);
		iv.setImage(newImg);
		draw.setDisable(false);
		iv.setOnMouseReleased(c);
		menuItem1.setDisable(false);
	}

	private void spawnThings() {
		
		mainPane.setCenter(p);
		mainPane.setBottom(bottomBox);
		mainPane.setTop(pane);
		mainPane.setLeft(leftBox);
		mainPane.setRight(rightBox);
		
		System.out.println(window.getHeight()/p.getHeight());

		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
	/**
	 * 
	 */
	private void open(){
		Image im = null;
		try {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Open Resource File");
			String path = fileChooser.showOpenDialog(window).toURI().toString();

			im = new Image(path , p.getWidth(), p.getHeight(), true, true);

			System.out.println("image found");
			System.out.println(im.getHeight());
			System.out.println(im.getWidth());
			changeImage(im);
			pxreader = im.getPixelReader();
			spawnThings();
			
		}
		catch(Exception e) {
			AlertBox.display("Error", "This file can not be readed");
			draw.setDisable(true);
			im = null;
			iv.setImage(im);
			return;
		}
	}
	
	
	int dataBoxElements = 1;	
	//volatile LinkedList<Node[]> nodeMatrix = new LinkedList<>();
	
	private void addPixel(int d, int f, Color color) {
		Label num = new Label("" + dataBoxElements);
		Label coord = new Label(d + "-" + f);
		Label colLabel = new Label();
		Button deleteLine = new Button("x");
		deleteLine.setTextFill(Color.TOMATO);
		deleteLine.setFont(Font.font(20));
		deleteLine.setBackground(Background.EMPTY);
		deleteLine.setOnAction(e -> {
			dataBox.getChildren().remove(colLabel);
			dataBox.getChildren().remove(num);
			dataBox.getChildren().remove(coord);
			dataBox.getChildren().remove(deleteLine);
		});
		
		colLabel.setMinWidth(30);
		colLabel.setMinHeight(30);;
		colLabel.setBackground(new Background(new BackgroundFill(color, new CornerRadii(6), Insets.EMPTY)));
		//nodeMatrix.add(new Node[] {new Label(d + "-" + f), colLabel, deleteLine});

		dataBox.add(coord, 1, dataBoxElements);
		dataBox.add(colLabel, 2, dataBoxElements);
		dataBox.add(deleteLine, 3, dataBoxElements);

		dataBoxElements ++;
	}
}
