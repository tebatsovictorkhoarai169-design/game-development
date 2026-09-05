package com.limkokwing.rabbitvsdogs.ui;

import com.limkokwing.rabbitvsdogs.app.RabbitVsDogsApp;
import com.limkokwing.rabbitvsdogs.util.Theme;
import com.limkokwing.rabbitvsdogs.util.UiFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PauseOverlay
{
    private final RabbitVsDogsApp app;
    private final GameScreen gameScreen;

    public PauseOverlay(RabbitVsDogsApp app, GameScreen gameScreen)
    {
        this.app = app;
        this.gameScreen = gameScreen;
    }

    public StackPane build()
    {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(9,20,30,0.72);");

        Label title = new Label("Game Paused");
        title.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 34));
        title.setTextFill(Color.web(Theme.TEXT_LIGHT));
        title.setEffect(new DropShadow(10, Color.BLACK));

        Button resume = UiFactory.consoleButton("Resume");
        Button restart = UiFactory.consoleButton("Restart");
        Button settings = UiFactory.consoleButton("Settings");
        Button mainMenu = UiFactory.consoleButton("Main Menu");

        resume.setOnAction(e -> gameScreen.hidePause());
        restart.setOnAction(e -> app.showGame(gameScreen.getDifficulty()));
        settings.setOnAction(e -> app.showSettings(app::resumeActiveGame));
        mainMenu.setOnAction(e -> app.showWelcome());

        HBox buttonRow = new HBox(18, resume, restart, mainMenu, settings);
        buttonRow.setAlignment(Pos.CENTER);

        VBox card = new VBox(26, title, buttonRow);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(40, 50, 40, 50));
        card.setStyle(
                "-fx-background-color: " + Theme.TEAL_DARKER_TRANSLUCENT + ";" +
                "-fx-background-radius: 14;" +
                "-fx-border-color: " + Theme.LIGHT_BLUE + ";" +
                "-fx-border-width: 2;" +
                "-fx-border-radius: 14;"
        );
        card.setEffect(new DropShadow(30, Color.rgb(0, 0, 0, 0.6)));
        card.setMaxWidth(720);

        overlay.getChildren().add(card);
        return overlay;
    }
}
