
package com.kileyowen.degrees_of_separation.gui;

import java.io.File;
import java.net.MalformedURLException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.kileyowen.degrees_of_separation.DijkstraControl;
import com.kileyowen.degrees_of_separation.ExceptionBadEndPageTitle;
import com.kileyowen.degrees_of_separation.ExceptionBadStartPageTitle;
import com.kileyowen.degrees_of_separation.PageTitle;
import com.kileyowen.degrees_of_separation.Path;
import com.kileyowen.degrees_of_separation.database.ExceptionPageLinksNotStored;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/*
    GUI Portion of the Wikipedia Racers
    This GUI implements the Dijkstra's algorithm
    and displays the relationship on
    Written by: Emerson Moniz
 */

public class GuiProgram extends Application {

	private static DijkstraControl dijkstra;

	public static void main(final String[] args) {

		Application.launch(args);

	}

	private final ConcurrentLinkedQueue<String> fromDijkstraToGuiPageTitles = new ConcurrentLinkedQueue<>();

	private final ConcurrentLinkedQueue<Path> fromDijstraToGuiPathWhenFinished = new ConcurrentLinkedQueue<>();

	private Mode mode;

	public final String EXPLORE = "Explore Page";

	public final String OFFLINE = "Offline";

	public final String ONLINE = "Online";

	public final String EMPTY = "";

	private TextField tfStartPage;

	private TextField tfEndPage;

	private boolean running = false;

	private TextArea taOutput;

	private int CheckForError(final TextField t1) {

		int errorValue = -1;
		if (this.IsValidString(t1)) {
			errorValue = 0;
		}
		return errorValue;
	}

	private int CheckForError(final TextField t1, final TextField t2) {

		int errorValue = -1;

		if (this.IsValidString(t1, t2)) {
			errorValue = 2;
		} else if (this.IsValidString(t1)) {
			errorValue = 0;
		} else if (this.IsValidString(t2)) {
			errorValue = 1;
		} else {
			errorValue = -1;
		}
		return errorValue;
	}

	private void ClearAllValues(final TextField t1, final TextField t2, final TextArea t3) {

		t1.setText(this.EMPTY);
		t2.setText(this.EMPTY);
		t3.setText(this.EMPTY);
		final Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Cleared Values");
		alert.setHeaderText("All values are cleared!");
		alert.setContentText(null);
		alert.showAndWait();
	}

	private void DisplayMessage(final TextField t1, final int errorNum) {

		if (errorNum == 0) {
			this.ErrorBox("Error", "There is no input! Please type something in!");
		} else {
			this.DisplaySearchMessage(t1);
		}
	}

	private void DisplayMessage(final TextField t1, final TextField t2, final int errorNum) {

		if (errorNum >= 0) {
			switch (errorNum) {
				case 0:
					this.ErrorBox("Error", "The input in start page is empty");
					break;
				case 1:
					this.ErrorBox("Error", "The input in end page is empty");
					break;
				default:
					this.ErrorBox("Error", "There are no inputs! Please type something in.");
			}
		} else {
			this.DisplaySearchMessage(t1, t2);
		}
	}

	private void DisplaySearchMessage(final TextField t1) {

		final Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Searching for Pages");
		alert.setContentText("This message is informing you that I am searching for all the links for " + t1.getText());
		alert.showAndWait();
	}

	private void DisplaySearchMessage(final TextField t1, final TextField t2) {

		final Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Searching for Pages");
		alert.setContentText("This message is informing you that I am searching for the degree between " + t1.getText() + " and " + t2.getText());
		alert.showAndWait();
	}

	// Create method for checking errors
	private void ErrorBox(final String TitleBox, final String MessageContent) {

		final Alert alertJava = new Alert(Alert.AlertType.ERROR);
		alertJava.setTitle(TitleBox);
		alertJava.setContentText(MessageContent);
		alertJava.showAndWait();
	}

	/*
	    PRE CONDITION: Instance
	    POST CONDITION:
	
	 */
	private boolean IsValidString(final TextField x) {

		return x.getText().isEmpty();
	}

	private boolean IsValidString(final TextField x1, final TextField x2) {

		if (!(!x1.getText().isEmpty() || !x2.getText().toString().isEmpty())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void start(final Stage primaryStage) throws MalformedURLException {

		this.mode = Mode.ONLINE;

		// Group root = new Group();
		final TabPane tabs = new TabPane();
		final AnchorPane root = new AnchorPane();

		//   HBox hbox = addHBox();
		final Scene scene = new Scene(root, 1024, 600);
		scene.getStylesheets().add(new File("stylesheet.css").toURI().toURL().toString());

		// Create tabs for user to choose options
		final Tab tabOnline = new Tab(this.ONLINE);
		final Tab tabOffline = new Tab(this.OFFLINE);
		final Tab tabExplore = new Tab(this.EXPLORE);

		this.taOutput = new TextArea();
		this.taOutput.setWrapText(true);
		this.taOutput.setEditable(false);
		this.taOutput.setText(this.EMPTY);
		this.taOutput.setPrefSize(600, 450);
		AnchorPane.setTopAnchor(this.taOutput, 100.0);
		AnchorPane.setRightAnchor(this.taOutput, 50.0);

		// Create Textfield to take in data from user
		final Label lblStart = new Label("Start Page:");
		final Label lblEnd = new Label("End Page:");
		this.tfStartPage = new TextField();
		this.tfEndPage = new TextField();

		// Create a GridPane to hold the values in the center
		final GridPane gPane = new GridPane();

		gPane.setVgap(20.0);
		gPane.add(lblStart, 0, 0);
		gPane.add(this.tfStartPage, 1, 0);
		gPane.add(lblEnd, 0, 1);
		gPane.add(this.tfEndPage, 1, 1);

		// Add gpane with hbuttons
		AnchorPane.setTopAnchor(gPane, 100.0);
		AnchorPane.setLeftAnchor(gPane, 50.0);

		// Set the root contents to not be changeable by resizing
		root.prefHeightProperty().bind(scene.heightProperty());
		root.prefWidthProperty().bind(scene.widthProperty());
		tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

		tabs.prefHeightProperty().bind(scene.heightProperty());
		tabs.prefWidthProperty().bind(scene.widthProperty());
		tabs.getTabs().addAll(tabOnline, tabOffline, tabExplore);

		// Create a listener to see the changes in the tabs
		tabs.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
			// If the tab selected is Explore Pages hide the label and textField
			this.TabSelected(tabs, lblEnd, this.tfEndPage);
		});

		AnchorPane.setTopAnchor(tabs, 0.0);

		// Create button to clear text
		final Button btnClear = new Button("Clear");
		btnClear.setOnAction(e -> this.ClearAllValues(this.tfStartPage, this.tfEndPage, this.taOutput));

		//

		// Create button to submit text
		final Button btnSubmit = new Button("Submit");
		btnSubmit.setOnAction(e -> this.SubmitValues(tabs, lblStart, this.tfStartPage, lblEnd, this.tfEndPage));

		// Create Hbox to hold the buttons
		final HBox hbButtons = new HBox(15, btnClear, btnSubmit);
		hbButtons.setAlignment(Pos.CENTER);

		// Set the buttons to be resizable according to the dimensions of screen
		AnchorPane.setTopAnchor(hbButtons, 200.0);
		AnchorPane.setLeftAnchor(hbButtons, 50.0);

		root.getChildren().addAll(tabs, hbButtons, gPane, this.taOutput);
		primaryStage.setResizable(false);
		primaryStage.setTitle("Test");
		primaryStage.setScene(scene);
		primaryStage.show();

		primaryStage.setOnCloseRequest((event) -> {

			Platform.exit();
			System.exit(0);

		});

		final Timeline oneSecondTimer = new Timeline(new KeyFrame(Duration.seconds(1), (event) -> {

			if (this.running) {

				while (!this.fromDijkstraToGuiPageTitles.isEmpty()) {

					this.taOutput.appendText(String.format("%n%s", this.fromDijkstraToGuiPageTitles.remove()));
				}

				if (!this.fromDijstraToGuiPathWhenFinished.isEmpty()) {

					this.taOutput.appendText(String.format("%nPath: %s", this.fromDijstraToGuiPathWhenFinished.remove().toStringForGui()));
					this.running = false;

					GuiProgram.dijkstra.commit();

				}

			}

		}));

		oneSecondTimer.setCycleCount(Animation.INDEFINITE);

		oneSecondTimer.play();

	}

	private void SubmitValues(final TabPane t, final Label l1, final TextField t1, final Label l2, final TextField t2) {

		int errorValue = -1;

		if (this.running) {

			this.ErrorBox("Already Running", "This is already running");
			return;

		}
		if (t.getSelectionModel().getSelectedItem().getText() == this.EXPLORE) {
			errorValue = this.CheckForError(t1);
			this.DisplayMessage(t1, errorValue);
		} else {
			errorValue = this.CheckForError(t1, t2);
			this.DisplayMessage(t1, t2, errorValue);
		}

		if (errorValue == -1) {

			switch (this.mode) {
				case ONLINE:

					this.running = true;

					new Thread(() -> {

						try {

							GuiProgram.dijkstra = new DijkstraControl(true, this.fromDijkstraToGuiPageTitles);

							this.fromDijstraToGuiPathWhenFinished.add(GuiProgram.dijkstra.runSearch(new PageTitle(this.tfStartPage.getText()), new PageTitle(this.tfEndPage.getText())));

						} catch (final ExceptionBadStartPageTitle e) {

							this.ErrorBox("Invalid Start Page", "This start page is not valid");

						} catch (final ExceptionBadEndPageTitle e) {

							this.ErrorBox("Invalid End Page", "This end page is not valid");

						} catch (final ExceptionPageLinksNotStored e) {

							this.ErrorBox("Links Not Stored", "This path is not stored offline");

						}

					}).start();

					break;

				case OFFLINE:

					this.running = true;

					new Thread(() -> {

						try {

							GuiProgram.dijkstra = new DijkstraControl(false, this.fromDijkstraToGuiPageTitles);

							this.fromDijstraToGuiPathWhenFinished.add(GuiProgram.dijkstra.runSearch(new PageTitle(this.tfStartPage.getText()), new PageTitle(this.tfEndPage.getText())));

						} catch (final ExceptionBadStartPageTitle e) {

							this.ErrorBox("Invalid Start Page", "This start page is not valid");

						} catch (final ExceptionBadEndPageTitle e) {

							this.ErrorBox("Invalid End Page", "This end page is not valid");

						} catch (final ExceptionPageLinksNotStored e) {

							this.ErrorBox("Links Not Stored", "This path is not stored offline");

						}

					}).start();

					break;

				case EXPLORE:

					this.running = true;

					new Thread(() -> {

						try {

							GuiProgram.dijkstra = new DijkstraControl(true, this.fromDijkstraToGuiPageTitles);

							GuiProgram.dijkstra.runSearch(null, new PageTitle(this.tfStartPage.getText()));

						} catch (final ExceptionBadStartPageTitle e) {

							this.ErrorBox("Invalid Start Page", "This start page is not valid");

						} catch (final ExceptionBadEndPageTitle e) {

							this.ErrorBox("Invalid End Page", "This end page is not valid");

						} catch (final ExceptionPageLinksNotStored e) {

							this.ErrorBox("Links Not Stored", "This path is not stored offline");

						}

					}).start();

					break;

				default:
					throw new RuntimeException("No Mode selected");
			}

		}
	}

	private void TabSelected(final TabPane t, final Label lbl, final TextField tf) {

		if (t.getSelectionModel().getSelectedItem().getText() == this.EXPLORE) {
			this.mode = Mode.EXPLORE;
			lbl.setVisible(false);
			tf.setVisible(false);
		} else {
			if (t.getSelectionModel().getSelectedItem().getText() == this.ONLINE) {

				this.mode = Mode.ONLINE;

			} else {

				this.mode = Mode.OFFLINE;

			}
			lbl.setVisible(true);
			tf.setVisible(true);
		}
	}

}