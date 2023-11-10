package com.example.thegoldrush;

import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CharacterSelectionScreen {

    private Stage primaryStage;
    private ImageView[] characterAvatars;
    private int currentIndex = 0;
    private double startX;
    private String selectedCharacter;
    private ImageView selectedAvatar;
    private int gold=0;
    private Label goldLabel;
    private String DATA_FILE = "levelData.txt";
    int[] characterCosts = {0, 150, 200, 300};


    public CharacterSelectionScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;
        goldLabel = new Label("" + gold);
        getGold();
    }


    private void getGold(){
        try {
            FileReader reader = new FileReader("/Users/jaobo/IdeaProjects/TheGoldRush/src/main/resources/"+DATA_FILE);
            BufferedReader bufferedReader = new BufferedReader(reader);
            for (int i = 0; i < 6; i++) {
                bufferedReader.readLine();
            }
            this.gold= Integer.parseInt(bufferedReader.readLine());
            goldLabel.setText("" + gold);
            reader.close();

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void setGold(){
        try {

            String[] fileData = new String[7];
            FileReader reader = new FileReader("/Users/jaobo/IdeaProjects/TheGoldRush/src/main/resources/"+DATA_FILE);

            BufferedReader bufferedReader = new BufferedReader(reader);
                for (int i = 0; i < 6; i++) {
                    fileData[i] = bufferedReader.readLine();

                }
            reader.close();

            fileData[6] = (gold+"");


            FileWriter writer = new FileWriter("/Users/jaobo/IdeaProjects/TheGoldRush/src/main/resources/"+DATA_FILE);
            for (String line : fileData) {
                writer.write(line + "\n");
            }
            writer.close();

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }
    public void show() {
        BorderPane characterLayout = new BorderPane();
        characterLayout.setPrefSize(950, 500);
        primaryStage.setMaxWidth(1000);
        primaryStage.setMaxHeight(600);

        HBox charactersBox = createCharactersBox();
        characterLayout.setCenter(charactersBox);
        HBox goldBox = createGoldBox();
        characterLayout.setTop(goldBox);


        Button continueButton = new Button();

        continueButton.setOnAction(event -> {
            if (currentIndex < characterCosts.length && gold >= characterCosts[currentIndex]) {
                gold -= characterCosts[currentIndex]; // Deduct the cost
                setGold();

                ChooseLevelScreen chooseLevelScreen = new ChooseLevelScreen(primaryStage, selectedCharacter);
                chooseLevelScreen.show();
            }
        });

        continueButton.getStyleClass().add("continuebutton");
        Button backButton = new Button();

        backButton.setOnAction(event -> {
            TheGoldRushGame goldRushGame = new TheGoldRushGame();
            goldRushGame.start(primaryStage);
        });

        backButton.getStyleClass().add("backbutton");


        HBox buttonsBox = new HBox(20, backButton, continueButton);
        buttonsBox.setSpacing(800);
        buttonsBox.setAlignment(Pos.CENTER);
        characterLayout.setBottom(buttonsBox);

        Scene characterScene = new Scene(characterLayout);
        characterScene.getStylesheets().add(getClass().getResource("css/characterSelection.css").toExternalForm());
        primaryStage.setScene(characterScene);
    }
    private HBox createGoldBox() {
        HBox goldBox = new HBox(2);
        goldBox.setAlignment(Pos.CENTER_RIGHT);

        ImageView goldImageView = new ImageView(new Image(getClass().getResourceAsStream("css/images/goldCollected.png")));
        goldImageView.setFitWidth(50);
        goldImageView.setFitHeight(50);
        goldLabel = new Label("" + gold);

        goldBox.getChildren().addAll(goldLabel, goldImageView);

        return goldBox;
    }

    private HBox createCharactersBox() {
        HBox charactersBox = new HBox(0);
        charactersBox.setAlignment(Pos.CENTER);

        String[] characterNames = {"Character_1", "Character_2", "Character_3", "Character_4"};
        characterAvatars = new ImageView[characterNames.length];

        for (int i = 0; i < characterNames.length; i++) {
            String characterName = characterNames[i];
            ImageView characterAvatar = createCharacterAvatar(characterName);
            characterAvatars[i] = characterAvatar;

            characterAvatar.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                startX = event.getX();
            });

            characterAvatar.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                        double endX = event.getX();
                        if (startX - endX > 40) {
                            // Swipe right-left
                            moveCenter((currentIndex + 1) % characterAvatars.length);
                        } else if (endX - startX > 40) {
                            // Swipe left-right
                            moveCenter((currentIndex - 1 + characterAvatars.length) % characterAvatars.length);
                        }
                    });

                    charactersBox.getChildren().add(characterAvatar);
        }

        updateCenter();
        return charactersBox;
    }


    private ImageView createCharacterAvatar(String characterName) {
        Image image = new Image(getClass().getResourceAsStream("css/images/"+characterName.toLowerCase() + ".png"));
        ImageView avatar = new ImageView(image);
        avatar.setId(characterName);
        avatar.setFitWidth(120);
        avatar.setFitHeight(210);
        avatar.setLayoutY(50);
        return avatar;
    }

    private void moveCenter(int newIndex) {
        if (newIndex != currentIndex) {
            resetSelectedAvatarScale();
            currentIndex = newIndex;
            updateCenter();
        }
    }

    private void resetSelectedAvatarScale() {
        if (selectedAvatar != null) {
            selectedAvatar.setScaleX(1.0);
            selectedAvatar.setScaleY(1.0);
        }
    }

    private void updateCenter() {
        selectedCharacter = characterAvatars[currentIndex].getId();

        for (int i = 0; i < characterAvatars.length; i++) {
            ImageView characterAvatar = characterAvatars[i];
            double targetX = (i - currentIndex) * 80.0;

            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), characterAvatar);
            transition.setToX(targetX);
            transition.play();

            if (i == currentIndex) {
                characterAvatar.setScaleX(1.6);
                characterAvatar.setScaleY(1.6);
                selectedAvatar = characterAvatar;
            } else {
                characterAvatar.setScaleX(1.0);
                characterAvatar.setScaleY(1.0);
            }
        }
    }
}