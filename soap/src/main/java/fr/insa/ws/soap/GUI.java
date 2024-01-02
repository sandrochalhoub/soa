package fr.insa.ws.soap;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GUI extends Application {

    private UserWS userWS;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        userWS = new UserWS();

        primaryStage.setTitle("Volunteering App");

        // Layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        // Elements GUI
        Label labelName = new Label("Name:");
        TextField textFieldName = new TextField();

        Label labelUserType = new Label("User type:");
        ChoiceBox<String> userTypeChoiceBox = new ChoiceBox<>();
        userTypeChoiceBox.getItems().addAll("HelpRequester", "Volunteer", "Admin");
        userTypeChoiceBox.setValue("HelpRequester");

        Button addUserButton = new Button("Add user");
        addUserButton.setOnAction(e -> addUser(textFieldName.getText(), userTypeChoiceBox.getValue()));

        Label labelHelpDescription = new Label("Mission description:");
        TextField textFieldHelpDescription = new TextField();

        Label labelUserId = new Label("Requester user ID:");
        TextField textFieldUserId = new TextField();

        Button requestHelpButton = new Button("Request help");
        requestHelpButton.setOnAction(e -> userWS.requestHelp(textFieldHelpDescription.getText(), textFieldUserId.getText()));
        
        Label labelMissionId = new Label("Mission ID:");
        TextField textFieldMissionId = new TextField();
        
        Label labelvolunteerId = new Label("Volunteer user ID:");
        TextField textFieldVolunteerId = new TextField();

        Button acceptMissionButton = new Button("Accept Mission");
        acceptMissionButton.setOnAction(e -> userWS.acceptMission(textFieldVolunteerId.getText(), textFieldMissionId.getText()));

        // Add elements to the layout
        gridPane.add(labelName, 0, 0);
        gridPane.add(textFieldName, 1, 0);
        gridPane.add(labelUserType, 0, 1);
        gridPane.add(userTypeChoiceBox, 1, 1);
        gridPane.add(addUserButton, 2, 0, 1, 2);
        gridPane.add(new Separator(), 0, 2, 3, 1);

        gridPane.add(labelHelpDescription, 0, 3);
        gridPane.add(textFieldHelpDescription, 1, 3);
        gridPane.add(labelUserId, 0, 4);
        gridPane.add(textFieldUserId, 1, 4);
        gridPane.add(requestHelpButton, 2, 3, 1, 2);
        gridPane.add(new Separator(), 0, 5, 3, 1);

        gridPane.add(labelMissionId, 0, 6);
        gridPane.add(textFieldMissionId, 1, 6);
        gridPane.add(labelvolunteerId, 0, 7);
        gridPane.add(textFieldVolunteerId, 1, 7);
        gridPane.add(acceptMissionButton, 2, 6, 1, 2);

        // Display
        Scene scene = new Scene(gridPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private void addUser(String name, String userType) {
        if ("HelpRequester".equals(userType)) {
        	userWS.manageHelpRequester(name);
        } else if ("Volunteer".equals(userType)) {
        	userWS.manageVolunteer(name);
        } else if ("Admin".equals(userType)) {
        	userWS.manageAdmin(name);
        }
    }
}
