package entities;

import java.io.File;

public class SubClassTransformationData {

	File metaDataFiles;
	String numberColumn;
	String documentType;
	String descriptionColumn;
	File transformationFile;
	String sheetName;
	String charForSplit;

	public SubClassTransformationData(File metaDataFiles, String numberColumn, String documentType,
			String descriptionColumn, File transformationFile, String sheetName, String charForSplit) {

		this.metaDataFiles = metaDataFiles;
		this.numberColumn = numberColumn;
		this.documentType = documentType;
		this.descriptionColumn = descriptionColumn;
		this.transformationFile = transformationFile;
		this.sheetName = sheetName;
		this.charForSplit = charForSplit;
	}

	public File getMetaDataFiles() {
		return metaDataFiles;
	}

	public void setMetaDataFiles(File metaDataFiles) {
		this.metaDataFiles = metaDataFiles;
	}

	public String getNumberColumn() {
		return numberColumn;
	}

	public void setNumberColumn(String numberColumn) {
		this.numberColumn = numberColumn;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDescriptionColumn() {
		return descriptionColumn;
	}

	public void setDescriptionColumn(String descriptionColumn) {
		this.descriptionColumn = descriptionColumn;
	}

	public File getTransformationFile() {
		return transformationFile;
	}

	public void setTransformationFile(File transformationFile) {
		this.transformationFile = transformationFile;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getCharForSplit() {
		return charForSplit;
	}

	public void setCharForSplit(String charForSplit) {
		this.charForSplit = charForSplit;
	}
}
