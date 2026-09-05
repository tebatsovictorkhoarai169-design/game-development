package com.limkokwing.rabbitvsdogs.ui;

import com.limkokwing.rabbitvsdogs.app.RabbitVsDogsApp;
import com.limkokwing.rabbitvsdogs.util.Assets;
import com.limkokwing.rabbitvsdogs.util.Theme;
import com.limkokwing.rabbitvsdogs.util.UiFactory;
import com.limkokwing.rabbitvsdogs.util.VideoBackground;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class InstructionsScreen
{
    private final RabbitVsDogsApp app;
    private final Runnable onBack;

    public InstructionsScreen(RabbitVsDogsApp app, Runnable onBack)
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
        BorderPane.setAlignment(logo, Pos.CENTER_LEFT);
        layout.setTop(logo);

        VBox card = new VBox(16);
        card.setPadding(new Insets(28, 34, 28, 34));
        card.setMaxWidth(760);
        card.setStyle("-fx-background-color: " + Theme.NAVY_PANEL_TRANSLUCENT + "; -fx-background-radius: 16;");

        Label title = new Label("How to Play");
        title.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 30));
        title.setTextFill(Color.web(Theme.GOLD));

        VBox body = new VBox(18,
                section("Overview",
                        "Rabbit vs 3 Dogs is a node-based pursuit game played on a graph of 11 " +
                        "connected positions (0-10). One white Rabbit starts at node 0; three " +
                        "black Dogs are placed randomly on three different nodes."),
                section("Objective",
                        "Dogs win by trapping the Rabbit so that every node next to it is blocked. " +
                        "The Rabbit wins by surviving until the countdown timer reaches zero, or if " +
                        "all three Dogs run out of legal moves."),
                section("Controls",
                        "Click one of your black Dog pieces to select it - its legal destinations " +
                        "light up in blue. Click a highlighted node to move there. The Rabbit moves " +
                        "automatically on its own turn."),
                section("Rules",
                        "1. Pieces move one step at a time along a connected line only.\n" +
                        "2. A piece can never move onto an already-occupied node.\n" +
                        "3. Turns alternate: Rabbit, then Dogs, then Rabbit again.\n" +
                        "4. The countdown keeps running no matter whose turn it is.\n" +
                        "5. Difficulty (Easy/Medium/Hard/Extreme) controls how long the Rabbit " +
                        "must survive - set it from the Settings screen.")
        );

        ScrollPane scroll = new ScrollPane(body);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(420);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        Button back = UiFactory.secondaryButton("Back");
        back.setOnAction(e -> onBack.run());

        card.getChildren().addAll(title, scroll, back);

        StackPane centered = new StackPane(card);
        layout.setCenter(centered);

        root.getChildren().addAll(video.getView(), layout);
        return root;
    }

    private VBox section(String heading, String bodyText)
    {
        Label h = new Label(heading);
        h.setFont(Font.font("System", FontWeight.BOLD, 17));
        h.setTextFill(Color.web(Theme.LIGHT_BLUE));

        Label b = new Label(bodyText);
        b.setFont(Font.font("System", FontWeight.NORMAL, 14));
        b.setTextFill(Color.web(Theme.TEXT_LIGHT));
        b.setWrapText(true);

        VBox box = new VBox(6, h, b);
        return box;
    }
}