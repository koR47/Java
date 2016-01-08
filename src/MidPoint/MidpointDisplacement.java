package MidPoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MidpointDisplacement extends BorderPane {
	static Random r = new Random();
	double [] pointL = new double[2];
	double [] pointR = new double[2];
	double samples = 3;
	double depth = 5;
	ArrayList<Double> midpointsX = new ArrayList<Double>();
	ArrayList<Double> midpointsY = new ArrayList<Double>();
	
	
	public MidpointDisplacement (){
		Canvas leinwand = new Canvas(600,600);
		Label segText = new Label("Segmente");
		Label smoothness = new Label("Smoothness");
		GraphicsContext gc = leinwand.getGraphicsContext2D();
		gc.strokeRect(0, 0, leinwand.getWidth(), leinwand.getHeight());
		
		Button button = new Button("New");
		button.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
		button.setOnAction( e -> {
			gc.clearRect(0, 0, leinwand.getWidth(), leinwand.getHeight());
			midpointsX.clear();
			midpointsY.clear();
			gc.strokeRect(0, 0, leinwand.getWidth(), leinwand.getHeight());
			berechne(gc, leinwand);
		});
		
		Slider segmente = new Slider(1, 50, samples);
		segmente.setBlockIncrement(1);
		segmente.valueProperty().addListener((observable, oldValue, newValue) -> {
		    System.out.println("Neue Anzahl der Segmente: " + newValue.intValue());
		    samples = newValue.intValue();
		});
		
		Slider tiefe = new Slider(2, 10, samples);
		tiefe.setBlockIncrement(0.1);
		tiefe.valueProperty().addListener((observable, oldValue, newValue) -> {
		    System.out.println("Neue Tiefe: " + newValue.doubleValue());
		    depth = newValue.intValue();
		});
		
		VBox steuerung = new VBox();
		steuerung.getChildren().addAll(segText, segmente, smoothness, tiefe, button);
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
		midpointsX.add(midX);
		midpointsY.add(midY);

		gc.fillRect(midX, midY, 3, 3);
		
		Collections.sort(midpointsX);
		System.out.println("Arraylaenge: " + midpointsX.size());
		for (int i = 0; i < midpointsX.size() - 1; i++){
			System.out.println("midX: " + midpointsX.get(i) + "\t | midY: " + midpointsY.get(i));
		}
		
		if (midpointsX.size() == samples * 2 + 1){
			for (int i = 0; i <= midpointsX.size() - 2; i++){
				gc.strokeLine(midpointsX.get(i), midpointsY.get(i), midpointsX.get(i+1), midpointsY.get(i+1));
				gc.strokeLine(midpointsX.get(i), midpointsY.get(i), midpointsX.get(i+1), midpointsY.get(i+1));
			}
		}
		
		if (xDiff > canvas.getWidth() / samples){
			midpoint(xL, yL, midX, midY, gc, canvas);
			midpoint(midX, midY, xR, yR, gc, canvas);
		}
	}
	
	
}
