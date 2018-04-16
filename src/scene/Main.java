package scene;

import java.io.File;
import java.lang.reflect.Field;

import application.ImportDataProcessor;
import application.ImportDataProcessorSAPDMS;
import entities.FormData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main {

	private File metaDataFile;
	private File directoryWithFile;
	private File resultsDirectoryFile;

	DirectoryChooser metaDatasChooser = new DirectoryChooser();
	DirectoryChooser directoryChooser = new DirectoryChooser();

	private static final String MC = "Master Control";
	private static final String SAPDMS = "SAP DMS";
	private static final String ENOVIA = "ENOVIA";

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

			final Button metadataButton = new Button("Open MetaDatas folder");
			HBox hbBtn = new HBox(10);
			hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			hbBtn.getChildren().add(metadataButton);
			grid.add(hbBtn, 0, 1);

			TextField metaDataPath = new TextField();
			grid.add(metaDataPath, 1, 1);

			metadataButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					metaDataFile = metaDatasChooser.showDialog(primaryStage);
					if (metaDataFile != null) {
						metaDataPath.setText(metaDataFile.getName());
					} else {
						metaDataPath.setText("");
					}
				}
			});

			final Button resultsButton = new Button("Select results folder");
			HBox resultsHbBtn = new HBox(10);
			resultsHbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			resultsHbBtn.getChildren().add(resultsButton);
			grid.add(resultsButton, 0, 2);

			TextField resultsPath = new TextField();
			grid.add(resultsPath, 1, 2);

			resultsButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					resultsDirectoryFile = directoryChooser.showDialog(primaryStage);
					if (resultsDirectoryFile != null) {
						resultsPath.setText(resultsDirectoryFile.getAbsolutePath());
					} else {
						resultsPath.setText("");
					}
				}
			});

			Label splitEach = new Label("Split Metadata file in how many rows:");
			grid.add(splitEach, 0, 3);

			TextField splitMetaDataEachRows = new TextField("500");
			grid.add(splitMetaDataEachRows, 1, 3);

			Tooltip splitMetaDataEachRowsTooltip = new Tooltip("After how many matches of actual documents are we splitting "
					+ "the metadata that will be created, 1,000 recommended for faster import time. 0 for no split");
			Tooltip.install(splitMetaDataEachRows, splitMetaDataEachRowsTooltip);

			CheckBox validateAttachments = new CheckBox("Validate attachments exists?");
			validateAttachments.setSelected(false);
			grid.add(validateAttachments, 0, 4);
			Tooltip t = new Tooltip(
					"Selecting this option will validate that the attachment exists in the defined folder, only rows that contain the attachment will be included in the import file");
			Tooltip.install(validateAttachments, t);

			final Button filesButton = new Button("Open Documents folder");
			HBox filesHbBtn = new HBox(10);
			filesHbBtn.setAlignment(Pos.BOTTOM_RIGHT);
			filesHbBtn.getChildren().add(filesButton);

			TextField documentsPath = new TextField();

			filesButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					directoryWithFile = directoryChooser.showDialog(primaryStage);
					if (directoryWithFile != null) {
						documentsPath.setText(directoryWithFile.getAbsolutePath());
					} else {
						documentsPath.setText("");
					}
				}
			});

			Label fileName = new Label("Column with attachments names or path:");

			TextField fileNameColumn = new TextField("info_card_id");

			Tooltip fileNameColumnTooltip = new Tooltip("On the metadata file, the name of the column that contains the names of the attachments.");
			Tooltip.install(fileNameColumn, fileNameColumnTooltip);

			Label pw = new Label("Column name with attachment Extensions:");

			TextField fileExtensionColumn = new TextField("File Extension");

			Tooltip fileExtensionColumnTooltip = new Tooltip("On the metadata file, the name of the column that contains the extension"
					+ " type of the attachments, empty if the attachments name already contains the extension.");
			Tooltip.install(fileExtensionColumn, fileExtensionColumnTooltip);

			Label removeFromPathLabel = new Label("Remove how many \\ from Path:");
			TextField removeFromPath = new TextField("");

			Tooltip removeFromPathTooltip = new Tooltip(
					"When the path of the file contains more that what we need, you use this field to remove everything before the number of backslashes input");
			Tooltip.install(removeFromPath, removeFromPathTooltip);

			// Add elements for indexFileCreation

			Label objecTypeLabel = new Label("Type of object");
			ObservableList<String> objecTypeOptions = FXCollections.observableArrayList("CHANGE", "DECLARATION", "FILEFOLDER", "ITEM", "MFR", "MFR_PART",
					"COMMODITY", "PRICE", "PSR", "ACTIVITY", "QCR", "RFQ", "RESPONSE", "PROJECT", "SPECIFICATION", "SUBSTANCE", "SUPPLIER");
			final ComboBox<String> objecType = new ComboBox<String>(objecTypeOptions);

			Label numberLabel = new Label("Column name with Title Block Number");
			TextField numberColumn = new TextField("Number");

			Tooltip numberColumnTooltip = new Tooltip(
					"On the metadata file, the name of the column that contains the Title Bock Number value (the id on agile)");
			Tooltip.install(numberColumn, numberColumnTooltip);

			Label revisionLabel = new Label("Column name with Title Block Revision");
			TextField revisionColumn = new TextField();

			Tooltip revisionColumnTooltip = new Tooltip(
					"On the metadata file, the name of the column that contains the Title Bock Revision value (the revision on agile)");
			Tooltip.install(revisionColumn, revisionColumnTooltip);

			Label pathToFileFromFileVaultLabel = new Label("Path to document in the FileVault");
			TextField pathToFileFromFileVault = new TextField();

			Tooltip pathToFileFromFileVaultTooltip = new Tooltip(
					"When the documents are uploaded into the FileVault, put the path where the documents are. E.g. if documents are on <fileVaultPath>/myTest/, then fill in here myTest/ ");
			Tooltip.install(pathToFileFromFileVault, pathToFileFromFileVaultTooltip);

			Label importTypeLabel = new Label("Import Type");
			ObservableList<String> importTypeOptions = FXCollections.observableArrayList("FILE", "INPLACE");
			final ComboBox<String> importType = new ComboBox<String>(importTypeOptions);

			Label descriptionLabel = new Label("Column name with Description");
			TextField descriptionColumn = new TextField();

			Tooltip descriptionColumnTooltip = new Tooltip("On the metadata file, the name of the column that contains the Description of the document value.");
			Tooltip.install(descriptionColumn, descriptionColumnTooltip);

			CheckBox forTesting = new CheckBox("For Testing");
			Tooltip forTestingTooltip = new Tooltip(
					"This will append a random set of numbers to all the Title Block Number references and into the index file if created. This to allow multiple imports.");
			Tooltip.install(forTesting, forTestingTooltip);

			Label prependStringLabel = new Label("Prepend this for testing:");
			TextField prependString = new TextField();
			Tooltip prependStringTooltip = new Tooltip("This will be prepended to the excel files names and title block number column and in the index file");
			Tooltip.install(prependString, prependStringTooltip);

			CheckBox createIndexFile = new CheckBox("Create Index File?");
			createIndexFile.setSelected(false);

			Tooltip createIndexFileToolTip = new Tooltip("Selecting this option will create the index file used for attaching the documents to agile");
			Tooltip.install(createIndexFile, createIndexFileToolTip);

			hackTooltipStartTiming(t);

			createIndexFile.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					if (createIndexFile.isSelected()) {
						grid.add(objecTypeLabel, 0, 10);
						grid.add(objecType, 1, 10);
						if (!forTesting.isSelected()) {
							grid.add(numberLabel, 0, 11);
							grid.add(numberColumn, 1, 11);
						}
						grid.add(revisionLabel, 0, 12);
						grid.add(revisionColumn, 1, 12);
						grid.add(pathToFileFromFileVaultLabel, 0, 13);
						grid.add(pathToFileFromFileVault, 1, 13);
						grid.add(importTypeLabel, 0, 14);
						grid.add(importType, 1, 14);
						grid.add(descriptionLabel, 0, 15);
						grid.add(descriptionColumn, 1, 15);

					} else {
						grid.getChildren().remove(objecTypeLabel);
						grid.getChildren().remove(objecType);
						if (!forTesting.isSelected()) {
							grid.getChildren().remove(numberLabel);
							grid.getChildren().remove(numberColumn);
						}
						grid.getChildren().remove(revisionLabel);
						grid.getChildren().remove(revisionColumn);
						grid.getChildren().remove(pathToFileFromFileVaultLabel);
						grid.getChildren().remove(pathToFileFromFileVault);
						grid.getChildren().remove(importTypeLabel);
						grid.getChildren().remove(importType);
						grid.getChildren().remove(descriptionLabel);
						grid.getChildren().remove(descriptionColumn);
					}
				}
			});

			validateAttachments.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					if (validateAttachments.isSelected()) {
						grid.add(filesHbBtn, 0, 5);
						grid.add(documentsPath, 1, 5);
						grid.add(fileName, 0, 6);
						grid.add(fileNameColumn, 1, 6);
						grid.add(pw, 0, 7);
						grid.add(fileExtensionColumn, 1, 7);

						grid.add(removeFromPathLabel, 0, 8);
						grid.add(removeFromPath, 1, 8);

						grid.add(createIndexFile, 0, 9);

					} else {
						grid.getChildren().remove(filesHbBtn);
						grid.getChildren().remove(documentsPath);
						grid.getChildren().remove(fileName);
						grid.getChildren().remove(fileNameColumn);
						grid.getChildren().remove(pw);
						grid.getChildren().remove(fileExtensionColumn);
						grid.getChildren().remove(createIndexFile);
						grid.getChildren().remove(removeFromPathLabel);
						grid.getChildren().remove(removeFromPath);

						if (!forTesting.isSelected()) {
							grid.getChildren().remove(numberLabel);
							grid.getChildren().remove(numberColumn);
						}

						if (createIndexFile.isSelected()) {
							grid.getChildren().remove(objecTypeLabel);
							grid.getChildren().remove(objecType);
							grid.getChildren().remove(revisionLabel);
							grid.getChildren().remove(revisionColumn);
							grid.getChildren().remove(pathToFileFromFileVaultLabel);
							grid.getChildren().remove(pathToFileFromFileVault);
							grid.getChildren().remove(importTypeLabel);
							grid.getChildren().remove(importType);
							grid.getChildren().remove(descriptionLabel);
							grid.getChildren().remove(descriptionColumn);
						}
						createIndexFile.setSelected(false);
					}
				}
			});

			grid.add(forTesting, 0, 17);
			forTesting.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					if (forTesting.isSelected() && !createIndexFile.isSelected()) {
						grid.add(numberLabel, 0, 10);
						grid.add(numberColumn, 1, 10);
					} else if (!forTesting.isSelected() && !createIndexFile.isSelected()) {
						grid.getChildren().remove(numberLabel);
						grid.getChildren().remove(numberColumn);
					}
					if (forTesting.isSelected()) {
						grid.add(prependStringLabel, 1, 17);
						grid.add(prependString, 2, 17);
					} else {
						grid.getChildren().remove(prependStringLabel);
						grid.getChildren().remove(prependString);
					}
				}
			});

			Label dataStreamLabel = new Label("Data Source");
			ObservableList<String> dataStreamOptions = FXCollections.observableArrayList(MC, SAPDMS, ENOVIA);
			final ComboBox<String> dataStream = new ComboBox<String>(dataStreamOptions);
			grid.add(dataStreamLabel, 0, 18);
			grid.add(dataStream, 1, 18);

			Label workstationNameLabel = new Label("Workstation Application Column name");
			TextField workstation = new TextField("Workstation Application");
			Tooltip workstationTooltip = new Tooltip("Column for SAP DMS where you can tell the type of the file PDX, WRD, LOG, DCR");
			Tooltip.install(workstation, workstationTooltip);
			grid.add(workstationNameLabel, 0, 19);
			grid.add(workstation, 1, 19);

			// HERE STARTS THE SECOND COLUMN

			Label agileUserNameLabel = new Label("User name");
			TextField agileUserName = new TextField("kmendoza");

			Label agilePasswordLabel = new Label("Password");
			PasswordField agilePassword = new PasswordField();

			Label agileUrlLabel = new Label("Server Url");
			TextField agileUrl = new TextField("http://a934dev1.kalypsocloud.com:7001/Agile");

			Label agileWorkFlowNameLabel = new Label("Workflow name for CO");
			TextField agileWorkFlowName = new TextField("Pack/Label Change Request");

			CheckBox createChangeOrders = new CheckBox("Create Change Order");
			createChangeOrders.setSelected(false);
			grid.add(createChangeOrders, 2, 8);
			createChangeOrders.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(final ActionEvent e) {
					if (createChangeOrders.isSelected()) {
						grid.add(agileUserNameLabel, 2, 9);
						grid.add(agileUserName, 3, 9);
						grid.add(agilePasswordLabel, 2, 10);
						grid.add(agilePassword, 3, 10);
						grid.add(agileUrlLabel, 2, 11);
						grid.add(agileUrl, 3, 11);
						grid.add(agileWorkFlowNameLabel, 2, 12);
						grid.add(agileWorkFlowName, 3, 12);
					} else {
						grid.getChildren().remove(agileUserNameLabel);
						grid.getChildren().remove(agileUserName);
						grid.getChildren().remove(agilePasswordLabel);
						grid.getChildren().remove(agilePassword);
						grid.getChildren().remove(agileUrlLabel);
						grid.getChildren().remove(agileUrl);
						grid.getChildren().remove(agileWorkFlowNameLabel);
						grid.getChildren().remove(agileWorkFlowName);

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
					if (forTesting.isSelected() && numberColumn.getText().equals("")) {
						displayMessage(AlertType.ERROR, "When \"For testing\" option is selected you need to select a \"Column name with Title Block Number\"");
						return;
					}

					ProgressIndicator pi = new ProgressIndicator();
					VBox box = new VBox(pi);
					box.setAlignment(Pos.CENTER);
					// Grey Background
					grid.setDisable(true);
					root.getChildren().add(box);

					FormData formData = new FormData(metaDataFile, directoryWithFile, fileExtensionColumn.getText(), fileNameColumn.getText(),
							splitMetaDataEachRows.getText(), objecType.getValue(), numberColumn.getText(), revisionColumn.getText(),
							pathToFileFromFileVault.getText(), importType.getValue(), descriptionColumn.getText(), createIndexFile.isSelected(),
							agileUserName.getText(), agilePassword.getText(), agileUrl.getText(), agileWorkFlowName.getText(), createChangeOrders.isSelected(),
							forTesting.isSelected(), resultsDirectoryFile, null, validateAttachments.isSelected(), prependString.getText(),
							removeFromPath.getText(), workstation.getText());

					if (dataStream.getValue().equals(SAPDMS)) {
						try {
							ImportDataProcessorSAPDMS.processData(formData);
							displayMessage(AlertType.INFORMATION, "Run succesfully");
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							displayMessage(AlertType.INFORMATION, "Run failed");
						}
					} else {
						try {
							ImportDataProcessor.processData(formData);
							displayMessage(AlertType.INFORMATION, "Run succesfully");
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							displayMessage(AlertType.INFORMATION, "Run failed");
						}
					}

					grid.setDisable(false);
					root.getChildren().remove(box);
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
