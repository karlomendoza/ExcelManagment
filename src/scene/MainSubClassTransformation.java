package scene;

import java.io.File;
import java.lang.reflect.Field;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainSubClassTransformation {

	private File metaDataFiles;
	private File transformationFile;

	DirectoryChooser fileChooser = new DirectoryChooser();
	FileChooser resultsChooser = new FileChooser();
	FileChooser transformationChooser = new FileChooser();

	public StackPane load(Stage primaryStage) {
		try {

			StackPane root = new StackPane();

			GridPane grid = new GridPane();
			grid.setAlignment(Pos.CENTER);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(50, 50, 50, 50));

			Text scenetitle = new Text("Please load all fields");
			scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
			grid.add(scenetitle, 0, 0, 2, 1);

			final Button metadataButton = new Button("Open MetaData files Directory");
			HBox hbBtn = new HBox(10);
			hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			hbBtn.getChildren().add(metadataButton);
			grid.add(hbBtn, 0, 1);

			TextField metaDataPath = new TextField();
			grid.add(metaDataPath, 1, 1);

			metadataButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					metaDataFiles = fileChooser.showDialog(primaryStage);
					if (metaDataFiles != null) {
						metaDataPath.setText(metaDataFiles.getName());
					} else {
						metaDataPath.setText("");
					}
				}
			});

			Label docNumberColumnLabel = new Label("Column name with Document Number");
			TextField docNumberColumn = new TextField("Document Number");
			grid.add(docNumberColumnLabel, 0, 3);
			grid.add(docNumberColumn, 1, 3);
			Tooltip docNumberColumnTooltip = new Tooltip("Column that's going to get the transformation applied to");
			Tooltip.install(docNumberColumn, docNumberColumnTooltip);

			Label docTypeColumnLabel = new Label("Column name with Document Type");
			TextField docTypeColumn = new TextField("Document Type");
			grid.add(docTypeColumnLabel, 0, 4);
			grid.add(docTypeColumn, 1, 4);

			Label descriptionColumnLabel = new Label("Column name with Document description");
			TextField descriptionColumn = new TextField("Document Description");
			grid.add(descriptionColumnLabel, 0, 5);
			grid.add(descriptionColumn, 1, 5);
			Tooltip descriptionColumnTooltip = new Tooltip("Column that's going to decide if the row gets added to the results file or not");
			Tooltip.install(descriptionColumn, descriptionColumnTooltip);

			final Button transformationButton = new Button("Choose Transformations file");
			HBox trBtn = new HBox(10);
			trBtn.setAlignment(Pos.BOTTOM_RIGHT);
			trBtn.getChildren().add(transformationButton);
			grid.add(trBtn, 0, 6);

			TextField transformationPath = new TextField();
			grid.add(transformationPath, 1, 6);
			transformationButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					configureFileChooser(transformationChooser);
					transformationFile = transformationChooser.showOpenDialog(primaryStage);
					if (transformationFile != null) {
						transformationPath.setText(transformationFile.getName());
					} else {
						transformationPath.setText("");
					}
				}
			});

			final Button processButton = new Button("Process");
			HBox processHbBtn = new HBox(10);
			processHbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			processHbBtn.getChildren().add(processButton);
			grid.add(processHbBtn, 3, 16);
			processButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					if (metaDataFiles == null) {
						// TODO send error message when it all breaks
					}

					// try {
					// SubClassTransformation.processData(metaDataFiles, transformationFile, docNumberColumn.getText(),
					// docTypeColumn.getText(), descriptionColumn.getText(), "\\|");
					// displayMessage(AlertType.INFORMATION, "Run succesfully");
					// } catch (Exception e1) {
					// displayMessage(AlertType.INFORMATION, "Run Failed");
					// e1.printStackTrace();
					// }

				}
			});

			root.getChildren().add(grid);
			return root;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void displayMessage(AlertType severity, String message) {
		Alert alert = new Alert(severity);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("Open Files");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.xlsx", "*.xls"));
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
