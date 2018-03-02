package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import utils.Utils;

public class OneOnOneTransformations {

	public static void main(String... strings) throws InvalidFormatException, IOException {
		File metaDataFiles = new File(
				"C:\\Users\\Karlo Mendoza\\Excel Work\\ICU MEDICAL\\SAP DMS\\T1\\T1_upload_files\\UPLOAD");

		File transformationFile = new File(
				"C:\\Users\\Karlo Mendoza\\Excel Work\\ICU MEDICAL\\SAP DMS\\TransformationForErrors.xlsx");

		processData(metaDataFiles, transformationFile);
	}

	public static Map<String, Map<String, String>> loadListData(File transformationFile)
			throws IOException, InvalidFormatException {
		Map<String, Map<String, String>> transformationData = new HashMap<>();
		try (Workbook listDataWorkbook = Utils.getWorkBook(transformationFile)) {
			Sheet dataListSheet = listDataWorkbook.getSheetAt(0);
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

	public static void processData(File metaDataFiles, File transformationFile)
			throws InvalidFormatException, IOException {

		Map<String, Map<String, String>> listData = null;
		Map<Integer, String> columnsToCheck = null;
		if (transformationFile != null && transformationFile.exists()) {
			listData = loadListData(transformationFile);
			columnsToCheck = new HashMap<>();
		}

		File[] listOfFiles = metaDataFiles.listFiles();

		for (File file : listOfFiles) {
			if (file.getName().contains("results") || file.isDirectory() || file.getName().endsWith("txt")) {
				continue;
			}

			FileInputStream fsIP = new FileInputStream(file);
			try (Workbook wb = new XSSFWorkbook(fsIP)) {
				Sheet readSheet = wb.getSheetAt(0);
				Row row;
				Cell cell;

				// Load HeaderRow

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

				for (int r = 0; r < rows; r++) {
					row = readSheet.getRow(r);
					if (row != null) {
						// if it's not the header
						if (r > 0) {
							// one on one transformations
							for (int c = 0; c < cols; c++) {
								if (columnsToCheck.containsKey(c)) {
									cell = row.getCell((int) c);
									if (cell != null) {
										String valueString = Utils.returnCellValueAsString(cell);

										if (listData.get(columnsToCheck.get(c)).containsKey(valueString)) {
											cell.setCellValue(listData.get(columnsToCheck.get(c)).get(valueString));
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
									// one on one transformation stuff
									if (listData.containsKey(valueString)) {
										columnsToCheck.put(c, valueString);
									}
								}
							}
						}
					}
				}
				fsIP.close();
				try (FileOutputStream outputStream = new FileOutputStream(file)) {
					wb.write(outputStream);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}
	}
}
