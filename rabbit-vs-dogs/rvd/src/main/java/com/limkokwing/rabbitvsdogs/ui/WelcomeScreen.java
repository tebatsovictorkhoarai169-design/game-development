package com.limkokwing.rabbitvsdogs.ui;

import com.limkokwing.rabbitvsdogs.app.RabbitVsDogsApp;
import com.limkokwing.rabbitvsdogs.model.Difficulty;
import com.limkokwing.rabbitvsdogs.util.Assets;
import com.limkokwing.rabbitvsdogs.util.SettingsManager;
import com.limkokwing.rabbitvsdogs.util.Theme;
import com.limkokwing.rabbitvsdogs.util.UiFactory;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

//Screen 1 - Main Menu. Play button
public class WelcomeScreen
{
    private final RabbitVsDogsApp app;

    public WelcomeScreen(RabbitVsDogsApp app)
    {
        this.app = app;
    }

    public StackPane build()
    {
        StackPane root = new StackPane();

        // Background image
        ImageView bg = new ImageView(new Image(Assets.url(Assets.WELCOME_BACKGROUND_IMAGE)));
        bg.setPreserveRatio(false);
        bg.fitWidthProperty().bind(root.widthProperty());
        bg.fitHeightProperty().bind(root.heightProperty());

        // Logo
        ImageView logo = new ImageView(new Image(Assets.url(Assets.LOGO_IMAGE)));
        logo.setPreserveRatio(true);
        logo.setFitWidth(560);
        logo.setEffect(new DropShadow(18, Color.rgb(0, 0, 0, 0.55)));

        Label subtitle = new Label("Morabaraba Oa 'Mutlanyane");
        subtitle.setFont(Font.font("System", FontWeight.NORMAL, 16));
        subtitle.setTextFill(Color.web(Theme.TEXT_LIGHT));
        subtitle.setOpacity(0.85);

        // Buttons
        Button playBtn = UiFactory.primaryButton("Play Game");
        Button instructionsBtn = UiFactory.primaryButton("Instructions");
        Button settingsBtn = UiFactory.primaryButton("Settings");
        Button exitBtn = UiFactory.dangerButton("Exit / Quit");

        // Play button
        playBtn.setOnAction(e -> {
            Difficulty selectedDifficulty = SettingsManager.getInstance().getDifficulty();
            app.showGame(selectedDifficulty);
        });

        instructionsBtn.setOnAction(e -> app.showInstructions(app::showWelcome));
        settingsBtn.setOnAction(e -> app.showSettings(app::showWelcome));
        exitBtn.setOnAction(e -> app.exitApplication());

        VBox menu = new VBox(16, logo, subtitle, spacer(20), playBtn, instructionsBtn, settingsBtn, exitBtn);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(40));

        // Fade in
        menu.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(600), menu);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        root.getChildren().addAll(bg, menu);
        return root;
    }

    private Region spacer(double h)
    {
        Region r = new Region();
        r.setPrefHeight(h);
        return r;
    }
}