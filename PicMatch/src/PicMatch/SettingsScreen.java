package PicMatch;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;

public class SettingsScreen {

	private Shell shell;
	private Display display;
	private String databasePath;
	private String resultsPath;
	private Boolean cleanResultsFolder;
	private Boolean edgeExtraction, histComparison;
	private Boolean perfData;
	private Settings appSettings;

	public SettingsScreen(Display display, Settings appSettings) {

		this.databasePath = appSettings.getDatabasePath();
		this.resultsPath = appSettings.getResultsPath();
		this.cleanResultsFolder = appSettings.getCleanResultsFolder();
		this.edgeExtraction = appSettings.getEdgeExtraction();
		this.histComparison = appSettings.getHistComparison();
		this.perfData = appSettings.getPerfData();
		this.display = display;
		this.appSettings = appSettings;

		// Create and open the application window
		shell = new Shell(display);
		shell.setText("Settings");
		shell.setImage(new Image(display, "SettingsButton.png"));
		shell.setSize(310, 290);
		shell.setLocation(1030, 150);

		setUp();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void setUp() {
		Label lblDatabase = new Label(shell, SWT.NONE);
		lblDatabase.setBounds(10, 20, 56, 16);
		lblDatabase.setText("Database:");

		Label lblResults = new Label(shell, SWT.NONE);
		lblResults.setText("Results:");
		lblResults.setBounds(10, 76, 56, 16);

		Label lblDirectoryPath = new Label(shell, SWT.NONE);
		lblDirectoryPath.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblDirectoryPath.setBounds(10, 42, 186, 16);
		lblDirectoryPath.setText(databasePath);

		Label lblResultsPath = new Label(shell, SWT.NONE);
		lblResultsPath.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblResultsPath.setText(resultsPath);
		lblResultsPath.setBounds(10, 95, 186, 16);

		Button btnChangeDatabase = new Button(shell, SWT.NONE);
		btnChangeDatabase.setBounds(217, 40, 70, 21);
		btnChangeDatabase.setText("Change");
		btnChangeDatabase.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String newDbPath = changeFileDirectory();
				if (newDbPath != null) {
					lblDirectoryPath.setText(newDbPath);
					databasePath = newDbPath;
				}
			}
		});

		Button btnChangeResults = new Button(shell, SWT.NONE);
		btnChangeResults.setText("Change");
		btnChangeResults.setBounds(217, 93, 70, 21);
		btnChangeResults.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String newResultsPath = changeFileDirectory();
				if (newResultsPath != null) {
					lblResultsPath.setText(resultsPath);
					resultsPath = newResultsPath;
				}
			}
		});

		Button btnCleanResults = new Button(shell, SWT.CHECK);
		btnCleanResults.setBounds(190, 133, 25, 16);
		btnCleanResults.setSelection(cleanResultsFolder);
		btnCleanResults.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button checkBtn = (Button) e.getSource();
				if (checkBtn.getSelection() == false) {
					cleanResultsFolder = false;
				} else {
					cleanResultsFolder = true;
				}
			}
		});

		Label lblCleanResultsFolder = new Label(shell, SWT.NONE);
		lblCleanResultsFolder.setBounds(10, 133, 170, 16);
		lblCleanResultsFolder.setText("Clean results folder every run");

		Button btnEdgeExtraction = new Button(shell, SWT.CHECK);
		btnEdgeExtraction.setBounds(190, 155, 25, 16);
		btnEdgeExtraction.setSelection(edgeExtraction);
		btnEdgeExtraction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button checkBtn = (Button) e.getSource();
				if (checkBtn.getSelection() == false) {
					edgeExtraction = false;
				} else {
					edgeExtraction = true;
				}
			}
		});

		Label lblEdgeExtraction = new Label(shell, SWT.NONE);
		lblEdgeExtraction.setBounds(10, 155, 175, 16);
		lblEdgeExtraction.setText("Use Edge Extraction (2.5x slower)");	
		
		Button btnHistComparison = new Button(shell, SWT.CHECK);
		btnHistComparison.setBounds(190, 175, 25, 16);
		btnHistComparison.setSelection(histComparison);
		btnHistComparison.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button checkBtn = (Button) e.getSource();
				if (checkBtn.getSelection() == false) {
					histComparison = false;
				} else {
					histComparison = true;
				}
			}
		});

		Label lblHistComparison = new Label(shell, SWT.NONE);
		lblHistComparison.setBounds(10, 175, 175, 16);
		lblHistComparison.setText("Use Histogram comparison");

		Button btnShowPerfData = new Button(shell, SWT.CHECK);
		btnShowPerfData.setBounds(190, 195, 25, 16);
		btnShowPerfData.setSelection(perfData);
		btnShowPerfData.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Button checkBtn = (Button) e.getSource();
				if (checkBtn.getSelection() == false) {
					perfData = false;
				} else {
					perfData = true;
				}
			}
		});

		Label lblShowPerfData = new Label(shell, SWT.NONE);
		lblShowPerfData.setBounds(10, 195, 166, 16);
		lblShowPerfData.setText("Show performance data");

		Button btnSave = new Button(shell, SWT.NONE);
		btnSave.setBounds(100, 220, 70, 21);
		btnSave.setText("SAVE");
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				appSettings.setDatabasePath(databasePath);
				appSettings.setResultsPath(resultsPath);
				appSettings.setCleanResultsFolder(cleanResultsFolder);
				appSettings.setEdgeExtraction(edgeExtraction);
				appSettings.setHistComparison(histComparison);
				appSettings.setShowPerfData(perfData);
				shell.close();
			}
		});
	}

	private String changeFileDirectory() {
		try {
			DirectoryDialog dialog = new DirectoryDialog(shell);
			dialog.setFilterPath("ImageDatabase");
			return dialog.open();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return null;
	}
}