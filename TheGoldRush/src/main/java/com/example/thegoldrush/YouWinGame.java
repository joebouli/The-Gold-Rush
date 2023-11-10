package com.example.thegoldrush;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class YouWinGame {

    public void start(Stage primaryStage, String selectedLevel, String selectedCharacter, int goldCollected,int time) {
        VBox youWinLayout = new VBox(0);
        youWinLayout.setAlignment(Pos.BOTTOM_CENTER);

        Text youWinText = new Text("\n" + "" + goldCollected);
        youWinText.getStyleClass().add("text");

        ChooseLevelScreen chooseLevelScreen=new ChooseLevelScreen(primaryStage,selectedCharacter);
        chooseLevelScreen.updateLevelData(selectedLevel,goldCollected,time);

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

        VBox.setMargin(youWinText, new Insets(100, 0, 0, 0));
        VBox.setMargin(playAgainButton, new Insets(10, 0, 0, 0));
        VBox.setMargin(backHomeButton, new Insets(0, 0, 40, 0));


        youWinLayout.getChildren().addAll(youWinText, playAgainButton, backHomeButton);
        Scene youWinScene = new Scene(youWinLayout, 950, 500);
        youWinScene.getStylesheets().add(getClass().getResource("css/youWin.css").toExternalForm());

        primaryStage.setScene(youWinScene);
        primaryStage.show();
    }
}

