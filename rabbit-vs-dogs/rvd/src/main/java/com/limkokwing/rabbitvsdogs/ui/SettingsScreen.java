package com.limkokwing.rabbitvsdogs.ui;

import com.limkokwing.rabbitvsdogs.app.RabbitVsDogsApp;
import com.limkokwing.rabbitvsdogs.model.Difficulty;
import com.limkokwing.rabbitvsdogs.util.Assets;
import com.limkokwing.rabbitvsdogs.util.SettingsManager;
import com.limkokwing.rabbitvsdogs.util.Theme;
import com.limkokwing.rabbitvsdogs.util.UiFactory;
import com.limkokwing.rabbitvsdogs.util.VideoBackground;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SettingsScreen
{
    private final RabbitVsDogsApp app;
    private final Runnable onBack;
    private final SettingsManager settings = SettingsManager.getInstance();

    public SettingsScreen(RabbitVsDogsApp app, Runnable onBack)
    {
        this.app = app;
        this.onBack = onBack;
    }

    public Parent build()
    {
        StackPane root = new StackPane();
        VideoBackground video = new VideoBackground(root);

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(30));

        ImageView logo = new ImageView(new Image(Assets.url(Assets.LOGO_IMAGE)));
        logo.setPreserveRatio(true);
        logo.setFitHeight(48);
        layout.setTop(logo);
        BorderPane.setAlignment(logo, Pos.CENTER_LEFT);

        VBox card = new VBox(24);
        card.setPadding(new Insets(36, 46, 36, 46));
        card.setMaxWidth(560);
        card.setStyle("-fx-background-color: " + Theme.NAVY_PANEL_TRANSLUCENT + "; -fx-background-radius: 18;"
                + "-fx-border-color: " + Theme.LIGHT_BLUE + "; -fx-border-width: 1.5; -fx-border-radius: 18;");

        Label title = new Label("Settings");
        title.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 30));
        title.setTextFill(Color.web(Theme.GOLD));

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(24);

        /* Sound Effects
        grid.add(rowLabel("\uD83D\uDD0A  Sound Effects:"), 0, 0);
        ToggleButton sfxToggle = greenToggle(settings.soundEffectsOnProperty().get());
        sfxToggle.selectedProperty().bindBidirectional(settings.soundEffectsOnProperty());
        grid.add(sfxToggle, 1, 0);*/

        // Background Music
        grid.add(rowLabel("\uD83C\uDFB5  Background Music:"), 0, 1);
        ToggleButton musicToggle = greenToggle(settings.musicOnProperty().get());
        musicToggle.selectedProperty().bindBidirectional(settings.musicOnProperty());
        grid.add(musicToggle, 1, 1);

        // Difficulty
        grid.add(rowLabel("\u23F1  Difficulty:"), 0, 2);
        ComboBox<Difficulty> difficultyBox = new ComboBox<>();
        difficultyBox.getItems().addAll(Difficulty.values());
        difficultyBox.setValue(settings.getDifficulty());
        difficultyBox.valueProperty().bindBidirectional(settings.difficultyProperty());
        difficultyBox.setStyle("-fx-font-size: 13px;");
        grid.add(difficultyBox, 1, 2);

        // Volume
        grid.add(rowLabel("\uD83D\uDD09  Volume:"), 0, 3);
        Slider volumeSlider = new Slider(0, 1, settings.getVolume());
        volumeSlider.setPrefWidth(200);
        volumeSlider.valueProperty().bindBidirectional(settings.volumeProperty());
        grid.add(volumeSlider, 1, 3);

        Button back = UiFactory.secondaryButton("Back");
        back.setOnAction(e -> onBack.run());

        VBox backRow = new VBox(back);
        backRow.setAlignment(Pos.CENTER_RIGHT);
        backRow.setPadding(new Insets(10, 0, 0, 0));

        card.getChildren().addAll(title, grid, backRow);

        StackPane centered = new StackPane(card);
        layout.setCenter(centered);

        root.getChildren().addAll(video.getView(), layout);
        return root;
    }

    private Label rowLabel(String text)
    {
        Label l = new Label(text);
        l.setFont(Font.font("System", FontWeight.NORMAL, 16));
        l.setTextFill(Color.web(Theme.TEXT_LIGHT));
        return l;
    }

    //ON/OFF switch
    private ToggleButton greenToggle(boolean initiallyOn)
    {
        ToggleButton toggle = new ToggleButton();
        toggle.setSelected(initiallyOn);
        toggle.setPrefWidth(56);
        toggle.setPrefHeight(28);
        applyToggleStyle(toggle);
        toggle.selectedProperty().addListener((obs, o, n) -> applyToggleStyle(toggle));
        return toggle;
    }

    private void applyToggleStyle(ToggleButton toggle)
    {
        boolean on = toggle.isSelected();
        String bg = on ? Theme.TOGGLE_GREEN : "#4A5568";
        String alignment = on ? "-fx-alignment: CENTER_RIGHT;" : "-fx-alignment: CENTER_LEFT;";
        toggle.setStyle(
                "-fx-background-radius: 20;" +
                "-fx-background-color: " + bg + ";" +
                alignment +
                "-fx-padding: 2 4 2 4;" +
                "-fx-cursor: hand;"
        );
        toggle.setText("\u25CF");
        toggle.setTextFill(Color.WHITE);
    }
}
