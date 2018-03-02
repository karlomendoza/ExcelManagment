package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import entities.FormData;
import utils.Utils;

public class ImportDataProcessor {

	private static final String CREATION_PATH_FOR_FILES = "\\attachments\\";
	private static final String INDEX_FILE_NAME = "indexFile.txt";
	private static final String BREAK_LINE = "\n";

	private static int numberColumnNumber = -1;
	private static String prependTestingText = "";

	public static CellStyle cellStyle;
	public static List<Integer> dates = new ArrayList<>();
	static {
		dates.add(4);
		// dates.add(16);
	}

	@SuppressWarnings("resource")
	public static void processData(FormData formData) throws InvalidFormatException, IOException {

		int splitEachNRows = 0;
		try {
			splitEachNRows = Integer.valueOf(formData.getSplitMetaDataEachRows());
		} catch (Exception e) {
			splitEachNRows = 0;
		}

		if (formData.isForTesting()) {
			if (formData.getPrependString().isEmpty())
				prependTestingText = String.valueOf(System.currentTimeMillis()) + "_";
			else
				prependTestingText = formData.getPrependString();
		}

		try (BufferedWriter indexFile = new BufferedWriter(
				new FileWriter(formData.getResultsDirectoryFile().getAbsolutePath() + "\\" + INDEX_FILE_NAME))) {

			File[] listOfFiles = formData.getMetaDataFiles().listFiles();
			for (File file : listOfFiles) {
				if (file.getName().contains("results") || file.isDirectory() || file.getName().endsWith("txt")) {
					continue;
				}

				try (Workbook wb = Utils.getWorkBook(file)) {
					Sheet readSheet = wb.getSheetAt(0);
					Row row;
					Row headerRow;
					Cell cell;

					// Load HeaderRow
					headerRow = readSheet.getRow(0);

					int rows; // No of rows
					rows = readSheet.getPhysicalNumberOfRows();

					int cols = 0; // No of columns
					int tmp = 0;

					// This trick ensures that we get the data properly even if it doesn't start
					// from first few rows
					for (int i = 0; i < 10 || i < rows; i++) {
						row = readSheet.getRow(i);
						if (row != null) {
							tmp = readSheet.getRow(i).getPhysicalNumberOfCells();
							if (tmp > cols)
								cols = tmp;
						}
					}

					Path resultsPath = Paths.get(formData.getResultsDirectoryFile().getAbsolutePath());
					if (!Files.exists(resultsPath)) {
						Files.createDirectory(resultsPath);
					}
					resultsPath = Paths
							.get(formData.getResultsDirectoryFile().getAbsolutePath() + CREATION_PATH_FOR_FILES);
					if (!Files.exists(resultsPath)) {
						Files.createDirectory(resultsPath);
					}

					Workbook writeBook = Utils.getWorkBook(null);
					cellStyle = writeBook.createCellStyle();
					cellStyle.setDataFormat((short) 14);

					// Map<String, Sheet> writeSheets = new HashMap<>();
					Sheet writeSheet = writeBook.createSheet("new sheet");
					int workBooksCreated = 1;

					int fileNameColumnNumber = -1;
					int fileExtensionColumNumber = -1;
					numberColumnNumber = -1;
					int revisionColumnNumber = -1;
					int descriptionColumnNumber = -1;

					// int rowsCreated = 0;
					for (int r = 0; r < rows; r++) {
						row = readSheet.getRow(r);
						if (row != null) {
							// if it's not the header
							if (r > 0) {

								Boolean passedFileExistance = false;
								String fullFileName = "";
								if (formData.isValidateAttachments()) {

									String fileName = Utils
											.returnCellValueAsString(row.getCell((int) fileNameColumnNumber));
									String fileType = "";
									if (fileExtensionColumNumber >= 0) {
										fileType = Utils
												.returnCellValueAsString(row.getCell((int) fileExtensionColumNumber));
									}

									fullFileName = formatFileName(fileName, fileType);

									if (formData.getRemoveFromPath() > 0) {
										StringJoiner sj = new StringJoiner("\\");
										String[] split = fullFileName.split("\\\\");
										for (int i = formData.getRemoveFromPath(); i < split.length; i++) {
											sj.add(split[i]);
										}
										fullFileName = sj.toString();
									}

									File f = new File(
											formData.getDirectoryWithFile().getAbsolutePath() + "\\" + fullFileName);
									if ((f.exists() && !f.isDirectory())) {

										if (fullFileName.contains("/PDX/") || fullFileName.contains("\\PDX\\")
												|| fullFileName.contains("/pdx/") || fullFileName.contains("\\PDX\\")) {
											System.out.println("This file has been excluded : " + fullFileName);
											continue;
										}

										passedFileExistance = true;

										if (formData.getRemoveFromPath() > 0) {
											Files.createDirectories(
													Paths.get(formData.getResultsDirectoryFile().getAbsolutePath()
															+ CREATION_PATH_FOR_FILES + fullFileName).getParent());
										}
									}
								}
								if (!formData.isValidateAttachments() || passedFileExistance) {

									if (writeSheet.getPhysicalNumberOfRows() == 0) {
										setCellsValuesToRow(writeSheet.createRow((int) 0), headerRow, cols);
									}

									Row createRow = writeSheet.createRow((int) writeSheet.getPhysicalNumberOfRows());
									setCellsValuesToRow(createRow, row, cols);

									if (splitEachNRows != 0 && writeSheet.getPhysicalNumberOfRows() > splitEachNRows) {

										String extension = file.getName();
										String[] split = extension.split("\\.");
										String name = split[0];
										extension = "." + split[split.length - 1];

										try (FileOutputStream outputStream = new FileOutputStream(
												formData.getResultsDirectoryFile().getAbsolutePath() + "\\"
														+ prependTestingText + name + workBooksCreated + extension)) {
											writeBook.write(outputStream);
										}
										writeBook = Utils.getWorkBook(null);
										writeSheet = writeBook.createSheet("new sheet");
										cellStyle = writeBook.createCellStyle();
										cellStyle.setDataFormat((short) 14);
										workBooksCreated++;
									}
									if (formData.isCreateIndexFile()) {
										String TITLEBLOCK_NUMBER = prependTestingText
												+ Utils.returnCellValueAsString(row.getCell((int) numberColumnNumber));

										DataFormatter formatter = new DataFormatter();
										String REVISION = formatter
												.formatCellValue(row.getCell((int) revisionColumnNumber));

										fullFileName = fullFileName.replace("\\", "/");

										String FILEPATH = formData.getPathToFileFromFileVault() + "\\" + fullFileName;
										if (formData.getPathToFileFromFileVault().isEmpty())
											FILEPATH = fullFileName;
										else
											FILEPATH = formData.getPathToFileFromFileVault() + "\\" + fullFileName;

										String IMPORT_TYPE = formData.getImportType();
										String DESCRIPTION = Utils
												.returnCellValueAsString(row.getCell((int) descriptionColumnNumber));
										indexFile.write(formData.getObjecType() + "|" + TITLEBLOCK_NUMBER + "|"
												+ REVISION + "|" + FILEPATH + "|" + IMPORT_TYPE + "|" + DESCRIPTION
												+ BREAK_LINE);
									}
								}
							} else if (r == 0) {
								// get the column number of the fileName and extension that we need
								for (int c = 0; c < cols; c++) {
									cell = row.getCell((int) c);
									if (cell != null) {
										String valueString = Utils.returnCellValueAsString(cell);
										// Set the number of the column
										if (valueString.equals(formData.getFileNameColumn()))
											fileNameColumnNumber = c;
										if (valueString.equals(formData.getFileExtensionColumn()))
											fileExtensionColumNumber = c;
										if (valueString.equals(formData.getNumberColumn()))
											numberColumnNumber = c;
										if (valueString.equals(formData.getRevisionColumn()))
											revisionColumnNumber = c;
										if (valueString.equals(formData.getDescriptionColumn()))
											descriptionColumnNumber = c;
										if (formData.isCreateIndexFile()) {
											if (valueString.equals(formData.getRevisionColumn()))
												revisionColumnNumber = c;
											if (valueString.equals(formData.getDescriptionColumn()))
												descriptionColumnNumber = c;
										}
									}
								}
							}
						}
					}

					String extension = file.getName();
					String[] split = extension.split("\\.");
					String name = split[0];
					extension = "." + split[split.length - 1];

					try (FileOutputStream outputStream = new FileOutputStream(
							formData.getResultsDirectoryFile().getAbsolutePath() + "\\" + prependTestingText + name
									+ workBooksCreated + extension)) {

						writeBook.write(outputStream);
						writeBook.close();
					}
				} catch (Exception ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}

	/**
	 * Returns a full file name given the fileName and the file Extension, file
	 * extension can be empty, can contain a "." as .txt of not
	 * 
	 * @param fileName
	 * @param fileType
	 * @return
	 */
	private static String formatFileName(String fileName, String fileType) {
		if (!fileType.equals("")) {
			if (!fileType.contains(".")) {
				fileType = "." + fileType;
			}
		}
		return fileName + fileType;
	}

	/**
	 * Gets all the cells from dataRow and copys them in writeToRow, basically it
	 * copies the whole row
	 * 
	 * @param writeToRow
	 * @param dataRow
	 * @param colsNumber
	 *            number of columns to copy
	 */
	private static void setCellsValuesToRow(Row writeToRow, Row dataRow, int colsNumber) {
		for (int c = 0; c < colsNumber; c++) {
			Cell cell = dataRow.getCell((int) c);
			if (cell != null) {

				Cell createCell = writeToRow.createCell(c);

				// when testing is activaded prepend the testing Text to the title block number
				// values, but not to the header
				if (numberColumnNumber == c && writeToRow.getRowNum() != 0) {
					String value = Utils.returnCellValueAsString(cell);
					createCell.setCellValue(prependTestingText + value);
				} else {
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_NUMERIC:
						createCell.setCellValue(cell.getNumericCellValue());
						if (dates.contains(c)) {
							createCell.setCellStyle(cellStyle);
						}
						break;
					case Cell.CELL_TYPE_STRING:
						createCell.setCellValue(cell.getStringCellValue());
						break;
					}

				}
			}
		}
	}
}
