package com.smartplanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class landingPage {

    private final Stage stage;

    public landingPage(Stage stage){
        this.stage = stage;
    }

    public Scene getlandingPage(){

        Button button1 = new Button();
        button1.setOnAction(
                e -> {
                    System.out.println("Login Button Was Clicked");
                    LoginPage loginPage = new LoginPage(stage);
                    Scene loginScene = new Scene(loginPage.getLoginPage());
                    loginScene.getStylesheets().add(
                            getClass().getResource("/styles/style.css").toExternalForm()
                    );
                     stage.setScene(loginScene);
                    stage.centerOnScreen();
                });
        button1.setText("Login");
        button1.getStyleClass().add("loginButton");

        Button button2 = new Button();
        button2.setOnAction( e -> {
                    System.out.println("Button 2 Was Clicked");
                    RegisterPage registerPage = new RegisterPage(stage);
                    Scene registerScene = new Scene(registerPage.getRegisterPage());
                    stage.setScene(registerScene);
                    registerScene.getStylesheets().add(
                            getClass().getResource("/styles/style.css").toExternalForm()
                    );
                    stage.setScene(registerScene);
                    stage.centerOnScreen();
                });
        button2.setText("Sign Up");
        button2.getStyleClass().add("loginButton");

        Button button3 = new Button();
        button3.setOnAction( e -> {
                    System.out.println("Button 3 Was Clicked");
                    SrmStudentLogin studentLoginPage = new SrmStudentLogin(stage);
                    Scene studentLoginScene = new Scene(studentLoginPage.getLoginPageSrm());
                    stage.setScene(studentLoginScene);
                    studentLoginScene.getStylesheets().add(
                            getClass().getResource("/styles/style.css").toExternalForm()
                    );
                    stage.centerOnScreen();
                });
        button3.setText("SRM Student Login");
        button3.getStyleClass().add("loginButton");

        Image imgLogo = new Image(getClass().getResource("/Images/LOGO.png").toExternalForm());

        ImageView logo = new ImageView(imgLogo);
        logo.setFitWidth(150);
        logo.setFitHeight(150);
        logo.setPreserveRatio(true);
        Circle logoClip = new Circle(75,75,75);
        logo.setClip(logoClip);

        Text txt = new Text();
        txt.setText("Welcome to UniSync!!..");
        txt.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        HBox rootHbox = new HBox(10);
        rootHbox.getChildren().addAll(button1,button2,button3);
        rootHbox.setAlignment(Pos.CENTER);
        rootHbox.setPadding(new Insets(20));

        VBox rootVbox = new VBox();
        rootVbox.getChildren().addAll(txt,rootHbox);
        rootVbox.setAlignment(Pos.CENTER);
        rootVbox.setMargin(txt, new Insets(20, 0, 20, 0));

        VBox logoContainer = new VBox();
        logoContainer.getChildren().addAll(logo ,rootVbox);
        logoContainer.setPadding(new Insets(24));
        logoContainer.setAlignment(Pos.CENTER);

        Scene sceneLandingPage = new Scene(logoContainer, 400, 900);
        sceneLandingPage.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());
        return  sceneLandingPage;
    }
}
