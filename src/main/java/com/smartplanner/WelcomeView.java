package com.smartplanner;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class WelcomeView extends VBox {

    public WelcomeView(String username) {
        setAlignment(Pos.CENTER);
        setSpacing(20);
        setPadding(new Insets(30));

        // Load the logo from resources
        Image imgLogo = new Image(getClass().getResource("/Images/LOGO.png").toExternalForm());
        ImageView logo = new ImageView(imgLogo);
        logo.setFitWidth(120);
        logo.setPreserveRatio(true);

        // Welcome text
        Text greeting = new Text("Hello " + username + "!");
        greeting.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-fill: #2c3e50;");

        Text subText = new Text("Welcome to Smart Planner 🎉");
        subText.setStyle("-fx-font-size: 16px; -fx-fill: #34495e;");

        // Add everything to layout
        getChildren().addAll(logo, greeting, subText);
    }
}
