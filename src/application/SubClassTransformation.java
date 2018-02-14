package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import TransformationRules.SapDMSSubclassTransformationRules;
import entities.SubClassTransformationData;
import utils.Utils;

public class SubClassTransformation {

	public static void main(String... strings) throws InvalidFormatException, IOException {
		File metaDataFiles = new File(
				"C:\\Users\\Karlo Mendoza\\Excel Work\\ICU MEDICAL\\SAP DMS\\All sap dms Records");
		String documentNumber = "Document Number";
		String documentType = "Document Type";
		String documentDescription = "Document Description";
		File transformationFile = new File(
				"C:\\Users\\Karlo Mendoza\\Excel Work\\ICU MEDICAL\\SAP DMS\\transformationRules.xlsx");
		String sheetName = "Sheet1";
		String charForSplit = "\\|";

		SubClassTransformationData formData = new SubClassTransformationData(metaDataFiles, documentNumber,
				documentType, documentDescription, transformationFile, sheetName, charForSplit);
		processData(formData);
	}

	public static Map<String, Map<String, String>> loadListData(File transformationFile, String sheetName)
			throws IOException, InvalidFormatException {
		Map<String, Map<String, String>> transformationData = new HashMap<>();
		try (Workbook listDataWorkbook = Utils.getWorkBook(transformationFile)) {
			Sheet dataListSheet = listDataWorkbook.getSheet(sheetName);
			Row dataListRow;
			int dataListCols = 0;
			int dataListTmp = 0;
			int numberOfRows = dataListSheet.getPhysicalNumberOfRows();

			for (int i = 0; i < 10 || i < numberOfRows; i++) {
				dataListRow = dataListSheet.getRow(i);
				if (dataListRow != null) {
					dataListTmp = dataListSheet.getRow(i).getPhysicalNumberOfCells();
					if (dataListTmp > dataListCols)
						dataListCols = dataListTmp;
				}
			}

			List<String> header = new ArrayList<>();

			for (int r = 0; r < numberOfRows; r++) {
				dataListRow = dataListSheet.getRow(r);
				if (dataListRow != null) {
					for (int c = 0; c < dataListCols; c += 2) {
						Cell cell = dataListRow.getCell((int) c);
						Cell cell2 = dataListRow.getCell((int) c + 1);
						if (cell != null && cell2 != null) {
							String valueString = Utils.returnCellValueAsString(cell);
							String valueString2 = Utils.returnCellValueAsString(cell2);
							if (r > 0) {
								if (!valueString.equals("")) {
									Map<String, String> map = transformationData.get(header.get(c / 2));
									map.put(valueString, valueString2);
								}
							} else {
								header.add(valueString);
								transformationData.put(valueString, new HashMap<String, String>());
							}
						}
					}
				}
			}
		}
		// return null;
		return transformationData;
	}

	public static void processData(SubClassTransformationData formData) throws InvalidFormatException, IOException {

		Map<String, Map<String, String>> listData = null;
		Map<Integer, String> columnsToCheck = null;
		if (formData.getTransformationFile() != null && formData.getTransformationFile().exists()) {
			listData = loadListData(formData.getTransformationFile(), formData.getSheetName());
			columnsToCheck = new HashMap<>();
		}

		File[] listOfFiles = formData.getMetaDataFiles().listFiles();

		try (SXSSFWorkbook writeIntoBook = new SXSSFWorkbook(100);) {
			Sheet writeSheet = writeIntoBook.createSheet("data");

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

					int rows = readSheet.getPhysicalNumberOfRows(); // No of rows
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

					int subClassColumnToTransfor = -1;
					int descriptionColumnNumber = -1;

					if (writeSheet.getPhysicalNumberOfRows() == 0) {
						Row createRow = writeSheet.createRow(0);
						Cell subClassHeader = createRow.createCell(0);
						subClassHeader.setCellValue("SubClass");
						setCellsValuesToRow(createRow, headerRow, cols);
					}

					for (int r = 0; r < rows; r++) {
						row = readSheet.getRow(r);
						if (row != null) {
							// if it's not the header
							if (r > 0) {

								String description = Utils
										.returnCellValueAsString(row.getCell((int) descriptionColumnNumber));

								if (description == null || description.equals("")) {
									continue;
								}

								String subClassColumnValueToTransform = Utils
										.returnCellValueAsString(row.getCell((int) subClassColumnToTransfor));

								String transformTo = "";
								transformTo = SapDMSSubclassTransformationRules.subClassTransformation(subClassColumnValueToTransform, "");

								Row writeToRow = writeSheet.createRow(writeSheet.getPhysicalNumberOfRows());

								Cell createCell = writeToRow.createCell(0);
								createCell.setCellValue(transformTo);
								setCellsValuesToRow(writeToRow, row, cols);

								// one on one transformations
								if (formData.getTransformationFile() != null
										&& formData.getTransformationFile().exists()) {
									for (int c = 0; c < cols; c++) {
										if (columnsToCheck.containsKey(c)) {
											cell = row.getCell((int) c);
											if (cell != null) {
												String valueString = Utils.returnCellValueAsString(cell);

												String[] split = valueString.split(formData.getCharForSplit());
												StringJoiner sj1 = new StringJoiner(";");
												StringJoiner sj2 = new StringJoiner(";");

												int cellMatchFoundIn = 0;
												for (int i = 0; i < split.length; i++) {
													String match = listData.get(columnsToCheck.get(c)).get(split[i]);

													for (String key : listData.keySet()) {
														if (key.equals("Distribution code discription"))
															continue;
														if (listData.get(key).containsKey(match)) {

															cellMatchFoundIn = Integer.valueOf(
																	key.substring(key.length() - 1, key.length()));

															if (cellMatchFoundIn == 1) {
																if (!sj1.toString()
																		.contains(listData.get(key).get(match))) {
																	sj1.add(listData.get(key).get(match));
																}
															}
															if (cellMatchFoundIn == 2) {
																if (!sj2.toString()
																		.contains(listData.get(key).get(match))) {
																	sj2.add(listData.get(key).get(match));
																}
															}
														}
													}
												}
												if (!sj1.toString().isEmpty()) {
													Cell writeCell = writeToRow.createCell(cols + 1);
													writeCell.setCellValue(sj1.toString());
												}
												if (!sj2.toString().isEmpty()) {
													Cell writeCell = writeToRow.createCell(cols + 2);
													writeCell.setCellValue(sj2.toString());
												}

												if (sj1.toString().isEmpty() && sj2.toString().isEmpty()) {
													Cell writeCell = writeToRow.createCell(1);
													writeCell.setCellValue("");
												}
											}
										}
									}
								}
							} else if (r == 0) {
								// get the column number of the subClass
								for (int c = 0; c < cols; c++) {
									cell = row.getCell((int) c);
									if (cell != null) {
										String valueString = Utils.returnCellValueAsString(cell);
										// Set the number of the column
										if (valueString.equals(formData.getNumberColumn())) {
											subClassColumnToTransfor = c;
										}
										if (valueString.equals(formData.getDescriptionColumn())) {
											descriptionColumnNumber = c;
										}

										// one on one transformation stuff
										if (formData.getTransformationFile() != null
												&& formData.getTransformationFile().exists()) {
											if (listData.containsKey(valueString)) {
												columnsToCheck.put(c, valueString);
											}
										}
									}
								}
							}
						}
					}

				}
			}
			File f = new File(formData.getMetaDataFiles().getParentFile() + "\\results.xlsx");
			try (FileOutputStream outputStream = new FileOutputStream(f)) {
				writeIntoBook.write(outputStream);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Gets all the cells from dataRow and copys them in writeToRow, basically it
	 * copies the whole row, but skips the first one to allow to put the subClass
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
				Cell createCell = writeToRow.createCell(c + 1);

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_NUMERIC:
					createCell.setCellValue(cell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_STRING:
					createCell.setCellValue(cell.getStringCellValue());
				}
			}
		}
	}
}
