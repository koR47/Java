package MidPoint;
import java.util.Random;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class MidpointDisplacement extends BorderPane {
	static Random r = new Random();
	double [] pointL = new double[2];
	double [] pointR = new double[2];
	double samples = 3;
	double depth = 5;
	
	public MidpointDisplacement (){
		Canvas leinwand = new Canvas(600,600);
		Label segText = new Label("Segmente");
		Label smoothness = new Label("Smoothness");
		TextField info = new TextField("Hi.");
		GraphicsContext gc = leinwand.getGraphicsContext2D();
		gc.setFill(Color.RED);
		gc.strokeRect(0, 0, leinwand.getWidth(), leinwand.getHeight());
		
		Button button = new Button("New");
		button.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
		button.setOnAction( e -> {
			gc.clearRect(0, 0, leinwand.getWidth(), leinwand.getHeight());
			gc.strokeRect(0, 0, leinwand.getWidth(), leinwand.getHeight());
			info.setText("Segments:  " + samples + "   ///   Smoothness:  " + depth);
			berechne(gc, leinwand);
		});
		
		Slider segmente = new Slider(1, 30, samples);
		segmente.setBlockIncrement(1);
		segmente.valueProperty().addListener((observable, oldValue, newValue) -> {
		    samples = newValue.intValue();
		    info.setText("Segments:  " + samples + "   ///   Smoothness:  " + depth);
		});
		
		Slider tiefe = new Slider(2, 8, samples);
		tiefe.setBlockIncrement(0.1);
		tiefe.valueProperty().addListener((observable, oldValue, newValue) -> {
		    depth = newValue.doubleValue();
		    info.setText("Segments:  " + samples + "   ///   Smoothness:  " + depth);
		});
		
		VBox steuerung = new VBox();
		steuerung.getChildren().addAll(button, segText, segmente, smoothness, tiefe, info);
		steuerung.setSpacing(10);
		
		this.setBottom(steuerung);
		this.setCenter(leinwand);
	}
	
	public void berechne(GraphicsContext gc, Canvas canvas){
		
		for (int i = 0; i == 1; i++){
			pointL[i] = 0;
			pointR[i] = 0;
		}
		
		pointL[0] = 0;
		pointL[1] = canvas.getHeight() / 2;
		pointR[0] = canvas.getWidth() - 10;
		pointR[1] = canvas.getHeight() / 2;
		
		gc.fillRect(pointL[0], pointL[1], 10, 10);
		gc.fillRect(pointR[0], pointR[1], 10, 10);
		
		midpoint(pointL[0],pointL[1],pointR[0],pointR[1], gc, canvas);
	}
	
	public void midpoint(double xL, double yL, double xR, double yR, GraphicsContext gc, Canvas canvas){
		double xDiff = xR - xL;
		double yDiff = yR - yL;
		
		if (yDiff < 0){
			yDiff = yDiff * -1;
		}
		
		double rnd = (r.nextDouble() * 2 - 1) * (xDiff/depth);
		
		double midX = (xR + xL) / 2;
		double midY = (yR + yL) / 2 + rnd;

		gc.fillRect(midX, midY, 3, 3);
		
		if (xDiff > canvas.getWidth() / samples){
			midpoint(xL, yL, midX, midY, gc, canvas);
			midpoint(midX, midY, xR, yR, gc, canvas);
		}else{
			gc.strokeLine(xL, yL, midX, midY);
			gc.strokeLine(midX, midY, xR, yR);
		}
	}
}
