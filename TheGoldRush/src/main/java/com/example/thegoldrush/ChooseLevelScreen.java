package com.example.thegoldrush;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChooseLevelScreen {
    private Stage primaryStage;
    private String selectedLevel = "Easy";
    private Label bestTimeLabel;
    private Label goldCollectedLabel;
    private String selectedCharacter;
    private Label levelLabel;

    private int easyGoldCollected;

    private int mediumGoldCollected;
    private int hardGoldCollected;
    private int easyBestTime;
    private int mediumBestTime;
    private int hardBestTime;
    private int gold;
    private String DATA_FILE = "levelData.txt";

    public ChooseLevelScreen(Stage primaryStage, String selectedCharacter) {
        this.primaryStage = primaryStage;
        this.selectedCharacter=selectedCharacter;

        loadLevelData();

    }
    private void loadLevelData() {
        try {
            FileReader reader = new FileReader("/Users/jaobo/IdeaProjects/TheGoldRush/src/main/resources/"+DATA_FILE);

            BufferedReader bufferedReader = new BufferedReader(reader);
            easyGoldCollected = Integer.parseInt(bufferedReader.readLine());
            mediumGoldCollected = Integer.parseInt(bufferedReader.readLine());
            hardGoldCollected = Integer.parseInt(bufferedReader.readLine());
            easyBestTime = Integer.parseInt(bufferedReader.readLine());
            mediumBestTime = Integer.parseInt(bufferedReader.readLine());
            hardBestTime = Integer.parseInt(bufferedReader.readLine());
            gold = Integer.parseInt(bufferedReader.readLine());
            reader.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }


    public void show() {
        BorderPane levelLayout = new BorderPane();
        Scene levelScene = new Scene(levelLayout, 950, 500);
        primaryStage.setMaxWidth(1000);
        primaryStage.setMaxHeight(600);
        levelScene.getStylesheets().add(getClass().getResource("css/chooseLevel.css").toExternalForm());

        Button easyButton = new Button();
        Button mediumButton = new Button();
        Button hardButton = new Button();

        easyButton.getStyleClass().add("easyButton");
        mediumButton.getStyleClass().add("mediumButton");
        hardButton.getStyleClass().add("hardButton");

        Tooltip tooltipEasy = new Tooltip("EASY");
        Tooltip.install(easyButton, tooltipEasy);
        easyButton.setOnAction(event -> {
            setSelectedLevel("Easy");
        });

        Tooltip tooltipMedium = new Tooltip("MEDIUM");
        Tooltip.install(mediumButton, tooltipMedium);

        mediumButton.setOnAction(event -> {
            setSelectedLevel("Medium");
        });
        Tooltip tooltipHard = new Tooltip("HARD");
        Tooltip.install(hardButton, tooltipHard);

        hardButton.setOnAction(event -> {
            setSelectedLevel("Hard");
        });


        Button continueButton = new Button();
        continueButton.setOnAction(event -> {
            startGame(selectedLevel);
        });
        continueButton.getStyleClass().add("continuebutton");

        Button backButton = new Button();
        backButton.setOnAction(event -> {
            TheGoldRushGame goldRushGame = new TheGoldRushGame();
            goldRushGame.start(primaryStage);
        });
        backButton.getStyleClass().add("backbutton");

        VBox difficultyBox = new VBox(easyButton, mediumButton, hardButton);
        VBox.setMargin(mediumButton,new Insets(0,10,0,0));
        VBox.setMargin(hardButton,new Insets(0,45,0,0));
        difficultyBox.setAlignment(Pos.BOTTOM_RIGHT);

        HBox buttonsBox = new HBox(backButton, continueButton);
        buttonsBox.setAlignment(Pos.BOTTOM_CENTER);
        buttonsBox.setSpacing(800);

        levelLabel = new Label(selectedLevel.toUpperCase());
        levelLabel.setStyle("-fx-font-size: 40px;");
        bestTimeLabel = new Label("BEST TIME: " + getBestTime(selectedLevel));
        goldCollectedLabel = new Label("GOLD COLLECTED: " + getGoldCollected(selectedLevel));


        VBox recordBox = new VBox(levelLabel, bestTimeLabel, goldCollectedLabel);
        recordBox.setAlignment(Pos.CENTER_LEFT);
        recordBox.setSpacing(20);
        recordBox.setTranslateX(40);
        levelLayout.setLeft(recordBox);
        levelLayout.setCenter(difficultyBox);
        levelLayout.setBottom(buttonsBox);

        primaryStage.setScene(levelScene);
    }

    private void startGame(String selectedLevel) {
        GameView gameScreen = new GameView(primaryStage,selectedLevel,selectedCharacter);
        gameScreen.show();
    }

    private void setSelectedLevel(String level) {
        selectedLevel = level;
        levelLabel.setText(selectedLevel.toUpperCase());
        bestTimeLabel.setText("BEST TIME: " + getBestTime(selectedLevel));
        goldCollectedLabel.setText("GOLD COLLECTED: " + getGoldCollected(selectedLevel));

    }

    private int getGoldCollected(String level) {
        if (level.equals("Easy")) {
            return easyGoldCollected;
        } else if (level.equals("Medium")) {
            return mediumGoldCollected;
        } else if (level.equals("Hard")) {
            return hardGoldCollected;
        }
        return 0;
    }
    private void saveLevelData() {
        try {
            FileWriter writer = new FileWriter("/Users/jaobo/IdeaProjects/TheGoldRush/src/main/resources/"+DATA_FILE);
            writer.write(easyGoldCollected + "\n");
            writer.write(mediumGoldCollected + "\n");
            writer.write(hardGoldCollected + "\n");
            writer.write(easyBestTime + "\n");
            writer.write(mediumBestTime + "\n");
            writer.write(hardBestTime + "\n");
            writer.write(gold+ "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateLevelData(String level, int goldCollected, int bestTime) {
        if (level.equals("Easy")) {
            if (bestTime < easyBestTime) {
                easyGoldCollected += goldCollected;
                easyBestTime = bestTime;
                saveLevelData();
            }
        } else if (level.equals("Medium")) {
            if (bestTime < mediumBestTime) {
                mediumGoldCollected += goldCollected;
                mediumBestTime = bestTime;
                saveLevelData();
            }
        } else if (level.equals("Hard")) {
            if (bestTime < hardBestTime) {
                hardGoldCollected += goldCollected;
                hardBestTime = bestTime;
                saveLevelData();
            }
        }

        this.gold+=goldCollected;
    }



    private String getBestTime(String level) {

        if (level.equals("Easy")) {
            return secondsToMinutes(easyBestTime);
        } else if (level.equals("Medium")) {
            return secondsToMinutes(mediumBestTime);
        } else if (level.equals("Hard")) {
            return secondsToMinutes(hardBestTime);
        }
        return secondsToMinutes(0);
    }

    public static String secondsToMinutes(int seconds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        Date date = new Date(seconds * 1000L);
        return dateFormat.format(date);
    }


}
