package com.limkokwing.rabbitvsdogs.app;

import com.limkokwing.rabbitvsdogs.engine.GameEngine;
import com.limkokwing.rabbitvsdogs.model.Difficulty;
import com.limkokwing.rabbitvsdogs.model.GameState;
import com.limkokwing.rabbitvsdogs.ui.GameOverScreen;
import com.limkokwing.rabbitvsdogs.ui.GameScreen;
import com.limkokwing.rabbitvsdogs.ui.InstructionsScreen;
import com.limkokwing.rabbitvsdogs.ui.PauseOverlay;
import com.limkokwing.rabbitvsdogs.ui.SettingsScreen;
import com.limkokwing.rabbitvsdogs.ui.WelcomeScreen;
import com.limkokwing.rabbitvsdogs.util.Assets;
import com.limkokwing.rabbitvsdogs.util.SoundManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class RabbitVsDogsApp extends Application
{
    public static final double MIN_WIDTH = 900;
    public static final double MIN_HEIGHT = 600;

    private Stage stage;
    private Scene scene;

    private GameScreen activeGameScreen;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        this.stage = primaryStage;

        stage.setTitle("Rabbit vs 3 Dogs");
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);

        try
        {
            stage.getIcons().add(new Image(Assets.url(Assets.LOGO_IMAGE)));
        }
        catch (Exception ignored)
        {
            // Icon is a nice-to-have; never let a missing icon crash startup.
        }

        scene = new Scene(new WelcomeScreen(this).build(), 1280, 800);
        stage.setScene(scene);
        stage.show();

        SoundManager.getInstance().startBackgroundMusic();

        stage.setOnCloseRequest(e -> exitApplication());
    }

    public Stage getStage()
    {
        return stage;
    }

    private void setRoot(Parent root)
    {
        scene.setRoot(root);
    }

    public void showWelcome()
    {
        activeGameScreen = null;
        setRoot(new WelcomeScreen(this).build());
    }

    public void showGame(Difficulty difficulty)
    {
        activeGameScreen = new GameScreen(this, difficulty);
        setRoot(activeGameScreen.build());
    }

    public void showInstructions(Runnable onBack)
    {
        setRoot(new InstructionsScreen(this, onBack).build());
    }

    public void showSettings(Runnable onBack)
    {
        setRoot(new SettingsScreen(this, onBack).build());
    }

    public void resumeActiveGame()
    {
        if (activeGameScreen != null)
        {
            setRoot(activeGameScreen.build());
        }
        else
        {
            showWelcome();
        }
    }

    public void showGameOver(GameEngine engine, Difficulty difficulty)
    {
        setRoot(new GameOverScreen(this, engine, difficulty).build());
    }

    public void exitApplication()
    {
        SoundManager.getInstance().stopBackgroundMusic();
        Platform.exit();
        System.exit(0);
    }
}
