package scene;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 */
public class MainTabs extends Application {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Kalypso Agile Rapid Loader Optimizer");
		Group root = new Group();
		Scene scene = new Scene(root, 900, 800, Color.WHITE);
		TabPane tabPane = new TabPane();
		BorderPane mainPane = new BorderPane();

		// Create Tabs
		Tab tabA = new Tab();
		tabA.setText("SubClass Transformation");
		// Add something in Tab

		MainSubClassTransformation mainSCT = new MainSubClassTransformation();
		StackPane mainSubClassTransformationPane = mainSCT.load(primaryStage);
		tabA.setContent(mainSubClassTransformationPane);

		tabPane.getTabs().add(tabA);

		Tab tabB = new Tab();
		tabB.setText("Split Tool");
		// Add something in Tab
		MainSubClassSeparator mainSCS = new MainSubClassSeparator();
		StackPane mainSubClassSeparator = mainSCS.load(primaryStage);
		tabB.setContent(mainSubClassSeparator);
		tabPane.getTabs().add(tabB);

		// Tab tabC = new Tab();
		// tabC.setText("Validation Tool");
		// MainSubClassValidation mainV = new MainSubClassValidation();
		// StackPane mainSubClassValidation = mainV.load(primaryStage);
		// tabC.setContent(mainSubClassValidation);
		// tabPane.getTabs().add(tabC);

		Tab tabD = new Tab();
		tabD.setText("Batch tool");
		// Add something in Tab
		Main main = new Main();
		StackPane pane = main.load(primaryStage);
		tabD.setContent(pane);
		tabPane.getTabs().add(tabD);

		mainPane.setCenter(tabPane);

		mainPane.prefHeightProperty().bind(scene.heightProperty());
		mainPane.prefWidthProperty().bind(scene.widthProperty());

		root.getChildren().add(mainPane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}