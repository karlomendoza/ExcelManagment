package entities;

import java.io.File;

public class SplitData {

	/**
	 * File that holds all the information, typically an excel file, only supported
	 * xlsx and xls
	 */
	private File metaDataFile;

	/**
	 * The name of the column we are using for splitting
	 */
	private String columnToSplitFor;

	/**
	 * The name of the column we are using for splitting
	 */
	private String columnToSplitFor2;

	public SplitData(File metaDataFile, String columnToSplitFor, String columnToSplitFor2) {
		this.metaDataFile = metaDataFile;
		this.columnToSplitFor = columnToSplitFor;
		this.columnToSplitFor2 = columnToSplitFor2;
	}

	public File getMetaDataFile() {
		return metaDataFile;
	}

	public void setMetaDataFile(File metaDataFile) {
		this.metaDataFile = metaDataFile;
	}

	public String getColumnToSplitFor() {
		return columnToSplitFor;
	}

	public void setColumnToSplitFor(String columnToSplitFor) {
		this.columnToSplitFor = columnToSplitFor;
	}

	public String getColumnToSplitFor2() {
		return columnToSplitFor2;
	}

	public void setColumnToSplitFor2(String columnToSplitFor2) {
		this.columnToSplitFor2 = columnToSplitFor2;
	}

}
