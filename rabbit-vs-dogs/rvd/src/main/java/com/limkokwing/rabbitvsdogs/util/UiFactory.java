package com.limkokwing.rabbitvsdogs.util;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public final class UiFactory
{
    private UiFactory() {}

    //Primary call-to-action button
    public static Button primaryButton(String text)
    {
        Button b = new Button(text);
        b.setFont(Font.font("System", FontWeight.BOLD, 16));
        b.setPrefWidth(230);
        b.setPrefHeight(46);
        String base = "-fx-background-radius: 10; -fx-background-color: " + Theme.GOLD + "; "
                + "-fx-text-fill: " + Theme.NAVY_PANEL + "; -fx-cursor: hand;";
        String hover = "-fx-background-radius: 10; -fx-background-color: " + Theme.GOLD_SELECTED + "; "
                + "-fx-text-fill: " + Theme.NAVY_PANEL + "; -fx-cursor: hand;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        b.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.45)));
        return b;
    }

    //outline button
    public static Button secondaryButton(String text)
    {
        Button b = new Button(text);
        b.setFont(Font.font("System", FontWeight.BOLD, 14));
        b.setPrefWidth(180);
        b.setPrefHeight(40);
        String base = "-fx-background-radius: 10; -fx-background-color: transparent; "
                + "-fx-border-color: " + Theme.TEXT_LIGHT + "; -fx-border-radius: 10; -fx-border-width: 2;"
                + "-fx-text-fill: " + Theme.TEXT_LIGHT + "; -fx-cursor: hand;";
        String hover = "-fx-background-radius: 10; -fx-background-color: rgba(255,255,255,0.15); "
                + "-fx-border-color: " + Theme.TEXT_LIGHT + "; -fx-border-radius: 10; -fx-border-width: 2;"
                + "-fx-text-fill: " + Theme.TEXT_LIGHT + "; -fx-cursor: hand;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        return b;
    }

    //Pause button
    public static Button consoleButton(String text) {
        Button b = new Button(text);
        b.setFont(Font.font("System", FontWeight.BOLD, 14));
        b.setPrefWidth(160);
        b.setPrefHeight(64);
        b.setPadding(new Insets(6));
        String base = "-fx-background-radius: 10; "
                + "-fx-background-color: linear-gradient(to bottom, #2AA9D6, #0E6E93); "
                + "-fx-text-fill: white; -fx-border-color: #BDEFFF; -fx-border-radius: 10; -fx-border-width: 1.5;"
                + "-fx-cursor: hand;";
        String hover = "-fx-background-radius: 10; "
                + "-fx-background-color: linear-gradient(to bottom, #3FC3F0, #1584AC); "
                + "-fx-text-fill: white; -fx-border-color: #E4FBFF; -fx-border-radius: 10; -fx-border-width: 1.5;"
                + "-fx-cursor: hand;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        b.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.5)));
        return b;
    }

    //Exit / Quit button
    public static Button dangerButton(String text)
    {
        Button b = new Button(text);
        b.setFont(Font.font("System", FontWeight.BOLD, 16));
        b.setPrefWidth(230);
        b.setPrefHeight(46);
        String base = "-fx-background-radius: 10; -fx-background-color: " + Theme.DANGER_RED + "; "
                + "-fx-text-fill: white; -fx-cursor: hand;";
        String hover = "-fx-background-radius: 10; -fx-background-color: #C62828; "
                + "-fx-text-fill: white; -fx-cursor: hand;";
        b.setStyle(base);
        b.setOnMouseEntered(e -> b.setStyle(hover));
        b.setOnMouseExited(e -> b.setStyle(base));
        b.setEffect(new DropShadow(8, Color.rgb(0, 0, 0, 0.45)));
        return b;
    }
}
