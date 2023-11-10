package com.example.thegoldrush;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class TheGoldRushGame extends Application {
    private Stage primaryStage;
    private VBox root;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("The Gold Rush");

        showIntroPage();

    }

    public void showIntroPage(){
        root = new VBox();
        root.setAlignment(Pos.CENTER);
        //root.setSpacing(20);
        Scene scene = new Scene(root, 950, 500);
        primaryStage.setMaxWidth(1000);
        primaryStage.setMaxHeight(600);

        scene.getStylesheets().add(getClass().getResource("css/intro.css").toExternalForm());

        Button playButton = new Button();
        Button howToPlayButton = new Button();
        Button storyButton = new Button();

        VBox.setMargin(playButton, new Insets(160, 0, 0, 0));
        VBox.setMargin(howToPlayButton, new Insets(10, 0, 0, 0));
        VBox.setMargin(storyButton, new Insets(10, 0, 0, 0));

        playButton.getStyleClass().add("playButton");
        howToPlayButton.getStyleClass().add("howToPlaybutton");
        storyButton.getStyleClass().add("storybutton");


        root.getChildren().addAll(playButton, howToPlayButton, storyButton);

        storyButton.setOnAction(event -> showStoryPage());

        howToPlayButton.setOnAction(event -> showHowToPlayPage());
        playButton.setOnAction(event ->{
            CharacterSelectionScreen characterSelectionScreen=new CharacterSelectionScreen(primaryStage);
            characterSelectionScreen.show();
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showStoryPage() {
        root.getChildren().clear();

        Text storyText = new Text("It all begins in January 1848 when a construction worker named "+'\n'+" James W.Marshall made a startling discovery...GOLD"+'\n'+"Little did he know that beneath the glittering surface of gold, "+'\n'+"hidden dangers lurked. As the GOLD RUSH attracted a "+'\n'+"diverse population from different background, it also brought"+'\n'+" forth a new threat: the discovery of unexploded mines.");
        storyText.getStyleClass().add("text");
        storyText.setTextAlignment(TextAlignment.CENTER);
        Button backButton = new Button();
        backButton.getStyleClass().add("backbutton");

        StackPane stackPane = new StackPane();

        StackPane.setAlignment(storyText, Pos.CENTER);
        StackPane.setMargin(storyText, new Insets(170, 0, 20, 0));
        StackPane.setMargin(backButton, new Insets(450, 850, 0, 0));
        stackPane.getChildren().addAll(storyText,backButton);

        backButton.setOnAction(event -> showIntroPage());
        root.getChildren().add(stackPane);
    }

    public void showHowToPlayPage(){
        root.getChildren().clear();
        Text storyText = new Text("The rule of the game is simple, the number on a block "+'\n'+"shows the number of mines adjacent to it and you have"+'\n'+" to flag all the mines. Choose your character and PLAY.");
        storyText.getStyleClass().add("text");
        storyText.setTextAlignment(TextAlignment.CENTER);
        Button backButton = new Button();
        backButton.getStyleClass().add("backbutton");

        StackPane stackPane = new StackPane();

        StackPane.setAlignment(storyText, Pos.CENTER);
        StackPane.setMargin(storyText, new Insets(170, 0, 20, 0));
        StackPane.setMargin(backButton, new Insets(450, 850, 0, 0));
        stackPane.getChildren().addAll(storyText,backButton);

        backButton.setOnAction(event -> showIntroPage());

        root.getChildren().add(stackPane);


    }


    public static void main(String[] args) {
        launch(args);
    }
}
