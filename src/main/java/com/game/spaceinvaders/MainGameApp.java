package com.game.spaceinvaders;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;

public class MainGameApp extends Application {
    //TODO: size playera a size invaders musíme nastavit podle obrázku na jeho sirku
    //TODO: a esi strela potka spodek aliena

    private static String font =  "Impact";//"Berlin Sans FB Demi Bold";

    private static final String INVADER_IMAGE_PATH = "/com/game/spaceinvaders/alien64x48.png";

    private static final String PLAYER_IMAGE_FULL = "/com/game/spaceinvaders/ship70x86.png";
    private static final String PLAYER_IMAGE_DAMAGED = "/com/game/spaceinvaders/ship70x86dmg2.png";
    private static final String PLAYER_IMAGE_CRITICAL = "/com/game/spaceinvaders/ship70x86dmg3.png";

    private ImageView playerImageView;
    private ColorAdjust grayScale;
    private Text countdownText;
    private PauseTransition countdownPause;
    private boolean isCountingDown = false;


    private Scene gameScene;
    private Timeline timeLine;
    private Timeline timeLineInvaderShoot;
    private static final int DELAY = 20; // ms   ///100
    private static final int FAST_SHOOT_INVADER = 2000; //ms

    private Color playerColor = Color.CADETBLUE;
    private Color invaderColor = Color.DARKRED;
    private Color wallColor = Color.RED;
    private Color projectileColor = Color.YELLOWGREEN;

    private Pane root = new Pane();

    private Text txtLives;
    private Text txtPoints;
    private GameState gameState;

    public static void main(String[] args) {
        launch(args);
    }


    //TODO:zivoty u invaderu v prvni linii a menici se obrazky
    @Override
    public void start(Stage primaryStage) {
        // Vytvoření kořenového Pane
        Pane root = new Pane();
        root.setPrefSize(700, 700);
        root.setStyle("-fx-background-color: black;");

        // Vytvoření textu "Space Invaders"
        Text titleText = new Text("Space Invaders");
        titleText.setFill(Color.web("#fff236"));
        titleText.setFont(Font.font(font, 75));
        titleText.setTextAlignment(TextAlignment.CENTER);
        titleText.setWrappingWidth(592.46875);
        titleText.setLayoutX(54);
        titleText.setLayoutY(333);

        // Vytvoření tlačítka PLAY
        Button playButton = new Button("PLAY");
        playButton.setLayoutX(267);
        playButton.setLayoutY(471);
        playButton.setPrefSize(166, 56);
        playButton.setStyle("-fx-border-color: white; -fx-background-color: black;");
        playButton.setTextFill(Color.WHITE);
        playButton.setFont(Font.font(font, 25));

        // Nastavení akce pro tlačítko PLAY
        playButton.setOnAction(e -> {
            startGame(primaryStage);
        });

        // Přidání prvků do kořenového Pane
        root.getChildren().addAll(titleText, playButton);

        // Vytvoření scény
        Scene mainMenuScene = new Scene(root, 700, 700);
        mainMenuScene.setFill(Color.BLACK);

        // Nastavení scény pro primaryStage
        primaryStage.setScene(mainMenuScene);
        primaryStage.setTitle("Space Invaders");
        primaryStage.show();
    }

    private void showPauseMenu(Stage primaryStage) {
        if (isCountingDown) {
            stopCountdown();
        }


        // Vytvoření kořenového Pane
        Pane root = new Pane();
        root.setPrefSize(700, 700);
        root.setStyle("-fx-background-color: black;");

        // Vytvoření textu "Game paused"
        Text pausedText = new Text("Game paused");
        pausedText.setFill(Color.WHITE);
        pausedText.setFont(Font.font(font, 63));
        pausedText.setTextAlignment(TextAlignment.CENTER);
        pausedText.setWrappingWidth(446.13671875);
        pausedText.setLayoutX(127);
        pausedText.setLayoutY(178);

        // Vytvoření tlačítka CONTINUE
        Button continueButton = createButton("CONTINUE", 267, 244, 166, 56);

        // Vytvoření tlačítka NEW GAME
        Button newGameButton = createButton("NEW GAME", 259, 350, 182, 56);

        // Vytvoření tlačítka EXIT
        Button exitButton = createButton("EXIT", 267, 463, 166, 56);

        // Nastavení akcí pro tlačítka
        continueButton.setOnAction(e -> {
            primaryStage.setScene(gameScene);
            resumeGame();
        });
        newGameButton.setOnAction(e -> {
            startGame(primaryStage);
        });

        exitButton.setOnAction(e -> {
            primaryStage.close();
        });



        // Přidání prvků do kořenového Pane
        root.getChildren().addAll(pausedText, continueButton, newGameButton, exitButton);

        // Vytvoření scény
        Scene pauseScene = new Scene(root, 700, 700);
        pauseScene.setFill(Color.BLACK);

        // Nastavení scény pro primaryStage
        primaryStage.setScene(pauseScene);
    }

    // Pomocná metoda pro vytvoření tlačítka
    private Button createButton(String text, double x, double y, double width, double height) {
        Button button = new Button(text);
        button.setLayoutX(x);
        button.setLayoutY(y);
        button.setPrefSize(width, height);
        button.setStyle("-fx-border-color: white; -fx-background-color: black;");
        button.setTextFill(Color.WHITE);
        button.setFont(Font.font(font, 25));
        return button;
    }
    private void resumeGame() {
        if (isCountingDown) {
            stopCountdown();
        }
        isCountingDown = true;
        grayScale = new ColorAdjust();
        grayScale.setSaturation(-1.0);
        root.setEffect(grayScale);

        countdownText = new Text();
        countdownText.setFont(Font.font(font, 100));
        countdownText.setFill(Color.WHITE);
        countdownText.setX(325);
        countdownText.setY(350);
        root.getChildren().add(countdownText);

        startCountdown(3);
    }

    private void startCountdown(int count) {
        if (count > 0 && isCountingDown) {
            countdownText.setText(Integer.toString(count));
            countdownPause = new PauseTransition(Duration.seconds(1));
            countdownPause.setOnFinished(event -> startCountdown(count - 1));
            countdownPause.play();
        } else if (isCountingDown) {
            stopCountdown();
            if (timeLine != null) timeLine.play();
            if (timeLineInvaderShoot != null) timeLineInvaderShoot.play();
        }
    }
    private void stopCountdown() {
        isCountingDown = false;
        if (countdownPause != null) {
            countdownPause.stop();
        }
        root.getChildren().remove(countdownText);
        root.setEffect(null);
    }
    private void pauseGame() {
        if (isCountingDown) {
            stopCountdown();
        }
        if (timeLine != null) timeLine.pause();
        if (timeLineInvaderShoot != null) timeLineInvaderShoot.pause();
    }


    private void startGame(Stage primaryStage) {
        // Initialize the game
        gameState = new GameState();
        root = new Pane();
        root.setStyle("-fx-background-color: black;");
        manageTexts();

        playerImageView = createGameObject(gameState.getPlayer(), PLAYER_IMAGE_FULL, 1.0);
        root.getChildren().add(playerImageView);

        gameState.livesProperty().addListener((observable, oldValue, newValue) -> {
            updatePlayerImage(newValue.intValue());
        });

        for (GameObject invader : gameState.getInvaders()) {
            root.getChildren().add(createGameObject(invader, INVADER_IMAGE_PATH, 1.0));
        }
        for (Wall wall : gameState.getWalls()) {
            root.getChildren().add(createWall(wall, wallColor));
        }

        setupGameTimelines();

        // Create and set the game scene
        gameScene = new Scene(root, 700, 700);
        gameScene.setFill(Color.BLACK);
        gameScene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                pauseGame();
                showPauseMenu(primaryStage);
            } else {
                dispatchKeyEvents(e);
            }
        });
        gameScene.setOnKeyReleased(this::dispatchKeyEventsShoot);

        primaryStage.setScene(gameScene);


    }

    private void setupGameTimelines() {
        timeLine = new Timeline();
        KeyFrame updates = new KeyFrame(
                Duration.millis(DELAY),
                e -> {
                    gameUpdate();
                }
        );
        timeLine.getKeyFrames().add(updates);
        timeLine.setCycleCount(Animation.INDEFINITE);
        timeLine.play();

        timeLineInvaderShoot = new Timeline();
        KeyFrame updatesInvaderShoot = new KeyFrame(
                Duration.millis(FAST_SHOOT_INVADER),
                e -> {
                    if(gameState.getInvaders().size() == 1) {
                        gameState.invaderFire(0);
                    }
                    else if(!gameState.getInvaders().isEmpty()) {
                        gameState.invaderFireRandom();
                    }
                    gameState.invadersMove();
                }
        );
        timeLineInvaderShoot.getKeyFrames().add(updatesInvaderShoot);
        timeLineInvaderShoot.setCycleCount(Animation.INDEFINITE);
        timeLineInvaderShoot.play();
    }

    private void updatePlayerImage(int lives) {
        String imagePath;
        switch (lives) {
            case 3:
                imagePath = PLAYER_IMAGE_FULL;
                break;
            case 2:
                imagePath = PLAYER_IMAGE_DAMAGED;
                break;
            case 1:
                imagePath = PLAYER_IMAGE_CRITICAL;
                break;
            default:
                return; // Není potřeba měnit obrázek
        }

        Image newImage = new Image(getClass().getResourceAsStream(imagePath));
        playerImageView.setImage(newImage);
    }




    ////

    private ImageView createGameObject(GameObject ship, String imagePath, double scale) {
        Image image = new Image(getClass().getResourceAsStream(imagePath));
        if (image.isError()) {
            System.err.println("Error loading image: " + imagePath);
            return new ImageView(); // Return empty ImageView if image fails to load
        }
        ImageView imageView = new ImageView(image);

        // Centrování obrázku na pozici herního objektu
        imageView.xProperty().bind(ship.rowProperty().subtract((image.getWidth() * scale) / 2));
        imageView.yProperty().bind(ship.colProperty().subtract((image.getWidth() * scale) / 2));



        imageView.visibleProperty().bind(ship.activeProperty());

        // Zvětšení obrázku
        //imageView.setScaleX(scale);
        //imageView.setScaleY(scale);

        System.out.println(imagePath);
        System.out.println(image.getWidth());
        System.out.println(image.getHeight());
        return imageView;
    }
    /////
    private void manageTexts(){
        // Lives
        Text txtLblLives = new Text("Lives: ");
        manageOneTextLb(txtLblLives, 10, 30);

        txtLives = new Text(Integer.toString(gameState.getLives()));
        manageOneTextLb(txtLives, 70, 30);
        txtLives.textProperty().bind(gameState.livesProperty().asString());

        // Points
        Text txtLblPoints = new Text("Points: ");
        manageOneTextLb(txtLblPoints, 570, 30);

        txtPoints = new Text(Integer.toString(gameState.getScore()));
        manageOneTextLb(txtPoints, 640, 30);
        txtPoints.textProperty().bind(gameState.scoreProperty().asString());

        // Přidání všech textových prvků do root
        root.getChildren().addAll(txtLblLives, txtLives, txtLblPoints, txtPoints);

    }

    private void manageOneTextLb(Text text, int x, int y){
        text.setLayoutX(x);
        text.setLayoutY(y);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
    }
    public void gameUpdate() {
        //eliminace nepratel
        gameState.isInvaderKilled();

        //konec hry
        //if (!gameState.isPlayerKilled()) endGame("Game over", false);
        if (!gameState.isPlayerKilled()) {
            updatePlayerImage(gameState.getLives());
            if (gameState.getLives() <= 0) {
                endGame("Game over", false);
            }
        }


        killedAllInvaders();
        isInvadersWon();

        //invaders
        shootsUpdate(gameState.getInvadersShoots(),3);

        //player
        shootsUpdate(gameState.getPlayerShoots(),-3);
        gameState.isWallDestroied();


        //odstrani strely
        for (GameObject circle : gameState.SHOOTS_TO_REMOVE) {
            root.getChildren().remove(circle);
        }
        gameState.SHOOTS_TO_REMOVE.clear();
        //prida strely
        for (GameObject circle : gameState.SHOOTS_TO_ADD) {
            root.getChildren().add(createProjectile(circle, projectileColor));
        }
        gameState.SHOOTS_TO_ADD.clear();

    }
    private void isInvadersWon(){
        GameObject circ = gameState.getInvaders().stream().filter(item -> item.getCol() >= 600  && item.activeProperty().getValue()).findAny().orElse(null);  //735
        if(circ != null){
            endGame("Game over", false);
        }
    }


    private void dispatchKeyEvents(KeyEvent e) {
        switch (e.getCode()) {
            case LEFT:  gameState.moveLeft(); break;
            case RIGHT: gameState.moveRight(); break;
            //       case SPACE: gameState.playerFire(root); break;
            default:
        }
    }
    private void dispatchKeyEventsShoot(KeyEvent e) {
        switch (e.getCode()) {
            case SPACE: gameState.playerFire(); break;
            default:
        }
    }

    private void shootsUpdate(List<GameObject> shoots, int num) {
        if(!shoots.isEmpty()) {
            for(int i = 0; i < shoots.size(); i++) {
                shoots.get(i).setCol(shoots.get(i).getCol() + num);
                if(shoots.get(i).getRow() <= 0) {    ///Y
                    root.getChildren().remove(shoots.get(i));
                    shoots.remove(i);
                }
            }
        }
    }
    private void endGame(String textString, boolean bool){
        Text text = new Text();
        text.setX(200);
        text.setY(400);
        if(bool){
            text.setFill(Color.YELLOW);
            text.setX(230);
        }
        else{
            text.setFill(Color.RED);
            text.setX(200);
        }
        text.setText(textString);
        root.getChildren().add(text);
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 50));


        //zastavi hru
        timeLine.stop();
        timeLineInvaderShoot.stop();

        //TODO: co to je
        pauseGame();

    }
    private void killedAllInvaders(){
        if(gameState.getInvaders().stream().filter(item -> item.isActive()).findFirst().isEmpty()){
            endGame("You win", true);
        }
    }

    private Circle createProjectile(GameObject projectile, Color color) {
        Circle circleShape = new Circle();
        circleShape.centerXProperty().bind(projectile.rowProperty());
        circleShape.centerYProperty().bind(projectile.colProperty());
        circleShape.radiusProperty().bind(projectile.widthProperty());
        circleShape.visibleProperty().bind(projectile.activeProperty());
        circleShape.setFill(color);
        return circleShape;
    }
    private Rectangle createWall(Wall wall, Color color) {
        Rectangle wallShape = new Rectangle(wall.getRow(),wall.getCol(), wall.getWidth(),wall.getHeight());

        if ((wall.getRow() + wall.getCol()) % 2 == 0) wallShape.setFill(Color.DARKRED);
        else wallShape.setFill(color);

        // provazani vlastnosti
        wallShape.xProperty().bind(wall.colProperty());
        wallShape.yProperty().bind(wall.rowProperty());
        wallShape.widthProperty().bind(wall.widthProperty());
        wallShape.heightProperty().bind(wall.heightProperty());
        wallShape.visibleProperty().bind(wall.activeProperty());

        return wallShape;

    }
}


