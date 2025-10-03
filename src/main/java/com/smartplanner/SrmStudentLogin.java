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

public class SrmStudentLogin {

    private final Stage stage;
    private VBox view;

    public SrmStudentLogin(Stage stage){
        this.stage = stage;
        loginPageUISrm();
    }

    private void loginPageUISrm(){
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

        TextField netId = new TextField();
        netId.setPromptText("Srm Net Id");

        PasswordField passWord = new PasswordField();
        passWord.setPromptText("Password");

        VBox form = new VBox(10, netId,passWord);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(16));

        view = new VBox(10, topBar, form);
        view.setAlignment(Pos.TOP_CENTER);
        view.setPadding(new Insets(12));

    }

    public VBox getLoginPageSrm(){
        return view;
    }
}
