package com.smartplanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

        PasswordField passWord1 = new PasswordField();
        passWord1.setPromptText("Enter Password");

        PasswordField passWord2 = new PasswordField();
        passWord2.setPromptText("Confirm Password");

        VBox form = new VBox(10, userName,passWord1,passWord2);
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
