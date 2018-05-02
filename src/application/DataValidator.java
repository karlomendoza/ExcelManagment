package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import entities.ValidateData;
import utils.Utils;

public class DataValidator {

	private static XSSFCellStyle style;

	public static Map<String, List<String>> loadListData(File fileList, String sheetName) throws IOException {
		Map<String, List<String>> listData = new HashMap<>();
		try (XSSFWorkbook listDataWorkbook = new XSSFWorkbook(fileList.getAbsolutePath())) {
			Sheet dataListSheet = listDataWorkbook.getSheet(sheetName);
			Row dataListRow;
			int dataListCols = 0;
			int dataListTmp = 0;
			int numberOfRows = dataListSheet.getPhysicalNumberOfRows();

			List<String> headers = new ArrayList<>();

			for (int i = 0; i < 10 || i < numberOfRows; i++) {
				dataListRow = dataListSheet.getRow(i);
				if (dataListRow != null) {
					dataListTmp = dataListSheet.getRow(i).getPhysicalNumberOfCells();
					if (dataListTmp > dataListCols)
						dataListCols = dataListTmp;
				}
			}

			for (int r = 0; r < numberOfRows; r++) {
				dataListRow = dataListSheet.getRow(r);
				if (dataListRow != null) {
					for (int c = 0; c < dataListCols; c++) {
						Cell cell = dataListRow.getCell((int) c);
						if (cell != null) {
							String valueString = Utils.returnCellValueAsString(cell);
							if (r == 0) {
								if (valueString.equals("")) {
									continue;
									// valueString = headers.get(c - 1);
									// headers.set(c - 1, valueString + " Level 1");
									// listData.remove(valueString);
									// listData.put(valueString + " Level 1", new ArrayList<>());
									//
									// valueString = valueString + " Level 2";
								}
								headers.add(valueString);
								listData.put(valueString, new ArrayList<>());
							} else {
								if (headers.size() > c && !valueString.isEmpty() && !valueString.equals("Level 1") && !valueString.equals("Level 2"))
									listData.get(headers.get(c)).add(valueString);
							}
						}
					}
				}
			}
		}
		return listData;
	}

	@SuppressWarnings("resource")
	public static void processData(ValidateData formData) throws InvalidFormatException, IOException {

		Map<String, List<String>> listData = loadListData(formData.getFileWithListValues(), formData.getSheetName());
		Map<Integer, String> columnsToCheck = new HashMap<>();

		SXSSFWorkbook writeBook = new SXSSFWorkbook(100);

		style = (XSSFCellStyle) writeBook.createCellStyle();
		style.setFillForegroundColor((IndexedColors.RED.getIndex()));
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		Sheet writeSheet = writeBook.createSheet("new sheet");
		int rowsCreated = 0;

		File[] listOfFiles = formData.getMetaDataFile().listFiles();

		for (File file : listOfFiles) {
			if (file.getName().contains("results") || file.isDirectory() || file.getName().endsWith("txt")) {
				continue;
			}

			try (XSSFWorkbook wb = new XSSFWorkbook(file)) {
				Sheet sheet = wb.getSheetAt(0);
				Row row;
				Cell cell;

				int rows; // No of rows
				rows = sheet.getPhysicalNumberOfRows();
				int cols = 0; // No of columns
				int tmp = 0;
				// This trick ensures that we get the data properly even if it doesn't start
				// from first few rows
				for (int i = 0; i < 10 || i < rows; i++) {
					row = sheet.getRow(i);
					if (row != null) {
						tmp = sheet.getRow(i).getPhysicalNumberOfCells();
						if (tmp > cols)
							cols = tmp;
					}
				}

				for (int r = 0; r < rows; r++) {
					row = sheet.getRow(r);
					if (row != null) {
						// if it's not the header
						if (r > 0) {

							List<Integer> columnsWithErrorsFound = new ArrayList<>();
							for (Integer column : columnsToCheck.keySet()) {
								cell = row.getCell((int) column);
								if (cell != null) {
									String valueString = Utils.returnCellValueAsString(cell);
									if (valueString.contains("Empty Containers")) {
										int a = 0;
										a++;
									}
									if (!listData.get(columnsToCheck.get(column)).contains(valueString.trim())) {
										columnsWithErrorsFound.add(column);
									}
								}
							}
							if (!columnsWithErrorsFound.isEmpty()) {
								Row createRow = writeSheet.createRow((int) rowsCreated);
								row = sheet.getRow(r);
								setCellsValuesToRow(createRow, row, cols, columnsWithErrorsFound);
								rowsCreated++;
							}
						} else if (r == 0) {
							Row createRow = writeSheet.createRow((int) rowsCreated);
							rowsCreated++;
							setCellsValuesToRow(createRow, row, cols, null);
							// get the column number of the fileName and extension
							for (int c = 0; c < cols; c++) {
								cell = row.getCell((int) c);
								if (cell != null) {
									String valueString = Utils.returnCellValueAsString(cell);
									if (listData.containsKey(valueString)) {
										columnsToCheck.put(c, valueString);
									}
								}

							}
						}
					}
				}
			} catch (Exception ioe) {
				ioe.printStackTrace();
			}

		}
		try (FileOutputStream outputStream = new FileOutputStream(formData.getMetaDataFile().getParentFile() + "\\errors.xlsx")) {
			writeBook.write(outputStream);
		}
	}

	/**
	 * Gets all the cells from dataRow and copys them in writeToRow, basically it copies the whole row
	 * 
	 * @param writeToRow
	 * @param dataRow
	 * @param colsNumber
	 *            number of columns to copy
	 */
	private static void setCellsValuesToRow(Row writeToRow, Row dataRow, int colsNumber, List<Integer> columnsWithErrorsFound) {
		for (int c = 0; c < colsNumber; c++) {
			Cell cell = dataRow.getCell((int) c);
			if (cell != null) {
				Cell createCell = writeToRow.createCell(c);
				if (columnsWithErrorsFound != null && columnsWithErrorsFound.contains(c)) {
					createCell.setCellStyle(style);
				}
				createCell.setCellValue(Utils.returnCellValueAsString(cell));
			}
		}
	}

}
