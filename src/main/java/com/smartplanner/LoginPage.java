package com.smartplanner;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.geometry.Insets;


import java.awt.*;

public class LoginPage {

    private final Stage stage;
    private VBox view;

    public LoginPage(Stage stage){
        this.stage = stage;
        loginPageUI();
    }

    private void loginPageUI(){
        Button backButton = new Button("<- Back");
        backButton.getStyleClass().add("backButton");
        backButton.setOnAction(e ->{
            landingPage landing = new landingPage(stage);
            Scene backScene = landing.getlandingPage();
                stage.setScene(backScene);
        });
        backButton.getStyleClass().add("loginButton");

        HBox topBar = new HBox(backButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(12));

        TextField userName = new TextField();
        userName.setPromptText("UserName");

        VBox form = new VBox(10, userName);
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
