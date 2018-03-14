package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParseErrorLogs {

	public static final File files = new File("C:\\Users\\Karlo Mendoza\\Excel Work\\ICU MEDICAL\\Master Control\\T0\\Logs\\");

	public static void main(String... strings) throws ParserConfigurationException, IOException, SAXException {
		parse();
	}

	public static void parse() throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setIgnoringElementContentWhitespace(true);
		DocumentBuilder builder = factory.newDocumentBuilder();

		try (SXSSFWorkbook writeBook = new SXSSFWorkbook(100);) {
			SXSSFSheet writeSheet = writeBook.createSheet();
			SXSSFRow headerRow = writeSheet.createRow(0);
			List<String> asList = Arrays.asList("Parameter", "Error1", "Value with error", "descriptiveError", "especial Errors");

			asList.forEach(value -> headerRow.createCell(headerRow.getPhysicalNumberOfCells()).setCellValue(value));

			for (File file : files.listFiles()) {
				Document doc = builder.parse(file);

				NodeList childNodes = doc.getChildNodes();
				NodeList warningsAndErrors = childNodes.item(1).getChildNodes();

				for (int i = 2; i < warningsAndErrors.getLength(); i++) {
					Node item = warningsAndErrors.item(i);
					if (item.getNodeName().equals("error")) {
						String errorMessage = item.getChildNodes().item(0).toString();
						try {

							if (errorMessage.contains("Existing object")) {
								String attributeWithError = "Documents.Title Block.Number";
								String error1 = errorMessage.replaceAll("\\[#text: Existing object '", "");
								String valueWithError = error1.substring(0, error1.indexOf("' "));

								String anotherErrorPrt = error1.substring(valueWithError.length() + 2, error1.length() - 2);

								SXSSFRow row = writeSheet.createRow(writeSheet.getPhysicalNumberOfRows());
								row.createCell(row.getPhysicalNumberOfCells()).setCellValue(attributeWithError);
								row.createCell(row.getPhysicalNumberOfCells()).setCellValue("");
								row.createCell(row.getPhysicalNumberOfCells()).setCellValue(valueWithError);
								row.createCell(row.getPhysicalNumberOfCells()).setCellValue(anotherErrorPrt);

							} else if (errorMessage.contains("Following required fields")) {
								// [#text: Following required fields ''Verification, Validation, and Qualification Specific Attributes.Output;''
								// were not provided for the new 'Verification, Validation, and Qualification' object 'T0_SVF-00269'. ]

								String error1 = errorMessage.replaceAll("\\[#text: Following required fields ''", "");
								String attributeWithError = "Following required fields " + error1.substring(0, error1.indexOf("''"));
								String valueWithError = error1.substring(error1.indexOf("object '") + 8, error1.length() - 4);

								String anotherErrorPrt = "were not provided";

								SXSSFRow row = writeSheet.createRow(writeSheet.getPhysicalNumberOfRows());
								row.createCell(row.getPhysicalNumberOfCells()).setCellValue(attributeWithError);
								row.createCell(row.getPhysicalNumberOfCells()).setCellValue("");
								row.createCell(row.getPhysicalNumberOfCells()).setCellValue(valueWithError);
								row.createCell(row.getPhysicalNumberOfCells()).setCellValue(anotherErrorPrt);

							} else {

								String error1 = errorMessage.replaceAll("\\[#text: ", "");
								String attributeWithError = error1.substring(0, error1.indexOf("', ") + 1);

								String error2 = error1.substring(error1.indexOf("', ") + 3).replace("the following message was received: ", "");
								String justTheErrorMessage = error2.substring(0, error2.indexOf(":"));

								String valueWithError = error2.substring(error2.indexOf(": value '") + 9, error2.indexOf("' "));

								String anotherErrorPrt = error2.substring(valueWithError.length() + justTheErrorMessage.length() + 11, error2.length() - 2);

								SXSSFRow row = writeSheet.createRow(writeSheet.getPhysicalNumberOfRows());
								row.createCell(row.getPhysicalNumberOfCells()).setCellValue(attributeWithError);
								row.createCell(row.getPhysicalNumberOfCells()).setCellValue(justTheErrorMessage);
								row.createCell(row.getPhysicalNumberOfCells()).setCellValue(valueWithError);
								row.createCell(row.getPhysicalNumberOfCells()).setCellValue(anotherErrorPrt);
							}
						} catch (Exception ex) {
							SXSSFRow row = writeSheet.createRow(writeSheet.getPhysicalNumberOfRows());
							row.createCell(4).setCellValue(errorMessage);
						}
					}
				}
			}

			File f = new File(files.getParentFile() + "\\logsProcessed.xlsx");
			try (FileOutputStream outputStream = new FileOutputStream(f)) {
				writeBook.write(outputStream);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
