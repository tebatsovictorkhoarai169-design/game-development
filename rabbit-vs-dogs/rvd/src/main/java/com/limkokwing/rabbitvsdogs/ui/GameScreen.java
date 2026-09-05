package com.limkokwing.rabbitvsdogs.ui;

import com.limkokwing.rabbitvsdogs.app.RabbitVsDogsApp;
import com.limkokwing.rabbitvsdogs.engine.GameEngine;
import com.limkokwing.rabbitvsdogs.model.BoardGraph;
import com.limkokwing.rabbitvsdogs.model.Difficulty;
import com.limkokwing.rabbitvsdogs.util.Assets;
import com.limkokwing.rabbitvsdogs.util.Theme;
import com.limkokwing.rabbitvsdogs.util.VideoBackground;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

import java.util.*;

public class GameScreen
{
    private final RabbitVsDogsApp app;
    private final Difficulty difficulty;
    private final GameEngine engine;
    private int secondsRemaining;

    private StackPane rootStack;
    private Pane boardPane;
    private Label timerLabel;
    private Label turnLabel;
    private Label movesLabel;
    private ProgressBar rabbitHealthBar;
    private ProgressBar dogsPressureBar;
    private StackPane pauseLayer;
    private VideoBackground videoBackground;
    private Timeline countdown;
    private PauseTransition rabbitThinking;
    private boolean interactionLocked = false;

    private Integer selectedDog = null;
    private final Set<Integer> highlightedMoves = new HashSet<>();
    private final Map<Integer, Circle> circlesByNode = new HashMap<>();

    private final List<int[]> edgeList;

    public GameScreen(RabbitVsDogsApp app, Difficulty difficulty)
    {
        this.app = app;
        this.difficulty = difficulty;
        this.engine = new GameEngine();
        this.secondsRemaining = difficulty.getSeconds();
        this.edgeList = buildEdgeList();
    }

    private List<int[]> buildEdgeList()
    {
        List<int[]> edges = new ArrayList<>();
        for (int node : BoardGraph.ADJACENCY.keySet())
        {
            for (int neighbour : BoardGraph.ADJACENCY.get(node))
            {
                if (node < neighbour) edges.add(new int[]{node, neighbour});
            }
        }
        return edges;
    }

    public Parent build()
    {
        if (rootStack != null) return rootStack;

        rootStack = new StackPane();
        videoBackground = new VideoBackground(rootStack);

        BorderPane layout = new BorderPane();
        layout.setTop(buildHeader());
        layout.setLeft(buildLeftPanel());
        layout.setRight(buildRightPanel());
        layout.setCenter(buildBoardArea());
        layout.setBottom(buildControlBar());

        pauseLayer = new PauseOverlay(app, this).build();
        pauseLayer.setVisible(false);
        pauseLayer.setManaged(false);

        rootStack.getChildren().addAll(videoBackground.getView(), layout, pauseLayer);

        redrawBoard();
        updateHud();
        startCountdown();

        if (engine.getTurn() == GameEngine.Turn.RABBIT)
        {
            scheduleRabbitMove();
        }

        return rootStack;
    }

    private HBox buildHeader()
    {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10, 24, 10, 24));
        header.setStyle("-fx-background-color: " + Theme.TEAL_DARK + ";");
        header.setEffect(new DropShadow(6, Color.rgb(0, 0, 0, 0.4)));

        ImageView logo = new ImageView(new Image(Assets.url(Assets.LOGO_IMAGE)));
        logo.setPreserveRatio(true);
        logo.setFitHeight(46);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label levelLabel = new Label("Level: " + difficulty.getLabel());
        levelLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        levelLabel.setTextFill(Color.web(Theme.GOLD));

        timerLabel = new Label(formatTime(secondsRemaining));
        timerLabel.setFont(Font.font("System", FontWeight.EXTRA_BOLD, 26));
        timerLabel.setTextFill(Color.web(Theme.GOLD));

        VBox rightBox = new VBox(2, levelLabel, timerLabel);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        header.getChildren().addAll(logo, spacer, rightBox);
        return header;
    }

    private VBox buildLeftPanel()
    {
        VBox panel = new VBox(14);
        panel.setPadding(new Insets(20, 16, 20, 16));
        panel.setPrefWidth(220);
        panel.setAlignment(Pos.TOP_CENTER);
        panel.setStyle("-fx-background-color: " + Theme.PANEL_BLUE_GRAY + ";");

        Label heading = new Label("Dogs Pressure");
        heading.setFont(Font.font("System", FontWeight.BOLD, 15));
        heading.setTextFill(Color.WHITE);

        dogsPressureBar = new ProgressBar(0.35);
        dogsPressureBar.setPrefWidth(180);
        dogsPressureBar.setStyle("-fx-accent: " + Theme.SUCCESS_GREEN + ";");

        ImageView dogImage = new ImageView(new Image(Assets.url(Assets.SETTING_DOG_IMAGE)));
        dogImage.setPreserveRatio(true);
        dogImage.setFitWidth(150);
        dogImage.setEffect(new DropShadow(6, Color.rgb(0, 0, 0, 0.35)));

        Label movesHeading = new Label("Moves Made");
        movesHeading.setFont(Font.font("System", FontWeight.BOLD, 13));
        movesHeading.setTextFill(Color.WHITE);

        movesLabel = new Label("0");
        movesLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        movesLabel.setTextFill(Color.web(Theme.GOLD));

        VBox box = new VBox(10, heading, dogsPressureBar, dogImage, movesHeading, movesLabel);
        box.setAlignment(Pos.TOP_CENTER);
        panel.getChildren().add(box);
        return panel;
    }

    private VBox buildRightPanel()
    {
        VBox panel = new VBox(14);
        panel.setPadding(new Insets(20, 16, 20, 16));
        panel.setPrefWidth(220);
        panel.setAlignment(Pos.TOP_CENTER);
        panel.setStyle("-fx-background-color: " + Theme.PANEL_BLUE_GRAY + ";");

        Label heading = new Label("Rabbit Health");
        heading.setFont(Font.font("System", FontWeight.BOLD, 15));
        heading.setTextFill(Color.WHITE);

        rabbitHealthBar = new ProgressBar(1.0);
        rabbitHealthBar.setPrefWidth(180);
        rabbitHealthBar.setStyle("-fx-accent: " + Theme.SUCCESS_GREEN + ";");

        ImageView rabbitImage = new ImageView(new Image(Assets.url(Assets.SITTING_RABBIT_IMAGE)));
        rabbitImage.setPreserveRatio(true);
        rabbitImage.setFitWidth(150);
        rabbitImage.setEffect(new DropShadow(6, Color.rgb(0, 0, 0, 0.35)));

        turnLabel = new Label();
        turnLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        turnLabel.setTextFill(Color.WHITE);
        updateTurnLabelText();

        VBox box = new VBox(10, heading, rabbitHealthBar, rabbitImage, turnLabel);
        box.setAlignment(Pos.TOP_CENTER);
        panel.getChildren().add(box);
        return panel;
    }

    private StackPane buildBoardArea()
    {
        StackPane wrapper = new StackPane();
        wrapper.setPadding(new Insets(18));

        boardPane = new Pane();
        boardPane.setStyle("-fx-background-color: " + Theme.BOARD_BG + "; -fx-background-radius: 10;");
        boardPane.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.25)));

        wrapper.getChildren().add(boardPane);
        boardPane.prefWidthProperty().bind(wrapper.widthProperty());
        boardPane.prefHeightProperty().bind(wrapper.heightProperty());

        boardPane.widthProperty().addListener((o, a, b) -> redrawBoard());
        boardPane.heightProperty().addListener((o, a, b) -> redrawBoard());

        return wrapper;
    }

    private void redrawBoard()
    {
        if (boardPane == null) return;
        double w = boardPane.getWidth();
        double h = boardPane.getHeight();
        if (w <= 0 || h <= 0) return;

        boardPane.getChildren().clear();
        circlesByNode.clear();

        double margin = Math.min(w, h) * 0.10;
        double usableW = w - margin * 2;
        double usableH = h - margin * 2;

        // Determine bounds of POSITIONS
        double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;
        for (double[] pos : BoardGraph.POSITIONS.values())
        {
            minX = Math.min(minX, pos[0]);
            maxX = Math.max(maxX, pos[0]);
            minY = Math.min(minY, pos[1]);
            maxY = Math.max(maxY, pos[1]);
        }
        double rangeX = maxX - minX;
        double rangeY = maxY - minY;
        if (rangeX == 0) rangeX = 1;
        if (rangeY == 0) rangeY = 1;

        // Draw edges
        for (int[] edge : edgeList)
        {
            double[] p1 = BoardGraph.POSITIONS.get(edge[0]);
            double[] p2 = BoardGraph.POSITIONS.get(edge[1]);
            double x1 = margin + ((p1[0] - minX) / rangeX) * usableW;
            double y1 = margin + ((p1[1] - minY) / rangeY) * usableH;
            double x2 = margin + ((p2[0] - minX) / rangeX) * usableW;
            double y2 = margin + ((p2[1] - minY) / rangeY) * usableH;
            Line line = new Line(x1, y1, x2, y2);
            line.setStroke(Color.web(Theme.EDGE_LINE));
            line.setStrokeWidth(Math.max(1.5, Math.min(w, h) * 0.006));
            boardPane.getChildren().add(line);
        }

        // Draw nodes
        double radius = Math.max(14, Math.min(w, h) * 0.045);
        for (int id = 0; id < BoardGraph.NODE_COUNT; id++)
        {
            double[] pos = BoardGraph.POSITIONS.get(id);
            double cx = margin + ((pos[0] - minX) / rangeX) * usableW;
            double cy = margin + ((pos[1] - minY) / rangeY) * usableH;

            Circle circle = new Circle(cx, cy, radius);
            circle.setStroke(Color.web(Theme.NODE_BORDER));
            circle.setStrokeWidth(2.5);
            styleNodeFill(circle, id);
            final int nodeId = id;
            circle.setOnMouseClicked(e -> onNodeClicked(nodeId));

            Label idLabel = new Label(String.valueOf(id));
            idLabel.setFont(Font.font("System", FontWeight.NORMAL, Math.max(10, radius * 0.6)));
            idLabel.setTextFill(Color.web("#9AA0A6"));
            idLabel.setLayoutX(cx - radius * 0.3);
            idLabel.setLayoutY(cy - radius * 2.1);
            idLabel.setMouseTransparent(true);

            boardPane.getChildren().addAll(circle, idLabel);
            circlesByNode.put(id, circle);
        }
    }

    private void styleNodeFill(Circle circle, int nodeId)
    {
        int dogIdx = engine.dogAt(nodeId);
        boolean isRabbit = engine.getRabbitPos() == nodeId;
        boolean isSelectedDog = selectedDog != null && engine.getDogPos(selectedDog) == nodeId;
        boolean isHighlighted = highlightedMoves.contains(nodeId);

        if (isSelectedDog)
        {
            circle.setFill(Color.web(Theme.GOLD_SELECTED));
        }
        else if (isHighlighted)
        {
            circle.setFill(Color.web(Theme.LEGAL_MOVE_HIGHLIGHT));
        }
        else if (dogIdx != -1)
        {
            circle.setFill(Color.web(Theme.DOG_FILL));
        }
        else if (isRabbit)
        {
            circle.setFill(Color.web(Theme.RABBIT_FILL));
        }
        else
        {
            circle.setFill(Color.web(Theme.NODE_EMPTY_FILL));
        }
    }

    // Interaction
    private void onNodeClicked(int nodeId)
    {
        // Only allow clicks when it's the dogs' turn, not paused, not game over
        if (interactionLocked || engine.isGameOver() || engine.getTurn() != GameEngine.Turn.DOGS)
        {
            return;
        }

        int dogIdx = engine.dogAt(nodeId);

        if (dogIdx != -1)
        {
            // Clicked a dog → select it and show its legal moves
            selectedDog = dogIdx;

            highlightedMoves.clear();
            highlightedMoves.addAll(engine.legalMovesFrom(nodeId));
            refreshAllNodeStyles();
            return;
        }

        if (selectedDog != null && highlightedMoves.contains(nodeId))
        {
            // Clicked a highlighted destination → move the selected dog
            moveDog(selectedDog, nodeId);
            return;
        }

        // Clicked an empty or non‑move node → clear selection
        selectedDog = null;
        highlightedMoves.clear();
        refreshAllNodeStyles();
    }

    private void moveDog(int dogIndex, int destination)
    {
        if (engine.moveDog(dogIndex, destination))
        {
            selectedDog = null;
            highlightedMoves.clear();
            refreshAllNodeStyles();
            updateHud();

            if (checkForGameOver()) return;

            scheduleRabbitMove();
        }
    }

    // Rabbit AI
    private void scheduleRabbitMove()
    {
        if (engine.isGameOver()) return;

        if (!engine.anyDogHasAMove())
        {
            checkForGameOver();
            return;
        }

        interactionLocked = true;
        rabbitThinking = new PauseTransition(Duration.millis(650));
        rabbitThinking.setOnFinished(e -> performRabbitMove());
        rabbitThinking.play();
    }

    private void performRabbitMove()
    {
        if (engine.isGameOver())
        {
            interactionLocked = false;
            return;
        }

        int destination = chooseRabbitMove();
        if (destination == -1)
        {
            checkForGameOver();
            return;
        }

        int from = engine.getRabbitPos();
        animatePieceMove(from, destination, () -> {
            engine.moveRabbit(destination);
            refreshAllNodeStyles();
            updateHud();

            if (checkForGameOver())
            {
                return;
            }

            interactionLocked = false;
            updateHud();
        });
    }

    private int chooseRabbitMove()
    {
        List<Integer> options = engine.legalMovesFrom(engine.getRabbitPos());
        if (options.isEmpty()) return -1;

        int bestNode = options.get(0);
        int bestScore = -1;

        for (int candidate : options)
        {
            int freeNeighbours = 0;
            for (int n : BoardGraph.neighbours(candidate))
            {
                if (!engine.isOccupied(n)) freeNeighbours++;
            }
            if (freeNeighbours > bestScore)
            {
                bestScore = freeNeighbours;
                bestNode = candidate;
            }
        }
        return bestNode;
    }

    // Animation
    private void animatePieceMove(int fromNode, int toNode, Runnable onFinished)
    {
        Circle circle = circlesByNode.get(fromNode);
        if (circle == null)
        {
            onFinished.run();
            return;
        }

        double w = boardPane.getWidth(), h = boardPane.getHeight();
        double margin = Math.min(w, h) * 0.10;
        double usableW = w - margin * 2;
        double usableH = h - margin * 2;

        double minX = Double.MAX_VALUE, maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE, maxY = -Double.MAX_VALUE;

        for (double[] pos : BoardGraph.POSITIONS.values())
        {
            minX = Math.min(minX, pos[0]);
            maxX = Math.max(maxX, pos[0]);
            minY = Math.min(minY, pos[1]);
            maxY = Math.max(maxY, pos[1]);
        }

        double rangeX = maxX - minX;
        double rangeY = maxY - minY;

        if (rangeX == 0)
        {
            rangeX = 1;
        }
        if (rangeY == 0)
        {
            rangeY = 1;
        }

        double[] fromPos = BoardGraph.POSITIONS.get(fromNode);
        double[] toPos = BoardGraph.POSITIONS.get(toNode);
        double fromX = margin + ((fromPos[0] - minX) / rangeX) * usableW;
        double fromY = margin + ((fromPos[1] - minY) / rangeY) * usableH;
        double toX = margin + ((toPos[0] - minX) / rangeX) * usableW;
        double toY = margin + ((toPos[1] - minY) / rangeY) * usableH;

        circle.setTranslateX(0);
        circle.setTranslateY(0);

        TranslateTransition tt = new TranslateTransition(Duration.millis(400), circle);
        tt.setFromX(0);
        tt.setFromY(0);
        tt.setToX(toX - fromX);
        tt.setToY(toY - fromY);
        tt.setOnFinished(e -> {
            circle.setTranslateX(0);
            circle.setTranslateY(0);
            onFinished.run();
        });
        tt.play();
    }

    private void refreshAllNodeStyles()
    {
        for (Map.Entry<Integer, Circle> entry : circlesByNode.entrySet())
        {
            styleNodeFill(entry.getValue(), entry.getKey());
        }
    }

    // Timer
    private void startCountdown()
    {
        countdown = new Timeline(new KeyFrame(Duration.seconds(1), e -> {

            if (engine.isGameOver())
            {
                countdown.stop();
                return;
            }

            secondsRemaining--;
            timerLabel.setText(formatTime(secondsRemaining));

            engine.onTimerTick(secondsRemaining);

            if (engine.isGameOver() || secondsRemaining <= 0)
            {
                checkForGameOver();
            }

            updateHud();
        }));
        countdown.setCycleCount(Timeline.INDEFINITE);
        countdown.play();
    }

    public void pauseTimer()
    {
        if (countdown != null) countdown.pause();
    }

    public void resumeTimer()
    {
        if (countdown != null && !engine.isGameOver()) countdown.play();
    }

    private String formatTime(int seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }

    private void updateHud()
    {
        timerLabel.setText(formatTime(secondsRemaining));
        movesLabel.setText(String.valueOf(engine.getRabbitMoveCount()));
        updateTurnLabelText();

        double dogPressure = Math.min(1.0, engine.getRabbitMoveCount() * 0.05 + 0.15);
        dogsPressureBar.setProgress(dogPressure);

        double timeFraction = difficulty.getSeconds() == 0
                ? 0
                : (double) secondsRemaining / difficulty.getSeconds();
        rabbitHealthBar.setProgress(Math.max(0, timeFraction));
    }

    private void updateTurnLabelText()
    {
        if (turnLabel == null) return;

        if (engine.isGameOver())
        {
            turnLabel.setText("Game Over");
            return;
        }
        String who = engine.getTurn() == GameEngine.Turn.DOGS
                ? "Your Turn: DOGS \uD83D\uDC15"
                : "Rabbit thinking... \uD83D\uDC07";
        turnLabel.setText(who);
    }

    private boolean checkForGameOver()
    {
        if (!engine.isGameOver()) return false;

        interactionLocked = true;
        pauseTimer();
        app.showGameOver(engine, difficulty);
        return true;
    }

    // Control Bar
    private HBox buildControlBar()
    {
        HBox bar = new HBox(16);
        bar.setAlignment(Pos.CENTER);
        bar.setPadding(new Insets(12));
        bar.setStyle("-fx-background-color: " + Theme.TEAL_DARK + ";");

        Button pauseBtn = com.limkokwing.rabbitvsdogs.util.UiFactory.consoleButton("Pause");
        pauseBtn.setPrefWidth(140);
        pauseBtn.setPrefHeight(44);
        pauseBtn.setOnAction(e -> showPause());

        bar.getChildren().addAll(pauseBtn);
        return bar;
    }

    public void showPause()
    {
        interactionLocked = true;
        pauseTimer();
        if (rabbitThinking != null) rabbitThinking.pause();
        pauseLayer.setVisible(true);
        pauseLayer.setManaged(true);
    }

    public void hidePause()
    {
        pauseLayer.setVisible(false);
        pauseLayer.setManaged(false);

        if (!engine.isGameOver())
        {
            interactionLocked = (engine.getTurn() == GameEngine.Turn.RABBIT);
            resumeTimer();

            if (rabbitThinking != null && rabbitThinking.getStatus() == javafx.animation.Animation.Status.PAUSED)
            {
                rabbitThinking.play();
            }
        }
    }

    public RabbitVsDogsApp getApp()
    {
        return app;
    }
    public Difficulty getDifficulty()
    {
        return difficulty;
    }
    public GameEngine getEngine()
    {
        return engine;
    }
}