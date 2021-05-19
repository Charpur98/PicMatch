package PicMatch;

public class Settings {

	private String databasePath;
	private String resultsPath;
	private Boolean cleanResultsFolder;
	private Boolean edgeExtraction;
	private Boolean histComparison;
	private Boolean perfData;

	public Settings(String databasePath, String resultsPath, Boolean cleanResultsFolder, Boolean edgeExtraction, Boolean histComparison) {
		this.databasePath = databasePath;
		this.resultsPath = resultsPath;
		this.setCleanResultsFolder(cleanResultsFolder);
		this.setEdgeExtraction(edgeExtraction);
		this.setHistComparison(histComparison);
		this.perfData = false;
	}

	public String getDatabasePath() {
		return databasePath;
	}

	public String getResultsPath() {
		return resultsPath;
	}

	public Boolean getCleanResultsFolder() {
		return cleanResultsFolder;
	}

	public Boolean getEdgeExtraction() {
		return edgeExtraction;
	}
	
	public Boolean getHistComparison() {
		return histComparison;
	}
	
	public Boolean getPerfData() {
		return perfData;
	}

	public void setDatabasePath(String path) {
		databasePath = path;
	}

	public void setResultsPath(String path) {
		resultsPath = path;
	}

	public void setCleanResultsFolder(Boolean cleanResultsFolder) {
		this.cleanResultsFolder = cleanResultsFolder;
	}

	public void setEdgeExtraction(Boolean useEdgeExtraction) {
		this.edgeExtraction = useEdgeExtraction;
	}
	
	public void setHistComparison(Boolean useHistComparison) {
		this.histComparison = useHistComparison;
	}
	
	public void setShowPerfData(Boolean perfData) {
		this.perfData = perfData;
	}

}
