package com.smartplanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.control.*;

public class Dashboard {

    String name;
    public Dashboard(String name) {
        this.name = name;
    }

    private Button createPill(String text){
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(48);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0 , 10 , 0 , 10));

        String base = String.join("; ",
                "-fx-background-color: linear-gradient(to right,#ffffff,#eee6ff,#d9cffa)",
                "-fx-background-radius: 12px",
                "-fx-border-color: rgba(0,0,0,0.18)",
                "-fx-border-radius: 12px",
                "-fx-border-width: 1",
                "-fx-font-size: 14px",
                "-fx-font-weight: 700",
                "-fx-text-fill: #2c2c2c",
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.18), 6, 0, 0, 2)"
        );
        btn.setStyle(base);

        btn.setOnMouseEntered(e -> btn.setStyle(base.replace("#eee6ff","#f4eeff")));
        btn.setOnMouseExited(e -> btn.setStyle(base));
        return  btn;
    }

    public void DashboardView(Stage stage) {
        BorderPane root = new BorderPane();
//        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #eafafa;");

        VBox sideBar = new VBox();
        sideBar.setPrefWidth(320);
        sideBar.setPadding(new Insets(24));
        sideBar.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #9a86f8, #5636d4);" +
                "-fx-background-radius: 0 26 26 0;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 2);"
        );
        root.setLeft(sideBar);
        sideBar.setAlignment(Pos.TOP_CENTER);  // center children horizontally
        sideBar.setFillWidth(true);



        Image imgLogo = new Image(getClass().getResource("/Images/LOGO.png").toExternalForm());

        ImageView logo = new ImageView(imgLogo);
        logo.setFitWidth(150);
        logo.setFitHeight(150);
        logo.setPreserveRatio(true);
        Circle logoClip = new Circle(75,75,75);
        logo.setClip(logoClip);

        StackPane logoHolder = new StackPane(logo);
        logoHolder.setPadding(new Insets(16, 0, 30, 0));

        Button btnName = createPill(name.toUpperCase());
        Button btnCourse = createPill("CSE");
        Button btnYear = createPill("2 Year");
        Button btnUpcmgTsk = createPill("3 Tasks");
        Button btnLogout = createPill("Log Out!");

        VBox.setMargin(btnName, new Insets(6, 0, 6, 0));
        VBox.setMargin(btnCourse, new Insets(6, 0, 6, 0));
        VBox.setMargin(btnYear, new Insets(6, 0, 6, 0));
        VBox.setMargin(btnUpcmgTsk, new Insets(6, 0, 6, 0));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sideBar.getChildren().addAll(
                logoHolder,
                btnName,
                btnCourse,
                btnYear,
                btnUpcmgTsk,
                spacer,
                btnLogout);

        Pane pane = new Pane();
        pane.setPadding(new Insets(20));
        root.setCenter(pane);

        Scene scene = new Scene(root, 1200, 720);
        stage.setScene(scene);
        stage.setTitle("Dashboard UniSync");
        stage.show();
    }
}
