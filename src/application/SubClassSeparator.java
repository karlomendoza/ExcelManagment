package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import utils.Utils;

public class SubClassSeparator {

	public static void main(String... strings) throws InvalidFormatException, IOException {

		File metaDataFile = new File("C:\\Users\\Karlo Mendoza\\Excel Work\\ICU MEDICAL\\SAP DMS\\Demo\\T2_\\MetaData Transformed.xlsx");
		String columnToSplitFor = "SubClass";

		processData(metaDataFile, columnToSplitFor);
	}

	public static CellStyle cellStyle;
	public static List<Integer> dates = new ArrayList<>();

	static {
		dates.add(7);
		dates.add(14);
	}

	public static void processData(File metaDataFile, String columnToSplitFor) throws InvalidFormatException, IOException {

		try (Workbook wb = Utils.getWorkBook(metaDataFile)) {
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
			String lastSubClassprocessed = "";
			String lastOtherProcessed = "";
			Workbook writeBook = null;
			Sheet writeSheet = null;
			File f = null;

			for (int r = 0; r < rows; r++) {
				row = readSheet.getRow(r);
				if (row != null) {
					// if it's not the header
					if (r > 0) {
						String subClass = Utils.returnCellValueAsString(row.getCell((int) subClassColumnNumber));
						if (subClass.equals(""))
							subClass = "ManualReview";

						subClass = cleanInput(subClass);

						// String other = Utils.returnCellValueAsString(row.getCell((int)
						// subClassColumnNumber2));
						String other = "";

						if (!lastSubClassprocessed.equals(subClass) || !lastOtherProcessed.equals(other)) {
							if (!lastSubClassprocessed.equals(""))
								saveExcel(writeBook, f);

							lastSubClassprocessed = subClass;
							lastOtherProcessed = other;

							File folder = new File(metaDataFile.getParentFile() + "\\SubclassSplits\\");
							if (!folder.exists()) {
								folder.mkdirs();
							}

							f = new File(metaDataFile.getParentFile() + "\\SubclassSplits\\" + subClass + ".xlsx");
							if (f.exists()) {
								writeBook = Utils.getWorkBook(f);
								writeSheet = writeBook.getSheet("data");

								cellStyle = writeBook.createCellStyle();
								cellStyle.setDataFormat((short) 14);
							} else {
								writeBook = Utils.getWorkBook(null);
								writeSheet = writeBook.createSheet("data");

								cellStyle = writeBook.createCellStyle();
								cellStyle.setDataFormat((short) 14);
							}
							Row createRow = writeSheet.createRow((int) 0);
							setCellsValuesToRow(createRow, headerRow, cols);
						}

						Row createRow2 = writeSheet.createRow((int) writeSheet.getPhysicalNumberOfRows());
						setCellsValuesToRow(createRow2, row, cols);

					} else if (r == 0) {
						// get the column number of the subClass
						for (int c = 0; c < cols; c++) {
							cell = row.getCell((int) c);
							if (cell != null) {
								String valueString = Utils.returnCellValueAsString(cell);
								// Set the number of the column
								if (valueString.equals(columnToSplitFor)) {
									subClassColumnNumber = c;
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
	 * Removes invalid characters from string, since we want to use that as a name for files in windows
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

	/**
	 * Gets all the cells from dataRow and copys them in writeToRow, basically it copies the whole row, but skips the first one to allow to put the
	 * subClass
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

				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_NUMERIC:
					createCell.setCellValue(cell.getNumericCellValue());
					if (dates.contains(c)) {
						createCell.setCellStyle(cellStyle);
					}
					break;
				case Cell.CELL_TYPE_STRING:
					createCell.setCellValue(cell.getStringCellValue());
				}
			}
		}
	}

}
