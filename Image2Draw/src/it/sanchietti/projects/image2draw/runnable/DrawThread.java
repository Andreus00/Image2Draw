package it.sanchietti.projects.image2draw.runnable;

import java.util.List;
import java.util.function.Consumer;

import it.sanchietti.projects.image2draw.AlertBox;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Paint;

public class DrawThread implements Runnable{
	
	private Image im;
	private Consumer<Image> s;
	private List<Paint> colors;
	private PixelReader pxReader;
	
	public DrawThread(Image im,List<Paint> colors, Consumer<Image> s2) {
		this.im = im;
		this.s = s2;
		this.colors = colors;
	}

	@Override
	public void run() {
		if(im == null) {
			AlertBox.display("Empty image", "Please select an image");
			return;
		}
		im.getPixelReader();
		System.out.println(colors);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//code to create the image
		Image newImage = draw();
		s.accept(newImage);
	}
	
	private Image draw() {
		return null;
	}
	
	

}
