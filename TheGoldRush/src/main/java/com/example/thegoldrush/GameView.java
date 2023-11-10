package com.example.thegoldrush;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameView {
    private Stage primaryStage;
    private Scene gameScene;
    Text gameText;
    private String selectedLevel;
    private int goldCollected = 0;
    private int paramCells;
    Text hintsText;
    private int bombs;
    private int totalCells;
    private int hints=0;
    private Timeline timer;
    private int secondsElapsed = 0;
    private boolean gameStarted = false;
    private boolean gamePaused = false;
    Button character2Button;
    private VBox gameLayout;
    private GridPane gridPane;
    private GridPane topGrid;
    private Text bombsText;
    private Text goldText;
    private String selectedCharacter;
    int timerStop=5;
    Text timerStopText;
    private Text timerText;
    private int goldCellsOpened = 0;
    private int flagsPlaced = 0;
    private boolean[][] isBomb;
    private boolean[][] isCellOpened;
    private boolean[][] isFlagPlaced;
    private int totalGoldCells;
    private GameModel gameModel;
    int cellWidth;
    private SoundRecognizer soundRecognizer;
    int cellHeight;
    private boolean helpButtonDragged = false;
    private PauseTransition pauseTransition;

    // Constructor for the GameView class
    public GameView(Stage primaryStage, String selectedLevel, String selectedCharacter) {
        this.primaryStage = primaryStage;
        this.selectedLevel=selectedLevel;
        this.selectedCharacter=selectedCharacter;
        this.totalGoldCells = totalCells - bombs;
        soundRecognizer = new SoundRecognizer();

        // Initialize a pause transition for game pause effect
        this.pauseTransition = new PauseTransition(Duration.seconds(5));
        this.pauseTransition.setOnFinished(event -> {
            timer.play();
        });
        // Initialize game parameters based on the selected level
        if (selectedLevel.equals("Easy")) {
            this.hints = 3;
            this.totalCells = 50;
            this.paramCells=50;
            this.bombs = 10;
        } else if (selectedLevel.equals("Medium")) {
            this.hints = 2;
            this.paramCells=70;
            this.totalCells = 98; // Increase the number of cells for the Medium level
            this.bombs = 20; // Increase the number of bombs for the Medium level
        } else if (selectedLevel.equals("Hard")) {
            this.hints = 1;
            this.totalCells=162;
            this.paramCells=90;
            this.bombs = 30; // Increase the number of bombs for the Hard level
        }

        // Initialize game model and data structures
        this.bombsText = new Text("" + bombs);
        gameModel=new GameModel(selectedLevel);
        gameModel.initializeGame(paramCells/10,paramCells/5);
        this.isBomb=gameModel.getIsBomb();
        this.isCellOpened=gameModel.getIsCellOpened();
        this.isFlagPlaced=gameModel.getIsFlagPlaced();
    }


    // Method to show the game view
    public void show() {

        gameLayout = new VBox();
        gameLayout.setPrefSize(950, 500);
        primaryStage.setMaxWidth(1000);
        primaryStage.setMaxHeight(600);
        gameLayout.setAlignment(Pos.BOTTOM_CENTER);
        gameScene = new Scene(gameLayout);
        gameScene.getStylesheets().add(getClass().getResource("css/pre-game.css").toExternalForm());

        gridPane = createGrid();
        gridPane.setAlignment(Pos.BOTTOM_CENTER);
        gameLayout.getChildren().add(gridPane);
        primaryStage.setScene(gameScene);

        // Set up a click event for starting the game
        gameScene.setOnMouseClicked(event -> {
            if (!gameStarted) {
                gameScene.getStylesheets().add(getClass().getResource("css/grid.css").toExternalForm());
                startGame();
            }
        });
    }


    // Method to start the game
    private void startGame() {
        gameStarted = true;
        gameLayout.getChildren().removeAll(gameText, gridPane, topGrid);

        // Initialize buttons for pause, restart, help, and character-specific actions
        Button pauseButton = new Button();
        pauseButton.getStyleClass().add("pauseButton");
        pauseButton.setOnAction(event -> {

            togglePause(pauseButton);

        });
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                togglePause(pauseButton);
            }
        });

        Button restartButton = new Button();
        restartButton.getStyleClass().add("restartButton");
        restartButton.setOnAction(event -> {
            GameView gamerestart=new GameView(primaryStage,selectedLevel,selectedCharacter);
            gamerestart.show();
        });

        StackPane helpContainer = new StackPane();
        helpContainer.setAlignment(Pos.CENTER_RIGHT);
        Button helpButton = new Button();
        helpButton.getStyleClass().add("helpButton");

        StackPane character2Container = new StackPane();
        character2Button = new Button();
        character2Button.getStyleClass().add("characterButton");
        timerStopText = new Text(""+ timerStop);
        timerStopText.getStyleClass().add("hints-text");

        hintsText = new Text(""+ hints);
        hintsText.getStyleClass().add("hints-text");

        StackPane.setAlignment(timerStopText, Pos.TOP_RIGHT);
        StackPane.setMargin(timerStopText, new Insets(0, 4, 0, 0));
        character2Container.getChildren().addAll(character2Button, timerStopText);

        StackPane.setAlignment(hintsText, Pos.TOP_RIGHT);
        StackPane.setMargin(hintsText, new Insets(0, 0, 0, 0));
        helpContainer.getChildren().addAll(helpButton, hintsText);

        // Enable dragging the help button for hints
        helpButton.setOnDragDetected(event -> {
            if(!gamePaused) {
                Dragboard db = helpButton.startDragAndDrop(TransferMode.ANY);
                ClipboardContent content = new ClipboardContent();
                content.putString("Help");
                db.setContent(content);
                helpButtonDragged = true;
                event.consume();
            }
        });


        bombsText = new Text("" + bombs);
        bombsText.getStyleClass().add("game-info");
        goldText = new Text("" + goldCollected);
        goldText.getStyleClass().add("game-info");
        timerText = new Text("00:00");
        timerText.getStyleClass().add("timer-text");

        topGrid = new GridPane();
        ImageView goldImageView = new ImageView(new Image(getClass().getResourceAsStream("css/images/goldCollected.png")));
        goldImageView.setFitWidth(50);
        goldImageView.setFitHeight(50);
        ImageView minesLeftImageView = new ImageView(new Image(getClass().getResourceAsStream("css/images/minesLeft.png")));
        minesLeftImageView.setFitWidth(50);
        minesLeftImageView.setFitHeight(50);
        ImageView soundBombImageView = new ImageView(new Image(getClass().getResourceAsStream("css/images/soundBomb.png")));
        soundBombImageView.setFitWidth(90);
        soundBombImageView.setFitHeight(70);

        topGrid.setAlignment(Pos.TOP_CENTER);
        topGrid.add(pauseButton, 0, 0);
        topGrid.add(timerText, 3, 0);
        topGrid.add(bombsText, 5, 0);
        topGrid.add(restartButton, 1, 0);
        topGrid.add(minesLeftImageView, 4, 0);
        topGrid.add(goldImageView, 6, 0);
        topGrid.add(goldText, 7, 0);
        topGrid.add(helpContainer, 2, 0);

        // Additional UI elements and character-specific actions based on selectedCharacter
        if(selectedCharacter.equals("Character_3")){
            topGrid.add(character2Container, 8, 0);
            GridPane.setMargin(character2Container, new Insets(0, 0, 0, 0));

            character2Button.setOnAction(event -> {
                if (!gamePaused && timerStop>0) {
                    timer.pause();
                    startTimerEffect(0,0,false);
                    pauseTransition.play();
                    timerStop--;
                    timerStopText.setText("" + timerStop);
                }
            });

        }
        if(selectedCharacter.equals("Character_4")){
            topGrid.add(soundBombImageView, 8, 0);
        }

        if(selectedCharacter.equals("Character_2")){
            if(selectedLevel.equals("Easy")) this.hints+=1;
            if(selectedLevel.equals("Medium")) this.hints+=2;
            if(selectedLevel.equals("Hard")) this.hints+=4;

            hintsText.setText("" + hints);
        }

        // Set the layout margins and add UI elements
        GridPane.setMargin(pauseButton, new Insets(0, 0, 0, 0));
        GridPane.setMargin(bombsText, new Insets(0, 0, 0, 0));
        GridPane.setMargin(restartButton, new Insets(0, 75, 0, 0));
        GridPane.setMargin(goldText, new Insets(0, 50, 0, 0));


        if(selectedCharacter.equals("Character_3"))
        {
            VBox.setMargin(topGrid, new Insets(0, 0, 20, 0));
            GridPane.setMargin(timerText, new Insets(0, 90, 0, 0));
            GridPane.setMargin(helpContainer, new Insets(0, 125, 0, 0));}
        else if (selectedCharacter.equals("Character_4")){
            VBox.setMargin(topGrid, new Insets(0, 0, 20, 0));
            GridPane.setMargin(timerText, new Insets(0, 82, 0, 0));
            GridPane.setMargin(helpContainer, new Insets(0, 133, 0, 0));
        }
        else{
            VBox.setMargin(topGrid, new Insets(0, 0, 30, 0));
            GridPane.setMargin(timerText, new Insets(0, 130, 0, 0));
            GridPane.setMargin(helpContainer, new Insets(0, 94, 0, 0));}


        gameLayout.getChildren().add(topGrid);

        gridPane = createGrid();
        gridPane.setAlignment(Pos.CENTER);
        gameLayout.getChildren().add(gridPane);

        // Start the timer for the game
        timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            secondsElapsed++;
            int minutes = secondsElapsed / 60;
            int seconds = secondsElapsed % 60;
            String time = String.format("%02d:%02d", minutes, seconds);
            timerText.setText(time);
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    // Method to update gold collected and display it
    private void updateGoldCollected(int bombsNearby) {
        goldCollected = goldCollected+1+bombsNearby;
        goldText.setText(""+goldCollected);
    }

    // Method to create the game grid
    private GridPane createGrid() {
        GridPane gridPane = new GridPane();
        for (int row = 0; row < (paramCells/10); row++) {
            for (int col = 0; col < (paramCells/5); col++) {
                Pane cell = createCell(row, col);
                gridPane.add(cell, col, row);
            }}
        return gridPane;
    }



    // Method to create a game cell
    private Pane createCell(int row, int col) {
        // Calculate cell width and height based on the grid size
        int numRows = (paramCells/10);
        int numCols = (paramCells/5);
        cellWidth = 800 / numCols;
        cellHeight = 400 / numRows;
        Pane cell = new Pane();
        cell.setMinSize(cellWidth, cellHeight);
        cell.getStyleClass().add("grid-cell");

        // Set up mouse events and drag-and-drop functionality for cells
        if(gameStarted) {
            cell.setOnMousePressed(event -> {
                // Handle left and right mouse clicks on the cell
                if(!gamePaused){
                    if(gameModel.checkWinCondition(flagsPlaced,goldCellsOpened)) showYouWinScreen();
                    if (event.getButton() == MouseButton.PRIMARY) {

                        if (isBomb[row][col] && !isFlagPlaced[row][col]) {

                            if(selectedCharacter.equals("Character_4")) {
                                cell.getStyleClass().add("grid-cell-bomb");
                                startTimerEffect(row, col,true);

                                new Thread(() -> {

                                    boolean bomb_exploded=true;
                                    long startTime = System.currentTimeMillis();
                                    long currentTime = startTime;
                                    while (currentTime - startTime < 5000) {

                                        soundRecognizer.startSoundRecognition(0)
                                        ;
                                        cell.getStyleClass().add("grid-cell-bomb");

                                        double sound = 0;
                                        sound = soundRecognizer.getIntensity();
                                        if (sound < 15000.0) {
                                            //keep processing
                                        } else {

                                            bomb_exploded=false;
                                            break;
                                        }
                                        currentTime = System.currentTimeMillis();
                                    }
                                    if(bomb_exploded) {
                                        Platform.runLater(() -> {
                                            showGameOverScreen();
                                        });}

                                }).start();
                            }else{
                                //Handle game over for other characters
                                showGameOverScreen();
                            }

                        } else if(!isBomb[row][col] && !isFlagPlaced[row][col]){
                            openCell(row, col);
                            cell.getStyleClass().add("grid-cell-opened");
                        }
                    } else if (event.getButton() == MouseButton.SECONDARY) {
                        if (!isCellOpened[row][col]) {
                            placeFlag(row, col);

                        }
                    }}
            });



        }



        cell.setOnDragOver(event -> {
            if (helpButtonDragged) {
                event.acceptTransferModes(TransferMode.ANY);
            }
            event.consume();
        });

        cell.setOnDragDropped(event -> {
            if (helpButtonDragged && hints>0) {
                int cellRow = GridPane.getRowIndex(cell);
                int cellCol = GridPane.getColumnIndex(cell);
                if (!isCellOpened[cellRow][cellCol]) {
                    if(!isBomb[row][col]){
                        openCell(cellRow, cellCol);
                        cell.getStyleClass().add("grid-cell-opened");}
                    else{
                        placeFlag(row, col);
                    }
                    hints--;
                    hintsText.setText("" + hints);
                }
                helpButtonDragged = false;
            } else if(hints==0){
                buyHints();
            }
            event.setDropCompleted(true);
            event.consume();
        });

        return cell;
    }

    // Method to handle buying hints in the game
    public void buyHints() {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(primaryStage);

        VBox popupContent = new VBox();
        popupContent.setAlignment(Pos.CENTER);
        popupContent.setSpacing(10);

        HBox buttonContainer = new HBox();
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setSpacing(10);

        Text hintText = new Text("Do you want to buy an hint for 50 gold?");
        hintText.getStyleClass().add("textTitle");
        Button buyButton = new Button("Yes");
        Button cancelButton = new Button("No");
        buyButton.getStyleClass().add("custom-button");
        cancelButton.getStyleClass().add("custom-button");

        buttonContainer.getChildren().addAll(buyButton, cancelButton);

        buyButton.setOnAction(event -> {
            if(goldCollected>=50){
                goldCollected-=50;
                goldText.setText(""+goldCollected);
                hints++;
                hintsText.setText("" + hints);
                popupStage.close();}
        });

        cancelButton.setOnAction(event -> {
            popupStage.close();
        });

        popupContent.getChildren().addAll(hintText, buttonContainer);


        Scene popupScene = new Scene(popupContent, 500, 250);
        popupStage.setScene(popupScene);
        popupStage.getScene().getStylesheets().add(getClass().getResource("css/popup.css").toExternalForm());

        popupStage.showAndWait();
    }


    // Method to create a timer effect for a cell or character
    private void startTimerEffect(int row, int col, boolean isCell) {

        if(isCell){
            double radius = cellWidth / 4.2;
            Arc timerArc = new Arc(cellWidth / 2, cellHeight / 2, radius,radius, 0, 360);
            timerArc.setStroke(Color.RED);
            timerArc.setFill(Color.TRANSPARENT);
            timerArc.setStrokeWidth(1);
            Pane cell = (Pane) gridPane.getChildren().get(row * (paramCells / 5) + col);
            cell.getChildren().add(timerArc);
            Duration duration = Duration.seconds(5);
            KeyValue keyValue = new KeyValue(timerArc.lengthProperty(), 0);
            KeyFrame keyFrame = new KeyFrame(duration, keyValue);
            Timeline timeline = new Timeline(keyFrame);
            timeline.setOnFinished(event -> {

                cell.getChildren().remove(timerArc);

            });
            timeline.play();}
        else{
            double radius = 20;
            double centerX = character2Button.getWidth() / 2;
            double centerY = character2Button.getHeight() / 2;
            Arc timerArc = new Arc(centerX, centerY, radius, radius, 0, 360);

            timerArc.setStroke(Color.RED);
            timerArc.setFill(Color.TRANSPARENT);
            timerArc.setStrokeWidth(2);

            character2Button.setGraphic(timerArc);

            Duration duration = Duration.seconds(5);
            KeyValue keyValue = new KeyValue(timerArc.lengthProperty(), 0);
            KeyFrame keyFrame = new KeyFrame(duration, keyValue);
            Timeline timeline = new Timeline(keyFrame);
            timeline.setOnFinished(event -> {
                character2Button.setGraphic(null);
            });
            timeline.play();


        }
    }

    // Method to open a game cell
    private void openCell(int row, int col) {
        if (!isCellOpened[row][col] && !isFlagPlaced[row][col]) {
            isCellOpened[row][col] = true;
            int bombsNearby = gameModel.calculateBombsNearby(row, col);
            goldCellsOpened++;
            updateGoldCollected(bombsNearby);

            if (bombsNearby > 0) {
                Text text = new Text(Integer.toString(bombsNearby));
                text.getStyleClass().add("bombsNearby");
                StackPane stackPane = new StackPane(text);
                stackPane.setAlignment(Pos.CENTER);
                gridPane.add(stackPane, col, row);
            }
            gridPane.getChildren().get(row * (paramCells/5) + col).getStyleClass().add("grid-cell-opened");

            if (bombsNearby == 0) {
                openAdjacentCells(row, col, true); // Open adjacent cells when no bombs are nearby
            }

            if(gameModel.checkWinCondition(flagsPlaced,goldCellsOpened)) showYouWinScreen();
        }
    }

    // Method to open adjacent cells when no bombs are nearby
    private void openAdjacentCells(int row, int col, boolean first) {
        if (row < 0 || col < 0 || row >= paramCells / 10 || col >= paramCells / 5) {
            return; // Check if the cell is out of bounds
        }
        if (isCellOpened[row][col] && !first) {
            return; // Check if the cell is out of bounds
        }
        if (isFlagPlaced[row][col]) {
            return; // Skip opened cells or flagged cells
        }

        int bombsNearby = gameModel.calculateBombsNearby(row, col);
        if (bombsNearby > 0) {
            openCell(row, col);
            return; // Stop opening cells if a cell with bombs nearby is encountered
        }

        openCell(row, col); // Open the current cell

        // Open adjacent cells recursively
        openAdjacentCells(row - 1, col - 1, false);
        openAdjacentCells(row - 1, col, false);
        openAdjacentCells(row - 1, col + 1,false);
        openAdjacentCells(row, col - 1,false);
        openAdjacentCells(row, col + 1,false);
        openAdjacentCells(row + 1, col - 1,false);
        openAdjacentCells(row + 1, col,false);
        openAdjacentCells(row + 1, col + 1,false);
    }



    // Method to toggle pause and resume the game
    private void togglePause(Button pauseButton) {
        if (!gamePaused) {
            timer.pause();
            gamePaused = true;
            pauseButton.getStyleClass().add("resumeButton");
        } else {
            timer.play();
            gamePaused = false;
            pauseButton.getStyleClass().remove("resumeButton");
            pauseButton.getStyleClass().add("pauseButton");
        }
    }


    private void updateFlagsText() {
        bombsText.setText("" + (bombs - flagsPlaced));
    }

    // Method to update the UI to reflect flag placement
    private void updateCellAppearance(int row, int col) {
        updateFlagsText();
        gridPane.getChildren().forEach(node -> {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                node.getStyleClass().add("grid-cell-flagged");
            }
        });
    }

    // Method to place a flag on a cell
    private void placeFlag(int row, int col) {
        if (!isCellOpened[row][col]) {
            if (!isFlagPlaced[row][col] && ((bombs - flagsPlaced)>0)) {
                isFlagPlaced[row][col] = true;
                flagsPlaced++;
                updateCellAppearance(row, col);
            } else if (isFlagPlaced[row][col]){
                isFlagPlaced[row][col] = false;
                flagsPlaced--;
                removeFlagAppearance(row, col);
            }

            if(gameModel.checkWinCondition(flagsPlaced,goldCellsOpened)) showYouWinScreen();

        }
    }

    // Method to update the "Mines Left" text
    private void removeFlagAppearance(int row, int col) {
        updateFlagsText();
        gridPane.getChildren().forEach(node -> {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {

                node.getStyleClass().remove("grid-cell-flagged");
            }
        });
    }

    // Method to show a game over screen
    private void showGameOverScreen() {
        gameStarted=false;
        GameOver gameOverScreen = new GameOver();
        gameOverScreen.start(primaryStage,selectedLevel,selectedCharacter);

    }

    // Method to show a "You Win" screen
    private void showYouWinScreen() {
        gameStarted=false;
        timer.stop();
        YouWinGame youWinScreen = new YouWinGame();
        youWinScreen.start(primaryStage,selectedLevel,selectedCharacter, goldCollected,secondsElapsed);
    }


}
