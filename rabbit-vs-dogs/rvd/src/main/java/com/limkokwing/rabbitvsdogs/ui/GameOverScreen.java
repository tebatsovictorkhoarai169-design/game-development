package com.limkokwing.rabbitvsdogs.ui;

import com.limkokwing.rabbitvsdogs.app.RabbitVsDogsApp;
import com.limkokwing.rabbitvsdogs.engine.GameEngine;
import com.limkokwing.rabbitvsdogs.model.Difficulty;
import com.limkokwing.rabbitvsdogs.util.Assets;
import com.limkokwing.rabbitvsdogs.util.Theme;
import com.limkokwing.rabbitvsdogs.util.UiFactory;
import com.limkokwing.rabbitvsdogs.util.VideoBackground;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameOverScreen
{
    private final RabbitVsDogsApp app;
    private final GameEngine engine;
    private final Difficulty difficulty;

    public GameOverScreen(RabbitVsDogsApp app, GameEngine engine, Difficulty difficulty)
    {
        this.app = app;
        this.engine = engine;
        this.difficulty = difficulty;
    }
    public Parent build()
    {
        StackPane root = new StackPane();
        VideoBackground video = new VideoBackground(root);

        boolean rabbitWon = engine.getOutcome() == GameEngine.Outcome.RABBIT_WIN_TIMEOUT ||
                engine.getOutcome() == GameEngine.Outcome.RABBIT_WIN_DOGS_STUCK;

        ImageView logo = new ImageView(new Image(Assets.url(Assets.LOGO_IMAGE)));
        logo.setPreserveRatio(true);
        logo.setFitWidth(420);

        Label resultTitle = new Label(rabbitWon ? "\uD83D\uDC07 RABBIT WINS!" : "\uD83D\uDC15 DOGS WIN!");
        resultTitle.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 44));
        resultTitle.setTextFill(Color.web(rabbitWon ? Theme.LIGHT_BLUE : Theme.GOLD));
        resultTitle.setEffect(new DropShadow(14, Color.BLACK));

        Label subtitle = new Label(rabbitWon
                ? "The Rabbit escaped the Dogs!"
                : "The Rabbit was trapped with nowhere left to go.");
        subtitle.setFont(Font.font("System", FontWeight.NORMAL, 16));
        subtitle.setTextFill(Color.web(Theme.TEXT_LIGHT));

        String outcomeText = "";

        if (engine.getOutcome() == GameEngine.Outcome.RABBIT_WIN_TIMEOUT)
        {
            outcomeText = "Rabbit survived the timer!";
        }
        else if (engine.getOutcome() == GameEngine.Outcome.RABBIT_WIN_DOGS_STUCK)
        {
            outcomeText = "Dogs have no legal moves!";
        }
        else if (engine.getOutcome() == GameEngine.Outcome.DOGS_WIN)
        {
            outcomeText = "Rabbit was trapped!";
        }

        Label scoreLabel = new Label(
                "Outcome: " + outcomeText +
                        "   \u2022   Moves: " + engine.getRabbitMoveCount() +
                        "   \u2022   Difficulty: " + difficulty.getLabel()
        );
        scoreLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        scoreLabel.setTextFill(Color.web(Theme.GOLD));

        Button playAgain = UiFactory.primaryButton("Play Again");
        Button mainMenu = UiFactory.secondaryButton("Main Menu");
        playAgain.setOnAction(e -> app.showGame(difficulty));
        mainMenu.setOnAction(e -> app.showWelcome());

        HBox buttons = new HBox(18, playAgain, mainMenu);
        buttons.setAlignment(Pos.CENTER);

        VBox card = new VBox(16, logo, resultTitle, subtitle, scoreLabel, buttons);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(46));
        card.setMaxWidth(620);
        card.setStyle("-fx-background-color: " + Theme.NAVY_PANEL_TRANSLUCENT + "; -fx-background-radius: 20;"
                + "-fx-border-color: " + (rabbitWon ? Theme.LIGHT_BLUE : Theme.GOLD) + "; -fx-border-width: 2; "
                + "-fx-border-radius: 20;");
        card.setEffect(new DropShadow(26, Color.rgb(0, 0, 0, 0.55)));

        root.getChildren().addAll(video.getView(), card);
        return root;
    }
}