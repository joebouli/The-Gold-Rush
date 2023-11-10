package com.example.thegoldrush;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameOver {
    public void start(Stage primaryStage, String selectedLevel, String selectedCharacter) {

        VBox gameOverLayout = new VBox(0);
        gameOverLayout.setAlignment(Pos.BOTTOM_CENTER);

        Button playAgainButton = new Button();
        playAgainButton.getStyleClass().add("playButton");
        playAgainButton.setOnAction(event -> {
            GameView gameView = new GameView(primaryStage, selectedLevel,selectedCharacter);
            gameView.show();
        });

        Button backHomeButton = new Button();
        backHomeButton.getStyleClass().add("backHomeButton");
        backHomeButton.setOnAction(event -> {
            TheGoldRushGame goldRushGame = new TheGoldRushGame();
            goldRushGame.start(primaryStage);
        });

        VBox.setMargin(playAgainButton, new Insets(70, 0, 0, 0));
        VBox.setMargin(backHomeButton, new Insets(0, 0, 40, 0));


        gameOverLayout.getChildren().addAll( playAgainButton, backHomeButton);
        Scene gameOverScene = new Scene(gameOverLayout, 950, 500);
        gameOverScene.getStylesheets().add(getClass().getResource("css/gameover.css").toExternalForm());

        primaryStage.setScene(gameOverScene);
        primaryStage.show();

    }
}
