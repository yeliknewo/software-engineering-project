import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/*
    GUI Portion of the Wikipedia Racers
    This GUI implements the Dijkstra's algorithm
    and displays the relationship on
    Written by: Emerson Moniz
 */

public class FirstTest extends Application {
    public final String EXPLORE = "Explore Page";
    public final String OFFLINE = "Offline";
    public final String ONLINE = "Online";
    public final String EMPTY = "";

    public void start(Stage primaryStage) {
        // Group root = new Group();
        TabPane tabs = new TabPane();
        AnchorPane root = new AnchorPane();

        //   HBox hbox = addHBox();
        Scene scene = new Scene(root, 1024, 600);
        scene.getStylesheets().add("stylesheet.css");

        // Create tabs for user to choose options
        Tab tabOnline = new Tab(ONLINE);
        Tab tabOffline = new Tab(OFFLINE);
        Tab tabExplore = new Tab(EXPLORE);

        // Create a textArea to display the path
        TextArea taOutput = new TextArea();
        taOutput.setWrapText(true);
        taOutput.setEditable(false);
        taOutput.setText(EMPTY);
        taOutput.setPrefSize(600, 450);
        AnchorPane.setTopAnchor(taOutput, 100.0);
        AnchorPane.setRightAnchor(taOutput, 50.0);

        // Create Textfield to take in data from user
        Label lblStart = new Label("Start Page:");
        Label lblEnd = new Label("End Page:");
        TextField tfStartPage = new TextField();
         TextField tfEndPage = new TextField();

        // Create a GridPane to hold the values in the center
        GridPane gPane = new GridPane();

        gPane.setVgap(20.0);
        gPane.add(lblStart, 0, 0);
        gPane.add(tfStartPage, 1, 0);
        gPane.add(lblEnd, 0, 1);
        gPane.add(tfEndPage, 1, 1);

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
            TabSelected(tabs, lblEnd, tfEndPage);
        });


        AnchorPane.setTopAnchor(tabs, 0.0);


        // Create button to clear text
        Button btnClear = new Button("Clear");
        btnClear.setOnAction(e -> ClearAllValues(tfStartPage, tfEndPage, taOutput));


        //

        // Create button to submit text
        Button btnSubmit = new Button("Submit");
        btnSubmit.setOnAction(e-> SubmitValues(tabs,lblStart,tfStartPage,lblEnd,tfEndPage));


        // Create Hbox to hold the buttons
        HBox hbButtons = new HBox(15, btnClear, btnSubmit);
        hbButtons.setAlignment(Pos.CENTER);

        // Set the buttons to be resizable according to the dimensions of screen
        AnchorPane.setTopAnchor(hbButtons, 200.0);
        AnchorPane.setLeftAnchor(hbButtons, 50.0);

        root.getChildren().addAll(tabs, hbButtons, gPane, taOutput);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void SubmitValues(TabPane t,Label l1,TextField t1,Label l2,TextField t2){
        int errorValue = -1;
        if (t.getSelectionModel().getSelectedItem().getText() == EXPLORE) {
            errorValue=CheckForError(t1);
            DisplayMessage(t1,errorValue);
        }
        else {
            errorValue=CheckForError(t1,t2);
            DisplayMessage(t1,t2,errorValue);
        }
    }


    private void ClearAllValues(TextField t1, TextField t2, TextArea t3) {
        t1.setText(EMPTY);
        t2.setText(EMPTY);
        t3.setText(EMPTY);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cleared Values");
        alert.setHeaderText("All values are cleared!");
        alert.setContentText(null);
        alert.showAndWait();
    }

    private void TabSelected(TabPane t, Label lbl, TextField tf) {
        if (t.getSelectionModel().getSelectedItem().getText() == EXPLORE) {
            lbl.setVisible(false);
            tf.setVisible(false);
        } else {
            lbl.setVisible(true);
            tf.setVisible(true);
        }
    }

    /*
        PRE CONDITION: Instance
        POST CONDITION:

     */
    private boolean IsValidString(TextField x){
        return x.getText().isEmpty();
    }

    private boolean IsValidString(TextField x1,TextField x2){
        if (!(!x1.getText().isEmpty() || !x2.getText().toString().isEmpty())) {
            return true;
        } else return false;
    }

    private void DisplayMessage(TextField t1,int errorNum) {
        if(errorNum == 0) {
            ErrorBox("Error","There is no input! Please type something in!");
        }
        else {
            DisplaySearchMessage(t1);
        }
    }

    private void DisplayMessage(TextField t1,TextField t2,int errorNum){
            if (errorNum >= 0) {
                switch (errorNum) {
                    case 0:
                        ErrorBox("Error", "The input in start page is empty");
                        break;
                    case 1:
                        ErrorBox("Error", "The input in end page is empty");
                        break;
                    default:
                        ErrorBox("Error", "There are no inputs! Please type something in.");
                }
            } else {
                DisplaySearchMessage(t1, t2);
            }
        }

    private void DisplaySearchMessage (TextField t1){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Searching for Pages");
        alert.setContentText("This message is informing you that I am searching for all the links for "
                + t1.getText());
        alert.showAndWait();
    }

    private void DisplaySearchMessage (TextField t1,TextField t2){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Searching for Pages");
        alert.setContentText("This message is informing you that I am searching for the degree between "
                + t1.getText() +" and " + t2.getText() );
        alert.showAndWait();
    }

    private int CheckForError(TextField t1){
        int errorValue=-1;
        if (IsValidString(t1)){
            errorValue=0;
        }
        return errorValue;
    }

    private int CheckForError(TextField t1,TextField t2){
        int errorValue=-1;

        if (IsValidString(t1,t2)){
            errorValue=2;
        }
        else if (IsValidString(t1)){
            errorValue=0;
        }
        else if(IsValidString(t2)){
            errorValue=1;
        }
        else{
            errorValue=-1;
        }
        return errorValue;
    }

    // Create method for checking errors
    private void ErrorBox(String TitleBox,String MessageContent){
        Alert alertJava = new Alert(Alert.AlertType.ERROR);
        alertJava.setTitle(TitleBox);
        alertJava.setContentText(MessageContent);
        alertJava.showAndWait();
    }
}
