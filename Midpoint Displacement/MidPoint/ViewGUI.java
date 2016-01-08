package MidPoint;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewGUI extends Application{
	
	public void start(Stage primaryStage) {
		MidpointDisplacement gui = new MidpointDisplacement();
		Scene scene = new Scene(gui, 700, 800);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Midpoint Displacement");
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String [] args){
		Application.launch(args);
	}
}