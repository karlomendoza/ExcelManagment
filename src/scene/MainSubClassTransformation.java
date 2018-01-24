package scene;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import com.agile.api.APIException;

import application.SubClassTransformation;
import entities.FormData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainSubClassTransformation extends Application {

	private File metaDataFile;
	private File resultsFile;

	FileChooser fileChooser = new FileChooser();
	FileChooser resultsChooser = new FileChooser();

	@Override
	public void start(Stage primaryStage) {
		try {

			StackPane root = new StackPane();

			primaryStage.setTitle("Kalypso Agile Rapid Loader Optimizer");

			GridPane grid = new GridPane();
			grid.setAlignment(Pos.CENTER);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(50, 50, 50, 50));

			Text scenetitle = new Text("Please load all fields");
			scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
			grid.add(scenetitle, 0, 0, 2, 1);

			final Button metadataButton = new Button("Open MetaData file");
			HBox hbBtn = new HBox(10);
			hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			hbBtn.getChildren().add(metadataButton);
			grid.add(hbBtn, 0, 1);

			TextField metaDataPath = new TextField();
			grid.add(metaDataPath, 1, 1);

			metadataButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					configureFileChooser(fileChooser);
					metaDataFile = fileChooser.showOpenDialog(primaryStage);
					if (metaDataFile != null) {
						metaDataPath.setText(metaDataFile.getName());
					} else {
						metaDataPath.setText("");
					}
				}
			});

			final Button resultsButton = new Button("Choose Results file");
			HBox rtBtn = new HBox(10);
			rtBtn.setAlignment(Pos.BOTTOM_RIGHT);
			rtBtn.getChildren().add(resultsButton);
			grid.add(rtBtn, 0, 2);

			TextField resultsPath = new TextField();
			grid.add(resultsPath, 1, 2);

			resultsButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					configureFileChooser(resultsChooser);
					resultsFile = resultsChooser.showOpenDialog(primaryStage);
					if (resultsFile != null) {
						resultsPath.setText(resultsFile.getName());
					} else {
						resultsPath.setText("");
					}
				}
			});

			Label subClassColumnLabel = new Label("Column name with Document Number");
			TextField subClassColumn = new TextField("Document Number");
			grid.add(subClassColumnLabel, 0, 3);
			grid.add(subClassColumn, 1, 3);
			Tooltip subClassColumnTooltip = new Tooltip("Column that's going to get the transformation applied to");
			Tooltip.install(subClassColumn, subClassColumnTooltip);

			Label descriptionColumnLabel = new Label("Column name with Document description");
			TextField descriptionColumn = new TextField("Document Description");
			grid.add(descriptionColumnLabel, 0, 4);
			grid.add(descriptionColumn, 1, 4);
			Tooltip descriptionColumnTooltip = new Tooltip(
					"Column that's going to decide if the row gets added to the results file or not");
			Tooltip.install(descriptionColumn, descriptionColumnTooltip);

			final Button processButton = new Button("Process");
			HBox processHbBtn = new HBox(10);
			processHbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			processHbBtn.getChildren().add(processButton);
			grid.add(processHbBtn, 3, 16);
			processButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					if (metaDataFile == null) {
						// TODO send error message when it all breaks
					}

					try {
						FormData formData = new FormData(metaDataFile, resultsFile, subClassColumn.getText(),
								descriptionColumn.getText());

						try {
							SubClassTransformation.processData(formData);
						} catch (APIException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						displayMessage(AlertType.INFORMATION, "Run succesfully");

					} catch (InvalidFormatException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});

			root.getChildren().add(grid);
			Scene scene = new Scene(root, 800, 700);
			primaryStage.setScene(scene);

			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void displayMessage(AlertType severity, String message) {
		Alert alert = new Alert(severity);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("Open Files");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters()
				.addAll(new FileChooser.ExtensionFilter("All Files", "*.xlsx", "*.xls", "*.csv"));
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void hackTooltipStartTiming(Tooltip tooltip) {
		try {
			Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
			fieldBehavior.setAccessible(true);
			Object objBehavior = fieldBehavior.get(tooltip);

			Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
			fieldTimer.setAccessible(true);
			Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

			objTimer.getKeyFrames().clear();
			objTimer.getKeyFrames().add(new KeyFrame(new Duration(250)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
