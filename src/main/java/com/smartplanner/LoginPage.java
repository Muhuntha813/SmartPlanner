package com.smartplanner;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.geometry.Insets;


//import java.awt.*;

public class LoginPage {

    private final Stage stage;
    private VBox view;

    public LoginPage(Stage stage){
        this.stage = stage;
        loginPageUI();
    }

    private void loginPageUI(){
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

        PasswordField passWord = new PasswordField();
        passWord.setPromptText("Password");
        passWord.getStyleClass().add("login-field-pass");

        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().add("loginButton");

        Label status = new Label();
        status.setStyle("-fx-text-fill: red;");

        loginBtn.setOnAction( e -> {
            String u = userName.getText().trim();
            String p = passWord.getText().trim();

            if(u.isEmpty() || p.isEmpty()) {
                status.setText("Enter User Name and PassWord Please!!");
                return;
            }

            AuthService authService = new AuthService();
            boolean isCorrect = authService.checkCredentials(u,p);

            if (isCorrect) {
                // If credentials are correct, go to welcome screen
//                WelcomeView welcome = new WelcomeView(u);
//                Scene scene = new Scene(welcome, 600, 400);
                Dashboard dash = new Dashboard(u);
                dash.DashboardView(stage);
//                stage.setScene(scene);
                stage.setMaximized(true);
                stage.centerOnScreen();
            } else {
                status.setText("Invalid username or password.");
            }
        });

        VBox form = new VBox(10, userName, passWord, loginBtn, status);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(16));

        view = new VBox(10, topBar, form);
        view.setAlignment(Pos.TOP_CENTER);
        view.setPadding(new Insets(12));

    }

    public VBox getLoginPage(){
        return view;
    }

}
