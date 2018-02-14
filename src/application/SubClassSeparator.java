package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import entities.SplitData;
import utils.Utils;

public class SubClassSeparator {

	public static void main(String... strings) throws InvalidFormatException, IOException {

		File metaDataFile = new File(
				"C:\\Users\\Karlo Mendoza\\Excel Work\\ICU MEDICAL\\SAP DMS\\T1\\1SAPDMS_transformed_onlyData_part1 - Copy.xlsx");
		String columnToSplitFor = "SubClass";
		String columnToSplitFor2 = "";

		SplitData formData = new SplitData(metaDataFile, columnToSplitFor, columnToSplitFor2);
		processData(formData);
	}

	public static void processData(SplitData formData) throws InvalidFormatException, IOException {

		try (Workbook wb = Utils.getWorkBook(formData.getMetaDataFile())) {
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

			int subClassColumnNumber = -1;
			int subClassColumnNumber2 = -1;
			String lastSubClassprocessed = "";
			String lastOtherProcessed = "";
			Workbook writeBook = null;
			Sheet writeSheet = null;
			File f = null;

			int i = 1;
			for (int r = 0; r < rows; r++) {
				row = readSheet.getRow(r);
				if (row != null) {
					// if it's not the header
					if (r > 0) {
						String subClass = Utils.returnCellValueAsString(row.getCell((int) subClassColumnNumber));
						if (subClass.equals(""))
							subClass = "NoSubClass";

						subClass = cleanInput(subClass);

						// String other = Utils.returnCellValueAsString(row.getCell((int)
						// subClassColumnNumber2));
						String other = "";

						if (!lastSubClassprocessed.equals(subClass) || !lastOtherProcessed.equals(other)) {
							if (!lastSubClassprocessed.equals(subClass))
								i = 1;
							if (!lastSubClassprocessed.equals(""))
								saveExcel(writeBook, f);

							lastSubClassprocessed = subClass;
							lastOtherProcessed = other;
							f = new File(formData.getMetaDataFile().getParentFile() + "\\" + subClass + ".xlsx");
							// f = new File(formData.getMetaDataFile().getParentFile() + "\\" + subClass + i
							// + ".xlsx");
							i++;
							if (f.exists()) {
								writeBook = Utils.getWorkBook(f);
								writeSheet = writeBook.getSheet("data");
							} else {
								writeBook = Utils.getWorkBook(null);
								writeSheet = writeBook.createSheet("data");
							}
							Row createRow = writeSheet.createRow((int) 0);
							Utils.setCellsValuesToRow(createRow, headerRow, cols);
						}

						Row createRow2 = writeSheet.createRow((int) writeSheet.getPhysicalNumberOfRows());
						// TODO regresar al utils
						Utils.setCellsValuesToRow(createRow2, row, cols);

					} else if (r == 0) {
						// get the column number of the subClass
						for (int c = 0; c < cols; c++) {
							cell = row.getCell((int) c);
							if (cell != null) {
								String valueString = Utils.returnCellValueAsString(cell);
								// Set the number of the column
								if (valueString.equals(formData.getColumnToSplitFor())) {
									subClassColumnNumber = c;
								}
								if (valueString.equals(formData.getColumnToSplitFor2())) {
									subClassColumnNumber2 = c;
								}
							}
						}
					}
				}
			}
			saveExcel(writeBook, f);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void saveExcel(Workbook writeBook, File f) throws FileNotFoundException, IOException {

		try (FileOutputStream outputStream = new FileOutputStream(f.getAbsolutePath())) {
			writeBook.write(outputStream);
			writeBook.close();
		}
	}

	/**
	 * Removes invalid characters from string, since we want to use that as a name
	 * for files in windows
	 * 
	 * @param input
	 * @return
	 */
	private static String cleanInput(String input) {
		input = input.replace("/", " ");
		input = input.replace("\\", " ");
		input = input.replace(":", " ");
		input = input.replace("*", " ");
		input = input.replace("?", " ");
		input = input.replace("\"", " ");
		input = input.replace("<", " ");
		input = input.replace(">", " ");
		input = input.replace("|", " ");
		return input;
	}

}
