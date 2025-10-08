package com.smartplanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterPage {

    private final Stage stage;
    private VBox view;

    public RegisterPage(Stage stage){
        this.stage = stage;
        RegisterPageUi();
    }

    private void RegisterPageUi(){
        Button backButton = new Button("<- Back");
        backButton.getStyleClass().add("loginButton");
        backButton.setOnAction(e ->{
            landingPage landing = new landingPage(stage);
            Scene backScene = landing.getlandingPage();
            stage.setScene(backScene);
        });

        HBox topBar = new HBox(backButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(12));

        TextField userName = new TextField();
        userName.setPromptText("UserName");
        userName.getStyleClass().add("login-field-user");

        PasswordField passWord1 = new PasswordField();
        passWord1.setPromptText("Enter Password");
        passWord1.getStyleClass().add("login-field-pass");

        PasswordField passWord2 = new PasswordField();
        passWord2.setPromptText("Confirm Password");
        passWord2.getStyleClass().add("login-field-pass");

        TextField currentOccupation = new TextField();
        currentOccupation.setPromptText("Student Course or Work");
        currentOccupation.getStyleClass().add("login-field-pass");

        TextField currentExperience = new TextField();
        currentExperience.setPromptText("Student Year of Study or Work Expereince in Years");
        currentExperience.getStyleClass().add("login-field-pass");

        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().add("loginButton");

        Label status = new Label();
        status.setStyle("-fx-text-fill: red;");

        loginBtn.setOnAction( e -> {
            String u = userName.getText().trim();
            String p1 = passWord1.getText().trim();
            String p2 = passWord2.getText().trim();
            String cO = currentOccupation.getText();
            String cE = currentExperience.getText();


            if(u.isEmpty() && p1.isEmpty() && p2.isEmpty()) {
                status.setText("Enter User Name and Password");
                return;
            }
            else if(!u.isEmpty() && p1.isEmpty() && p2.isEmpty()) {
                status.setText("Enter Password Fields");
                return;
            }
            else if(!u.isEmpty() && !p1.isEmpty() && p2.isEmpty()) {
                status.setText("Confrom PassWord");
                return;
            }
            else if(!p1.isEmpty() && !p2.isEmpty() && !u.isEmpty()) {
                if(p1.equals(p2)) {
                    status.setText("Done and Dusted");
                    status.setStyle("-fx-text-fill: green;");
                    AuthService auth = new AuthService();
                    boolean isCreated = auth.createUser(u,p1,cO,cE);
                    if(isCreated){
                        WelcomeView welcome = new WelcomeView(u);
                        Scene scene = new Scene(welcome, 800, 600);
                        stage.setScene(scene);
                        stage.setMaximized(true);   // fill the screen like you wanted
                        stage.centerOnScreen();
                    }
                    else {
                        status.setText("UserName Already exist");
                        status.setStyle("-fx-text-fill: blue;");
                    }
                }
                else{
                    status.setText("PassWords Dont Match");
                    return;
                }
            }

        });

        VBox form = new VBox(10,
                userName,
                passWord1,
                passWord2,
                currentOccupation,
                currentExperience,
                loginBtn,
                status);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(16));

        view = new VBox(10, topBar, form);
        view.setAlignment(Pos.TOP_CENTER);
        view.setPadding(new Insets(12));
    }

    public VBox getRegisterPage(){
        return view;
    }
}
