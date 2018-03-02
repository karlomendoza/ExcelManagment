package application;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.agile.api.APIException;
import com.agile.api.AgileSessionFactory;
import com.agile.api.ChangeConstants;
import com.agile.api.IAgileList;
import com.agile.api.IAgileSession;
import com.agile.api.ICell;
import com.agile.api.IChange;
import com.agile.api.IStatus;
import com.agile.api.ITable;
import com.agile.api.IUser;
import com.agile.api.ItemConstants;

import utils.Utils;

public class AgileCloseEcos {

	private static final String METADATA_PATH = "C:\\Users\\Karlo Mendoza\\Excel Work\\ICU MEDICAL\\Master Control\\T0\\Upload_done\\";

	public static void main(String... string) throws APIException {
		IAgileSession session;
		while (true) {
			try {
				session = connectToAgile("data.loader", "agile", "http://icuaglapp301.icumed.com:7006/Agile");
				break;
			} catch (Exception ex) {
			}

		}
		File filesToUpload = new File(METADATA_PATH);
		for (File file : filesToUpload.listFiles()) {
			if (file.isDirectory() || !file.getName().endsWith("xlsx"))
				continue;
			ParsedFile parsedFile = parseFile(file);
			releaseEco(parsedFile, session);
		}

	}

	public static IAgileSession connectToAgile(String userId, String password, String url) throws APIException {
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(AgileSessionFactory.USERNAME, userId);
		map.put(AgileSessionFactory.PASSWORD, password);

		System.out.println("connecting to agile");
		AgileSessionFactory aFactory = AgileSessionFactory.getInstance(url);
		IAgileSession session = aFactory.createSession(map);
		return session;
	}

	public static void releaseEco(ParsedFile parsedFile, IAgileSession session) throws APIException {
		try {
			IChange changeOrder = (IChange) session.getObject(session.getAdminInstance().getAgileClass("ECO"),
					parsedFile.eco);

			ITable affectedItems = changeOrder.getTable(ChangeConstants.TABLE_AFFECTEDITEMS);

			if (parsedFile.numberOfRows != -1 && affectedItems.size() != parsedFile.numberOfRows
					&& affectedItems.size() <= 0) {
				System.out.println("ECO: " + parsedFile.eco + " has zero records, thats bad, file name: "
						+ parsedFile.fullNameWithoutExtension);
				return;
			}
			System.out.println("ECO: " + parsedFile.eco + " has " + affectedItems.size() + " records, file name: "
					+ parsedFile.fullNameWithoutExtension);

			Map<String, String> values = new HashMap<String, String>();
			values.put("descriptionOfChange", parsedFile.fullNameWithoutExtension);
			values.put("reasonForChange", "INITIAL DATA LOAD");

			ICell cell = changeOrder.getCell(ItemConstants.ATT_PAGE_TWO_LIST01);
			// Get the current IAgileList object for Part Category
			IAgileList listValues = cell.getAvailableValues();
			listValues.setSelection(new Object[] { "ICUMED - Enterprise" });
			cell.setValue(listValues);

			changeOrder.setValues(values);

			nextStatus(changeOrder, null, null, null);
		} catch (Exception ex) {
			System.out.println("Error al procesar el ECO de: " + parsedFile.nameWithoutExtension);
		}

	}

	public static void nextStatus(IChange change, IUser[] notifyList, IUser[] approvers, IUser[] observers) {
		try {
			// Check if the user has privileges to change to the next status
			IStatus nextStatus = change.getDefaultNextStatus();
			if (nextStatus == null) {
				System.out.println("Insufficient privileges to change status.");
				return;
			}
			// Change to the next status
			else {
				change.changeStatus(nextStatus, true, "", true, true, notifyList, approvers, observers, false);
			}
		} catch (APIException ex) {
			System.out.println(ex);
		}
	}

	private static ParsedFile parseFile(File file) {
		String nameWithNoise = file.getName();
		String eco = nameWithNoise.split("_")[1];

		String numberWithNoise = nameWithNoise.substring(nameWithNoise.length() - 8, nameWithNoise.length() - 5);
		String number = "";
		for (char elem : numberWithNoise.toCharArray()) {
			if (elem == '0' || elem == '1' || elem == '2' || elem == '3' || elem == '4' || elem == '5' || elem == '6'
					|| elem == '7' || elem == '8' || elem == '9') {
				number += elem;
			}
		}

		String fullNameWithoutExtension = nameWithNoise.substring(0, nameWithNoise.length() - 5);
		String nameWithoutExtension = nameWithNoise.substring(12, nameWithNoise.length() - 5 - number.length());

		int numberOfRows = -1;
		try (Workbook listDataWorkbook = Utils.getWorkBook(file)) {
			Sheet dataListSheet = listDataWorkbook.getSheetAt(0);
			numberOfRows = dataListSheet.getPhysicalNumberOfRows();
		} catch (InvalidFormatException | IOException e) {

			e.printStackTrace();
		}

		return new ParsedFile(eco, nameWithoutExtension, number, fullNameWithoutExtension, numberOfRows);
	}

	static class ParsedFile {
		public String eco;
		public String nameWithoutExtension;
		public String fileNumber;
		public String fullNameWithoutExtension;
		public int numberOfRows;

		ParsedFile(String eco, String nameWithoutExtension, String fileNumber, String fullNameWithoutExtension,
				int numberOfRows) {
			this.eco = eco;
			this.nameWithoutExtension = nameWithoutExtension;
			this.fileNumber = fileNumber;
			this.fullNameWithoutExtension = fullNameWithoutExtension;
			this.numberOfRows = numberOfRows;
		}
	}
}
