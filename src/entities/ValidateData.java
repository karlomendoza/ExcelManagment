package entities;

import java.io.File;

public class ValidateData {

	/**
	 * File that holds all the information, typically an excel file, only supported
	 * xlsx and xls
	 */
	private File metaDataFile;
	/**
	 * File that contains the lists values used to validate the metaDataFile.
	 */
	private File fileWithListValues;

	/**
	 * The name of the Sheet with the lists values
	 */
	private String sheetName;

	public ValidateData(File metaDataFile, File fileWithListValues, String sheetName) {
		this.metaDataFile = metaDataFile;
		this.fileWithListValues = fileWithListValues;
		this.sheetName = sheetName;
	}

	public File getMetaDataFile() {
		return metaDataFile;
	}

	public void setMetaDataFile(File metaDataFile) {
		this.metaDataFile = metaDataFile;
	}

	public File getFileWithListValues() {
		return fileWithListValues;
	}

	public void setFileWithListValues(File fileWithListValues) {
		this.fileWithListValues = fileWithListValues;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

}
