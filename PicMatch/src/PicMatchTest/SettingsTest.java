package PicMatchTest;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import PicMatch.Settings;

class SettingsTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	final void testGetDefaultDatabasePath() {
		String expectedDatabasePath = "ImageDatabase\\All";
		Settings settings = new Settings(expectedDatabasePath, "ImageDatabase\\Results", true, false, false);

		String actualtDatabasePath = settings.getDatabasePath();

		assertEquals(expectedDatabasePath, actualtDatabasePath);
	}

	@Test
	final void testSetDatabasePath() {
		String originalDatabasePath = "ImageDatabase\\All";
		String expectedDatabasePath = "ImageDatabase\\NewDatabase";
		Settings settings = new Settings(originalDatabasePath, "ImageDatabase\\Results", true, false, false);

		settings.setDatabasePath(expectedDatabasePath);

		assertEquals(expectedDatabasePath, settings.getDatabasePath());
	}

	@Test
	final void testGetDefaultResultsPath() {
		String expectedResultsPath = "ImageDatabase\\Results";
		Settings settings = new Settings("ImageDatabase\\All", expectedResultsPath, true, false, false);

		String actualResultsPath = settings.getResultsPath();

		assertEquals(expectedResultsPath, actualResultsPath);
	}

	@Test
	final void testSetResultsPath() {
		String originalResultsPath = "ImageDatabase\\Results";
		String expectedResultsPath = "ImageDatabase\\NewResults";
		Settings settings = new Settings("ImageDatabase\\All", originalResultsPath, true, false, false);

		settings.setDatabasePath(expectedResultsPath);

		assertEquals(expectedResultsPath, settings.getDatabasePath());
	}

	@Test
	final void testGetCleanResultsFolder() {
		Boolean isCleanResultsSelected = true;
		Settings settings = new Settings("ImageDatabase\\All", "ImageDatabase\\Resu;ts", isCleanResultsSelected, false, false);

		Boolean actualSelectionStatus = settings.getCleanResultsFolder();

		assertEquals(isCleanResultsSelected, actualSelectionStatus);
	}

	@Test
	final void testSetCleanResultsFolder() {
		Boolean isCleanResultsSelected = true;
		Settings settings = new Settings("ImageDatabase\\All", "ImageDatabase\\Resu;ts", isCleanResultsSelected, false, false);

		Boolean expectedSelection = false;
		settings.setCleanResultsFolder(expectedSelection);
		Boolean actualSelectionStatus = settings.getCleanResultsFolder();

		assertEquals(expectedSelection, actualSelectionStatus);
	}

	@Test
	final void testGetEdgeExtraction() {	
		Boolean isEdgeExtractionSelected = false;
		Settings settings = new Settings("ImageDatabase\\All", "ImageDatabase\\Resu;ts", true, isEdgeExtractionSelected, false);

		Boolean actualSelectionStatus = settings.getEdgeExtraction();

		assertEquals(isEdgeExtractionSelected, actualSelectionStatus);
	}

	@Test
	final void testSetEdgeExtraction() {
		Boolean isEdgeExtractionSelected = false;
		Settings settings = new Settings("ImageDatabase\\All", "ImageDatabase\\Resu;ts", true, isEdgeExtractionSelected, false);

		Boolean expectedSelection = true;
		settings.setEdgeExtraction(expectedSelection);
		Boolean actualSelectionStatus = settings.getEdgeExtraction();

		assertEquals(expectedSelection, actualSelectionStatus);
	}
	
	@Test
	final void testGetHistComparison() {	
		Boolean isHistSelected = false;
		Settings settings = new Settings("ImageDatabase\\All", "ImageDatabase\\Resu;ts", true, false, isHistSelected);

		Boolean actualSelectionStatus = settings.getHistComparison();

		assertEquals(isHistSelected, actualSelectionStatus);
	}

	@Test
	final void testSetHistComparisonn() {
		Boolean isHistSelected = false;
		Settings settings = new Settings("ImageDatabase\\All", "ImageDatabase\\Resu;ts", true, false, isHistSelected);

		Boolean expectedSelection = true;
		settings.setHistComparison(expectedSelection);
		Boolean actualSelectionStatus = settings.getHistComparison();

		assertEquals(expectedSelection, actualSelectionStatus);
	}

	@Test
	final void testGetPerfData() {
		Boolean isPerfDataSelected = false;
		Settings settings = new Settings("ImageDatabase\\All", "ImageDatabase\\Resu;ts", true, false, false);

		Boolean actualSelectionStatus = settings.getPerfData();

		assertEquals(isPerfDataSelected, actualSelectionStatus);
	}

	@Test
	final void testSetPerfData() {
		Settings settings = new Settings("ImageDatabase\\All", "ImageDatabase\\Resu;ts", true, false, false);

		Boolean expectedSelection = true;
		settings.setShowPerfData(expectedSelection);
		Boolean actualSelectionStatus = settings.getPerfData();

		assertEquals(expectedSelection, actualSelectionStatus);
	}

}
