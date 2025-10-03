package com.smartplanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {

        stage.setMinWidth(700);
        stage.setMinHeight(1000);
        stage.centerOnScreen();

    landingPage landingPageObj = new landingPage(stage);
//    LoginPage loginPageObj = new LoginPage();


    Scene landPageScreen = landingPageObj.getlandingPage();
            stage.setScene(landPageScreen);
            stage.setTitle("Smart Planner");
            stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

//   StackPane root = new StackPane();   Will Use this When I Need To Stack on Top of Each Other
//   VBox root = new VBox();             Will Use this When I Need them to Stack Below Each Other